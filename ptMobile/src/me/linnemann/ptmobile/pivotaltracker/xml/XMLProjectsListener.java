package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.EntityFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.datatype.ProjectDataTypeFactory;

public class XMLProjectsListener implements XMLStackListener {

	private DBAdapter db;
	private EntityFromAPIBuilder builder;
	private DataTypeFactory factory;
	
	public XMLProjectsListener(DBAdapter db) {
		this.db = db;
		this.factory = new ProjectDataTypeFactory();
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
