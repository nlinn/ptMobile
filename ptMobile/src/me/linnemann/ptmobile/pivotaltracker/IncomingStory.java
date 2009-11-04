package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.StoryField;

import android.content.ContentValues;

/**
 * Story Value
 * 
 * @author niels
 *
 */
public class IncomingStory implements IncomingData {

	private Map<StoryField,String> stringData;
	private DBAdapter db;	
	
	public IncomingStory(DBAdapter db, String project_id, String iteration_number) {
		this.db = db;
		
		stringData = new HashMap<StoryField,String>(); // prepare map
		addDataForKey(StoryField.PROJECT_ID, project_id);
		addDataForKey(StoryField.ITERATION_NUMBER, iteration_number);
	}
	
	public void addDataForKey(Object key, String value) {
		
		if (!stringData.containsKey(key)) {
			stringData.put((StoryField) key, value); // add
		} else {
			String tmp = stringData.get(key);
			stringData.put((StoryField) key, tmp+value);
		}
	}
	
	/**
	 * Get data in a db friendly way
	 * 
	 * @return
	 */
	private ContentValues getDataAsContentValues() {
		ContentValues v = new ContentValues();
		
		for (StoryField f : stringData.keySet()) {
			// --- note: db field name is lowercase!
			v.put(f.getDBFieldName(), stringData.get(f));
		}
		
	    return v;
	}

	/* (non-Javadoc)
	 * @see me.linnemann.ptmobile.pivotaltracker.IncomingData#save()
	 */
	public void save() {
		this.db.insertStory(getDataAsContentValues());
	}
}
