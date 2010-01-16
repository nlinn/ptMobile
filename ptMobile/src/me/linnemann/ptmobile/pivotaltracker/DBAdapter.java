package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
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

	public abstract long insertProject(ContentValues cv);

	public abstract long insertActivity(ContentValues cv);

	public abstract long insertNote(ContentValues cv);

//	public abstract long insertStory(ContentValues cv);

	public abstract void insertStory(Story story);
	
	public abstract long insertIteration(ContentValues cv);

	public abstract boolean deleteAllProjects();

	public abstract boolean deleteAllActivities();

	public abstract boolean deleteStoriesInProject(Integer project_id, long timestamp, String iteration_group);

	public abstract Cursor fetchStoriesAll(Integer project_id);

	public abstract ProjectsCursor getProjectsCursor();

	public abstract ActivitiesCursor getActivitiesCursor();

	public abstract ProjectsCursor getProject(Integer project_id);

	public abstract Integer getProjectIdByName(String project_name);

	public abstract int getVelocityForProject(Integer project_id);

	public abstract StoriesCursorImpl getStoriesCursor(Integer project_id, String filter);
		
	public abstract Story getStory(Integer story_id);

	public abstract IterationCursor getIteration(Integer project_id, Integer number);
	
	public abstract void flush();

	public abstract void saveStoriesUpdatedTimestamp(Integer project_id, String iteration_group);

	public abstract void saveProjectsUpdatedTimestamp();

	public abstract void saveActivitiesUpdatedTimestamp();

	public abstract boolean storiesNeedUpdate(Integer project_id, String iteration_group);

	public abstract boolean projectsNeedUpdate();

	public abstract boolean activitiesNeedUpdate();
	
	public void updateStory(Story story);

	public String getCommentsAsString(Integer project_id);

}