package me.linnemann.ptmobile.test.pivotaltracker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.Story;

public class DBAdapterMock implements DBAdapter {

	private static final String TAG = "DBAdapterMock";
	
	private List<ContentValues> cvlist;
	private List<Story> stories;
	
	public DBAdapterMock() {
		cvlist = new ArrayList<ContentValues>();
		stories = new ArrayList<Story>();
	}
	
	public List<ContentValues> getContentValuesList() {
		return cvlist;
	}
	
	public List<Story> getStories() {
		return stories;
	}
	
	
	// --- Implementation of DBAdapter
	
	public boolean activitiesNeedUpdate() {
		// TODO Auto-generated method stub
		return false;
	}

	public void close() {
		// TODO Auto-generated method stub

	}

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

	public ProjectsCursor getProject(Integer projectId) {
		// TODO Auto-generated method stub
		return null;
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

	public int getVelocityForProject(Integer projectId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long insertActivity(ContentValues cv) {
		Log.d(TAG, "insertActivity");
		this.cvlist.add(cv);
		return 1;
	}

	public long insertProject(ContentValues cv) {
		Log.d(TAG, "insertProject");
		this.cvlist.add(cv);
		return 1;
	}

	public long insertStory(ContentValues cv) {
		Log.d(TAG, "insertStory");
		this.cvlist.add(cv);
		return 1;
	}

	public DBAdapter open() throws SQLException {
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

	public IterationCursor getIteration(Integer projectId, Integer number) {
		// TODO Auto-generated method stub
		return null;
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

	public void insertStory(Story story) {
		Log.d(TAG,"insertStory(story)");
		stories.add(story);
	}

	public void updateStory(Story story) {
		// TODO Auto-generated method stub
		
	}

	public void wipeUpdateTimestamp(Integer projectId, String iterationGroup) {
		// TODO Auto-generated method stub
		
	}

}
