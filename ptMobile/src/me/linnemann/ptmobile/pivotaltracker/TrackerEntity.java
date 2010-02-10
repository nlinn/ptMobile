package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.fields.DataType;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public abstract class TrackerEntity {

	protected Map<DataType, TrackerValue> data; 
	private Map<DataType, TrackerValue> modifiedData;

	public TrackerEntity() {
		data = new HashMap<DataType, TrackerValue>();
		modifiedData = new HashMap<DataType, TrackerValue>();
	}
	
	public void putDataAndTrackChanges(DataType key, TrackerValue value) {
		Log.v("TrackerEntity","Key(DB Name): "+key.getDBFieldName()+" Value: "+value.getValueAsString());
		
		TrackerValue oldValue = data.put(key, value);

		// --- put into modified, only if data has really changed
		if ((oldValue == null) || (!oldValue.equals(value))) {
			modifiedData.put(key, value);
		}
	}

	public Map<DataType, TrackerValue> getData() {
		return data;
	}
	
	public Map<DataType, TrackerValue> getModifiedData() {
		return modifiedData;
	}
	
	public void resetModifiedDataTracking() {
		modifiedData.clear();
	}
}
