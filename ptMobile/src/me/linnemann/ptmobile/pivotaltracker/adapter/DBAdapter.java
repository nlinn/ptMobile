package me.linnemann.ptmobile.pivotaltracker.adapter;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.Story;
import android.content.ContentValues;
import android.database.Cursor;

public interface DBAdapter {

	public abstract void close();

	public abstract void insertProject(Project project);
	
	public abstract void insertActivity(ContentValues cv);

	public abstract long insertNote(ContentValues cv);

	public abstract void insertStory(Story story);
	
	public abstract long insertIteration(ContentValues cv);

	public abstract boolean deleteAllProjects();

	public abstract boolean deleteAllActivities();

	public abstract boolean deleteStoriesInProject(Integer project_id, long timestamp, String iteration_group);

	public abstract Cursor fetchStoriesAll(Integer project_id);

	public abstract ProjectsCursor getProjectsCursor();

	public abstract ActivitiesCursor getActivitiesCursor();

	public abstract Project getProject(Integer project_id);

	public abstract Integer getProjectIdByName(String project_name);

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

	public void wipeUpdateTimestamp(Integer project_id, String iteration_group);
}