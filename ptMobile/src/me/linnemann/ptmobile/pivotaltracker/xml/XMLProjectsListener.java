package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.ProjectFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.ProjectImpl;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;

public class XMLProjectsListener implements XMLStackListener {

	private DBAdapter db;
	private ProjectFromAPIBuilder builder;
	
	public XMLProjectsListener(DBAdapter db) {
		this.db = db;
		builder = new ProjectFromAPIBuilder();
	}
	
	private void initProject() {
		builder.clear();
	}
	
	public void elementPoppedFromStack() {
		Project project = ProjectImpl.buildInstance(builder);
		db.insertProject(project);
		initProject();
	}

	public void handleSubElement(String element, String data) {
		builder.add(element, data);
	}
}
