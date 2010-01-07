package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.IterationData;
import android.content.ContentValues;

public class IncomingIteration implements IncomingData {

	private Map<IterationData,String> stringData;
	private DBAdapter db;	

	public IncomingIteration (DBAdapter db, Integer project_id,  String iteration_group) {
		this.db = db;
		stringData = new HashMap<IterationData,String>();
		stringData.put(IterationData.PROJECT_ID, project_id.toString());
		stringData.put(IterationData.ITERATION_GROUP, iteration_group);
	}

	public void addDataForKey(Object key, String value) {

		if (!stringData.containsKey(key)) {
			stringData.put((IterationData) key, value); // add
		} else {
			String tmp = stringData.get(key);
			stringData.put((IterationData) key, tmp+value); //append
		}
	}

	public String getIterationNumber() {
		return stringData.get(IterationData.NUMBER);
	}
	
	/**
	 * Get data in a db friendly way
	 * 
	 * @return
	 */
	private ContentValues getDataAsContentValues() {
		ContentValues v = new ContentValues();

		for (IterationData f : stringData.keySet()) {
			if ( (f.equals(IterationData.START)) || (f.equals(IterationData.FINISH)) ) {
				// --- convert date
				v.put(f.getDBFieldName(), stringData.get(f).replaceAll("/", "-").replaceAll(" UTC",""));
			} else {
				v.put(f.getDBFieldName(), stringData.get(f));
			}
		}
		// --- important: add updatetimestamp
		v.put("updatetimestamp", Long.toString(System.currentTimeMillis()));
		return v;
	}

	/**
	 * save in db
	 */
	public void save() {
		this.db.insertIteration(getDataAsContentValues());
	}
}
