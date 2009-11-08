package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.ActivityData;

import android.content.ContentValues;

public class IncomingActivity implements IncomingData {

	private Map<ActivityData,String> stringData;
	private DBAdapter db;	

	public IncomingActivity (DBAdapter db) {
		this.db = db;
		stringData = new HashMap<ActivityData,String>();
	}

	public void addDataForKey(Object key, String value) {

		if (!stringData.containsKey(key)) {
			stringData.put((ActivityData) key, value); // add
		} else {
			String tmp = stringData.get(key);
			stringData.put((ActivityData) key, tmp+value);
		}
	}

	/**
	 * Get data in a db friendly way
	 * 
	 * @return
	 */
	private ContentValues getDataAsContentValues() {
		ContentValues v = new ContentValues();

		for (ActivityData f : stringData.keySet()) {
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