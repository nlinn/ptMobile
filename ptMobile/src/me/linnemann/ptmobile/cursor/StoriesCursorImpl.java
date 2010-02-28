package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataType;
import me.linnemann.ptmobile.pivotaltracker.datatype.StoryDataType;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class StoriesCursorImpl extends SQLiteCursor implements StoriesCursor {

	private DBAdapter dbAdapter;
	
	private static final String SQL_STORYFIELDS ="s._id, s.id, s.name, s.iteration_number, s.project_id, " +
	"s.estimate, s.s.story_type, s.labels, s.deadline, s.position, " +
	"s.description, s.current_state, s.requested_by, s.iteration_group, " +
	"s.owned_by, s.created_at, s.accepted_at, s.iteration_number ";
	
	public static String sqlSingleStory(Integer story_id) {
		return "select " +
		SQL_STORYFIELDS +
		"from stories s " +
		"where " +
		"s.id="+story_id+" order by updatetimestamp desc";
	}

	public static String sqlCurrent(Integer project_id) {

		return "select " +
		"i.start, i.finish, " +
		SQL_STORYFIELDS +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"i.iteration_group='current' and " +
		"s.project_id='"+project_id+"' and i.project_id='"+project_id+"' order by s.iteration_number asc, s.position asc";
	}

	public static String sqlBacklog(Integer project_id) {

		return "select " +
		"i.start, i.finish, " +
		SQL_STORYFIELDS +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"i.iteration_group='backlog' " +
		"and s.project_id='"+project_id+"' and i.project_id='"+project_id+"' order by s.iteration_number asc, s.position asc";
	}

	public static String sqlDone(Integer project_id) {

		return "select " +
		"i.start, i.finish, " +
		SQL_STORYFIELDS +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"i.iteration_group='done' " +
		"and i.project_id='"+project_id+"' and s.project_id='"+project_id+"' order by s.iteration_number asc, s.position asc";
	}

	public static String sqlIcebox(Integer project_id) {

		return "select " +
		SQL_STORYFIELDS +
		"from stories s " +
		"where " +
		"s.iteration_group='icebox' " +
		"and s.project_id='"+project_id+"' order by s.position asc";
	}
	
	public StoriesCursorImpl(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	public static class Factory implements SQLiteDatabase.CursorFactory {

		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver driver, String editTable,
				SQLiteQuery query) {

			StoriesCursorImpl sc = new StoriesCursorImpl(db, driver, editTable, query);
			return sc;
		}
	}

	public void setDBAdapter(DBAdapter db) {
		this.dbAdapter = db;
	}
	

	public String getIterationStart() {
		return getString(getColumnIndexOrThrow("start"));
	}

	public String getIterationFinish() {
		return getString(getColumnIndexOrThrow("finish"));
	}
	
	public Story getStory() {
		Story s = new Story(dbAdapter);
		fillStory(s);
		s.resetModifiedDataTracking();
		return s;
	}

	private void fillStory(Story story) {
		
		story.changeId(getInt(getColumnIndexOrThrow(StoryDataType.ID.getDBColName())));
		story.changeName(getStringFrom(StoryDataType.NAME));
		
		StoryType type = StoryType.valueOf(getStringFrom(StoryDataType.STORY_TYPE).toUpperCase());
		story.changeStoryType(type);
		
		State state = State.valueOf(getStringFrom(StoryDataType.CURRENT_STATE).toUpperCase());
		story.changeCurrentState(state);
		
		Estimate estimate = getEstimate();
		story.changeEstimate(estimate);
		
		story.changeProjectId(getIntegerFrom(StoryDataType.PROJECT_ID));
		story.changePosition(getIntegerFrom(StoryDataType.POSITION));
		story.changeIterationNumber(getIntegerFrom(StoryDataType.ITERATION_NUMBER));
		
		story.changeCreatedAt(getStringFrom(StoryDataType.CREATED_AT));
		story.changeAcceptedAt(getStringFrom(StoryDataType.ACCEPTED_AT));
		story.changeIterationGroup(getStringFrom(StoryDataType.ITERATION_GROUP));
		
		String deadline = getStringFrom(StoryDataType.DEADLINE);
		if ((deadline != null) && (deadline.length() > 0)) {
			story.changeDeadline(getStringFrom(StoryDataType.DEADLINE));
		}
		
		story.changeOwnedBy(getStringFrom(StoryDataType.OWNED_BY));
		story.changeRequestedBy(getStringFrom(StoryDataType.REQUESTED_BY));
		story.changeDescription(getStringFrom(StoryDataType.DESCRIPTION));
		story.changeLabels(getStringFrom(StoryDataType.LABELS));
	}
	
	private String getStringFrom(DataType data) {
		return getString(getColumnIndexOrThrow(data.getDBColName()));
	}
	
	// return null so you may want to check for null when used
	private Integer getIntegerFrom(DataType field) {
		int columnNotFound = -1;
		
		int index = getColumnIndex(field.getDBColName());
		
		if ((index == columnNotFound) || isNull(index)) {
			return null;	
		} else {
			return getInt(index);
		}
	}
	
	private Estimate getEstimate() {
		Estimate estimate;
		Integer estimateNumeric = getIntegerFrom(StoryDataType.ESTIMATE);
		
		if (estimateNumeric == null) {
			estimate = Estimate.NO_ESTIMATE;
		} else {
			estimate = Estimate.valueOfNumeric(estimateNumeric);
		}
		return estimate;
	}
}