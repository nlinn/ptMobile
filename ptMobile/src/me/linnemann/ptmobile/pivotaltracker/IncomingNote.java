package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.fields.NoteData;
import android.content.ContentValues;
import android.util.Log;

public class IncomingNote implements IncomingData {

	private static final String TAG = "IncomingNote";
	
	private Map<NoteData,String> data;
	private DBAdapter db;	

	public IncomingNote(DBAdapter db) {
		this.db = db;
		data = new HashMap<NoteData,String>();
	}
	
	/**
	@deprecated
	*/
	public IncomingNote(DBAdapter db, Integer project_id, Integer story_id) {
		this(db);
		data.put(NoteData.STORY_ID, story_id.toString());
		data.put(NoteData.PROJECT_ID, project_id.toString());
	}

	public void addDataForKey(Object key, String value) {

		try {
			NoteData dataType = NoteData.valueOf(key.toString().toUpperCase()); 
			if (!data.containsKey(dataType)) {
				data.put(dataType, value); // add
			} else {
				String tmp = data.get(dataType);
				data.put(dataType, tmp+value);
			}
		} catch (IllegalArgumentException e) {
			Log.w("IncomingNote","ignoring element: "+key.toString());
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
		
		Log.v(TAG,"ContentValues from Note: "+v.toString());
		
		return v;
	}

	/**
	 * save in db
	 */
	public void save() {
		this.db.insertNote(getDataAsContentValues());
	}
}

