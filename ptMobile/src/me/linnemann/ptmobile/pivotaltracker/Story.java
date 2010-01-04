package me.linnemann.ptmobile.pivotaltracker;

import java.util.Set;

import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Lifecycle;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;

public interface Story {

	public Integer getId();
	public void changeId(Integer id);

	public Integer getProjectId();
	public void changeProjectId(Integer projectId);
	
	public String getName();
	public void changeName(String name);
	
	public Integer getIterationNumber();
	public void changeIterationNumber(Integer iterationNumber);
	
	public Integer getEstimate();
	public void changeEstimate(Integer estimate);

	public StoryType getStoryType();
	public void changeStoryType(StoryType type);
	
	public String getLabels();
	public void changeLabels(String labels);
	
	public State getCurrentState();
	public void changeCurrentState(State state); // please consider changing state via lifecycle!
	
	public String getDescription();
	public void changeDescription(String description);
	
	public String getDeadline();
	public void changeDeadline(String deadline);
	
	public String getRequestedBy();
	public void changeRequestedBy(String requestedBy);

	public String getOwnedBy();
	public void changeOwnedBy(String ownedBy);
	
	public String getCreatedAt();
	public void changeCreatedAt(String createdAt);

	public String getAcceptedAt();
	public void changeAcceptedAt(String acceptedAt);

	public String getIterationGroup();
	public void changeIterationGroup(String iterationGroup);

	public Set<StoryData> getModifiedFields();
	public void resetModifiedFieldsTracking();
	public Lifecycle getLifecycle();
	public boolean needsEstimate();
}