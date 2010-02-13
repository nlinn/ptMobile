package me.linnemann.ptmobile.pivotaltracker.fields;

import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public class ProjectDataType extends DataType {

	public static DataType ID, NAME, USE_HTTPS, ITERATION_LENGTH, WEEK_START_DAY, POINT_SCALE, CURRENT_VELOCITY, LABELS;
	private String dbColName;
	private int valueType;
	private static final int NUMERIC = 0;
	private static final int TEXT = 1;

	static {
		ID = new ProjectDataType("id",NUMERIC);
		NAME = new ProjectDataType("name",TEXT);
		ITERATION_LENGTH = new ProjectDataType("iteration_length",TEXT);
		WEEK_START_DAY = new ProjectDataType("week_start_day",TEXT);
		POINT_SCALE = new ProjectDataType("point_scale",TEXT);
		CURRENT_VELOCITY = new ProjectDataType("current_velocity",NUMERIC);
		LABELS = new ProjectDataType("labels",TEXT);
		USE_HTTPS = new ProjectDataType("use_https",TEXT);
	}

	private ProjectDataType(String colName, int valueType) {
		this.dbColName = colName;
		this.valueType = valueType;
	}

	public String getDBColName() {
		return dbColName;
	}
	
	// TODO: wipe that stupid TrackerValue interface
	public String getDBFieldName() {
		return getDBColName();
	}


	public TrackerValue getValueFromString(String from) {
		TrackerValue value;
		switch(valueType) {
		case NUMERIC:
			value = new Numeric(from);
			break;
		case TEXT:
		default:
			value = new Text(from);
		}
		return value;
	}
}