package me.linnemann.ptmobile.cursor;

import java.util.HashSet;
import java.util.Set;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryFromCursorBuilder;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

public class StoriesCursorImpl extends SQLiteCursor implements StoriesCursor {

	private Set<Integer> iterationStarters;
	
	public static String sqlSingleStory(Integer story_id) {
		return "select " +
		"s._id, s.id, s.name, s.iteration_number, s.project_id, " +
		"s.estimate, s.s.story_type, s.labels, s.deadline, " +
		"s.description, s.current_state, s.requested_by, " +
		"s.owned_by, s.created_at, s.accepted_at, s.iteration_number " +
		"from stories s " +
		"where " +
		"s.id="+story_id;
	}

	public static String sqlCurrent(Integer project_id) {

		return "select " +
		"s._id, s.id, s.name, s.iteration_number, s.project_id, " +
		"s.estimate, s.s.story_type, s.labels, " +
		"s.description, s.current_state, " +
		"i.start, i.finish " +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"i.iteration_group='current' and " +
		"s.project_id='"+project_id+"' and i.project_id='"+project_id+"'";
	}

	public static String sqlBacklog(Integer project_id) {

		return "select s._id, s.id, s.name, s.iteration_number, s.project_id, " +
		"s.estimate, s.story_type, " +
		"s.description, s.labels, s.current_state, " +
		"i.start, i.finish " +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"i.iteration_group='backlog' " +
		"and s.project_id='"+project_id+"' and i.project_id='"+project_id+"'";
	}

	public static String sqlDone(Integer project_id) {

		return "select s._id, s.id, s.name, s.iteration_number, s.project_id, " +
		"s.estimate, s.s.story_type, s.labels, " +
		"s.description, s.current_state, " +
		"i.start, i.finish " +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"i.iteration_group='done' " +
		"and i.project_id='"+project_id+"' and s.project_id='"+project_id+"' order by s._id desc";
	}

	public static String sqlIcebox(Integer project_id) {

		return "select s._id, s.id, s.name, s.iteration_number, s.project_id, " +
		"s.estimate, s.s.story_type, s.labels, " +
		"s.description, s.current_state " +
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
			sc.compileIterationStarters();
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

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getEstimate()
	 */
	public Integer getEstimate() {

		String est = getString(getColumnIndexOrThrow(StoryData.ESTIMATE.getDBFieldName()));

		if (est == null) {
			return null;
		} else {
			return new Integer(est);
		}
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getLabels()
	 */
	public String getLabels() {
		return getString(getColumnIndexOrThrow(StoryData.LABELS.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getCurrentState()
	 */
	public String getCurrentState() {
		return getString(getColumnIndexOrThrow(StoryData.CURRENT_STATE.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getDescription()
	 */
	public String getDescription() {
		return getString(getColumnIndexOrThrow(StoryData.DESCRIPTION.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getRequestedBy()
	 */
	public String getRequestedBy() {
		return getString(getColumnIndexOrThrow(StoryData.REQUESTED_BY.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getOwnedBy()
	 */
	public String getOwnedBy() {
		return getString(getColumnIndexOrThrow(StoryData.OWNED_BY.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getDeadline()
	 */
	public String getDeadline() {
		return getString(getColumnIndexOrThrow(StoryData.DEADLINE.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getIterationNumber()
	 */
	public Integer getIterationNumber() {
		return getInt(getColumnIndexOrThrow(StoryData.ITERATION_NUMBER.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getProjectId()
	 */
	public Integer getProjectId() {
		return getInt(getColumnIndexOrThrow(StoryData.PROJECT_ID.getDBFieldName()));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getIterationStart()
	 */
	public String getIterationStart() {
		return getString(getColumnIndexOrThrow("start"));
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getIterationFinish()
	 */
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
	
	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#getStory()
	 */
	public Story getStory() {
		Story s = StoryImpl.buildInstance(new StoryFromCursorBuilder(this));
		return s;
	}

	// -----------------------------------------

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#hasDescription()
	 */
	public boolean hasDescription() {
		return hasField(StoryData.DESCRIPTION);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#hasDeadline()
	 */
	public boolean hasDeadline() {
		return hasField(StoryData.DEADLINE);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#hasLabels()
	 */
	public boolean hasLabels() {
		return hasField(StoryData.LABELS);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#hasRequestedBy()
	 */
	public boolean hasRequestedBy() {
		return hasField(StoryData.REQUESTED_BY);
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#hasOwnedBy()
	 */
	public boolean hasOwnedBy() {
		return hasField(StoryData.OWNED_BY);
	}

	private boolean hasField(StoryData field) {
		String s = getString(getColumnIndexOrThrow(field.getDBFieldName()));

		if ((s == null) || (s.length() < 1)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * iteration starter is the first story inside iteration (rendered with a special iteration header)
	 */
	private void compileIterationStarters() {

		Log.i("StoriesCursor","compileIterationStarters called");

		iterationStarters = new HashSet<Integer>();
		Integer currentIteration = null;

		if (this.moveToFirst()) {

			currentIteration = this.getIterationNumber();
			iterationStarters.add(this.getId());

			if (currentIteration != null) {
				while (this.moveToNext()) {

					// found new iteration?
					if (!currentIteration.equals(this.getIterationNumber())) {
						currentIteration = this.getIterationNumber();
						iterationStarters.add(this.getId());
					}
				}
			}

			this.moveToFirst(); // reset cursor
		}

		Log.i("StoriesCursor","interationStarters "+iterationStarters.toString());
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#isIterationStarter()
	 */
	public boolean isIterationStarter() {
		return iterationStarters.contains(this.getId());
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.cursor.StoriesCursor#hasEstimate()
	 */
	public boolean hasEstimate() {

		Integer estimate = getEstimate();

		if ((estimate == null) || (estimate < 0)) {
			return false;
		} else {
			return true;
		}
	}
}