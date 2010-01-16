package me.linnemann.ptmobile.pivotaltracker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import me.linnemann.ptmobile.pivotaltracker.xml.CreateCommentCommand;
import me.linnemann.ptmobile.pivotaltracker.xml.CreateStoryCommand;
import me.linnemann.ptmobile.pivotaltracker.xml.UpdateStoryCommand;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLActivitiesHandler;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLNotesHandler;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLProjectsHandler;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStoriesHandler;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLTokenHandler;

import org.xml.sax.SAXException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * APIAdapter is connecting to pivotaltracker.com to load/save data. The adapter is interfacing with
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
public class APIAdapter {

	private static final String TAG="APIAdapter";
	private static final int TIMEOUT_MILLIS = 20000; // http read timeout
	private static final String TRACKER_TOKEN_NAME = "X-TrackerToken";

	private static final String URL_PROJECTS ="http://www.pivotaltracker.com/services/v2/projects";
	private static final String URL_ACTIVITIES ="http://www.pivotaltracker.com/services/v2/activities";
	//private static final String URL_STORIES ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories";
	//private static final String URL_ITERATIONS ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations";
	private static final String URL_ITERATIONS_CURRENT ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations/current";
	private static final String URL_ITERATIONS_DONE ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations/done?offset=-3";
	private static final String URL_ICEBOX ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories?filter=state%3Aunscheduled";
	private static final String URL_ITERATIONS_BACKLOG ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations/backlog";
	private static final String URL_TOKEN ="https://www.pivotaltracker.com/services/tokens/active";

	private String apikey;

	private DBAdapter db;
	private RESTSupport rest;

	public APIAdapter(Context ctx, DBAdapter db) {
		this.db = db;

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
		apikey = settings.getString("APIKEY", "");
		rest = new RESTSupport(TIMEOUT_MILLIS);
	}

	/**
	 * connects to pivotaltracker to retrieve a API token
	 * catches all exceptions and returns null if unsuccessful
	 * 
	 * @param username
	 * @param password
	 * @return token or null if unsuccessful
	 */
	public String readAPIToken(String username, String password) {

		String token=null;

		try {
			token = new XMLTokenHandler().getToken(loadToken(username, password));
			Log.i(APIAdapter.class.toString(),"token: "+token);
		} catch (IOException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readAPIToken failed, IO: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readAPIToken failed, ParserConfig: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readAPIToken failed, SAX: "+e.getMessage());
		}

		return token;
	}

	public boolean readProjects() {
		boolean successful=false;

		db.deleteAllProjects();	// TODO: bad boy! make sure to delete only if API update is successful!

		try {
			new XMLProjectsHandler(db).go(loadURL(URL_PROJECTS));
			db.saveProjectsUpdatedTimestamp();
			successful = true;
		} catch (IOException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readProjects, IO: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readProjects, ParserConfig: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readProjects, SAX: "+e.getMessage());
		}

		return successful;
	}

	public boolean readActivities() {
		boolean successful=false;

		db.deleteAllActivities();	// Todo: bad boy! make sure to delete only if API update is successful!

		try {
			new XMLActivitiesHandler(db).go(loadURL(URL_ACTIVITIES));
			db.saveActivitiesUpdatedTimestamp();
			successful = true;
		} catch (IOException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readActivities, IO: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readActivities, ParserConfig: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readActivities, SAX: "+e.getMessage());
		}

		return successful;
	}

	/**
	 * 
	 * @param project_id
	 * @param iteration_group current, backlog, done
	 * @return
	 */
	public boolean readStories(Integer project_id, String iteration_group) {
		boolean successful=false;

		Log.i(TAG,"readStories "+project_id + " group "+iteration_group);

		String url="";
		
		if (iteration_group.equalsIgnoreCase("current")) {
			url = URL_ITERATIONS_CURRENT.replaceAll("PROJECT_ID", project_id.toString()); // create project specific url
		}
		if (iteration_group.equalsIgnoreCase("done")) {
			url = URL_ITERATIONS_DONE.replaceAll("PROJECT_ID", project_id.toString()); // create project specific url
		}
		if (iteration_group.equalsIgnoreCase("backlog")) {
			url = URL_ITERATIONS_BACKLOG.replaceAll("PROJECT_ID", project_id.toString()); // create project specific url
		}
		if (iteration_group.equalsIgnoreCase("icebox")) {
			url = URL_ICEBOX.replaceAll("PROJECT_ID", project_id.toString()); // create project specific url
		}
		
		Log.i(TAG,url);

		long timestamp = System.currentTimeMillis();
		
		try {
			new XMLStoriesHandler(db, project_id, iteration_group).go(loadURL(url));
			db.saveStoriesUpdatedTimestamp(project_id, iteration_group);
			db.deleteStoriesInProject(project_id, timestamp, iteration_group);
			successful=true;	
		} catch (IOException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readStories, IO: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readStories, ParserConfig: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(APIAdapter.class.getSimpleName(),"readStories, SAX: "+e.getMessage());
		}
		return successful;
	}

	public void updateStory(Story story) {
		
		UpdateStoryCommand command = new UpdateStoryCommand(story);

		Map<String,String> properties = new HashMap<String,String>();
		properties.put(TRACKER_TOKEN_NAME,apikey);
		properties.put("Content-type","application/xml");

		try {
			ByteArrayInputStream body = new ByteArrayInputStream(command.getXMLBytes());

			InputStream is = rest.doPUT(command.getURL(), properties, body);
			Log.d(TAG,"RESP: "+rest.textFromURL(is));

			is.close();
		} catch (IOException e) {
			Log.e(TAG,"editStory, IO: "+e.getMessage());
		}
	}
	
	public void createStory(Story story) {
		
		CreateStoryCommand command = new CreateStoryCommand(story);

		Map<String,String> properties = new HashMap<String,String>();
		properties.put(TRACKER_TOKEN_NAME,apikey);
		properties.put("Content-type","application/xml");

		try {
			ByteArrayInputStream body = new ByteArrayInputStream(command.getXMLBytes());

			InputStream is = rest.doPOST(command.getURL(), properties, body);
			Log.d(TAG,"RESP: "+rest.textFromURL(is));

			is.close();
		} catch (IOException e) {
			Log.e(TAG,"createStory, IO: "+e.getMessage());
		}
	}
	
	
	/**
	 * creates a comment via api, parses response to store 
	 * details to db.
	 * 
	 * @param story Story to add comment to
	 * @param comment comment text
	 * @return successful true/false
	 */
	public boolean createComment(Story story, String comment) {
		boolean successful = false;
		CreateCommentCommand acc = new CreateCommentCommand(story, comment);

		Map<String,String> properties = new HashMap<String,String>();
		properties.put(TRACKER_TOKEN_NAME,apikey);
		properties.put("Content-type","application/xml");

		try {
			ByteArrayInputStream body = new ByteArrayInputStream(acc.getXMLBytes());
			InputStream is = rest.doPOST(acc.getURL(), properties, body);
			new XMLNotesHandler(db,story).go(is);
			is.close();
			successful = true;
		} catch (IOException e) {
			Log.e(TAG,"createComment, IO: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(TAG,"createComment, ParserConfig: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(TAG,"createComment, SAX: "+e.getMessage());
		}
		
		return successful;
	}
	
	private InputStream loadToken(final String username,final String password) throws IOException {
		return rest.doGET(new URL(URL_TOKEN), null, username, password);
	}

	private InputStream loadURL(String urlString) throws IOException {
		Map<String,String> properties = new HashMap<String,String>();
		properties.put(TRACKER_TOKEN_NAME,apikey);
		return rest.doGET(new URL(urlString), properties);
	}
}