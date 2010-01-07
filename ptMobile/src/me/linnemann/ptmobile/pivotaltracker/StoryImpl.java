package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashSet;
import java.util.Set;

import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Lifecycle;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.LifecycleFactoryImpl;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;

public class StoryImpl implements Story {

	@SuppressWarnings("unused")
	private static final String TAG = "StoryImpl";

	private Set<StoryData> modifiedFields;
	private State initialState;
	private StoryType type;
	private Lifecycle lifecycle;
	private Integer estimate;
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
	
	public static Story buildInstance(StoryBuilder builder) {
		return builder.getStory();
	}
	
	public StoryImpl() {
		this(StoryType.UNKNOWN);
	}
	
	public StoryImpl(StoryType type) {
		this.type = type;
		initialState = State.UNSTARTED;
		modifiedFields = new HashSet<StoryData>();
	}
	
	public StoryType getStoryType() {
		return type;
	}
	
	public void changeStoryType(StoryType type) {
		this.type = type;
		modifiedFields.add(StoryData.STORY_TYPE);
	}
	
	public void changeInitialState(State state) {
		this.initialState = state;
		modifiedFields.add(StoryData.CURRENT_STATE);
	}
	
	public void changeName(String name) {
		this.name = name;
		modifiedFields.add(StoryData.NAME);
	}

	public String getName() {
		return name;
	}
	
	public void changeDescription(String description) {
		this.description = description;
		modifiedFields.add(StoryData.DESCRIPTION);
	}

	public String getDescription() {
		return description;
	}
	
	public void changeLabels(String labels) {
		this.labels = labels;
		modifiedFields.add(StoryData.LABELS);
	}

	public String getLabels() {
		return labels;
	}
	
	public Lifecycle getLifecycle() {
		if (lifecycle == null)
			lifecycle = new LifecycleFactoryImpl().getRemainingLifecycle(type, initialState);
		return lifecycle;
	}
	
	public Set<StoryData> getModifiedFields() {
		return modifiedFields;
	}

	public void resetModifiedFieldsTracking() {
		modifiedFields.clear();
	}

	public void changeEstimate(Integer estimate) {
		this.estimate = estimate;
		modifiedFields.add(StoryData.ESTIMATE);
		getLifecycle().unblockStartTransition();
	}

	public Integer getEstimate() {
		return estimate;
	}

	public void changeId(Integer id) {
		this.id = id;
		modifiedFields.add(StoryData.ID);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void changeProjectId(Integer projectId) {
		this.projectId = projectId;
		modifiedFields.add(StoryData.PROJECT_ID);
	}
	
	public Integer getProjectId() {
		return projectId;
	}
	
	public State getCurrentState() {
		lifecycle = getLifecycle();
		return lifecycle.getState();
	}
	
	public State getInitialState() {
		return initialState;
	}

	/**
	 * Features are supposed to get estimated before they get started
	 * @return
	 */
	public boolean needsEstimate() {
		return (StoryType.FEATURE.equals(this.type) && ((getEstimate() == null) || (getEstimate() < 0)));
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
		this.initialState = state;
		lifecycle = null;	// wipe lifecycle, needs to be recreated
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

	public String getAcceptedAt() {
		return acceptedAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getDeadline() {
		return deadline;
	}

	public String getIterationGroup() {
		return iterationGroup;
	}

	public Integer getIterationNumber() {
		return iterationNumber;
	}

	public String getOwnedBy() {
		return ownedBy;
	}

	public String getRequestedBy() {
		return requestedBy;
	}	
}