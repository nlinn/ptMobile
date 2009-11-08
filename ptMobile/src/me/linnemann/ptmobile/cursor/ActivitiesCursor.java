package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.fields.ActivityData;
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
		return getByField(ActivityData.STORY);
	}

	public String getDescription() {
		return getByField(ActivityData.DESCRIPTION);
	}

	public String getProject() {
		return getByField(ActivityData.PROJECT);
	}

	public String getAuthor() {
		return getByField(ActivityData.AUTHOR);
	}

	public String getWhen() {
		return getByField(ActivityData.WHEN);
	}
	
	public String getId() {
		return getByField(ActivityData.ID);
	}
	
	private String getByField(ActivityData field) {
		return getString(getColumnIndexOrThrow(field.getDBFieldName()));
	}
}
