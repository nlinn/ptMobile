package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.datatype.DataType;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;
import android.util.Log;

public class EntityFromAPIBuilder {

	private static final String TAG = "EntityFromAPIBuilder";
	
	DataTypeFactory factory;
	TrackerEntity entity;
	
	public EntityFromAPIBuilder(DataTypeFactory factory) {
		Log.v(TAG,"Init with factory "+factory.getClass().getName());
		this.factory = factory;
		this.entity = factory.getEmptyEntity();
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
