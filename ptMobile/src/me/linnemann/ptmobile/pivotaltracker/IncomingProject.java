package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.ProjectData;

import android.content.ContentValues;

public class IncomingProject implements IncomingData {

	private Map<ProjectData,String> stringData;
	private DBAdapter db;	
	
	public IncomingProject (DBAdapter db) {
		this.db = db;
		stringData = new HashMap<ProjectData,String>();
	}
	
	public void addDataForKey(Object key, String value) {
		
		if (!stringData.containsKey(key)) {
			stringData.put((ProjectData) key, value); // add
		} else {
			String tmp = stringData.get(key);
			stringData.put((ProjectData) key, tmp+value);
		}
	}
	
	/**
	 * Get data in a db friendly way
	 * 
	 * @return
	 */
	private ContentValues getDataAsContentValues() {
		ContentValues v = new ContentValues();
		
		for (ProjectData f : stringData.keySet()) {
			// --- note: db field name is lowercase!
			v.put(f.getDBFieldName(), stringData.get(f));
		}
		
	    return v;
	}

	/**
	 * save in db
	 */
	public void save() {
		this.db.insertProject(getDataAsContentValues());
	}



	
}
