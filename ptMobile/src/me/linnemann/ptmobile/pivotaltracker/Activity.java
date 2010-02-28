package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.ActivityDataType;
import me.linnemann.ptmobile.pivotaltracker.value.DateTime;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;

public class Activity extends TrackerEntity {
	
	private DBAdapter db;

	public static Activity emptyActivity() {
		return new Activity();
	}
	
	public void setDBAdapter(DBAdapter db) {
		this.db = db;
	}
	
	public Numeric getId() {
		return (Numeric) data.get(ActivityDataType.ID);
	}
	
	public Numeric getProjectId() {
		return (Numeric) data.get(ActivityDataType.PROJECT_ID);
	}
	
	public Numeric getVersion() {
		return (Numeric) data.get(ActivityDataType.VERSION);
	}
	
	public Text getDescription() {
		return (Text) data.get(ActivityDataType.DESCRIPTION);
	}
	
	public Text getAuthor() {
		return (Text) data.get(ActivityDataType.AUTHOR);
	}
	
	public Text getEventType() {
		return (Text) data.get(ActivityDataType.EVENT_TYPE);
	}
	
	public DateTime getOccuredAt() {
		return (DateTime) data.get(ActivityDataType.OCCURRED_AT);
	}

	@Override
	public String getTableName() {
		return "activities";
	}
	
	public Project getProject() {
		return db.getProject(getProjectId().getValue());
	}
}
