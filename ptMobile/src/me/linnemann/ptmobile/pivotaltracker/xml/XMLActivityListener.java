package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.EntityFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.ActivityDataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataTypeFactory;

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
		builder = new EntityFromAPIBuilder(factory);
	}
	
	public void elementPoppedFromStack() {
		TrackerEntity activity = builder.getEntity();
		db.insertEntity(activity);
		initBuilder();
	}

	public void handleSubElement(String element, String data) {
		builder.add(element, data);
	}
}
