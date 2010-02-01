package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class StoriesCursorImpl extends SQLiteCursor implements StoriesCursor {

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


	

	public String getIterationStart() {
		return getString(getColumnIndexOrThrow("start"));
	}

	public String getIterationFinish() {
		return getString(getColumnIndexOrThrow("finish"));
	}
	
	public Story getStory() {
		Story s = new StoryImpl();
		fillStory(s);
		s.resetModifiedDataTracking();
		return s;
	}

	private void fillStory(Story story) {
		
		story.changeId(getInt(getColumnIndexOrThrow(StoryData.ID.getDBFieldName())));
		story.changeName(getStringFrom(StoryData.NAME));
		
		StoryType type = StoryType.valueOf(getStringFrom(StoryData.STORY_TYPE).toUpperCase());
		story.changeStoryType(type);
		
		State state = State.valueOf(getStringFrom(StoryData.CURRENT_STATE).toUpperCase());
		story.changeCurrentState(state);
		
		Estimate estimate = getEstimate();
		story.changeEstimate(estimate);
		
		story.changeProjectId(getIntegerFrom(StoryData.PROJECT_ID));
		story.changePosition(getIntegerFrom(StoryData.POSITION));
		story.changeIterationNumber(getIntegerFrom(StoryData.ITERATION_NUMBER));
		
		story.changeCreatedAt(getStringFrom(StoryData.CREATED_AT));
		story.changeAcceptedAt(getStringFrom(StoryData.ACCEPTED_AT));
		story.changeIterationGroup(getStringFrom(StoryData.ITERATION_GROUP));
		story.changeDeadline(getStringFrom(StoryData.DEADLINE));
		story.changeOwnedBy(getStringFrom(StoryData.OWNED_BY));
		story.changeRequestedBy(getStringFrom(StoryData.REQUESTED_BY));
		story.changeDescription(getStringFrom(StoryData.DESCRIPTION));
		story.changeLabels(getStringFrom(StoryData.LABELS));
	}
	
	private String getStringFrom(StoryData data) {
		return getString(getColumnIndexOrThrow(data.getDBFieldName()));
	}
	
	// return null so you may want to check for null when used
	private Integer getIntegerFrom(StoryData field) {
		int columnNotFound = -1;
		
		int index = getColumnIndex(field.getDBFieldName());
		
		if ((index == columnNotFound) || isNull(index)) {
			return null;	
		} else {
			return getInt(index);
		}
	}
	
	private Estimate getEstimate() {
		Estimate estimate;
		Integer estimateNumeric = getIntegerFrom(StoryData.ESTIMATE);
		
		if (estimateNumeric == null) {
			estimate = Estimate.NO_ESTIMATE;
		} else {
			estimate = Estimate.valueOfNumeric(estimateNumeric);
		}
		return estimate;
	}
}