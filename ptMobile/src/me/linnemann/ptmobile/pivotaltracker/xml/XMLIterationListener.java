package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.EntityFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.Iteration;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.datatype.IterationDataType;
import me.linnemann.ptmobile.pivotaltracker.datatype.IterationDataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;

public class XMLIterationListener implements XMLStackListener, IterationContext {

	private DBAdapter db;
	private Integer project_id;
	private String iteration_group;
	
	
	private DataTypeFactory factory;
	private EntityFromAPIBuilder builder;

	
	public XMLIterationListener(DBAdapter db, Integer project_id, String iteration_group) {
		this.db = db;
		this.project_id = project_id;
		this.iteration_group = iteration_group;
		
		this.factory = new IterationDataTypeFactory();
		
		initBuilder();
	}

	
	private void initBuilder() {
		builder = new EntityFromAPIBuilder(factory);
	}
	
	public void elementPoppedFromStack() {
		Iteration iteration = (Iteration) builder.getEntity();
		addMetaDataToNote(iteration);
		db.insertEntity(iteration);
		initBuilder();
	}
	
	private void addMetaDataToNote(Iteration iteration) {
		iteration.putDataAndTrackChanges(IterationDataType.PROJECT_ID, new Numeric(project_id));
		iteration.putDataAndTrackChanges(IterationDataType.ITERATION_GROUP, new Text(iteration_group));
	}
	
	public void handleSubElement(String element, String data) {
		builder.add(element, data);
	}
	
	public Integer getProjectId() {
		return project_id;
	}
	
	public String getIterationGroup() {
		return iteration_group;
	}

	public Integer getIterationNumber() {
		Iteration iteration = (Iteration) builder.getEntity();
		Numeric iteration_number = iteration.getNumber();
		
		if (iteration_number == null) {
			return null;
		} else {
			return iteration_number.getValue();
		}
	}
}
