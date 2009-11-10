package me.linnemann.ptmobile.pivotaltracker.fields;

public enum ProjectData {

	ID, NAME, ITERATION_LENGTH, WEEK_START_DAY, POINT_SCALE;
	
	public String getDBFieldName() {
		return toString().toLowerCase();
	}
}
