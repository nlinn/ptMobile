package me.linnemann.ptmobile.pivotaltracker.fields;

public enum ActivityField {

	ID("id"), PROJECT("project"), STORY("story"), DESCRIPTION("description"), AUTHOR("author"), WHEN("_when");
	
	private final String db_field;
	
	ActivityField(String db_field) {
		this.db_field = db_field;
	}
	
	public String getDBFieldName() {
		return db_field;
	}
}
