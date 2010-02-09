package me.linnemann.ptmobile.pivotaltracker.fields;

import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public class ProjectDataType extends DataType {

	public static DataType ID, NAME, ITERATION_LENGTH, WEEK_START_DAY, POINT_SCALE, CURRENT_VELOCITY, LABELS;
	private String dbColName;
	private int valueType;
	private static final int NUMERIC = 0;
	private static final int TEXT = 1;

	static {
		ID = new ProjectDataType("id",NUMERIC);
		NAME = new ProjectDataType("id",TEXT);
		ITERATION_LENGTH = new ProjectDataType("id",TEXT);
		WEEK_START_DAY = new ProjectDataType("id",TEXT);
		POINT_SCALE = new ProjectDataType("id",TEXT);
		CURRENT_VELOCITY = new ProjectDataType("id",NUMERIC);
		LABELS = new ProjectDataType("id",TEXT);
	}

	private ProjectDataType(String colName, int valueType) {
		this.dbColName = colName;
		this.valueType = valueType;
	}

	public String getDBColName() {
		return dbColName;
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