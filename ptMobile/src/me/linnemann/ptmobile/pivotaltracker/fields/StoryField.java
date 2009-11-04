package me.linnemann.ptmobile.pivotaltracker.fields;

public enum StoryField {
	
	ID, NAME, ESTIMATE, STORY_TYPE, LABELS, CURRENT_STATE, DESCRIPTION, REQUESTED_BY, OWNED_BY,
	CREATED_AT, ACCEPTED_AT, DEADLINE, ITERATION_NUMBER, PROJECT_ID;
	
	public String getDBFieldName() {
		return toString().toLowerCase();
	}
}
