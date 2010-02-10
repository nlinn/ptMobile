package me.linnemann.ptmobile.pivotaltracker.fields;

import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public class ActivityDataType extends DataType {

	public static DataType ID, PROJECT, STORY, DESCRIPTION, AUTHOR, WHEN;
	private String dbColName;
	private int valueType;
	private static final int NUMERIC = 0;
	private static final int TEXT = 1;

	static {
		ID = new ActivityDataType("id",0);
		PROJECT = new ActivityDataType("project",1);
		STORY = new ActivityDataType("story",1);
		DESCRIPTION = new ActivityDataType("description",1);
		AUTHOR = new ActivityDataType("author",1);
		WHEN = new ActivityDataType("_when",1);
	}

	private ActivityDataType(String colName, int valueType) {
		this.dbColName = colName;
		this.valueType = valueType;
	}
	
	// TODO: wipe that stupid TrackerValue interface
	public String getDBFieldName() {
		return getDBColName();
	}

	public String getDBColName() {
		return dbColName;
	}

	public TrackerValue getValueFromString(String from) {
		TrackerValue value;
		switch(valueType) {
		case NUMERIC:
			value = new Numeric(from);
			break;
		case TEXT:
		default:
			value = new Text(from);
		}
		return value;
	}
}