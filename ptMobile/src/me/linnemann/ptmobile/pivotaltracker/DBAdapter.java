package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursor;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

public interface DBAdapter {

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException if the database could be neither opened or created
	 */
	public abstract DBAdapter open() throws SQLException;

	public abstract void close();

	/**
	 * Create a new project. if the project is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @return rowId or -1 if failed
	 */
	public abstract long insertProject(ContentValues cv);

	/**
	 * Create a new activity
	 * 
	 * @return rowId or -1 if failed
	 */
	public abstract long insertActivity(ContentValues cv);

	/**
	 * Create a new story. if the story is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @return rowId or -1 if failed
	 */
	public abstract long insertStory(ContentValues cv);

	/**
	 * Create a new iteration. if the iteration is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @return rowId or -1 if failed
	 */
	public abstract long insertIteration(ContentValues cv);

	public abstract boolean deleteAllProjects();

	public abstract boolean deleteAllActivities();

	public abstract boolean deleteStoriesInProject(String project_id, long timestamp, String iteration_group);

	public abstract Cursor fetchStoriesAll(String project_id);

	public abstract ProjectsCursor getProjectsCursor();

	public abstract ActivitiesCursor getActivitiesCursor();

	public abstract ProjectsCursor getProject(String project_id);

	public abstract String getProjectIdByName(String project_name);

	public abstract int getVelocityForProject(String project_id);

	public abstract StoriesCursor getStoriesCursor(String project_id, String filter);
		
	public abstract StoriesCursor getStory(String story_id);

	public abstract IterationCursor getIteration(String project_id, String number);
	
	public abstract void flush();

	public abstract void saveStoriesUpdatedTimestamp(String project_id, String iteration_group);

	public abstract void saveProjectsUpdatedTimestamp();

	public abstract void saveActivitiesUpdatedTimestamp();

	public abstract boolean storiesNeedUpdate(String project_id, String iteration_group);

	public abstract boolean projectsNeedUpdate();

	public abstract boolean activitiesNeedUpdate();
	
	public long updateStory(ContentValues cv);

}