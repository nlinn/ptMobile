package me.linnemann.ptmobile.cursor;

import java.util.HashSet;
import java.util.Set;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

public class StoriesCursor extends SQLiteCursor {
	
	private Set<String> iterationStarters;
	
	public static String sqlSingleStory(String story_id) {
		return "select " +
		"s._id, s.id, s.name, s.iteration_number, s.project_id, " +
		"s.estimate, s.s.story_type, s.labels, s.deadline, " +
		"i.start, i.finish, " +
		"s.description, s.current_state, s.requested_by, s.owned_by, s.created_at, s.accepted_at, s.iteration_number " +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"s.id='" + story_id+"' and i.project_id=s.project_id";
	}

	public static String sqlCurrent(String project_id) {

		return "select " +
		"s._id, s.id, s.name, s.iteration_number, s.project_id, " +
		"s.estimate, s.s.story_type, s.labels, " +
		"s.description, s.current_state, " +
		"i.start, i.finish " +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where " +
		"i.finish > datetime('now','-1 day') and " +
		"i.start < datetime('now') and " +
		"s.project_id='" + project_id+"' and i.project_id='" + project_id+"'";
	}

	public static String sqlBacklog(String project_id) {

		return "select s._id, s.id, s.name, s.iteration_number, s.project_id, " +
		"s.estimate, s.story_type, " +
		"s.description, s.labels, s.current_state, " +
		"i.start, i.finish " +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where i.start > datetime('now') " +
		"and s.project_id='" + project_id+"' and i.project_id='" + project_id+"'";
	}

	public static String sqlDone(String project_id) {

		return "select s._id, s.id, s.name, s.iteration_number, s.project_id, " +
		"s.estimate, s.s.story_type, s.labels, " +
		"s.description, s.current_state, " +
		"i.start, i.finish " +
		"from stories s " +
		"left join iterations i on s.iteration_number=i.number " +
		"where finish < datetime('now') and s.project_id='" + project_id+"' " +
		"and i.project_id='" + project_id+"' order by s._id desc";
	}

	public StoriesCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	public static class Factory implements SQLiteDatabase.CursorFactory {

		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver driver, String editTable,
				SQLiteQuery query) {
			
			StoriesCursor sc = new StoriesCursor(db, driver, editTable, query);
			sc.compileIterationStarters();
			return sc;
		}
	}

	public String getName() {
		return getString(getColumnIndexOrThrow(StoryData.NAME.getDBFieldName()));
	}

	public String getId() {
		return getString(getColumnIndexOrThrow(StoryData.ID.getDBFieldName()));
	}

	public String getStoryType() {
		return getString(getColumnIndexOrThrow(StoryData.STORY_TYPE.getDBFieldName()));
	}

	public Integer getEstimate() {
		
		String est = getString(getColumnIndexOrThrow(StoryData.ESTIMATE.getDBFieldName()));
		
		if (est == null) {
			return null;
		} else {
			return new Integer(est);
		}
	}

	public String getLabels() {
		return getString(getColumnIndexOrThrow(StoryData.LABELS.getDBFieldName()));
	}

	public String getCurrentState() {
		return getString(getColumnIndexOrThrow(StoryData.CURRENT_STATE.getDBFieldName()));
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
	
	public String getIterationNumber() {
		return getString(getColumnIndexOrThrow(StoryData.ITERATION_NUMBER.getDBFieldName()));
	}
	
	public String getProjectId() {
		return getString(getColumnIndexOrThrow(StoryData.PROJECT_ID.getDBFieldName()));
	}

	public String getIterationStart() {
		return getString(getColumnIndexOrThrow("start"));
	}
	
	public String getIterationFinish() {
		return getString(getColumnIndexOrThrow("finish"));
	}
	
	public Story getStory() {
		StoryImpl s = new StoryImpl();
		s.initFromCursor(this);
		
		return s;
	}
	
	// -----------------------------------------
	
	public boolean hasDescription() {
		return hasField(StoryData.DESCRIPTION);
	}

	public boolean hasDeadline() {
		return hasField(StoryData.DEADLINE);
	}
	
	public boolean hasLabels() {
		return hasField(StoryData.LABELS);
	}

	public boolean hasRequestedBy() {
		return hasField(StoryData.REQUESTED_BY);
	}

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
		
		iterationStarters = new HashSet<String>();
		String currentIteration = "";
		
		if (this.moveToFirst()) {
		
			currentIteration = this.getIterationNumber();
			iterationStarters.add(this.getId());
			
			while (this.moveToNext()) {
				
				// found new iteration?
				if (!currentIteration.equalsIgnoreCase(this.getIterationNumber())) {
					currentIteration = this.getIterationNumber();
					iterationStarters.add(this.getId());
				}
			}
		
			this.moveToFirst(); // reset cursor
		}
		
		Log.i("StoriesCursor","interationStarters "+iterationStarters.toString());
	}
	
	public boolean isIterationStarter() {
		return iterationStarters.contains(this.getId());
	}
	
	public boolean hasEstimate() {
		
		Integer estimate = getEstimate();

		if ((estimate == null) || (estimate < 0)) {
			return false;
		} else {
			return true;
		}
	}
}