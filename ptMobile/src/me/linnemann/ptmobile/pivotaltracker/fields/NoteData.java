package me.linnemann.ptmobile.pivotaltracker.fields;

public enum NoteData {

	ID("id"), STORY_ID("story_id"), PROJECT_ID("project_id"), TEXT("_text"), 
	AUTHOR("author"), NOTED_AT("noted_at");
	
	private final String db_field;
	
	NoteData(String db_field) {
		this.db_field = db_field;
	}
	
	public String getDBFieldName() {
		return db_field;
	}
}

