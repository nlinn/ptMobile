package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.fields.ProjectData;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;
import android.util.Log;

/**
 * creates a project object from pivotaltracker API
 * found story data is marked modified
 * 
 * @author nlinn
 *
 */
public class ProjectFromAPIBuilder implements ProjectBuilder {

	private static final String TAG = "ProjectFromAPIBuilder";
	private ProjectImpl project;

	public ProjectFromAPIBuilder() {
		clear();	
	}

	public void clear() {
		project = new ProjectImpl();
	}

	public void construct() {
		// empty :(
	}

	public void add(String elementName, String elementData) {
		
		if (elementName == null) return; // handler spams me with null values -> ignore
		
		try {
			ProjectData element = ProjectData.valueOf(elementName.toUpperCase());
			add(element,elementData);
		} catch (IllegalArgumentException e) {
			Log.w(TAG, "ignoring unknown element: "+elementName);
		}
	}

	public void add(final ProjectData element, String elementData) {
		Log.d(TAG,element +": "+elementData);
		TrackerValue value = element.getValueFromString(elementData);
		project.putDataAndModified(element, value);
	}

	public Project getConstructedProject() {
		return project;
	}

}
