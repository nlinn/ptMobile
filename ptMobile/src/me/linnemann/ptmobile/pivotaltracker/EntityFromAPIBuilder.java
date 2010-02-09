package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.fields.DataType;
import me.linnemann.ptmobile.pivotaltracker.fields.DataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;
import android.util.Log;

public class EntityFromAPIBuilder {

	private static final String TAG = "EntityFromAPIBuilder";
	
	DataTypeFactory factory;
	TrackerEntity entity;
	
	public EntityFromAPIBuilder(DataTypeFactory factory, TrackerEntity entity) {
		this.factory = factory;
		this.entity = entity;
	}
	
	public TrackerEntity getEntity() {
		return entity;
	}
	
	public void add(String elementName, String elementData) {
		
		if (elementName == null) return; // handler spams me with null values -> ignore
	
		try {
			DataType type = factory.getDataTypeFromString(elementName);
			TrackerValue value = type.getValueFromString(elementData);
			entity.putDataAndTrackChanges(type, value);
		} catch(RuntimeException e) {
			Log.w(TAG, e.getMessage());
		}
	}
}
