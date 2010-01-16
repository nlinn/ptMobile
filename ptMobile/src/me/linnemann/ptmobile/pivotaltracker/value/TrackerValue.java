package me.linnemann.ptmobile.pivotaltracker.value;

public interface TrackerValue {
	public String getValueAsString();
	public String getUIString();
	public Object getValue();
	public boolean isEmpty();
}
