package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.fields.ActivityField;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class ActivitiesCursor extends SQLiteCursor {

	public static String getListSQL() {
		return "SELECT _id, id, project, story, description, author, _when FROM activities";
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

	public String getStory() {
		return getByField(ActivityField.STORY);
	}

	public String getDescription() {
		return getByField(ActivityField.DESCRIPTION);
	}

	public String getProject() {
		return getByField(ActivityField.PROJECT);
	}

	public String getAuthor() {
		return getByField(ActivityField.AUTHOR);
	}

	public String getWhen() {
		return getByField(ActivityField.WHEN);
	}
	
	public String getId() {
		return getByField(ActivityField.ID);
	}
	
	private String getByField(ActivityField field) {
		return getString(getColumnIndexOrThrow(field.getDBFieldName()));
	}
}
