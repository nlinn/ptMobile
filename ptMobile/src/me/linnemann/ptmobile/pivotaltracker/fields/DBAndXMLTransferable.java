package me.linnemann.ptmobile.pivotaltracker.fields;

import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public interface DBAndXMLTransferable {
	public String getDBFieldName();
	public String getXMLWrappedValue(TrackerValue value);
}
