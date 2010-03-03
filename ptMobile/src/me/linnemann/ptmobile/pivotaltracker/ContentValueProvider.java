package me.linnemann.ptmobile.pivotaltracker;

import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.datatype.DataType;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;
import android.content.ContentValues;

/**
 * this class deals with converting data into ContentValues. ContentValues can easily be stored
 * in db
 * 
 * @author nlinn
 *
 */
public class ContentValueProvider {

	private static final String UPDATETIMESTAMP_KEY = "updatetimestamp";
	private ContentValues values;
	private Map<?,TrackerValue> data;
	
	public ContentValueProvider(TrackerEntity entity) {
		this.values = new ContentValues();
		data = entity.getData();
	}
	
	public ContentValues getValues() {
		return values;
	}
	
	public void fill() {
		for (Object key : data.keySet()) { 
			putStoryData((DataType) key, data.get(key));
		}
		
		addUpdateTimestampIfNotEmpty();
	}
	
	private void putStoryData(DataType key, TrackerValue value) {
		values.put(key.getDBColName(), value.getValueAsString());
	}
	
	private void addUpdateTimestampIfNotEmpty() {
		if (values.size() > 0)
			values.put(UPDATETIMESTAMP_KEY, Long.toString(System.currentTimeMillis()));
	}
}
