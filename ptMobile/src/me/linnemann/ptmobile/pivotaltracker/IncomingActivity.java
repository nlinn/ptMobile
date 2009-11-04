package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.ActivityField;

import android.content.ContentValues;

public class IncomingActivity implements IncomingData {

	private Map<ActivityField,String> stringData;
	private DBAdapter db;	

	public IncomingActivity (DBAdapter db) {
		this.db = db;
		stringData = new HashMap<ActivityField,String>();
	}

	public void addDataForKey(Object key, String value) {

		if (!stringData.containsKey(key)) {
			stringData.put((ActivityField) key, value); // add
		} else {
			String tmp = stringData.get(key);
			stringData.put((ActivityField) key, tmp+value);
		}
	}

	/**
	 * Get data in a db friendly way
	 * 
	 * @return
	 */
	private ContentValues getDataAsContentValues() {
		ContentValues v = new ContentValues();

		for (ActivityField f : stringData.keySet()) {
			v.put(f.getDBFieldName(), stringData.get(f));
		}

		return v;
	}

	/**
	 * save in db
	 */
	public void save() {
		this.db.insertActivity(getDataAsContentValues());
	}
}