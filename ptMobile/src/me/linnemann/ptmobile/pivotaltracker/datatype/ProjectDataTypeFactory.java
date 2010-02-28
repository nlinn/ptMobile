package me.linnemann.ptmobile.pivotaltracker.datatype;

import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;

public class ProjectDataTypeFactory extends DataTypeFactory {
	
	public ProjectDataTypeFactory() {
		super(ProjectDataType.KNOWNTYPES);
	}
	
	@Override
	public TrackerEntity getEmptyEntity() {
		return Project.emptyProject();
	}

}
