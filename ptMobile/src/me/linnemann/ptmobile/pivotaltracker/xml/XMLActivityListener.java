package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.IncomingActivity;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;

public class XMLActivityListener implements XMLStackListener {

	private IncomingActivity activity;
	private DBAdapter db;
	
	public XMLActivityListener(DBAdapter db) {
		this.db = db;
		initActivity();
	}
	
	private void initActivity() {
		activity = new IncomingActivity(db);
	}
	
	public void elementPoppedFromStack() {
		activity.save();
		initActivity();
	}

	public void handleSubElement(String element, String data) {
		activity.addDataForKey(element, data);
	}
}
