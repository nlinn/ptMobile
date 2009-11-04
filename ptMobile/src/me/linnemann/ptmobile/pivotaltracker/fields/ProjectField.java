package me.linnemann.ptmobile.pivotaltracker.fields;

public enum ProjectField {

	ID, NAME, ITERATION_LENGTH, WEEK_START_DAY, POINT_SCALE;
	
	public String getDBFieldName() {
		return toString().toLowerCase();
	}
}
