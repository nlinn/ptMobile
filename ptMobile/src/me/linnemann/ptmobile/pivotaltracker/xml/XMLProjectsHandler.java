package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.IncomingProject;
import me.linnemann.ptmobile.pivotaltracker.fields.ProjectData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLProjectsHandler extends XMLBaseHandler {

	private static final String TAG_PROJECT = "project";
	private static final String TAG_MEMBERSHIPS = "memberships";

	private IncomingProject project;
	private boolean isProject;

	public XMLProjectsHandler(DBAdapter db) {
		super(db);
	}

	public void startElement(String uri, String name, String qName, Attributes attr) {
		super.startElement(uri, name, qName, attr);

		if (name.equalsIgnoreCase(TAG_PROJECT)) {
			project = new IncomingProject(db);
			isProject = true;
		}
		
		if (name.equalsIgnoreCase(TAG_MEMBERSHIPS)) {
			isProject = false;	// ignore membership list
		}
	}

	public void endElement(String uri, String name, String qName) throws SAXException {
		super.endElement(uri, name, qName);

		// end of a project block >> save data to db
		if (name.equalsIgnoreCase(TAG_PROJECT)) {
			project.save();
			project = null;
			isProject = false;
		}
	}

	public void characters(char ch[], int start, int length) {

		String chars = (new String(ch).substring(start, start + length));

		if (isProject) {
			checkAndFillString(project, currentElementName, ProjectData.ID, chars);
			checkAndFillString(project, currentElementName, ProjectData.NAME, chars);
			checkAndFillString(project, currentElementName, ProjectData.ITERATION_LENGTH, chars);
			checkAndFillString(project, currentElementName, ProjectData.WEEK_START_DAY, chars);
			checkAndFillString(project, currentElementName, ProjectData.POINT_SCALE, chars);
		}
	}
}
