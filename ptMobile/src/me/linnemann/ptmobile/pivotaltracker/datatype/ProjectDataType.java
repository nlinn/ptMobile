package me.linnemann.ptmobile.pivotaltracker.datatype;

import java.util.HashMap;

public class ProjectDataType extends DataType {

	public static DataType ID, NAME, USE_HTTPS, ITERATION_LENGTH, WEEK_START_DAY, POINT_SCALE, CURRENT_VELOCITY, LABELS;
	public static HashMap<String, DataType> KNOWNTYPES = new HashMap<String, DataType>();
	
	static {
		ID = new ProjectDataType("id",VALUE_TYPE_NUMERIC);
		NAME = new ProjectDataType("name",VALUE_TYPE_TEXT);
		ITERATION_LENGTH = new ProjectDataType("iteration_length",VALUE_TYPE_TEXT);
		WEEK_START_DAY = new ProjectDataType("week_start_day",VALUE_TYPE_TEXT);
		POINT_SCALE = new ProjectDataType("point_scale",VALUE_TYPE_TEXT);
		CURRENT_VELOCITY = new ProjectDataType("current_velocity",VALUE_TYPE_NUMERIC);
		LABELS = new ProjectDataType("labels",VALUE_TYPE_TEXT);
		USE_HTTPS = new ProjectDataType("use_https",VALUE_TYPE_TEXT);
	}

	private ProjectDataType(String xmlFieldAndColName, int valueType) {
		this(xmlFieldAndColName,xmlFieldAndColName, valueType);
	}

	private ProjectDataType(String xmlFieldName, String colName, int valueType) {
		super(xmlFieldName, colName,valueType);
		KNOWNTYPES.put(xmlFieldName, this);
	}
}