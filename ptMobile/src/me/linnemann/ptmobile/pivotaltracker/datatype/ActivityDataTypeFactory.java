package me.linnemann.ptmobile.pivotaltracker.datatype;

import me.linnemann.ptmobile.pivotaltracker.Activity;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;

public class ActivityDataTypeFactory extends DataTypeFactory {
	
	public ActivityDataTypeFactory() {
		super(ActivityDataType.KNOWNTYPES);
	}
	
	@Override
	public TrackerEntity getEmptyEntity() {
		return Activity.emptyActivity();
	}
}
