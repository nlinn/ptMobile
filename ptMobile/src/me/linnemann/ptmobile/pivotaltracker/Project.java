package me.linnemann.ptmobile.pivotaltracker;

import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.fields.DBAndXMLTransferable;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public interface Project {

	public abstract void changeId(Numeric id);

	public abstract Numeric getId();

	public abstract void changeLabels(Text labels);
	
	public abstract Text getLabels();
	
	public abstract void changeName(Text name);

	public abstract Text getName();

	public abstract void changeIterationLength(Text iterationLength);

	public abstract Text getIterationLength();

	public abstract void changeWeekStartDay(Text weekStartDay);

	public abstract Text getWeekStartDay();

	public abstract void changePointScale(Text pointScale);

	public abstract Text getPointScale();

	public abstract void changeCurrentVelocity(Numeric velocity);

	public abstract Numeric getCurrentVelocity();

	public abstract Map<DBAndXMLTransferable, TrackerValue> getModifiedData();

	public abstract void resetModifiedFieldsTracking();

}