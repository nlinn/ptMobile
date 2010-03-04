package me.linnemann.ptmobile.pivotaltracker;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataType;
import me.linnemann.ptmobile.pivotaltracker.datatype.StoryDataType;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Lifecycle;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.LifecycleFactoryImpl;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.pivotaltracker.value.DateTime;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public class Story  extends TrackerEntity {

	private static final String TAG = "StoryImpl";
	private DBAdapter db;
	
	public Story(DBAdapter db) {
		this(db,StoryType.CHORE);
	}
	
	public Story() {
		this(null,StoryType.CHORE);
	}
	
	public Story(StoryType type) {
		this(null,type);
	}

	public Story(DBAdapter db, StoryType type) {
		initStoryData();
		this.db = db;
		changeStoryType(type);
	}
	
	private void initStoryData() {
		
		putDataAndTrackChanges(StoryDataType.NAME, Text.getEmptyValue());
		putDataAndTrackChanges(StoryDataType.CURRENT_STATE, State.UNSCHEDULED);
		putDataAndTrackChanges(StoryDataType.ESTIMATE, Estimate.NO_ESTIMATE);
		putDataAndTrackChanges(StoryDataType.ITERATION_NUMBER, Numeric.getEmptyValue());
		putDataAndTrackChanges(StoryDataType.ID, Numeric.getEmptyValue());
		putDataAndTrackChanges(StoryDataType.DESCRIPTION, Text.getEmptyValue());
		putDataAndTrackChanges(StoryDataType.LABELS, Text.getEmptyValue());
		putDataAndTrackChanges(StoryDataType.ACCEPTED_AT, Text.getEmptyValue());
		putDataAndTrackChanges(StoryDataType.REQUESTED_BY, Text.getEmptyValue());
		putDataAndTrackChanges(StoryDataType.OWNED_BY, Text.getEmptyValue());
		putDataAndTrackChanges(StoryDataType.POSITION, Numeric.getEmptyValue());
		putDataAndTrackChanges(StoryDataType.PROJECT_ID, Numeric.getEmptyValue());
		resetModifiedDataTracking();
		// TODO: this is a bit strange: story type must be marked modified... because of add new story
		putDataAndTrackChanges(StoryDataType.STORY_TYPE, StoryType.CHORE);
	}
	
	public StoryType getStoryType() {
		return (StoryType) data.get(StoryDataType.STORY_TYPE);
	}

	public void changeStoryType(StoryType type) {
		putDataAndTrackChanges(StoryDataType.STORY_TYPE, type);
		
		if ( type.equals(StoryType.FEATURE) && getEstimate().equals(Estimate.NO_ESTIMATE) ) {
			putDataAndTrackChanges(StoryDataType.ESTIMATE, Estimate.UNESTIMATED);
		}
		
	
	}
	
	@Override
	public void putDataAndTrackChanges(DataType key, TrackerValue value) {
		super.putDataAndTrackChanges(key, value);
		
		// --- rule: Release and Bugs never have estimates
		if ((value.equals(StoryType.RELEASE) || value.equals(StoryType.BUG) )) {
			Log.i(TAG,"Release or Bug -> no estimate");
			putDataAndTrackChanges(StoryDataType.ESTIMATE, Estimate.NO_ESTIMATE);
		}
	}

	public void changeName(String name) {
		putDataAndTrackChanges(StoryDataType.NAME, new Text(name));
	}

	public Text getName() {
		return (Text) data.get(StoryDataType.NAME);
	}

	public void changeDescription(String description) {
		putDataAndTrackChanges(StoryDataType.DESCRIPTION, new Text(description));
	}

	public Text getDescription() {
		return (Text) data.get(StoryDataType.DESCRIPTION);
	}

	public void changeLabels(String labels) {
		Text newlabels = new Text(labels);
		Text oldlabels = getLabels();
		
		if ((newlabels != null) && (!newlabels.equals(oldlabels))) {
			putDataAndTrackChanges(StoryDataType.LABELS, newlabels);
		}
	}

	public Text getLabels() {
		return (Text) data.get(StoryDataType.LABELS);
	}

	public Lifecycle getLifecycle() {
		return LifecycleFactoryImpl.getLifecycleForType(getStoryType());
	}

	public void changeEstimate(Estimate estimate) {
		putDataAndTrackChanges(StoryDataType.ESTIMATE, estimate);
	}

	public Estimate getEstimate() {		
		return (Estimate) data.get(StoryDataType.ESTIMATE);
	}

	public void changeId(Integer id) {
		putDataAndTrackChanges(StoryDataType.ID, new Numeric(id));
	}

	public Numeric getId() {
		return (Numeric) data.get(StoryDataType.ID);
	}

	public void changeProjectId(Integer projectId) {
		putDataAndTrackChanges(StoryDataType.PROJECT_ID, new Numeric(projectId));
	}

	public Numeric getProjectId() {
		return (Numeric) data.get(StoryDataType.PROJECT_ID);
	}

	public State getCurrentState() {
		return (State) data.get(StoryDataType.CURRENT_STATE);
	}


	/**
	 * Features are supposed to get estimated before they get started
	 * @return
	 */
	public boolean needsEstimate() {
		Estimate estimate = getEstimate();
		StoryType type = getStoryType();
		return (StoryType.FEATURE.equals(type) && (Estimate.UNESTIMATED.equals(estimate)));
	}

	public void changeAcceptedAt(String acceptedAt) {
		putDataAndTrackChanges(StoryDataType.ACCEPTED_AT, new Text(acceptedAt));
	}

	public void changeCreatedAt(String createdAt) {
		putDataAndTrackChanges(StoryDataType.CREATED_AT, new Text(createdAt));
	}

	public void changeCurrentState(State state) {
		putDataAndTrackChanges(StoryDataType.CURRENT_STATE, state);
	}

	public void changeDeadline(String deadline) {
		putDataAndTrackChanges(StoryDataType.DEADLINE, new DateTime(deadline));
	}

	public void changeIterationGroup(String iterationGroup) {
		putDataAndTrackChanges(StoryDataType.ITERATION_GROUP, new Text(iterationGroup));
	}

	public void changeIterationNumber(Integer iterationNumber) {
		putDataAndTrackChanges(StoryDataType.ITERATION_NUMBER, new Numeric(iterationNumber));
	}

	public void changeOwnedBy(String ownedBy) {
		putDataAndTrackChanges(StoryDataType.OWNED_BY, new Text(ownedBy));
	}

	public void changeRequestedBy(String requestedBy) {
		putDataAndTrackChanges(StoryDataType.REQUESTED_BY, new Text(requestedBy));
	}

	public void changePosition(Integer position) {
		putDataAndTrackChanges(StoryDataType.POSITION, new Numeric(position));
	}

	public Numeric getPosition() {
		return (Numeric) data.get(StoryDataType.POSITION);
	}

	public boolean isFirstInIteration() {
		Numeric position = getPosition();
		return ((!position.isEmpty()) && (position.getValue() == 1));
	}

	public DateTime getAcceptedAt() {
		return (DateTime) data.get(StoryDataType.ACCEPTED_AT);
	}

	public DateTime getCreatedAt() {
		return (DateTime) data.get(StoryDataType.CREATED_AT);
	}

	public DateTime getDeadline() {
		DateTime deadline = (DateTime) data.get(StoryDataType.DEADLINE);
		if (deadline == null) {
			deadline = DateTime.getEmptyValue();
		}
		return deadline;
	}

	public Text getIterationGroup() {
		return (Text) data.get(StoryDataType.ITERATION_GROUP);
	}

	public Numeric getIterationNumber() {
		return (Numeric) data.get(StoryDataType.ITERATION_NUMBER);
	}

	public Text getOwnedBy() {
		return (Text) data.get(StoryDataType.OWNED_BY);
	}

	public Text getRequestedBy() {
		return (Text) data.get(StoryDataType.REQUESTED_BY);
	}

	public void applyTransition(Transition trans) {
		changeCurrentState(trans.getResultingState());
	}

	public List<Transition> getTransitions() {
		if (needsEstimate())
			return new ArrayList<Transition>();
		else
			return getLifecycle().getAvailableTransitions(getCurrentState());
	}

	@Override
	public String getTableName() {
		return "stories";
	}	
	
	public Iteration getIteration() {
		return db.getIteration(getProjectId(), getIterationNumber());
	}
	
	public Project getProject() {
		return db.getProject(getProjectId().getValue());
	}
}