package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryFromCursorBuilder;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
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
		"s.project_id='"+project_id+"' and i.project_id='"+project_id+"'";
	}

	public static String sqlBacklog(Integer project_id) {

		return "select " +
		"i.start, i.finish, " +
		SQL_STORYFIELDS +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"i.iteration_group='backlog' " +
		"and s.project_id='"+project_id+"' and i.project_id='"+project_id+"'";
	}

	public static String sqlDone(Integer project_id) {

		return "select " +
		"i.start, i.finish, " +
		SQL_STORYFIELDS +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"i.iteration_group='done' " +
		"and i.project_id='"+project_id+"' and s.project_id='"+project_id+"' order by s._id desc";
	}

	public static String sqlIcebox(Integer project_id) {

		return "select " +
		SQL_STORYFIELDS +
		"from stories s " +
		"where " +
		"s.iteration_group='icebox' " +
		"and s.project_id='"+project_id+"' order by s._id desc";
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

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getName()
	 */
	public String getName() {
		return getString(getColumnIndexOrThrow(StoryData.NAME.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getId()
	 */
	public Integer getId() {
		return getInt(getColumnIndexOrThrow(StoryData.ID.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getStoryType()
	 */
	public String getStoryType() {
		return getString(getColumnIndexOrThrow(StoryData.STORY_TYPE.getDBFieldName()));
	}

	public Estimate getEstimate() {
		Integer estimateNumeric = getInt(getColumnIndexOrThrow(StoryData.ESTIMATE.getDBFieldName()));
		Estimate estimate = Estimate.valueOfNumeric(estimateNumeric);
		return estimate;
	}

	public String getLabels() {
		return getString(getColumnIndexOrThrow(StoryData.LABELS.getDBFieldName()));
	}

	public State getCurrentState() {
		String stateName = getString(getColumnIndexOrThrow(StoryData.CURRENT_STATE.getDBFieldName()));
		return State.valueOf(stateName.toUpperCase());
	}

	public String getDescription() {
		return getString(getColumnIndexOrThrow(StoryData.DESCRIPTION.getDBFieldName()));
	}

	public String getRequestedBy() {
		return getString(getColumnIndexOrThrow(StoryData.REQUESTED_BY.getDBFieldName()));
	}

	public String getOwnedBy() {
		return getString(getColumnIndexOrThrow(StoryData.OWNED_BY.getDBFieldName()));
	}

	public String getDeadline() {
		return getString(getColumnIndexOrThrow(StoryData.DEADLINE.getDBFieldName()));
	}

	public Integer getIterationNumber() {
		return getIntegerFrom(StoryData.ITERATION_NUMBER);
	}

	public Integer getProjectId() {
		return getIntegerFrom(StoryData.PROJECT_ID);
	}

	public String getIterationStart() {
		return getString(getColumnIndexOrThrow("start"));
	}

	public String getIterationFinish() {
		return getString(getColumnIndexOrThrow("finish"));
	}

	public String getIterationGroup() {
		return getString(getColumnIndexOrThrow(StoryData.ITERATION_GROUP.getDBFieldName()));
	}
	
	public String getAcceptedAt() {
		return getString(getColumnIndexOrThrow(StoryData.ACCEPTED_AT.getDBFieldName()));
	}
	
	public String getCreatedAt() {
		return getString(getColumnIndexOrThrow(StoryData.CREATED_AT.getDBFieldName()));
	}
	
	public Integer getStoryPosition() {
		return getIntegerFrom(StoryData.POSITION);
	}
	
	public Story getStory() {
		Story s = StoryImpl.buildInstance(new StoryFromCursorBuilder(this));
		return s;
	}

	private Integer getIntegerFrom(StoryData field) {
		int columnNotFound = -1;
		
		int index = getColumnIndex(field.getDBFieldName());
		
		if ((index == columnNotFound) || isNull(index)) {
			return null;	
		} else {
			return getInt(index);
		}
	}
}