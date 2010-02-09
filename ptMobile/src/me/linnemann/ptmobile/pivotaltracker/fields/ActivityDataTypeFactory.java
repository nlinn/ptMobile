package me.linnemann.ptmobile.pivotaltracker.fields;

public class ActivityDataTypeFactory extends DataTypeFactory {
	
	public ActivityDataTypeFactory() {
		addKnownType("id", ActivityDataType.ID);
		addKnownType("project", ActivityDataType.PROJECT);
		addKnownType("story", ActivityDataType.STORY);
		addKnownType("description", ActivityDataType.DESCRIPTION);
		addKnownType("author", ActivityDataType.AUTHOR);
		addKnownType("when", ActivityDataType.WHEN);
	}
}
