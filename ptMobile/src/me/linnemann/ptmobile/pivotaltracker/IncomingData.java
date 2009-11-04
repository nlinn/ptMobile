package me.linnemann.ptmobile.pivotaltracker;


public interface IncomingData {

	/**
	 * data is stored as strings in first place
	 * data is appended to existing data
	 * 
	 * @param key key is equivalent to db column name
	 * @param value value to append
	 */
	public abstract void addDataForKey(Object key, String value);

	/**
	 * save in db
	 */
	public abstract void save();


}