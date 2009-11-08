package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.IncomingActivity;
import me.linnemann.ptmobile.pivotaltracker.fields.ActivityData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLActivitiesHandler extends XMLBaseHandler {

	private static final String TAG_ACTIVITY = "activity";

	private IncomingActivity activity;
	private boolean isActivity;

	public XMLActivitiesHandler(DBAdapter db) {
		super(db);
	}

	public void startElement(String uri, String name, String qName, Attributes attr) {
		super.startElement(uri, name, qName, attr);

		if (name.equalsIgnoreCase(TAG_ACTIVITY)) {
			activity = new IncomingActivity(db);
			isActivity = true;
		}
	}

	public void endElement(String uri, String name, String qName) throws SAXException {
		super.endElement(uri, name, qName);

		// end of a project block >> save data to db
		if (name.equalsIgnoreCase(TAG_ACTIVITY)) {
			activity.save();
			activity = null;
			isActivity = false;
		}
	}

	public void characters(char ch[], int start, int length) {

		String chars = (new String(ch).substring(start, start + length));

		if (isActivity) {
			checkAndFillString(activity, currentElementName, ActivityData.ID, chars);
			checkAndFillString(activity, currentElementName, ActivityData.PROJECT, chars);
			checkAndFillString(activity, currentElementName, ActivityData.STORY, chars);
			checkAndFillString(activity, currentElementName, ActivityData.DESCRIPTION, chars);
			checkAndFillString(activity, currentElementName, ActivityData.AUTHOR, chars);
			checkAndFillString(activity, currentElementName, ActivityData.WHEN, chars);
		}
	}
}
