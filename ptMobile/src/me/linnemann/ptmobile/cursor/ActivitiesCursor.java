package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.Activity;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.ActivityDataType;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataType;
import me.linnemann.ptmobile.pivotaltracker.value.DateTime;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class ActivitiesCursor extends SQLiteCursor {

	private DBAdapter dbAdapter;
	
	public static String getListSQL() {
		return "SELECT * FROM activities";
	}

	public void setDBAdapter(DBAdapter db) {
		this.dbAdapter = db;
	}
	
	public ActivitiesCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	public static class Factory implements SQLiteDatabase.CursorFactory {

		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver driver, String editTable,
				SQLiteQuery query) {
			return new ActivitiesCursor(db, driver, editTable, query);
		}
	}

	public Activity getActivity() {
		Activity a = new Activity();
		a.setDBAdapter(dbAdapter);
		
		a.putDataAndTrackChanges(ActivityDataType.EVENT_TYPE, new Text(getEventType()));
		a.putDataAndTrackChanges(ActivityDataType.DESCRIPTION, new Text(getDescription()));
		a.putDataAndTrackChanges(ActivityDataType.PROJECT_ID, new Numeric(getProjectId()));
		a.putDataAndTrackChanges(ActivityDataType.AUTHOR, new Text(getAuthor()));
		a.putDataAndTrackChanges(ActivityDataType.ID, new Numeric(getId()));
		a.putDataAndTrackChanges(ActivityDataType.VERSION, new Numeric(getVersion()));
		a.putDataAndTrackChanges(ActivityDataType.OCCURRED_AT, new DateTime(getOccuredAt()));
		
		return a;
	}
	
	private String getVersion() {
		return getByField(ActivityDataType.VERSION);
	}

	private String getEventType() {
		return getByField(ActivityDataType.EVENT_TYPE);
	}
	
	private String getDescription() {
		return getByField(ActivityDataType.DESCRIPTION);
	}

	private String getProjectId() {
		return getByField(ActivityDataType.PROJECT_ID);
	}

	private String getAuthor() {
		return getByField(ActivityDataType.AUTHOR);
	}

	private String getOccuredAt() {
		return getByField(ActivityDataType.OCCURRED_AT);
	}
	
	private String getId() {
		return getByField(ActivityDataType.ID);
	}
	
	private String getByField(DataType field) {
		return getString(getColumnIndexOrThrow(field.getDBColName()));
	}
}
