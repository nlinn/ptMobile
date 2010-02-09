package me.linnemann.ptmobile.pivotaltracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.TrackerData;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Lifecycle;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.LifecycleFactoryImpl;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public class StoryImpl implements Story {

	@SuppressWarnings("unused")
	private static final String TAG = "StoryImpl";

	private Map<TrackerData, TrackerValue> modifiedData;
	private Map<StoryData, TrackerValue> data;

	public static Story buildInstance(StoryBuilder builder) {
		return builder.getStory();
	}

	public StoryImpl() {
		this(StoryType.FEATURE);
	}

	public StoryImpl(StoryType type) {
		modifiedData = new HashMap<TrackerData, TrackerValue>();
		data = new HashMap<StoryData, TrackerValue>();
		initStoryData();
		changeStoryType(type);
		resetModifiedDataTracking();
	}

	private void initStoryData() {
		data.put(StoryData.CURRENT_STATE, State.UNSCHEDULED);
		data.put(StoryData.ESTIMATE, Estimate.NO_ESTIMATE);
		data.put(StoryData.ITERATION_NUMBER, Numeric.getEmptyValue());
		data.put(StoryData.ID, Numeric.getEmptyValue());
		data.put(StoryData.DESCRIPTION, Text.getEmptyValue());
		data.put(StoryData.LABELS, Text.getEmptyValue());
		data.put(StoryData.ACCEPTED_AT, Text.getEmptyValue());
		data.put(StoryData.REQUESTED_BY, Text.getEmptyValue());
		data.put(StoryData.OWNED_BY, Text.getEmptyValue());
		data.put(StoryData.DEADLINE, Text.getEmptyValue());
		data.put(StoryData.NAME, Text.getEmptyValue());
		data.put(StoryData.ITERATION_GROUP, Text.getEmptyValue());
		data.put(StoryData.POSITION, Numeric.getEmptyValue());
		data.put(StoryData.PROJECT_ID, Numeric.getEmptyValue());
	}
	
	public StoryType getStoryType() {
		return (StoryType) data.get(StoryData.STORY_TYPE);
	}

	public void changeStoryType(StoryType type) {

		modifiedData.put(StoryData.STORY_TYPE,type);
		StoryType oldType = getStoryType();
		
		if (!type.equals(oldType)) { // really changed?
			data.put(StoryData.STORY_TYPE, type);
			final Estimate estimate = getEstimate();
			
			if (type.equals(StoryType.FEATURE)) {
				if (Estimate.NO_ESTIMATE.equals(estimate)) {
					data.put(StoryData.ESTIMATE, Estimate.UNESTIMATED);
				}
			}  else {
				data.put(StoryData.ESTIMATE, Estimate.NO_ESTIMATE);
			}
		}
	}

	public void changeName(String name) {
		putDataAndModified(StoryData.NAME, new Text(name));
	}

	public Text getName() {
		return (Text) data.get(StoryData.NAME);
	}

	public void changeDescription(String description) {
		putDataAndModified(StoryData.DESCRIPTION, new Text(description));
	}

	public Text getDescription() {
		return (Text) data.get(StoryData.DESCRIPTION);
	}

	public void changeLabels(String labels) {
		Text newlabels = new Text(labels);
		Text oldlabels = getLabels();
		
		if ((newlabels != null) && (!newlabels.equals(oldlabels))) {
			putDataAndModified(StoryData.LABELS, newlabels);
		}
	}

	public Text getLabels() {
		return (Text) data.get(StoryData.LABELS);
	}

	public Lifecycle getLifecycle() {
		return LifecycleFactoryImpl.getLifecycleForType(getStoryType());
	}

	public Map<TrackerData, TrackerValue> getModifiedData() {
		removeEstimateFromModifiedDataIfNO_ESTIMATE();
		return modifiedData;
	}

	private void removeEstimateFromModifiedDataIfNO_ESTIMATE() {
		if (Estimate.NO_ESTIMATE.equals(getEstimate())) {
			modifiedData.remove(StoryData.ESTIMATE);
		}		
	}
	
	public void resetModifiedDataTracking() {
		modifiedData.clear();
	}

	public void changeEstimate(Estimate estimate) {
		putDataAndModified(StoryData.ESTIMATE, estimate);
	}

	public Estimate getEstimate() {		
		return (Estimate) data.get(StoryData.ESTIMATE);
	}

	public void changeId(Integer id) {
		putDataAndModified(StoryData.ID, new Numeric(id));
	}

	public Numeric getId() {
		return (Numeric) data.get(StoryData.ID);
	}

	public void changeProjectId(Integer projectId) {
		putDataAndModified(StoryData.PROJECT_ID, new Numeric(projectId));
	}

	public Numeric getProjectId() {
		return (Numeric) data.get(StoryData.PROJECT_ID);
	}

	public State getCurrentState() {
		return (State) data.get(StoryData.CURRENT_STATE);
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
		putDataAndModified(StoryData.ACCEPTED_AT, new Text(acceptedAt));
	}

	public void changeCreatedAt(String createdAt) {
		putDataAndModified(StoryData.CREATED_AT, new Text(createdAt));
	}

	public void changeCurrentState(State state) {
		putDataAndModified(StoryData.CURRENT_STATE, state);
	}

	public void changeDeadline(String deadline) {
		putDataAndModified(StoryData.DEADLINE, new Text(deadline));
	}

	public void changeIterationGroup(String iterationGroup) {
		putDataAndModified(StoryData.ITERATION_GROUP, new Text(iterationGroup));
	}

	public void changeIterationNumber(Integer iterationNumber) {
		putDataAndModified(StoryData.ITERATION_NUMBER, new Numeric(iterationNumber));
	}

	public void changeOwnedBy(String ownedBy) {
		putDataAndModified(StoryData.OWNED_BY, new Text(ownedBy));
	}

	public void changeRequestedBy(String requestedBy) {
		putDataAndModified(StoryData.REQUESTED_BY, new Text(requestedBy));
	}

	public void changePosition(Integer position) {
		putDataAndModified(StoryData.POSITION, new Numeric(position));
	}

	public Numeric getPosition() {
		return (Numeric) data.get(StoryData.POSITION);
	}

	public boolean isFirstInIteration() {
		Numeric position = getPosition();
		return ((!position.isEmpty()) && (position.getValue() == 1));
	}

	public Text getAcceptedAt() {
		return (Text) data.get(StoryData.ACCEPTED_AT);
	}

	public Text getCreatedAt() {
		return (Text) data.get(StoryData.CREATED_AT);
	}

	public Text getDeadline() {
		return (Text) data.get(StoryData.DEADLINE);
	}

	public Text getIterationGroup() {
		return (Text) data.get(StoryData.ITERATION_GROUP);
	}

	public Numeric getIterationNumber() {
		return (Numeric) data.get(StoryData.ITERATION_NUMBER);
	}

	public Text getOwnedBy() {
		return (Text) data.get(StoryData.OWNED_BY);
	}

	public Text getRequestedBy() {
		return (Text) data.get(StoryData.REQUESTED_BY);
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
	
	private void putDataAndModified(StoryData key, TrackerValue value) {
		TrackerValue oldValue = data.put(key, value);

		// --- put into modified, only if data has really changed
		if ((oldValue == null) || (!oldValue.equals(value))) {
			modifiedData.put(key, value);
		}
	}

	public Map<StoryData, TrackerValue> getData() {
		return data;
	}
}