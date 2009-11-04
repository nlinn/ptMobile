package me.linnemann.ptmobile.pivotaltracker;

import java.util.Set;

import me.linnemann.ptmobile.pivotaltracker.fields.StoryField;

public interface Story {

	/**
	 * get all fields that have been modified (since last reset)
	 * use this before you update data via API
	 * 
	 * @see resetModifiedFieldsTracking
	 * @return
	 */
	public abstract Set<StoryField> getModifiedFields();

	public abstract void resetModifiedFieldsTracking();

	public abstract void setEstimate(Integer estimate);

	public abstract String getNextState();

	public abstract boolean toNextState();
	
	public String getData(StoryField field);
	public void setData(StoryField field, Object value);

}