package me.linnemann.ptmobile.pivotaltracker.fields;

public class ProjectDataTypeFactory extends DataTypeFactory {
	
	public ProjectDataTypeFactory() {
		addKnownType("id", ProjectDataType.ID);
		addKnownType("name", ProjectDataType.NAME);
		addKnownType("iteration_length", ProjectDataType.ITERATION_LENGTH);
		addKnownType("week_start_day", ProjectDataType.WEEK_START_DAY);
		addKnownType("point_scale", ProjectDataType.POINT_SCALE);
		addKnownType("current_velocity", ProjectDataType.CURRENT_VELOCITY);
		addKnownType("labels", ProjectDataType.LABELS);
	}
}
