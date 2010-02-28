package me.linnemann.ptmobile.pivotaltracker.adapter;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.Iteration;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import android.content.ContentValues;
import android.database.Cursor;

public interface DBAdapter {

	public abstract void close();
	
	public abstract void insertEntity(TrackerEntity entity);
	
	public abstract long insertIteration(ContentValues cv);

	public abstract boolean deleteAllProjects();

	public abstract boolean deleteAllActivities();

	public abstract boolean deleteStoriesInProject(Integer project_id, long timestamp, String iteration_group);

	public abstract Cursor fetchStoriesAll(Integer project_id);

	public abstract ProjectsCursor getProjectsCursor();

	public abstract ActivitiesCursor getActivitiesCursor();

	public abstract Project getProject(Integer project_id);

	public abstract StoriesCursorImpl getStoriesCursor(Integer project_id, String filter);
		
	public abstract Story getStory(Integer story_id);

	public abstract Iteration getIteration(Numeric project_id, Numeric number);
	
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