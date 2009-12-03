package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.fields.IterationData;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class IterationCursor extends SQLiteCursor {

	public static String sqlSingleIteration(String number, String project_id) {
		return "select " +
		"i._id, i.id, i.number, i.iteration_group, i.project_id, " +
		"i.start, i.finish " +
		"from iterations i " +
		"where " +
		"i.number="+number+" and i.project_id="+project_id;
	}

	public IterationCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	public static class Factory implements SQLiteDatabase.CursorFactory {

		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver driver, String editTable,
				SQLiteQuery query) {
			return new IterationCursor(db, driver, editTable, query);
		}
	}

	public String getId() {
		return getByField(IterationData.ID);
	}

	public String getStart() {
		return getByField(IterationData.START);
	}

	public String getFinish() {
		return getByField(IterationData.FINISH);
	}

	public String getNumber() {
		return getByField(IterationData.NUMBER);
	}

	public String getProjectId() {
		return getByField(IterationData.PROJECT_ID);
	}
	
	public String getIterationGroup() {
		return getByField(IterationData.ITERATION_GROUP);
	}
	
	private String getByField(IterationData field) {
		return getString(getColumnIndexOrThrow(field.getDBFieldName()));
	}
}
