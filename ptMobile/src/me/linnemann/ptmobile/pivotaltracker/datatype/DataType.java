package me.linnemann.ptmobile.pivotaltracker.datatype;

import me.linnemann.ptmobile.pivotaltracker.value.DateTime;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

// TODO: check if trackerdata is needed or just legacy stuff
public class DataType {

	private int valueType;
	private String dbColName;
	private String xmlTag;

	protected static final int VALUE_TYPE_NUMERIC = 0;
	protected static final int VALUE_TYPE_TEXT = 1;
	protected static final int VALUE_TYPE_ESTIMATE = 2;
	protected static final int VALUE_TYPE_STATE = 3;
	protected static final int VALUE_TYPE_STORYTYPE = 4;
	protected static final int VALUE_TYPE_DATETIME = 5;

	protected DataType(String xmlTag, String dbColName, int valueType) {
		this.valueType = valueType;
		this.dbColName = dbColName;
		this.xmlTag = xmlTag;
	}

	public TrackerValue getValueFromString(String from) {
		TrackerValue value;
		switch(valueType) {
		case VALUE_TYPE_STORYTYPE:
			value = StoryType.valueOf(from.toUpperCase());
			break;
		case VALUE_TYPE_STATE:
			value = State.valueOf(from.toUpperCase());
			break;
		case VALUE_TYPE_ESTIMATE:
			value = Estimate.valueOfNumeric(new Integer(from));
			break;
		case VALUE_TYPE_NUMERIC:
			value = new Numeric(from);
			break;
		case VALUE_TYPE_DATETIME:
			value = new DateTime(from);
			break;
		case VALUE_TYPE_TEXT:
		default:
			value = new Text(from);
		}
		return value;
	}

	public String getDBColName() {
		return dbColName;
	}

	public String getXMLWrappedValue(TrackerValue value) {
		return value.getXMLWrappedValue(xmlTag);
	}
}

