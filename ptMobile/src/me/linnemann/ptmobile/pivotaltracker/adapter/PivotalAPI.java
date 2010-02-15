package me.linnemann.ptmobile.pivotaltracker.adapter;

import java.io.IOException;
import java.io.InputStream;

import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.RESTSupport;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.xml.CreateCommentCommand;
import me.linnemann.ptmobile.pivotaltracker.xml.CreateStoryCommand;
import me.linnemann.ptmobile.pivotaltracker.xml.IterationContext;
import me.linnemann.ptmobile.pivotaltracker.xml.RESTXMLCommand;
import me.linnemann.ptmobile.pivotaltracker.xml.StoryContext;
import me.linnemann.ptmobile.pivotaltracker.xml.UpdateStoryCommand;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLActivityListener;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLIterationListener;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLNotesListener;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLProjectsListener;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStack;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStackHandler;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStoriesListener;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLTokenHandler;
import android.content.Context;
import android.util.Log;

/**
 * PivotalAPI is connecting to pivotaltracker.com via an APIAdapter to load/save data. PivotalAPI is interfacing with
 * the local db, it is not meant to serve data directly to UI
 * 
 * method names follow CRUD scheme:
 * 
 * read... = retrieve (get) data from tracker
 * update... = update (put) existing elements
 * create... = add (post) new elements to tracker
 * 
 * @author nlinn
 *
 */
public class PivotalAPI {
	
	private static final String TAG="APIAdapterImpl";
	
	private DBAdapter db;
	private APIAdapter adapter;

	public PivotalAPI(Context ctx, DBAdapter db, APIAdapter adapter) {
		this.db = db;
		this.adapter = adapter;
	}

	public String readAPIToken(String username, String password) {
		InputStream in = adapter.getTokenStream(username, password);
		String token = new XMLTokenHandler().getTokenAndClose(in);
		Log.v(TAG,"API Token: "+token);
		return token;
	}

	public void readProjects() {
		XMLStackHandler handler = initProjectsHandler();
		db.deleteAllProjects();	// TODO: bad boy! make sure to delete only if API update is successful!
		parseXMLFromInputStreamAndClose(handler, adapter.getProjectsStream());
		db.saveProjectsUpdatedTimestamp();
	}

	public void readActivities() {
		XMLStackHandler handler = initActivitesHandler();
		db.deleteAllActivities();	// TODO: bad boy! make sure to delete only if API update is successful!
		parseXMLFromInputStreamAndClose(handler, adapter.getActivitiesStream());
		db.saveActivitiesUpdatedTimestamp();
	}

	private void parseXMLFromInputStreamAndClose(XMLStackHandler handler, InputStream in) {
		try {
			handler.parse(in);
			in.close();
		} catch (IOException e) {
			Log.e(TAG,"IOException closing stream: "+e.getMessage());
			// i'm ignoring exceptions while close
		}
	}

	private XMLStackHandler initProjectsHandler() {
		XMLProjectsListener projectsListener = new XMLProjectsListener(db);
		XMLStack stack = new XMLStack();
		stack.addListener("projects.project", projectsListener);

		return new XMLStackHandler(stack);
	}

	private XMLStackHandler initActivitesHandler() {
		XMLActivityListener activitiesListener = new XMLActivityListener(db);
		XMLStack stack = new XMLStack();
		stack.addListener("activities.activity", activitiesListener);

		return new XMLStackHandler(stack);
	}

	/**
	 * 
	 * @param project_id
	 * @param iteration_group current, backlog, done
	 * @return
	 */
	public void readStories(Integer project_id, String iteration_group) {

		Log.i(TAG,"readStories "+project_id + " group "+iteration_group);

		long timestamp = System.currentTimeMillis();

		XMLStackHandler handler;
		
		if (iteration_group.equalsIgnoreCase("icebox")) {
			handler = getXMLHandlerWithListenersForStories(project_id, iteration_group);
		} else {
			handler = getXMLHandlerWithListenersForIterations(project_id, iteration_group);
		}

		InputStream in=getStreamForGroup(project_id, iteration_group); 
		parseXMLFromInputStreamAndClose(handler, in);
		
		db.saveStoriesUpdatedTimestamp(project_id, iteration_group);
		db.deleteStoriesInProject(project_id, timestamp, iteration_group);
	}
	
	private XMLStackHandler getXMLHandlerWithListenersForIterations(Integer project_id, String iteration_group) {
		XMLIterationListener iterationListener = new XMLIterationListener(db, project_id, iteration_group);
		XMLStoriesListener storyListener = new XMLStoriesListener(db, iterationListener);
		XMLNotesListener noteListener = new XMLNotesListener(db, storyListener);

		XMLStack stack = new XMLStack();
		stack.addListener("iterations.iteration", iterationListener);
		stack.addListener("iterations.iteration.stories.story", storyListener);
		stack.addListener("iterations.iteration.stories.story.notes.note", noteListener);
		return new XMLStackHandler(stack);
	}
	
	private XMLStackHandler getXMLHandlerWithListenersForStories(final Integer project_id, final String iteration_group) {

		// --- these stories have no iteration context, so i create an empty one, only with project
		IterationContext iteration_context = new IterationContext() {
			
			public Integer getProjectId() {
				return project_id;
			}
			
			public Integer getIterationNumber() {
				return null;
			}
			
			public String getIterationGroup() {
				return iteration_group;
			}
		};
		
		XMLStoriesListener storyListener = new XMLStoriesListener(db, iteration_context);
		XMLNotesListener noteListener = new XMLNotesListener(db, storyListener);

		XMLStack stack = new XMLStack();
		stack.addListener("stories.story", storyListener);
		stack.addListener("stories.story.notes.note", noteListener);
		return new XMLStackHandler(stack);
	}
	
	private InputStream getStreamForGroup(Integer project_id, String iteration_group) {
		Project project = db.getProject(project_id);
		String protocol = project.getProtocol();
		InputStream in = null;
		if (iteration_group.equalsIgnoreCase("current")) {
			in = adapter.getCurrentStream(project_id, protocol);
		}
		if (iteration_group.equalsIgnoreCase("done")) {
			in = adapter.getDoneStream(project_id, protocol);
		}
		if (iteration_group.equalsIgnoreCase("backlog")) {
			in = adapter.getBacklogStream(project_id, protocol);
		}
		if (iteration_group.equalsIgnoreCase("icebox")) {
			in = adapter.getIceboxStream(project_id, protocol);
		}

		return in;
	}

	public void updateStory(Story story) {
		String protocol = getProtocolForStory(story);
		UpdateStoryCommand command = new UpdateStoryCommand(story, protocol);
		executeCommandAndClose(command);
	}

	public void createStory(Story story) {
		String protocol = getProtocolForStory(story);
		CreateStoryCommand command = new CreateStoryCommand(story, protocol);
		executeCommandAndClose(command);
	}

	private void executeCommandAndClose(RESTXMLCommand command) {
		try {
			InputStream in = adapter.getStreamForCommand(command);
			Log.v(TAG, RESTSupport.textFromURL(in));
			in.close();
		} catch (IOException e) {
			Log.e(TAG,"IO while closing stream: "+e.getMessage());
		}
	}

	public void createComment(Story story, String comment) {
		
		String protocol = getProtocolForStory(story);
		
		CreateCommentCommand command = new CreateCommentCommand(story, comment, protocol);
		try {
			InputStream in = adapter.getStreamForCommand(command);
			XMLStackHandler handler = getXMLHandlerWithListenersForComments(story.getProjectId().getValue(), story.getId().getValue());
			handler.parse(in);
			in.close();
		} catch (IOException e) {
			Log.e(TAG,"IO while closing stream: "+e.getMessage());
			// --- ignore exceptions while closing
		}
	}
	
	private String getProtocolForStory(Story story) {
		// --- TODO design: should a story should know its projects protocol
		Project project = db.getProject(story.getProjectId().getValue());
		String protocol = project.getProtocol();
		return protocol;
	}

	private XMLStackHandler getXMLHandlerWithListenersForComments(final Integer project_id, final Integer story_id) {

		StoryContext story = new StoryContext() {
			
			public Integer getStoryId() {
				return story_id;
			}
			
			public Integer getProjectId() {
				return project_id;
			}
		};
		
		XMLNotesListener noteListener = new XMLNotesListener(db, story);

		XMLStack stack = new XMLStack();
		stack.addListener("note", noteListener);
		return new XMLStackHandler(stack);
	}
}