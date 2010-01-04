package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.List;

public interface Lifecycle {

	/**
	 * get all possible transitions for this story
	 * transition is an action pushing the story from
	 * on state to another.
	 * 
	 * it can be more than one transition available to
	 * choose from (e.g. a delivered feature has 2 transitions
	 * "accept" and "reject") 
	 * 
	 * E.g. transition "start" is available in state
	 * "unstarted" and leads to state "started"
	 * 
	 * Please Note: Lifecycles of feature/release/bug differ
	 *  
	 * @return list of available transitions
	 */
	public List<Transition> getAvailableTransitions();
	
	/**
	 * performs a specific transition, leading to a new state
	 * make sure to choose only from available transitions
	 * @param trans transition to apply
	 * @see getAvailableTransitions()
	 * @return true if successful
	 */
	public boolean doTransition(Transition trans);
	
	public State getState();
	
	public void unblockStartTransition();
	
}
