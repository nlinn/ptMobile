package me.linnemann.ptmobile.pivotaltracker.fields;

import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public interface TrackerData {
	public TrackerValue getValueFromString(String from);
	public String getDBFieldName();
	public String getXMLWrappedValue(TrackerValue value);
}
