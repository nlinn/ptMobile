package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.NoteData;
import android.content.ContentValues;

public class IncomingNote implements IncomingData {

	private Map<NoteData,String> data;
	private DBAdapter db;	

	public IncomingNote(DBAdapter db, String project_id, String story_id) {
		this.db = db;
		data = new HashMap<NoteData,String>();
		data.put(NoteData.STORY_ID, story_id);
		data.put(NoteData.PROJECT_ID, project_id);
	}

	public void addDataForKey(Object key, String value) {

		if (!data.containsKey(key)) {
			data.put((NoteData) key, value); // add
		} else {
			String tmp = data.get(key);
			data.put((NoteData) key, tmp+value); //append
		}
	}

	/**
	 * Get data in a db friendly way
	 * 
	 * @return
	 */
	private ContentValues getDataAsContentValues() {
		ContentValues v = new ContentValues();

		for (NoteData f : data.keySet()) {
			if (f.equals(NoteData.NOTED_AT)) {
				// --- convert date
				v.put(f.getDBFieldName(), data.get(f).replaceAll("/", "-").replaceAll(" UTC",""));
			} else {
				v.put(f.getDBFieldName(), data.get(f));
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
		this.db.insertNote(getDataAsContentValues());
	}
}

