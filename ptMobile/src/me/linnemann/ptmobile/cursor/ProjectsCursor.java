package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.fields.ProjectField;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class ProjectsCursor extends SQLiteCursor {

	public static String getListSQL() {
		return "SELECT _id, id, name FROM projects";
	}

	public static String getProjectById(String project_id) {
		return "SELECT _id, id, name, iteration_length, point_scale, week_start_day FROM projects WHERE id="+project_id;
	}

	public static String getProjectByName(String project_name) {
		return "SELECT _id, id, name, iteration_length, point_scale, week_start_day FROM projects WHERE name='"+project_name+"'";
	}
	
	public static String getVelocity(String project_id) {
		return "select sum(s.estimate), s.iteration_number  " +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where finish < datetime('now') and s.project_id='" + project_id+"' " +
		"and i.project_id='" + project_id+"' " + 
		"group by s.iteration_number "+
		"order by i.number desc";
	}


	public ProjectsCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	public static class Factory implements SQLiteDatabase.CursorFactory {

		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver driver, String editTable,
				SQLiteQuery query) {
			return new ProjectsCursor(db, driver, editTable, query);
		}

	}

	public int getVelocity() {

		int count = 0;
		int sum = 0;
		int vel = 0;
		
		if (this.moveToFirst()) {

			int startIteration = this.getInt(1);
			int tillIteration = startIteration - 3;
			count=1;
			sum = this.getInt(0);

			while (this.moveToNext()) {
				
				if (this.getInt(1) > tillIteration) {
					count++;
					sum += this.getInt(0);
				}
			}
			
			vel = (sum / count);
		}

		return vel;
	}

	public String getName() {
		return getByField(ProjectField.NAME);
	}

	public String getIterationLength() {
		return getByField(ProjectField.ITERATION_LENGTH);
	}

	public String getPointScale() {
		return getByField(ProjectField.POINT_SCALE);
	}

	public String getWeekStartDay() {
		return getByField(ProjectField.WEEK_START_DAY);
	}

	public String getId() {
		return getByField(ProjectField.ID);
	}

	private String getByField(ProjectField field) {
		return getString(getColumnIndexOrThrow(field.getDBFieldName()));
	}
}
