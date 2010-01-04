package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursor;
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

	private static final String TAG = "DBAdapterImpl";
	private DatabaseHelper dbHelper;
	SQLiteDatabase db;

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 32;

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
					+ "point_scale text not null, "
					+ "name text not null);");

			// --- STORIES
			db.execSQL("create table stories (_id integer primary key autoincrement, "
					+ "id text not null, "
					+ "iteration_number integer, "
					+ "project_id text not null, "
					+ "estimate integer, "
					+ "story_type text not null, "
					+ "labels text, "
					+ "current_state text, "
					+ "description text, "
					+ "deadline date, "
					+ "requested_by text, "
					+ "owned_by text, "
					+ "created_at date, "
					+ "accepted_at date, "
					+ "updatetimestamp integer, "
					+ "iteration_group text, "
					+ "name text not null);");

			// --- ITERATIONS
			db.execSQL("create table iterations (_id integer primary key autoincrement, "
					+ "id text not null, "
					+ "number integer not null, "
					+ "start date not null, "
					+ "finish date not null, "
					+ "updatetimestamp integer not null, "
					+ "iteration_group text, "
					+ "project_id text not null);");

			// --- ACTIVITIES
			db.execSQL("create table activities (_id integer primary key autoincrement, "
					+ "id text not null, "
					+ "project text, "
					+ "story text, "
					+ "description text, "
					+ "author text, "
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
					+ "updatetimestamp integer, "
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
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#open()
	 */
	public DBAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(ctx);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#close()
	 */
	public void close() {
		dbHelper.close();
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#insertProject(android.content.ContentValues)
	 */
	public long insertProject(ContentValues cv) {
		return db.insert("projects", null, cv);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#insertActivity(android.content.ContentValues)
	 */
	public long insertActivity(ContentValues cv) {
		return db.insert("activities", null, cv);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#insertStory(android.content.ContentValues)
	 */
	public long insertStory(ContentValues cv) {
		long result = db.insert("stories", null, cv);
		Log.i("DB","insert: "+result);
		return result;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#insertNote(android.content.ContentValues)
	 */
	public long insertNote(ContentValues cv) {
		long result = db.insert("notes", null, cv);
		Log.i("DB","insert: "+result);
		return result;
	}

	public long updateStory(ContentValues cv) {
		Log.d(TAG, "updating story: "+cv.toString());
		return db.update("stories", cv, "id=?", new String[]{cv.getAsString("id")});
	}

	public long insertIteration(ContentValues cv) {
		return db.insert("iterations", null, cv);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#insertIteration(me.linnemann.ptmobile.pivotaltracker.Iteration)
	 */
	//public long insertIteration(Iteration iteration) {
	//	return db.insert("iterations", null, iteration.getDataAsContentValues());
	//}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#deleteAllProjects()
	 */
	public boolean deleteAllProjects() {
		return db.delete("projects", null, null) > 0;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#deleteAllActivities()
	 */
	public boolean deleteAllActivities() {
		return db.delete("activities", null, null) > 0;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#deleteStoriesInProject(java.lang.String)
	 */
	public boolean deleteStoriesInProject(String project_id, long timestamp, String iteration_group) {
		Log.i(TAG, "deleting stories in project "+project_id+" group "+iteration_group+" older than "+timestamp);
		db.delete("stories", "project_id=? AND iteration_group=? AND updatetimestamp < ?", new String[]{project_id, iteration_group, Long.toString(timestamp)});
		db.delete("iterations", "project_id=? AND iteration_group=? AND updatetimestamp < ?", new String[]{project_id, iteration_group, Long.toString(timestamp)});
		db.delete("notes", "project_id=? AND updatetimestamp < ?", new String[]{project_id, Long.toString(timestamp)});
		return true;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#fetchStoriesAll(java.lang.String)
	 */
	public Cursor fetchStoriesAll(String project_id) {
		return db.query("stories", new String[] {"_id", "name", "iteration_number"}, "project_id='" + project_id+"'", null, null, null, null);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getProjectsCursor()
	 */
	public ProjectsCursor getProjectsCursor() {
		ProjectsCursor c = (ProjectsCursor) db.rawQueryWithFactory(new ProjectsCursor.Factory(), ProjectsCursor.getListSQL(), null, null);
		c.moveToFirst();
		return c;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getActivitiesCursor()
	 */
	public ActivitiesCursor getActivitiesCursor() {
		ActivitiesCursor c = (ActivitiesCursor) db.rawQueryWithFactory(new ActivitiesCursor.Factory(), ActivitiesCursor.getListSQL(), null, null);
		c.moveToFirst();
		return c;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getProject(java.lang.String)
	 */
	public ProjectsCursor getProject(String project_id) {
		ProjectsCursor c = (ProjectsCursor) db.rawQueryWithFactory(new ProjectsCursor.Factory(), ProjectsCursor.getProjectById(project_id), null, null);
		c.moveToFirst();
		return c;
	}

	public IterationCursor getIteration(String project_id, String number) {
		IterationCursor c = (IterationCursor) db.rawQueryWithFactory(new IterationCursor.Factory(), IterationCursor.sqlSingleIteration(number, project_id), null, null);
		c.moveToFirst();
		return c;
	}


	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getProjectIdByName(java.lang.String)
	 */
	public String getProjectIdByName(String project_name) {
		ProjectsCursor c = (ProjectsCursor) db.rawQueryWithFactory(new ProjectsCursor.Factory(), ProjectsCursor.getProjectByName(project_name), null, null);
		c.moveToFirst();

		String id = c.getId();
		c.close();
		return id;
	}


	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getVelocityForProject(java.lang.String)
	 */
	public int getVelocityForProject(String project_id) {
		int vel = 0;

		ProjectsCursor c1 = (ProjectsCursor) db.rawQueryWithFactory(new ProjectsCursor.Factory(), ProjectsCursor.getVelocity(project_id), null, null);
		vel = c1.getVelocity();
		c1.close();

		return vel;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getStoriesCursorBacklog(java.lang.String)
	 */
	public StoriesCursor getStoriesCursor(String project_id, String filter) {

		String sql = "";
		// --- TODO clean up this mess:
		if (filter.equalsIgnoreCase("icebox")) {
			sql = StoriesCursor.sqlIcebox(project_id);
		} else if (filter.equalsIgnoreCase("done")) {
			sql = StoriesCursor.sqlDone(project_id);
		} else if (filter.equalsIgnoreCase("current")) {
			sql = StoriesCursor.sqlCurrent(project_id);
		} else if (filter.equalsIgnoreCase("backlog")) {
			sql = StoriesCursor.sqlBacklog(project_id);
		}

		StoriesCursor c = (StoriesCursor) db.rawQueryWithFactory(new StoriesCursor.Factory(), sql, null, null);
		c.moveToFirst();
		return c;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#getStory(java.lang.String)
	 */
	public StoriesCursor getStory(String story_id) {
		StoriesCursor c = (StoriesCursor) db.rawQueryWithFactory(new StoriesCursor.Factory(), StoriesCursor.sqlSingleStory(story_id), null, null);
		c.moveToFirst();
		return c;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#flush()
	 */
	public void flush() {
		dbHelper.onUpgrade(db, DATABASE_VERSION, DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#saveStoriesUpdatedTimestamp(java.lang.String)
	 */
	public void saveStoriesUpdatedTimestamp(String project_id, String iteration_group) {
		saveUpdatedTimestamp("project"+project_id+iteration_group);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#saveProjectsUpdatedTimestamp()
	 */
	public void saveProjectsUpdatedTimestamp() {
		saveUpdatedTimestamp("projects");
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#saveActivitiesUpdatedTimestamp()
	 */
	public void saveActivitiesUpdatedTimestamp() {
		saveUpdatedTimestamp("activities");
	}

	private void saveUpdatedTimestamp(String timestampName) {
		try {
			System.currentTimeMillis();
			db.execSQL("DELETE FROM timestamps WHERE key='"+timestampName+"'");
			db.execSQL("INSERT INTO timestamps (key,eventtime) VALUES ('"+timestampName+"', "+System.currentTimeMillis()+")");
		} catch (SQLException e) { 
			Log.e("DBAdapter", e.toString()); 
		}
	}


	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#storiesNeedUpdate(java.lang.String)
	 */
	public boolean storiesNeedUpdate(String project_id, String iteration_group) {

		Long interval = new Long(PreferenceManager.getDefaultSharedPreferences(ctx).getString("story_refresh_interval", "60000"));

		Log.i("update interval","found: "+interval);

		if (interval > 0) {
			return needsUpdate("SELECT eventtime FROM timestamps WHERE key='project"+project_id+iteration_group+"'",interval);
		} else {
			return false;
		}

	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IDBAdapter#projectsNeedUpdate()
	 */
	public boolean projectsNeedUpdate() {

		Long interval = new Long(PreferenceManager.getDefaultSharedPreferences(ctx).getString("project_refresh_interval", "60000"));

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

		Long interval = new Long(PreferenceManager.getDefaultSharedPreferences(ctx).getString("activity_refresh_interval", "60000"));

		Log.i("update interval","found: "+interval);

		if (interval > 0) {
			return needsUpdate("SELECT eventtime FROM timestamps WHERE key='activities'", interval);
		} else {
			return false;
		}
	}

	public String getCommentsAsString(String story_id) {
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
}
