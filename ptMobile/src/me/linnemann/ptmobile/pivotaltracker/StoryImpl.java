package me.linnemann.ptmobile.pivotaltracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Lifecycle;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.LifecycleFactoryImpl;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.IntegerValue;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.pivotaltracker.value.StringValue;

public class StoryImpl implements Story {

	@SuppressWarnings("unused")
	private static final String TAG = "StoryImpl";

	private Set<StoryData> modifiedFields;
	private State state;
	private StoryType type;
	private Estimate estimate;
	private String name;
	private Integer id;
	private Integer projectId;
	private String description;
	private String labels;
	private String acceptedAt;
	private String createdAt;
	private String ownedBy;
	private String requestedBy;
	private String deadline;
	private Integer iterationNumber;
	private String iterationGroup;
	private Integer position;

	public static Story buildInstance(StoryBuilder builder) {
		builder.construct();
		return builder.getStory();
	}

	public StoryImpl() {
		this(StoryType.FEATURE);
	}

	public StoryImpl(StoryType type) {
		state = State.UNSCHEDULED;
		estimate = Estimate.NO_ESTIMATE;
		modifiedFields = new HashSet<StoryData>();	
		changeStoryType(type);
		resetModifiedFieldsTracking();
	}

	public StoryType getStoryType() {
		return type;
	}

	public void changeStoryType(StoryType type) {
		
			this.type = type;
			modifiedFields.add(StoryData.STORY_TYPE);
		
			if (type.equals(StoryType.FEATURE)) {
				if (Estimate.NO_ESTIMATE.equals(estimate)) {
					estimate =Estimate.UNESTIMATED;
				}
			} else {
				estimate =Estimate.NO_ESTIMATE;
			}
		
		
	}

	public void changeName(String name) {
		if (!name.equals(this.name)) {
			this.name = name;
			modifiedFields.add(StoryData.NAME);
		}
	}

	public StringValue getName() {
		return new StringValue(name);
	}

	public void changeDescription(String description) {
		if (!description.equals(this.description)) {
			this.description = description;
			modifiedFields.add(StoryData.DESCRIPTION);
		}
	}

	public StringValue getDescription() {
		return new StringValue(description);
	}

	public void changeLabels(String labels) {
		if ((labels != null) && (!labels.equals(this.labels))) {
			this.labels = labels;
			modifiedFields.add(StoryData.LABELS);
		}
	}

	public StringValue getLabels() {
		return new StringValue(labels);
	}

	public Lifecycle getLifecycle() {
		return LifecycleFactoryImpl.getLifecycleForType(type);
	}

	public Set<StoryData> getModifiedFields() {
		return modifiedFields;
	}

	public void resetModifiedFieldsTracking() {
		modifiedFields.clear();
	}

	public void changeEstimate(Estimate estimate) {
		
		// --- no estimate allowed if not feature
		if ((!StoryType.FEATURE.equals(type)) && 
		(!Estimate.NO_ESTIMATE.equals(estimate))) {
			throw new RuntimeException("Cannot change estimate if story type: "+type);
		}

		this.estimate = estimate;
		modifiedFields.add(StoryData.ESTIMATE);

	}

	public Estimate getEstimate() {		
		return estimate;
	}

	public void changeId(Integer id) {
		if (!id.equals(this.id)) {
		this.id = id;
		modifiedFields.add(StoryData.ID);
		}
	}

	public IntegerValue getId() {
		return new IntegerValue(id);
	}

	public void changeProjectId(Integer projectId) {
		if (!projectId.equals(this.projectId)) {
		this.projectId = projectId;
		modifiedFields.add(StoryData.PROJECT_ID);
		}
	}

	public IntegerValue getProjectId() {
		return new IntegerValue(projectId);
	}

	public State getCurrentState() {
		return state;
	}


	/**
	 * Features are supposed to get estimated before they get started
	 * @return
	 */
	public boolean needsEstimate() {
		return (StoryType.FEATURE.equals(this.type) && (Estimate.UNESTIMATED.equals(estimate)));
	}

	public void changeAcceptedAt(String acceptedAt) {
		this.acceptedAt = acceptedAt;
		modifiedFields.add(StoryData.ACCEPTED_AT);
	}

	public void changeCreatedAt(String createdAt) {
		this.createdAt = createdAt;
		modifiedFields.add(StoryData.CREATED_AT);
	}

	public void changeCurrentState(State state) {
		this.state = state;
		modifiedFields.add(StoryData.CURRENT_STATE);
	}

	public void changeDeadline(String deadline) {
		this.deadline = deadline;
		modifiedFields.add(StoryData.DEADLINE);
	}

	public void changeIterationGroup(String iterationGroup) {
		this.iterationGroup = iterationGroup;
		modifiedFields.add(StoryData.ITERATION_GROUP);		
	}

	public void changeIterationNumber(Integer iterationNumber) {
		this.iterationNumber = iterationNumber;
		modifiedFields.add(StoryData.ITERATION_NUMBER);
	}

	public void changeOwnedBy(String ownedBy) {
		this.ownedBy = ownedBy;
		modifiedFields.add(StoryData.OWNED_BY);
	}

	public void changeRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
		modifiedFields.add(StoryData.REQUESTED_BY);
	}

	public void changePosition(Integer position) {
		this.position = position;
		modifiedFields.add(StoryData.POSITION);
	}
	
	public IntegerValue getPosition() {
		return new IntegerValue(position);
	}
	
	public boolean isFirstInIteration() {
		return ((this.position != null) && (this.position == 1));
	}
	
	public StringValue getAcceptedAt() {
		return new StringValue(acceptedAt);
	}

	public StringValue getCreatedAt() {
		return new StringValue(createdAt);
	}

	public StringValue getDeadline() {
		return new StringValue(deadline);
	}

	public StringValue getIterationGroup() {
		return new StringValue(iterationGroup);
	}

	public IntegerValue getIterationNumber() {
		return new IntegerValue(iterationNumber);
	}

	public StringValue getOwnedBy() {
		return new StringValue(ownedBy);
	}

	public StringValue getRequestedBy() {
		return new StringValue(requestedBy);
	}

	public void applyTransition(Transition trans) {
		changeCurrentState(trans.resultingState().getState());
	}

	public List<Transition> getTransitions() {
		if (needsEstimate())
			return new ArrayList<Transition>();
		else
			return getLifecycle().getAvailableTransitions(state);
	}	
}