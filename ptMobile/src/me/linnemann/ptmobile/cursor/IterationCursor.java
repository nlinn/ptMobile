package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.Iteration;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataType;
import me.linnemann.ptmobile.pivotaltracker.datatype.IterationDataType;
import me.linnemann.ptmobile.pivotaltracker.value.DateTime;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class IterationCursor extends SQLiteCursor {

	public static String sqlSingleIteration(Integer number, Integer project_id) {
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
	
	public Iteration getIteration() {
		Iteration iteration = new Iteration();
		fillIteration(iteration);
		iteration.resetModifiedDataTracking();
		return iteration;
	}
	
	private void fillIteration(Iteration iteration) {
		iteration.putDataAndTrackChanges(IterationDataType.ID, getId());
		iteration.putDataAndTrackChanges(IterationDataType.START, getStart());
		iteration.putDataAndTrackChanges(IterationDataType.FINISH, getFinish());
		iteration.putDataAndTrackChanges(IterationDataType.NUMBER, getNumber());
		iteration.putDataAndTrackChanges(IterationDataType.PROJECT_ID, getProjectId());
		iteration.putDataAndTrackChanges(IterationDataType.ITERATION_GROUP, getIterationGroup());
	}

	private Numeric getId() {
		return new Numeric(getByField(IterationDataType.ID));
	}

	private DateTime getStart() {
		return new DateTime(getByField(IterationDataType.START));
	}

	private DateTime getFinish() {
		return new DateTime(getByField(IterationDataType.FINISH));
	}

	private Numeric getNumber() {
		return new Numeric(getByField(IterationDataType.NUMBER));
	}

	private Numeric getProjectId() {
		return new Numeric(getByField(IterationDataType.PROJECT_ID));
	}
	
	private Text getIterationGroup() {
		return new Text(getByField(IterationDataType.ITERATION_GROUP));
	}
	
	private String getByField(DataType field) {
		return getString(getColumnIndexOrThrow(field.getDBColName()));
	}
}
