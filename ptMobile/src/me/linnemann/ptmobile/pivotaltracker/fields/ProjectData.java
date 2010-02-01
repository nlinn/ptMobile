package me.linnemann.ptmobile.pivotaltracker.fields;

import android.util.Log;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public enum ProjectData implements DBAndXMLTransferable {

	
	ID(1), NAME(0), ITERATION_LENGTH(0), WEEK_START_DAY(0), POINT_SCALE(0), CURRENT_VELOCITY(1), LABELS(0);

	private static final int TEXTTYPE = 0;
	private static final int NUMERICTYPE = 1;
	private int type;

	
	ProjectData(int type) {
		this.type = type;
	}
	
	public String getDBFieldName() {
		return toString().toLowerCase();
	}

	public TrackerValue getValueFromString(String from) {
		TrackerValue value;
		
		switch (this.type) {
			case NUMERICTYPE:
				value = new Numeric(from);
				break;
			case TEXTTYPE:
			default:
				value = new Text(from);
		}
		
		return value;
	}
	
	public String getXMLWrappedValue(TrackerValue value) {
		Log.w("ProjectData","not implemented yet");
		return "";
	}
}