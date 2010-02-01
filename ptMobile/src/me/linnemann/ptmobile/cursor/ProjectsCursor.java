package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.ProjectImpl;
import me.linnemann.ptmobile.pivotaltracker.fields.ProjectData;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class ProjectsCursor extends SQLiteCursor {
	
	public static String getListSQL() {
		return "SELECT _id, id, current_velocity, name, iteration_length, point_scale, week_start_day FROM projects";
	}

	public static String getProjectById(Integer project_id) {
		return "SELECT _id, id, current_velocity, name, iteration_length, point_scale, week_start_day FROM projects WHERE id="+project_id;
	}

	public static String getProjectByName(String project_name) {
		return "SELECT _id, id, current_velocity, name, iteration_length, point_scale, week_start_day FROM projects WHERE name='"+project_name+"'";
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

	public Project getProject() {
		Project project = new ProjectImpl();
		fillProject(project);
		project.resetModifiedFieldsTracking();
		
		return project;
	}
	
	private void fillProject(Project project) {
		project.changeCurrentVelocity(new Numeric(getByField(ProjectData.CURRENT_VELOCITY)));
		project.changeId(new Numeric(getByField(ProjectData.ID)));
		project.changeIterationLength(new Text(getByField(ProjectData.ITERATION_LENGTH)));
		project.changeWeekStartDay(new Text(getByField(ProjectData.WEEK_START_DAY)));
		project.changeName(new Text(getByField(ProjectData.NAME)));
		project.changePointScale(new Text(getByField(ProjectData.POINT_SCALE)));
	}

	private String getByField(ProjectData field) {
		return getString(getColumnIndexOrThrow(field.getDBFieldName()));
	}
}
