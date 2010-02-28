package me.linnemann.ptmobile.pivotaltracker.datatype;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;

public class StoryDataTypeFactory extends DataTypeFactory {
	
	public StoryDataTypeFactory() {
		super(StoryDataType.KNOWNTYPES);
	}
	
	@Override
	public TrackerEntity getEmptyEntity() {
		return new Story(); // TODO refactore whole getEmtpyEntity stuff
	}

}
