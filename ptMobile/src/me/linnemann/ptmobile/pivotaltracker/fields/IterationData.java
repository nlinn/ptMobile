package me.linnemann.ptmobile.pivotaltracker.fields;

public enum IterationData {

	ID("id"), NUMBER("number"), START("start"), 
	FINISH("finish"), PROJECT_ID("project_id"), ITERATION_GROUP("iteration_group");
	
	private final String db_field;
	
	IterationData(String db_field) {
		this.db_field = db_field;
	}
	
	public String getDBFieldName() {
		return db_field;
	}
}
