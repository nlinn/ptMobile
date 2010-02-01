package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.fields.ActivityData;

import android.content.ContentValues;
import android.util.Log;

public class IncomingActivity implements IncomingData {

	private Map<ActivityData,String> stringData;
	private DBAdapter db;	

	public IncomingActivity(DBAdapter db) {
		this.db = db;
		stringData = new HashMap<ActivityData,String>();
	}

	public void addDataForKey(Object key, String value) {

		try {
			ActivityData dataType = ActivityData.valueOf(key.toString().toUpperCase()); 
			if (!stringData.containsKey(dataType)) {
				stringData.put(dataType, value); // add
			} else {
				String tmp = stringData.get(key);
				stringData.put(dataType, tmp+value);
			}
		} catch (IllegalArgumentException e) {
			Log.w("IncomingActivity","ignoring element: "+key.toString());
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

	protected boolean isField(Object field, String element) {
		if (field == null) {
			return false;
		} else {

			return field.toString().equalsIgnoreCase(element);
		}
	}
}