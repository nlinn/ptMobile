package me.linnemann.ptmobile.pivotaltracker;

import java.util.List;
import java.util.Set;

import android.content.ContentValues;

import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.state.Transition;

public interface Story {

	/**
	 * get all fields that have been modified (since last reset)
	 * use this before you update data via API
	 * 
	 * @see resetModifiedFieldsTracking
	 * @return
	 */
	public abstract Set<StoryData> getModifiedFields();

	public abstract void resetModifiedFieldsTracking();

	public abstract void setEstimate(Integer estimate);

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
	
	public String getData(StoryData field);
	public void setData(StoryData field, Object value);
	
	public ContentValues getDataAsContentValues();

	public Integer getEstimate();
	
	/**
	 * Features are supposed to get estimated before they get started
	 * @return
	 */
	public boolean needsEstimate();
}