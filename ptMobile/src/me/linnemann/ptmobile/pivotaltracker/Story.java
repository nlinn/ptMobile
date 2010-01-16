package me.linnemann.ptmobile.pivotaltracker;

import java.util.List;
import java.util.Set;

import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.IntegerValue;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.pivotaltracker.value.StringValue;

public interface Story {

	public IntegerValue getId();
	public void changeId(Integer id);

	public IntegerValue getProjectId();
	public void changeProjectId(Integer projectId);
	
	public StringValue getName();
	public void changeName(String name);
	
	public IntegerValue getIterationNumber();
	public void changeIterationNumber(Integer iterationNumber);
	
	public Estimate getEstimate();
	public void changeEstimate(Estimate estimate);

	public StoryType getStoryType();
	public void changeStoryType(StoryType type);
	
	public StringValue getLabels();
	public void changeLabels(String labels);
	
	public State getCurrentState();
	public void changeCurrentState(State state); // please consider changing state via lifecycle!
	
	public List<Transition> getTransitions();
	public void applyTransition(Transition trans);
	
	public StringValue getDescription();
	public void changeDescription(String description);
	
	public StringValue getDeadline();
	public void changeDeadline(String deadline);
	
	public StringValue getRequestedBy();
	public void changeRequestedBy(String requestedBy);

	public StringValue getOwnedBy();
	public void changeOwnedBy(String ownedBy);
	
	public StringValue getCreatedAt();
	public void changeCreatedAt(String createdAt);

	public StringValue getAcceptedAt();
	public void changeAcceptedAt(String acceptedAt);

	public StringValue getIterationGroup();
	public void changeIterationGroup(String iterationGroup);

	public Set<StoryData> getModifiedFields();
	public void resetModifiedFieldsTracking();
	public boolean needsEstimate();
}