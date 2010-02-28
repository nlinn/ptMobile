package me.linnemann.ptmobile.pivotaltracker.datatype;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;

public abstract class DataTypeFactory {

	//private static final String TAG ="DataTypeFactory";
	public Map<String, DataType> knownTypes;

	public DataTypeFactory(HashMap<String, DataType> knownTypes) {
		this.knownTypes = knownTypes;
		Log.v("DataTypeFactory","init with types: "+knownTypes.toString());
	}
	
	public abstract TrackerEntity getEmptyEntity();
	
	public DataType getDataTypeFromString(String from) {

		DataType dataType = knownTypes.get(from.toLowerCase());
		if (dataType == null)
			throw new RuntimeException("No Type for String: "+from+" in factory: "+this.getClass().getName());
		return dataType;
	}
	
	protected void addKnownType(String key, DataType dataType) {
		knownTypes.put(key, dataType);
	}
}