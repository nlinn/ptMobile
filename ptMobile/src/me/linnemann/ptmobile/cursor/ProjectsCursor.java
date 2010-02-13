package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.fields.DataType;
import me.linnemann.ptmobile.pivotaltracker.fields.ProjectDataType;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class ProjectsCursor extends SQLiteCursor {
	
	public static String getListSQL() {
		return "SELECT _id, id, use_https, current_velocity, name, iteration_length, point_scale, week_start_day FROM projects";
	}

	public static String getProjectById(Integer project_id) {
		return "SELECT _id, id, use_https, current_velocity, name, iteration_length, point_scale, week_start_day FROM projects WHERE id="+project_id;
	}

	public static String getProjectByName(String project_name) {
		return "SELECT _id, id, use_https, current_velocity, name, iteration_length, point_scale, week_start_day FROM projects WHERE name='"+project_name+"'";
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
		Project project = new Project();
		fillProject(project);
		project.resetModifiedDataTracking();
		
		return project;
	}
	
	private void fillProject(Project project) {
		addFieldToProject(project, ProjectDataType.ID);
		addFieldToProject(project, ProjectDataType.NAME);
		addFieldToProject(project, ProjectDataType.POINT_SCALE);
		addFieldToProject(project, ProjectDataType.WEEK_START_DAY);
		addFieldToProject(project, ProjectDataType.ITERATION_LENGTH);
		addFieldToProject(project, ProjectDataType.CURRENT_VELOCITY);
		addFieldToProject(project, ProjectDataType.USE_HTTPS);
	}

	private void addFieldToProject(Project project, DataType type) {
		TrackerValue value = type.getValueFromString(getByField(type));
		project.putDataAndTrackChanges(type, value);
	}
	
	private String getByField(DataType field) {
		return getString(getColumnIndexOrThrow(field.getDBFieldName()));
	}
}
