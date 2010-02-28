package me.linnemann.ptmobile.pivotaltracker.datatype;

import java.util.HashMap;

public class StoryDataType extends DataType {
	
	public static DataType ID, NAME, ESTIMATE, STORY_TYPE, LABELS, CURRENT_STATE, DESCRIPTION, REQUESTED_BY, OWNED_BY,
	CREATED_AT, ACCEPTED_AT, DEADLINE, ITERATION_NUMBER, PROJECT_ID, ITERATION_GROUP, POSITION;
	
	public static HashMap<String, DataType> KNOWNTYPES = new HashMap<String, DataType>();
	
	static {
		ID = new StoryDataType("id",VALUE_TYPE_NUMERIC);
		NAME = new StoryDataType("name",VALUE_TYPE_TEXT);
		ESTIMATE = new StoryDataType("estimate",VALUE_TYPE_ESTIMATE);
		STORY_TYPE = new StoryDataType("story_type",VALUE_TYPE_STORYTYPE);
		LABELS = new StoryDataType("labels",VALUE_TYPE_TEXT);
		CURRENT_STATE = new StoryDataType("current_state",VALUE_TYPE_STATE);
		DESCRIPTION = new StoryDataType("description",VALUE_TYPE_TEXT);
		REQUESTED_BY = new StoryDataType("requested_by",VALUE_TYPE_TEXT);
		OWNED_BY = new StoryDataType("owned_by",VALUE_TYPE_TEXT);
		CREATED_AT = new StoryDataType("created_at",VALUE_TYPE_DATETIME);
		ACCEPTED_AT = new StoryDataType("accepted_at",VALUE_TYPE_DATETIME);
		DEADLINE = new StoryDataType("deadline",VALUE_TYPE_DATETIME);
		ITERATION_NUMBER = new StoryDataType("iteration_number",VALUE_TYPE_NUMERIC);
		PROJECT_ID = new StoryDataType("project_id",VALUE_TYPE_NUMERIC);
		ITERATION_GROUP = new StoryDataType("iteration_group",VALUE_TYPE_TEXT);
		POSITION = new StoryDataType("position",VALUE_TYPE_NUMERIC);
	}
	
	private StoryDataType(String xmlFieldAndColName, int valueType) {
		this(xmlFieldAndColName,xmlFieldAndColName, valueType);
	}
	
	private StoryDataType(String xmlFieldName, String colName, int valueType) {
		super(xmlFieldName, colName,valueType);
		KNOWNTYPES.put(xmlFieldName, this);
	}
}
