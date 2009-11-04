package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryField;

public class StoryImpl implements Story {

	private Set<StoryField> modifiedFields;
	private Map<StoryField,Object> data;
	
	public StoryImpl() {
		modifiedFields = new HashSet<StoryField>();
		data = new HashMap<StoryField, Object>();
		data.put(StoryField.CURRENT_STATE, "unstarted");
	}
	
	public void initFromCursor(StoriesCursor c) {
		data.put(StoryField.ID,c.getId());
		data.put(StoryField.PROJECT_ID,c.getProjectId());
		data.put(StoryField.CURRENT_STATE,c.getCurrentState());
		data.put(StoryField.ESTIMATE,c.getEstimate());
		// Todo implement missing fields
	}
	
	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.Story#getModifiedFields()
	 */
	public Set<StoryField> getModifiedFields() {
		return modifiedFields;
	}
	
	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.Story#resetModifiedFieldsTracking()
	 */
	public void resetModifiedFieldsTracking() {
		modifiedFields.clear();
	}
	
	
	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.Story#setEstimate(java.lang.Integer)
	 */
	public void setEstimate(Integer estimate) {
		data.put(StoryField.ESTIMATE, estimate);
		modifiedFields.add(StoryField.ESTIMATE);
	}
	
	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.Story#getNextState()
	 */
	public String getNextState() {
		
		// TODO implement states for bugs, chores, releases!
		
		String rc = null;
		String state = (String) data.get(StoryField.CURRENT_STATE);
		
		if (state.equals("unstarted")) {
			Integer estimate = (Integer) data.get(StoryField.ESTIMATE);
			
			if ((estimate != null) && (estimate >= 0)) {
				rc="started";
			}
		} else if (state.equals("started")) {
			rc="finished";
		} else if (state.equals("finished")) {
			rc="delivered";
		} else if (state.equals("delivered")) {
			rc="accepted";
		}
		
		return rc;
	}
	
	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.Story#toNextState()
	 */
	public boolean toNextState() {		
		String nextState = getNextState();
		
		if (nextState == null) {
			return false;
		} else {
			setData(StoryField.CURRENT_STATE,nextState);
			return true;
		}
	}

	public void setData(StoryField field, Object value) {
		data.put(field, value);
		modifiedFields.add(field);
	}
		
	public String getData(StoryField field) {
		return (String) data.get(field);
	}
	
}
