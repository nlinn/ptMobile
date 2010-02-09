package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.fields.ActivityDataType;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;

public class Activity extends TrackerEntity {

	public static Activity emptyActivity() {
		return new Activity();
	}
	
	public Numeric getId() {
		return (Numeric) data.get(ActivityDataType.ID);
	}
	
	public Text getProject() {
		return (Text) data.get(ActivityDataType.PROJECT);
	}
	
	public Text getStory() {
		return (Text) data.get(ActivityDataType.STORY);
	}
	
	public Text getDescription() {
		return (Text) data.get(ActivityDataType.DESCRIPTION);
	}
	
	public Text getAuthor() {
		return (Text) data.get(ActivityDataType.AUTHOR);
	}
	
	public Text getWhen() {
		return (Text) data.get(ActivityDataType.WHEN);
	}
}
