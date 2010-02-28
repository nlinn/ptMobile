package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.datatype.ProjectDataType;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;

public class Project extends TrackerEntity {
	
	public static Project emptyProject() {
		return new Project();
	}
	
	public Numeric getId() {
		return (Numeric) data.get(ProjectDataType.ID);
	}
	
	public Text getLabels() {		
		return (Text) data.get(ProjectDataType.LABELS);
	}
	
	public Text getName() {
		return (Text) data.get(ProjectDataType.NAME);
	}
	
	public Text getIterationLength() {
		return (Text) data.get(ProjectDataType.ITERATION_LENGTH);
	}
	
	public Text getWeekStartDay() {
		return (Text) data.get(ProjectDataType.WEEK_START_DAY);
	}
	
	public Text getPointScale() {
		return (Text) data.get(ProjectDataType.POINT_SCALE);
	}
	
	public Numeric getCurrentVelocity() {
		return (Numeric) data.get(ProjectDataType.CURRENT_VELOCITY);
	}
	
	public Text getUseHTTPS() {
		return (Text) data.get(ProjectDataType.USE_HTTPS);
	}
	
	public String getProtocol() {		
		String s = getUseHTTPS().getValue();
		if (s.equalsIgnoreCase("true")) {
			return "https://";
		} else {
			return "http://";
		}
	}
	
	@Override
	public String getTableName() {
		return "projects";
	}

}
