package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.Activity;
import me.linnemann.ptmobile.pivotaltracker.EntityFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.fields.ActivityDataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.fields.DataTypeFactory;

public class XMLActivityListener implements XMLStackListener {

	//private static final String TAG = "XMLActivityListener";
	private DBAdapter db;
	
	private DataTypeFactory factory;
	private EntityFromAPIBuilder builder;
	
	public XMLActivityListener(DBAdapter db) {
		this.db = db;
		this.factory = new ActivityDataTypeFactory();
		initBuilder();
	}
	
	private void initBuilder() {
		builder = new EntityFromAPIBuilder(factory, Activity.emptyActivity());
	}
	
	public void elementPoppedFromStack() {
		Activity activity = (Activity) builder.getEntity();
		db.insertActivity(activity);
		initBuilder();
	}

	public void handleSubElement(String element, String data) {
		builder.add(element, data);
	}
}
