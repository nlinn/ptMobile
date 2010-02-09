package me.linnemann.ptmobile.pivotaltracker;

import java.util.List;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.TrackerData;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public interface Story {

	public Numeric getId();
	public void changeId(Integer id);

	public Numeric getProjectId();
	public void changeProjectId(Integer projectId);
	
	public Text getName();
	public void changeName(String name);
	
	public Numeric getIterationNumber();
	public void changeIterationNumber(Integer iterationNumber);
	
	public Estimate getEstimate();
	public void changeEstimate(Estimate estimate);

	public StoryType getStoryType();
	public void changeStoryType(StoryType type);
	
	public Text getLabels();
	public void changeLabels(String labels);
	
	public State getCurrentState();
	public void changeCurrentState(State state);
	
	public List<Transition> getTransitions();
	public void applyTransition(Transition trans);
	
	public Text getDescription();
	public void changeDescription(String description);
	
	public Text getDeadline();
	public void changeDeadline(String deadline);
	
	public Text getRequestedBy();
	public void changeRequestedBy(String requestedBy);

	public Text getOwnedBy();
	public void changeOwnedBy(String ownedBy);
	
	public Text getCreatedAt();
	public void changeCreatedAt(String createdAt);

	public Text getAcceptedAt();
	public void changeAcceptedAt(String acceptedAt);

	public Text getIterationGroup();
	public void changeIterationGroup(String iterationGroup);

	public Numeric getPosition();
	public void changePosition(Integer position);
	public boolean isFirstInIteration();
	
	public Map<StoryData, TrackerValue> getData();
	public Map<TrackerData, TrackerValue> getModifiedData();
	public void resetModifiedDataTracking();
	public boolean needsEstimate();
}