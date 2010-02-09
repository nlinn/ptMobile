package me.linnemann.ptmobile.pivotaltracker;

import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.TrackerData;
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
	private static final String ID_KEY = "id";
	private ContentValues values;
	private String id;
	private Map<?,TrackerValue> modified;
	
	public ContentValueProvider(Activity activity) {
		throw new RuntimeException("not implemented");
	}
	
	public ContentValueProvider(Project project) {
		this.values = new ContentValues();
		this.id = project.getId().getValueAsString();
		modified = project.getModifiedData();
	}
	
	public ContentValueProvider(Story story) {
		this.values = new ContentValues();
		this.id = story.getId().getValueAsString();
		modified = story.getData();
	}
	
	public ContentValues getValues() {
		return values;
	}
	
	public void fill() {
		for (Object key : modified.keySet()) { 
			putStoryData((TrackerData) key, modified.get(key));
		}
		
		addUpdateTimestampIfNotEmpty();
		addIDIfNotEmpty();
	}
	
	private void putStoryData(TrackerData key, TrackerValue value) {
		values.put(key.getDBFieldName(), value.getValueAsString());
	}
	
	private void addUpdateTimestampIfNotEmpty() {
		if (values.size() > 0)
			values.put(UPDATETIMESTAMP_KEY, Long.toString(System.currentTimeMillis()));
	}
	
	private void addIDIfNotEmpty() {
		if (values.size() > 0)
			values.put(ID_KEY, id);
	}
}
