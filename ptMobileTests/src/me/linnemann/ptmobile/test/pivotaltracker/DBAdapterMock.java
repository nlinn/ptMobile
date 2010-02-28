package me.linnemann.ptmobile.test.pivotaltracker;

import java.util.ArrayList;
import java.util.List;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.Activity;
import me.linnemann.ptmobile.pivotaltracker.Iteration;
import me.linnemann.ptmobile.pivotaltracker.Note;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.ProjectDataType;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class DBAdapterMock implements DBAdapter {

	private static final String TAG = "DBAdapterMock";
	
	private List<ContentValues> cvlist;
	private List<TrackerEntity> entities;
	
	private List<Story> stories;
	private List<Iteration> iterations;
	private List<Note> notes;
	private List<Project> projects;
	private List<Activity> activities;
	
	public DBAdapterMock() {
		cvlist = new ArrayList<ContentValues>();
		entities = new ArrayList<TrackerEntity>();
		stories = new ArrayList<Story>();
		iterations = new ArrayList<Iteration>();
		notes = new ArrayList<Note>();
		projects = new ArrayList<Project>();
		activities = new ArrayList<Activity>();
	}
	
	public List<ContentValues> getContentValuesList() {
		return cvlist;
	}
	
	public List<TrackerEntity> getEntities() {
		return entities;
	}
	
	public List<Story> getStories() {
		return stories;
	}
	
	public List<Iteration> getIterations() {
		return iterations;
	}
	
	public List<Note> getNotes() {
		return notes;
	}
	
	public List<Project> getProjects() {
		return projects;
	}
	
	public List<Activity> getActivities() {
		return activities;
	}
	
	// --- Implementation of DBAdapter
	
	public boolean activitiesNeedUpdate() {
		// TODO Auto-generated method stub
		return false;
	}

	public void close() {}

	public boolean deleteAllActivities() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteAllProjects() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteStoriesInProject(Integer projectId, long timestamp) {
		// TODO Auto-generated method stub
		return false;
	}

	public Cursor fetchStoriesAll(Integer projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void flush() {
		// TODO Auto-generated method stub

	}

	public ActivitiesCursor getActivitiesCursor() {
		// TODO Auto-generated method stub
		return null;
	}

	public Project getProject(Integer projectId) {
		Project project = new Project();
		project.putDataAndTrackChanges(ProjectDataType.ID, new Numeric(projectId));
		project.putDataAndTrackChanges(ProjectDataType.USE_HTTPS, new Text("false"));
		return project;
	}

	public Integer getProjectIdByName(String projectName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProjectsCursor getProjectsCursor() {
		// TODO Auto-generated method stub
		return null;
	}

	public Story getStory(Integer storyId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean projectsNeedUpdate() {
		// TODO Auto-generated method stub
		return false;
	}

	public void saveActivitiesUpdatedTimestamp() {
		// TODO Auto-generated method stub

	}

	public void saveProjectsUpdatedTimestamp() {
		// TODO Auto-generated method stub

	}

	public void saveStoriesUpdatedTimestamp(Integer projectId) {
		// TODO Auto-generated method stub

	}

	public boolean storiesNeedUpdate(Integer projectId) {
		// TODO Auto-generated method stub
		return false;
	}

	public long updateStory(ContentValues cv) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean deleteStoriesInProject(Integer projectId, long timestamp,
			String iterationGroup) {
		// TODO Auto-generated method stub
		return false;
	}

	public StoriesCursorImpl getStoriesCursor(Integer projectId, String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public long insertIteration(ContentValues cv) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void saveStoriesUpdatedTimestamp(Integer projectId,
			String iterationGroup) {
		// TODO Auto-generated method stub
		
	}

	public boolean storiesNeedUpdate(Integer projectId, String iterationGroup) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getCommentsAsString() {
		return null;
	}

	public String getCommentsAsString(Integer projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	public long insertNote(ContentValues cv) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void updateStory(Story story) {
		// TODO Auto-generated method stub
		
	}

	public void wipeUpdateTimestamp(Integer projectId, String iterationGroup) {
		// TODO Auto-generated method stub
		
	}

	public void insertEntity(TrackerEntity entity) {
		Log.d(TAG, "insertEntity");
		entities.add(entity);		
		
		if (entity instanceof Story) {
			Log.d(TAG, "Story");
			stories.add((Story) entity);
		}
		
		if (entity instanceof Project) {
			Log.d(TAG, "Project");
			projects.add((Project) entity);
		}
		
		if (entity instanceof Iteration) {
			Log.d(TAG, "Iteration");
			iterations.add((Iteration) entity);
		}
		
		if (entity instanceof Note) {
			Log.d(TAG, "Note");
			notes.add((Note) entity);
		}
		
		if (entity instanceof Activity) {
			Log.d(TAG, "Activity");
			activities.add((Activity) entity);
		}
	}

	public Iteration getIteration(Numeric projectId, Numeric number) {
		// TODO Auto-generated method stub
		return null;
	}

}
