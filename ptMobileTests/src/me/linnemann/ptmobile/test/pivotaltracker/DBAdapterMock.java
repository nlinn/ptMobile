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
import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.DBAdapter;

public class DBAdapterMock implements DBAdapter {

	private static final String TAG = "DBAdapterMock";
	
	private List<ContentValues> cvlist;
	
	public DBAdapterMock() {
		cvlist = new ArrayList<ContentValues>();
	}
	
	public List<ContentValues> getContentValuesList() {
		return cvlist;
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

	public boolean deleteStoriesInProject(String projectId, long timestamp) {
		// TODO Auto-generated method stub
		return false;
	}

	public Cursor fetchStoriesAll(String projectId) {
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

	public ProjectsCursor getProject(String projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProjectIdByName(String projectName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProjectsCursor getProjectsCursor() {
		// TODO Auto-generated method stub
		return null;
	}

	public StoriesCursor getStoriesCursorBacklog(String projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	public StoriesCursor getStoriesCursorCurrent(String projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	public StoriesCursor getStoriesCursorDone(String projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	public StoriesCursor getStory(String storyId) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getVelocityForProject(String projectId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long insertActivity(ContentValues cv) {
		Log.d(TAG, "insertActivity");
		this.cvlist.add(cv);
		return 0;
	}

	public long insertProject(ContentValues cv) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long insertStory(ContentValues cv) {
		// TODO Auto-generated method stub
		return 0;
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

	public void saveStoriesUpdatedTimestamp(String projectId) {
		// TODO Auto-generated method stub

	}

	public boolean storiesNeedUpdate(String projectId) {
		// TODO Auto-generated method stub
		return false;
	}

	public long updateStory(ContentValues cv) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean deleteStoriesInProject(String projectId, long timestamp,
			String iterationGroup) {
		// TODO Auto-generated method stub
		return false;
	}

	public IterationCursor getIteration(String projectId, String number) {
		// TODO Auto-generated method stub
		return null;
	}

	public StoriesCursor getStoriesCursor(String projectId, String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public long insertIteration(ContentValues cv) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void saveStoriesUpdatedTimestamp(String projectId,
			String iterationGroup) {
		// TODO Auto-generated method stub
		
	}

	public boolean storiesNeedUpdate(String projectId, String iterationGroup) {
		// TODO Auto-generated method stub
		return false;
	}

}
