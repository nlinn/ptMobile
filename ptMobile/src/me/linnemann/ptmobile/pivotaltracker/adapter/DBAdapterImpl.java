package me.linnemann.ptmobile.pivotaltracker.adapter;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.ContentValueProvider;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.ui.OutputStyler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * DBAdapter is in charge of Database CRUD Operations
 * 
 * @author niels
 *
 */
public class DBAdapterImpl implements DBAdapter {

	private static final String SYSTEM_DEFAULT_INTERVAL_ACTIVITIES = "60000";
	private static final String SYSTEM_DEFAULT_INTERVAL_STORIES = "1800000";
	private static final String SYSTEM_DEFAULT_INTERVAL_PROJECTS = "1800000";


	private static final String TAG = "DBAdapterImpl";
	private DatabaseHelper dbHelper;
	SQLiteDatabase db;

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 42;

	private final Context ctx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			// --- PROJECTS
			db.execSQL("create table projects (_id integer primary key autoincrement, "
					+ "id text not null, "
					+ "iteration_length text not null, "
					+ "week_start_day text not null, "
					+ "current_velocity integer not null, "
					+ "point_scale text not null, "
					+ "labels text not null, "
					+ "updatetimestamp integer not null, "
					+ "name text not null);");

			// --- STORIES
			db.execSQL("create table stories (_id integer primary key autoincrement, "
					+ "id integer not null, "
					+ "iteration_number integer, "
					+ "position integer not null, "
					+ "project_id integer not null, "
					+ "estimate integer not null, "
					+ "story_type text not null, "
					+ "labels text not null, "
					+ "current_state text not null, "
					+ "description text not null, "
					+ "deadline date, "
					+ "requested_by text, "
					+ "owned_by text, "
					+ "created_at date, "
					+ "accepted_at date, "
					+ "updatetimestamp integer not null, "
					+ "iteration_group text not null, "
					+ "name text not null);");

			// --- ITERATIONS
			db.execSQL("create table iterations (_id integer primary key autoincrement, "
					+ "id text not null, "
					+ "number integer not null, "
					+ "start date not null, "
					+ "finish date not null, "
					+ "updatetimestamp integer not null, "
					+ "iteration_group text not null, "
					+ "project_id text not null);");

			// --- ACTIVITIES
			db.execSQL("create table activities (_id integer primary key autoincrement, "
					+ "id text not null, "
					+ "project text not null, "
					+ "story text not null, "
					+ "description text not null, "
					+ "author text not null, "
					+ "_when date not null);");

			// --- TIMESTAMPS
			db.execSQL("create table timestamps (_id integer primary key autoincrement, "
					+ "key text not null, "
					+ "eventtime integer not null)");

			// --- NOTES
			db.execSQL("create table notes (_id integer primary key autoincrement, "
					+ "id text not null, "
					+ "story_id text not null, "
					+ "project_id text not null, "
					+ "_text text not null, "
					+ "author text not null, "
					+ "updatetimestamp integer not null, "
					+ "noted_at date not null)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS projects");
			db.execSQL("DROP TABLE IF EXISTS stories");
			db.execSQL("DROP TABLE IF EXISTS iterations");
			db.execSQL("DROP TABLE IF EXISTS activities");
			db.execSQL("DROP TABLE IF EXISTS timestamps");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
	// ---- end of database helper -----------------------------------------------

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx the Context within which to work
	 */
	public DBAdapterImpl(Context ctx) {
		this.ctx = ctx;
		dbHelper = new DatabaseHelper(ctx);
	}

	private DBAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#close()
	 */
	public void close() {
		dbHelper.close();
	}

	private void insertProject(ContentValues cv) {
		openDBOnDemand();
		long rc = db.insert("projects", null, cv);
		if (rc < 1) {
			throw new RuntimeException("error saving data.");
		}
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#insertActivity(android.content.ContentValues)
	 */
	public void insertActivity(ContentValues cv) {
		openDBOnDemand();
		long rc = db.insert("activities", null, cv);
		if (rc < 1) {
			throw new RuntimeException("error saving data.");
		}
	}

	private long insertStory(ContentValues cv) {
		openDBOnDemand();
		Log.v("DB","CV: "+cv);
		long result = db.insert("stories", null, cv);
		Log.v("DB","insert: "+result);
		return result;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#insertNote(android.content.ContentValues)
	 */
	public long insertNote(ContentValues cv) {
		openDBOnDemand();
		long result = db.insert("notes", null, cv);
		Log.v("DB","insert: "+result);
		return result;
	}

	public void updateStory(Story story) {
		openDBOnDemand();
		ContentValueProvider provider = new ContentValueProvider(story);
		provider.fill();
		ContentValues values = provider.getValues();

		Log.d(TAG, "updating story: "+values.toString());
		db.update("stories", values, "id=?", new String[]{values.getAsString("id")});
	}

	public long insertIteration(ContentValues cv) {
		openDBOnDemand();
		return db.insert("iterations", null, cv);
	}

	public boolean deleteAllProjects() {
		openDBOnDemand();
		return db.delete("projects", null, null) > 0;
	}

	public boolean deleteAllActivities() {
		openDBOnDemand();
		return db.delete("activities", null, null) > 0;
	}

	public boolean deleteStoriesInProject(Integer project_id, long timestamp, String iteration_group) {
		openDBOnDemand();
		Log.i(TAG, "deleting stories in project "+project_id+" group "+iteration_group+" older than "+timestamp);
		db.delete("stories", "project_id=? AND iteration_group=? AND updatetimestamp < ?", new String[]{project_id.toString(), iteration_group, Long.toString(timestamp)});
		db.delete("iterations", "project_id=? AND iteration_group=? AND updatetimestamp < ?", new String[]{project_id.toString(), iteration_group, Long.toString(timestamp)});
		db.delete("notes", "project_id=? AND updatetimestamp < ?", new String[]{project_id.toString(), Long.toString(timestamp)});
		return true;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#fetchStoriesAll(java.lang.String)
	 */
	public Cursor fetchStoriesAll(Integer project_id) {
		openDBOnDemand();
		return db.query("stories", new String[] {"_id", "name", "iteration_number"}, "project_id='" + project_id+"'", null, null, null, null);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getProjectsCursor()
	 */
	public ProjectsCursor getProjectsCursor() {
		openDBOnDemand();
		ProjectsCursor c = (ProjectsCursor) db.rawQueryWithFactory(new ProjectsCursor.Factory(), ProjectsCursor.getListSQL(), null, null);
		c.moveToFirst();
		return c;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getActivitiesCursor()
	 */
	public ActivitiesCursor getActivitiesCursor() {
		openDBOnDemand();
		ActivitiesCursor c = (ActivitiesCursor) db.rawQueryWithFactory(new ActivitiesCursor.Factory(), ActivitiesCursor.getListSQL(), null, null);
		c.moveToFirst();
		return c;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getProject(java.lang.String)
	 */
	public Project getProject(Integer project_id) {
		openDBOnDemand();
		ProjectsCursor c = (ProjectsCursor) db.rawQueryWithFactory(new ProjectsCursor.Factory(), ProjectsCursor.getProjectById(project_id), null, null);
		c.moveToFirst();
		Project project = c.getProject();
		c.close();
		return project;
	}

	public IterationCursor getIteration(Integer project_id, Integer number) {
		openDBOnDemand();
		IterationCursor c = (IterationCursor) db.rawQueryWithFactory(new IterationCursor.Factory(), IterationCursor.sqlSingleIteration(number, project_id), null, null);
		c.moveToFirst();
		return c;
	}


	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getProjectIdByName(java.lang.String)
	 */
	public Integer getProjectIdByName(String project_name) {
		openDBOnDemand();
		ProjectsCursor c = (ProjectsCursor) db.rawQueryWithFactory(new ProjectsCursor.Factory(), ProjectsCursor.getProjectByName(project_name), null, null);
		c.moveToFirst();

		Integer project_id = 0; //c.getId();
		c.close();
		return project_id;
	}


	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getStoriesCursorBacklog(java.lang.String)
	 */
	public StoriesCursorImpl getStoriesCursor(Integer project_id, String filter) {
		openDBOnDemand();
		String sql = "";
		// --- TODO clean up this mess:
		if (filter.equalsIgnoreCase("icebox")) {
			sql = StoriesCursorImpl.sqlIcebox(project_id);
		} else if (filter.equalsIgnoreCase("done")) {
			sql = StoriesCursorImpl.sqlDone(project_id);
		} else if (filter.equalsIgnoreCase("current")) {
			sql = StoriesCursorImpl.sqlCurrent(project_id);
		} else if (filter.equalsIgnoreCase("backlog")) {
			sql = StoriesCursorImpl.sqlBacklog(project_id);
		}

		StoriesCursorImpl c = (StoriesCursorImpl) db.rawQueryWithFactory(new StoriesCursorImpl.Factory(), sql, null, null);
		c.moveToFirst();
		return c;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getStory(java.lang.String)
	 */
	public Story getStory(Integer story_id) {
		openDBOnDemand();
		StoriesCursorImpl c = (StoriesCursorImpl) db.rawQueryWithFactory(new StoriesCursorImpl.Factory(), StoriesCursorImpl.sqlSingleStory(story_id), null, null);
		c.moveToFirst();
		Story story = c.getStory();
		c.close();
		return story;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#flush()
	 */
	public void flush() {
		openDBOnDemand();
		dbHelper.onUpgrade(db, DATABASE_VERSION, DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#saveStoriesUpdatedTimestamp(java.lang.String)
	 */
	public void saveStoriesUpdatedTimestamp(Integer project_id, String iteration_group) {
		openDBOnDemand();
		saveUpdatedTimestamp(getStoryIterationGroupTimestampKey(project_id, iteration_group));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#saveProjectsUpdatedTimestamp()
	 */
	public void saveProjectsUpdatedTimestamp() {
		openDBOnDemand();
		saveUpdatedTimestamp("projects");
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#saveActivitiesUpdatedTimestamp()
	 */
	public void saveActivitiesUpdatedTimestamp() {
		openDBOnDemand();
		saveUpdatedTimestamp("activities");
	}

	private void saveUpdatedTimestamp(String timestampName) {
		openDBOnDemand();
		try {
			System.currentTimeMillis();
			db.execSQL("DELETE FROM timestamps WHERE key='"+timestampName+"'");
			db.execSQL("INSERT INTO timestamps (key,eventtime) VALUES ('"+timestampName+"', "+System.currentTimeMillis()+")");
		} catch (SQLException e) { 
			Log.e("DBAdapter", e.toString()); 
		}
	}

	public void wipeUpdateTimestamp(Integer project_id, String iteration_group) {
		openDBOnDemand();
		String timestampName = getStoryIterationGroupTimestampKey(project_id, iteration_group);

		try {
			db.execSQL("DELETE FROM timestamps WHERE key='"+timestampName+"'");
		} catch (SQLException e) { 
			Log.e("DBAdapter", e.toString()); 
		}
	}

	private String getStoryIterationGroupTimestampKey(Integer project_id, String iteration_group) {
		openDBOnDemand();
		return "project"+project_id+iteration_group;
	}

	public boolean storiesNeedUpdate(Integer project_id, String iteration_group) {
		Long interval = new Long(PreferenceManager.getDefaultSharedPreferences(ctx).getString("story_refresh_interval", SYSTEM_DEFAULT_INTERVAL_STORIES));

		Log.i("update interval","found: "+interval);

		if (interval > 0) {
			return needsUpdate("SELECT eventtime FROM timestamps WHERE key='project"+project_id+iteration_group+"'",interval);
		} else {
			return false;
		}

	}

	public boolean projectsNeedUpdate() {

		Long interval = new Long(PreferenceManager.getDefaultSharedPreferences(ctx).getString("project_refresh_interval", SYSTEM_DEFAULT_INTERVAL_PROJECTS));

		Log.i("update interval","found: "+interval);

		if (interval > 0) {
			return needsUpdate("SELECT eventtime FROM timestamps WHERE key='projects'", interval);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#activitiesNeedUpdate()
	 */
	public boolean activitiesNeedUpdate() {


		Long interval = new Long(PreferenceManager.getDefaultSharedPreferences(ctx).getString("activity_refresh_interval", SYSTEM_DEFAULT_INTERVAL_ACTIVITIES));

		Log.i("update interval","found: "+interval);

		if (interval > 0) {
			return needsUpdate("SELECT eventtime FROM timestamps WHERE key='activities'", interval);
		} else {
			return false;
		}
	}

	public String getCommentsAsString(Integer story_id) {
		openDBOnDemand();
		StringBuilder comments = new StringBuilder();

		Cursor c=null;
		try { 
			c = db.rawQuery("Select _text, author, noted_at from notes where story_id='"+story_id+"'",null);

			if (c.moveToFirst()) {
				do {
					comments.append(OutputStyler.getCommentAsText(c.getString(0), c.getString(1), c.getString(2)));
					comments.append("\n\n");
				} while (c.moveToNext());
			}
		} catch (SQLException e) { 
			Log.e("DBAdapter", e.toString()); 
		} finally {
			c.close();
		}

		if (comments.length() < 1) {
			comments.append("(no comments)");
		}

		return comments.toString();

	}

	private boolean needsUpdate(String sql, long updateIterval) {
		openDBOnDemand();
		boolean needsUpdate = true; // default if cursor is empty

		Cursor c=null;
		try { 
			c = db.rawQuery(sql,null);

			if (c.moveToFirst()) { 
				long ts = c.getLong(0);
				needsUpdate = ((System.currentTimeMillis() - ts) > updateIterval);
			} 
		} catch (SQLException e) { 
			Log.e("DBAdapter", e.toString()); 
		} finally {
			c.close();
		}

		return needsUpdate;
	}

	public void insertStory(Story story) {
		openDBOnDemand();
		ContentValueProvider provider = new ContentValueProvider(story);
		provider.fill();
		insertStory(provider.getValues());
	}
	
	public void insertProject(Project project) {
		openDBOnDemand();
		ContentValueProvider provider = new ContentValueProvider(project);
		provider.fill();
		insertProject(provider.getValues());
	}
	
	private void openDBOnDemand() {
		if ((db == null) || (!db.isOpen()))
			open();
	}
}
