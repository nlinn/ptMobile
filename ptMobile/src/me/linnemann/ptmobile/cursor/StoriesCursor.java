package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;

public interface StoriesCursor {

	public abstract String getName();

	public abstract Integer getId();

	public abstract String getStoryType();

	public abstract Estimate getEstimate();

	public abstract String getLabels();

	public abstract State getCurrentState();

	public abstract String getDescription();

	public abstract String getRequestedBy();

	public abstract String getOwnedBy();

	public abstract String getDeadline();

	public abstract Integer getIterationNumber();

	public abstract Integer getProjectId();

	public abstract String getIterationStart();

	public abstract String getIterationFinish();

	public abstract Story getStory();

	public abstract boolean hasDescription();

	public abstract boolean hasDeadline();

	public abstract boolean hasLabels();

	public abstract boolean hasRequestedBy();

	public abstract boolean hasOwnedBy();

	public abstract boolean isIterationStarter();

	public abstract String getIterationGroup();
	
	public abstract String getAcceptedAt();
	
	public abstract String getCreatedAt();
}