package me.linnemann.ptmobile.pivotaltracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.state.State;
import me.linnemann.ptmobile.pivotaltracker.state.Transition;
import android.content.ContentValues;
import android.util.Log;

public class StoryImpl implements Story {

	private static final String TAG = "StoryImpl";

	private Set<StoryData> modifiedFields;
	private Map<StoryData,Object> data;
	private State currentState;

	public StoryImpl() {
		modifiedFields = new HashSet<StoryData>();
		data = new HashMap<StoryData, Object>();
		data.put(StoryData.CURRENT_STATE, "unstarted");
	}

	public void initFromCursor(StoriesCursor c) {
		data.put(StoryData.ID,c.getId());
		data.put(StoryData.NAME,c.getName());
		data.put(StoryData.ESTIMATE,c.getEstimate());
		data.put(StoryData.CURRENT_STATE,c.getCurrentState());
		data.put(StoryData.STORY_TYPE,c.getStoryType());
		data.put(StoryData.LABELS,c.getLabels());
		data.put(StoryData.DESCRIPTION,c.getDescription());
		//data.put(StoryData.REQUESTED_BY,c.getRequestedBy());
	//	data.put(StoryData.OWNED_BY,c.getOwnedBy());
//		data.put(StoryData.CREATED_AT,c.getCr());
//		data.put(StoryData.ACCEPTED_AT,c.get());
		data.put(StoryData.PROJECT_ID,c.getProjectId());
		
		

//		CREATED_AT, ACCEPTED_AT, DEADLINE, ITERATION_NUMBER, PROJECT_ID;

		

		// TODO implement missing fields
		prepareLifecycle();
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.Story#getModifiedFields()
	 */
	public Set<StoryData> getModifiedFields() {
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
		data.put(StoryData.ESTIMATE, estimate);
		modifiedFields.add(StoryData.ESTIMATE);
	}

	public List<Transition> getAvailableTransitions() {

		// --- special treatment: block transitions if feature is noch estimated
	 	if ( ("feature".equals(data.get(StoryData.STORY_TYPE))) &&
	 		("unstarted".equals(data.get(StoryData.CURRENT_STATE))) &&
	 			(data.get(StoryData.ESTIMATE) == null) ) {
	 		return new ArrayList<Transition>(); // empty list
 		} else {
			return currentState.getTransitions();
	 	}
	}

	public boolean doTransition(Transition trans) {
		if (getAvailableTransitions().contains(trans)) {
			Log.d(TAG,"applying transition: "+trans.getName());
			currentState = trans.resultingState();
			Log.d(TAG,"current state is now: "+currentState.getName());
			setData(StoryData.CURRENT_STATE, currentState.getName());
			return true;
		} else {
			Log.e(TAG,"transition not found: "+trans.getName());
			return false;
		}
	}

	public void setData(StoryData field, Object value) {
		data.put(field, value);
		modifiedFields.add(field);

		if (field == StoryData.STORY_TYPE) {
			prepareLifecycle();
		}
	}

	private void prepareLifecycle() {
		Log.d(TAG,"preparing lifecycle");
		String type = getData(StoryData.STORY_TYPE);
		if (type.equalsIgnoreCase("feature") || type.equalsIgnoreCase("bug")) {
			this.currentState = prepareLifecycleBugFeature();
		}
		if (type.equalsIgnoreCase("chore")) {
			this.currentState = prepareLifecycleChore();
		}
		if (type.equalsIgnoreCase("release")) {
			this.currentState = prepareLifecycleRelease();
		}
	}
	
	public String getData(StoryData field) {
		return (String) data.get(field);
	}
	
	/**
	 * Get data in a db friendly way
	 * 
	 * @return
	 */
	public ContentValues getDataAsContentValues() {
		ContentValues v = new ContentValues();
		
		for (StoryData f : data.keySet()) {
			// --- note: db field name is lowercase!
			if (data.get(f) != null) {
				v.put(f.getDBFieldName(), data.get(f).toString());
			} 
		}
		
	    return v;
	}

	public State getCurrentState() {
		return currentState;
	}

	/**
	 * prepares the _remaining_ lifecycle of your story
	 * starts with stories current state (thus its the remaining lifecycle)
	 * 
	 * @return current state in lifecycle
	 */
	private State prepareLifecycleChore() {
		Log.d(TAG,"preparing lifecycle for chore");
		String state = (String) data.get(StoryData.CURRENT_STATE);

		State accepted = new State("accepted");
		if (state.equalsIgnoreCase(accepted.getName())) return accepted;
		Transition finish = new Transition("finish",accepted);
		State started = new State("started",finish);
		if (state.equalsIgnoreCase(started.getName())) return started;
		Transition start = new Transition("start",started);
		State unstarted = new State("unstarted",start);

		return unstarted;
	}

	/**
	 * prepares the _remaining_ lifecycle of your story
	 * starts with stories current state (thus its the remaining lifecycle)
	 * 
	 * @return current state in lifecycle
	 */
	private State prepareLifecycleRelease() {
		Log.d(TAG,"preparing lifecycle for release");
		String state = (String) data.get(StoryData.CURRENT_STATE);

		State accepted = new State("accepted");
		if (state.equalsIgnoreCase(accepted.getName())) return accepted;
		Transition finish = new Transition("finish",accepted);
		State unstarted = new State("unstarted",finish);

		return unstarted;
	}

	/**
	 * prepares the _remaining_ lifecycle of your story
	 * starts with stories current state (thus its the remaining lifecycle)
	 * 
	 * @return current state in lifecycle
	 */
	private State prepareLifecycleBugFeature() {
		Log.d(TAG,"preparing lifecycle for bug/feature");
		String state = (String) data.get(StoryData.CURRENT_STATE);

		State accepted = new State("accepted");
		if (state.equalsIgnoreCase(accepted.getName())) return accepted;
		State rejected = new State("rejected");
		if (state.equalsIgnoreCase(rejected.getName())) return rejected;

		List<Transition> acceptreject = new ArrayList<Transition>();
		acceptreject.add(new Transition("accept", accepted));
		acceptreject.add(new Transition("reject", rejected));

		State delivered = new State("delivered",acceptreject);
		if (state.equalsIgnoreCase(delivered.getName())) return delivered;
		Transition deliver = new Transition("deliver",delivered);

		State finished = new State("finished",deliver);
		if (state.equalsIgnoreCase(finished.getName())) return finished;
		Transition finish = new Transition("finish",finished);

		State started = new State("started",finish);
		if (state.equalsIgnoreCase(started.getName())) return started;
		Transition start = new Transition("start",started);

		State unstarted = new State("unstarted",start);
	
		return unstarted;
	}
}
