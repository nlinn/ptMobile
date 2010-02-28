package me.linnemann.ptmobile.pivotaltracker.datatype;

import java.util.HashMap;


public class IterationDataType extends DataType {

	public static DataType ID, NUMBER, START, FINISH, PROJECT_ID, ITERATION_GROUP;
	public static HashMap<String, DataType> KNOWNTYPES = new HashMap<String, DataType>();
	

	static {
		ID = new IterationDataType("id",VALUE_TYPE_NUMERIC);
		NUMBER = new IterationDataType("number",VALUE_TYPE_NUMERIC);
		START = new IterationDataType("start",VALUE_TYPE_DATETIME);
		FINISH = new IterationDataType("finish",VALUE_TYPE_DATETIME);
		PROJECT_ID = new IterationDataType("project_id",VALUE_TYPE_NUMERIC);
		ITERATION_GROUP = new IterationDataType("iteration_group",VALUE_TYPE_TEXT);
	}
	
	private IterationDataType(String xmlFieldAndColName, int valueType) {
		this(xmlFieldAndColName, xmlFieldAndColName, valueType);
	}
	
	private IterationDataType(String xmlFieldName, String colName, int valueType) {
		super(xmlFieldName, colName,valueType);
		KNOWNTYPES.put(xmlFieldName, this);
	}
}
