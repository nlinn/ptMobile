package me.linnemann.ptmobile.pivotaltracker.datatype;

import java.util.HashMap;


public class NoteDataType extends DataType {

	public static DataType ID, STORY_ID, PROJECT_ID, TEXT, AUTHOR, NOTED_AT;
	public static HashMap<String, DataType> KNOWNTYPES = new HashMap<String, DataType>();
	
	static {
		ID = new NoteDataType("id",VALUE_TYPE_NUMERIC);
		STORY_ID = new NoteDataType("story_id",VALUE_TYPE_NUMERIC);
		PROJECT_ID = new NoteDataType("project_id",VALUE_TYPE_NUMERIC);
		TEXT = new NoteDataType("text","_text",VALUE_TYPE_TEXT);
		AUTHOR = new NoteDataType("author",VALUE_TYPE_TEXT);
		NOTED_AT = new NoteDataType("noted_at",VALUE_TYPE_DATETIME);
	}
	
	private NoteDataType(String xmlFieldAndDBColName, int valueType) {
		this(xmlFieldAndDBColName, xmlFieldAndDBColName, valueType);
	}
	
	private NoteDataType(String xmlFieldName, String colName, int valueType) {
		super(xmlFieldName, colName, valueType);
		KNOWNTYPES.put(xmlFieldName, this);
	}
}
