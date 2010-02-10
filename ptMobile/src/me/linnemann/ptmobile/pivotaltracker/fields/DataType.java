package me.linnemann.ptmobile.pivotaltracker.fields;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

// TODO: check if trackerdata is needed or just legacy stuff
public class DataType implements TrackerData {

	public static DataType ID;
	public static Map<String, DataType> knownTypes;
	
	static {
		knownTypes = new HashMap<String, DataType>();
		
		
		knownTypes.put("id", ID);
	}
	
	protected static void addKnownType(String key, DataType dataType) {
		knownTypes.put("id", ID);
	}
	
	public static DataType getDataTypeFromString(String from) {
		DataType dataType = knownTypes.get(from.toLowerCase());
		if (dataType == null)
			throw new RuntimeException("No Type for String: "+from);
		return dataType;
	}
	
	public TrackerValue getValueFromString(String from) {
		return null;
	}
	
	public String getDBFieldName() {
		return null;
	}
	
	public String getXMLWrappedValue(TrackerValue value) {
		return null;
	}
}

