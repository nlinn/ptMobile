package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursor;
import android.content.Context;

/**
 * PivotalTracker is coping with the pivotal API and storage, so
 * that you don't have to.
 * 
 * @author niels
 *
 */
public class PivotalTracker {

	private DBAdapter db;
	private APIAdapter api;
	
	public PivotalTracker(Context ctx) {
		// initialize api an db helpers
		db = new DBAdapterImpl(ctx);
		db.open();
		api = new APIAdapter(ctx,db);
	}
		
	/**
	 * please call pause() when app is supposed to close/pause. whatever
	 */
	public void pause() {
		db.close();
	}
	
	public String fetchAPIToken(final String username, final String password) {
		return api.getAPIToken(username, password);
	}	
	
	/**
	 * All Projects as specialized Cursor
	 * @return
	 */
	public ProjectsCursor getProjectsCursor() {
		return db.getProjectsCursor();
	}
	
	public ActivitiesCursor getActivitiesCursor() {
		return db.getActivitiesCursor();
	}
	
	public StoriesCursor getStoriesCursorBacklog(String project_id) {
		return db.getStoriesCursorBacklog(project_id);
	}

	public StoriesCursor getStoriesCursorDone(String project_id) {
		return db.getStoriesCursorDone(project_id);
	}

	public StoriesCursor getStoriesCursorCurrent(String project_id) {
		return db.getStoriesCursorCurrent(project_id);
	}

	public StoriesCursor getStory(String story_id) {
		return db.getStory(story_id);
	}
	
	public ProjectsCursor getProject(String project_id) {
		return db.getProject(project_id);
	}
	
	public int getVelocityForProject(String project_id) {
		return db.getVelocityForProject(project_id);
	}
	
	public void updateStoriesForProject(String project_id) {
		api.updateStoriesInProject(project_id);
	}
	
	public boolean storiesNeedUpdate(String project_id) {
		return db.storiesNeedUpdate(project_id);
	}
	
	public boolean projectsNeedUpdate() {
		return db.projectsNeedUpdate();
	}

	public boolean activitiesNeedUpdate() {
		return db.activitiesNeedUpdate();
	}
	
	public boolean updateProjects() {
		return api.updateProjects();
	}
	
	public boolean updateActivities() {
		return api.updateActivities();
	}
	
	public String getProjectIdByName(String project_name) {
		return db.getProjectIdByName(project_name);
	}
	
	public void commitChanges(Story story) {
		db.updateStory(story.getDataAsContentValues());
		api.editStory(story);
	}

	public void flush() {
		db.flush();
	}
	
}
