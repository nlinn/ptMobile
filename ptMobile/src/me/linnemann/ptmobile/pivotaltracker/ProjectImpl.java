package me.linnemann.ptmobile.pivotaltracker;

import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.DBAndXMLTransferable;
import me.linnemann.ptmobile.pivotaltracker.fields.ProjectData;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public class ProjectImpl implements Project {

	private Map<ProjectData, TrackerValue> data; 
	private Map<DBAndXMLTransferable, TrackerValue> modified;
	
	public static Project buildInstance(ProjectBuilder builder) {
		return builder.getConstructedProject();
	}
	
	public ProjectImpl() {
		data = new HashMap<ProjectData, TrackerValue>();
		modified = new HashMap<DBAndXMLTransferable, TrackerValue>();
	}
	
	public void changeId(Numeric id) {		
		putDataAndModified(ProjectData.ID, id);
	}
	
	public void putDataAndModified(ProjectData key, TrackerValue value) {
		data.put(key, value);
		modified.put(key, value);
	}
	
	public Numeric getId() {
		return (Numeric) data.get(ProjectData.ID);
	}
	
	public void changeName(Text name) {		
		putDataAndModified(ProjectData.NAME, name);
	}
	
	public void changeLabels(Text labels) {		
		putDataAndModified(ProjectData.LABELS, labels);
	}
	
	public Text getLabels() {		
		return (Text) data.get(ProjectData.LABELS);
	}
	
	public Text getName() {
		return (Text) data.get(ProjectData.NAME);
	}
	
	public void changeIterationLength(Text iterationLength) {		
		putDataAndModified(ProjectData.ITERATION_LENGTH, iterationLength);
	}
	
	public Text getIterationLength() {
		return (Text) data.get(ProjectData.ITERATION_LENGTH);
	}
	
	public void changeWeekStartDay(Text weekStartDay) {		
		putDataAndModified(ProjectData.WEEK_START_DAY, weekStartDay);
	}
	
	public Text getWeekStartDay() {
		return (Text) data.get(ProjectData.WEEK_START_DAY);
	}
	
	public void changePointScale(Text pointScale) {		
		putDataAndModified(ProjectData.POINT_SCALE, pointScale);
	}
	
	public Text getPointScale() {
		return (Text) data.get(ProjectData.POINT_SCALE);
	}
	
	public void changeCurrentVelocity(Numeric velocity) {		
		putDataAndModified(ProjectData.CURRENT_VELOCITY, velocity);
	}
	
	public Numeric getCurrentVelocity() {
		return (Numeric) data.get(ProjectData.CURRENT_VELOCITY);
	}
	
	public Map<DBAndXMLTransferable, TrackerValue> getModifiedData() {
		return modified;
	}
	public void resetModifiedFieldsTracking() {
		modified.clear();
	}
}
