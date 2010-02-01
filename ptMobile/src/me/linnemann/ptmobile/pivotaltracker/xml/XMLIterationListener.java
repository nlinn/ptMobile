package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.IncomingIteration;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;

public class XMLIterationListener implements XMLStackListener, IterationContext {

	private IncomingIteration iteration;
	private DBAdapter db;
	private Integer project_id;
	private String iteration_group;
	
	public XMLIterationListener(DBAdapter db, Integer project_id, String iteration_group) {
		this.db = db;
		this.project_id = project_id;
		this.iteration_group = iteration_group;
		initIteration();
	}

	private void initIteration() {
		iteration = new IncomingIteration(db, project_id, iteration_group);	
	}
	
	public void elementPoppedFromStack() {
		iteration.save();
		initIteration();
	}
	
	public void handleSubElement(String element, String data) {
		iteration.addDataForKey(element, data);
	}
	
	public Integer getProjectId() {
		return project_id;
	}
	
	public String getIterationGroup() {
		return iteration_group;
	}

	public Integer getIterationNumber() {
		String iteration_number = iteration.getIterationNumber();
		if (iteration_number == null) {
			return null;
		} else {
			return Integer.parseInt(iteration_number);
		}
	}
}
