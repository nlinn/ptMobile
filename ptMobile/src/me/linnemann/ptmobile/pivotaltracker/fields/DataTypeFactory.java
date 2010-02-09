package me.linnemann.ptmobile.pivotaltracker.fields;

import java.util.HashMap;
import java.util.Map;

public abstract class DataTypeFactory {

	//private static final String TAG ="DataTypeFactory";
	public Map<String, DataType> knownTypes;

	public DataTypeFactory() {
		knownTypes = new HashMap<String, DataType>();
	}
	
	public DataType getDataTypeFromString(String from) {

		DataType dataType = knownTypes.get(from.toLowerCase());
		if (dataType == null)
			throw new RuntimeException("No Type for String: "+from);
		return dataType;
	}
	
	protected void addKnownType(String key, DataType dataType) {
		knownTypes.put(key, dataType);
	}
}