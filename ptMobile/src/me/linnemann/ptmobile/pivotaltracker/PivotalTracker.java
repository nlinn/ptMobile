package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.adapter.APIAdapterImpl;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapterImpl;
import me.linnemann.ptmobile.pivotaltracker.adapter.PivotalAPI;
import android.content.Context;

/**
 * PivotalTracker is coping with the pivotal API and storage, so
 * that you don't have to.
 * 
 * @author nlinn
 *
 */
public class PivotalTracker {

	private DBAdapter db;
	private PivotalAPI api;

	public PivotalTracker(Context ctx) {
		db = new DBAdapterImpl(ctx);
		
		APIAdapterImpl adapter = new APIAdapterImpl(ctx);
		api = new PivotalAPI(ctx,db,adapter);
	}

	public void pause() {
		db.close();
	}

	public String fetchAPIToken(final String username, final String password) {
		return api.readAPIToken(username, password);
	}	

	public ProjectsCursor getProjectsCursor() {
		return db.getProjectsCursor();
	}

	public ActivitiesCursor getActivitiesCursor() {
		return db.getActivitiesCursor();
	}

	public StoriesCursorImpl getStoriesCursor(Integer project_id, String filter) {
		return db.getStoriesCursor(project_id, filter);
	}

	public Story getStory(Integer story_id) {
		return db.getStory(story_id);
	}

	public Project getProject(Integer project_id) {
		return db.getProject(project_id);
	}

	public void updateStoriesForProject(Integer project_id, String iteration_group) {
		api.readStories(project_id, iteration_group);
	}

	public boolean storiesNeedUpdate(Integer project_id, String iteration_group) {
		return db.storiesNeedUpdate(project_id, iteration_group);
	}

	public boolean projectsNeedUpdate() {
		return db.projectsNeedUpdate();
	}

	public boolean activitiesNeedUpdate() {
		return db.activitiesNeedUpdate();
	}

	public void updateProjects() {
		api.readProjects();
	}

	public void updateActivities() {
		api.readActivities();
	}

	public void commitChanges(Story story) {
		
		if (story.getId().isEmpty()) {
			if (story.getModifiedData().size() > 0) {
				api.createStory(story);	
			
				// i tried to refresh icebox directly after creating story like this:
				// api.readStories(story.getProjectId().getValue(), "icebox");
				// but the newly created story needs some time to appear in icebox
				// so i remove the timestamp instead to make sure that icebox is
				// refreshed when the user clicks on it...
				db.wipeUpdateTimestamp(story.getProjectId().getValue(), "icebox");
			}	
		} else {
			if (story.getModifiedData().size() > 0) {
				db.updateStory(story);
				api.updateStory(story);
			}
		}
	}

	public void addComment(Story story, String comment) {
		api.createComment(story, comment);
	}

	public void flush() {
		db.flush();
	}

	public String getCommentsAsString(Integer story_id) {
		return db.getCommentsAsString(story_id);
	}

	public Story getEmptyStoryForProject(Integer project_id) {
		Story story = new Story();
		story.changeProjectId(project_id);
		story.resetModifiedDataTracking();
		return story;
	}
}
