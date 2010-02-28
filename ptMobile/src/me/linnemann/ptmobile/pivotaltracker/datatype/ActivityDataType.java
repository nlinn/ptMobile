package me.linnemann.ptmobile.pivotaltracker.datatype;

import java.util.HashMap;


public class ActivityDataType extends DataType {

	public static DataType ID, VERSION, EVENT_TYPE, OCCURRED_AT, AUTHOR, PROJECT_ID, DESCRIPTION;
	public static HashMap<String, DataType> KNOWNTYPES = new HashMap<String, DataType>();

	static {
		ID = new ActivityDataType("id",VALUE_TYPE_NUMERIC);
		VERSION = new ActivityDataType("version",VALUE_TYPE_NUMERIC);
		EVENT_TYPE = new ActivityDataType("event_type",VALUE_TYPE_TEXT);
		OCCURRED_AT = new ActivityDataType("occurred_at",VALUE_TYPE_DATETIME);
		AUTHOR = new ActivityDataType("author",VALUE_TYPE_TEXT);
		PROJECT_ID = new ActivityDataType("project_id",VALUE_TYPE_NUMERIC);
		DESCRIPTION = new ActivityDataType("description",VALUE_TYPE_TEXT);
	}

	private ActivityDataType(String xmlFieldAndColName, int valueType) {
		this(xmlFieldAndColName, xmlFieldAndColName, valueType);
	}
	
	private ActivityDataType(String xmlFieldName, String colName, int valueType) {
		super(xmlFieldName, colName, valueType);
		KNOWNTYPES.put(xmlFieldName, this);
	}
}