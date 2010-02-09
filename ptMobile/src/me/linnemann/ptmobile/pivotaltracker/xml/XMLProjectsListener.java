package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.EntityFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.fields.DataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.fields.ProjectDataTypeFactory;

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
		builder = new EntityFromAPIBuilder(factory, Project.emptyProject());
	}
	
	public void elementPoppedFromStack() {
		Project project = (Project) builder.getEntity();
		db.insertProject(project);
		initBuilder();
	}

	public void handleSubElement(String element, String data) {
		builder.add(element, data);
	}
}
