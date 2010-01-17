package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * PivotalTracker is coping with the pivotal API and storage, so
 * that you don't have to.
 * 
 * @author nlinn
 *
 */
public class PivotalTracker {

	private DBAdapter db;
	private APIAdapter api;
	private Context ctx;

	public PivotalTracker(Context ctx) {
		this.ctx = ctx;
		// initialize api an db helpers
		db = new DBAdapterImpl(ctx);
		db.open();
		api = new APIAdapter(ctx,db);
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

	public IterationCursor getIterationCursor(Integer project_id, Integer number) {
		return db.getIteration(project_id, number);
	}

	public StoriesCursorImpl getStoriesCursor(Integer project_id, String filter) {
		return db.getStoriesCursor(project_id, filter);
	}

	public Story getStory(Integer story_id) {
		return db.getStory(story_id);
	}

	public ProjectsCursor getProject(Integer project_id) {
		return db.getProject(project_id);
	}

	public int getVelocityForProject(Integer project_id) {
		return db.getVelocityForProject(project_id);
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

	public boolean updateProjects() {

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
		boolean success = api.readProjects();

		// --- refresh all "done" stories to calculate velocity
		if (success && settings.getBoolean("auto_update_velocity", false)) {
			ProjectsCursor pc = db.getProjectsCursor();
			try {	
				if (pc.moveToFirst()) {
					do {
						api.readStories(pc.getId(),"done");
					} while (pc.moveToNext());
				}
			} finally {
				pc.close();
			}
		}

		return success;
	}

	public boolean updateActivities() {
		return api.readActivities();
	}

	public Integer getProjectIdByName(String project_name) {
		return db.getProjectIdByName(project_name);
	}

	public void commitChanges(Story story) {
		
		if (story.getId().isEmpty()) {
			if (story.getModifiedFields().size() > 0) {
				api.createStory(story);	
			
				// i tried to refresh icebox directly after creating story like this:
				// api.readStories(story.getProjectId().getValue(), "icebox");
				// but the newly created story needs some time to appear in icebox
				// so i remove the timestamp instead to make sure that icebox is
				// refreshed when the user clicks on it...
				db.wipeUpdateTimestamp(story.getProjectId().getValue(), "icebox");
			}	
		} else {
			if (story.getModifiedFields().size() > 0) {
				db.updateStory(story);
				api.updateStory(story);
			}
		}
	}

	public boolean addComment(Story story, String comment) {
		return api.createComment(story, comment);
	}

	public void flush() {
		db.flush();
	}

	public String getCommentsAsString(Integer story_id) {
		return db.getCommentsAsString(story_id);
	}

	public Story getEmptyStoryForProject(Integer project_id) {
		Story story = new StoryImpl();
		story.changeProjectId(project_id);
		story.resetModifiedFieldsTracking();
		return story;
	}
}
