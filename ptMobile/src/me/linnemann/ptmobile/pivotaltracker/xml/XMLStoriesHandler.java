package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.IncomingData;
import me.linnemann.ptmobile.pivotaltracker.IncomingStory;
import me.linnemann.ptmobile.pivotaltracker.Iteration;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLStoriesHandler extends XMLBaseHandler {

	private String project_id;

	private IncomingData story;
	private Iteration iteration;
	private String currentIteration;

	private boolean isIteration;
	private boolean isStory;
	private String iteration_group;

	public XMLStoriesHandler(DBAdapter db, String project_id, String iteration_group) {
		super(db);
		this.project_id = project_id;
		this.iteration_group = iteration_group;
	}


	public void startElement(String uri, String name, String qName, Attributes attr) {
		super.startElement(uri, name, qName, attr);

		if (name.equalsIgnoreCase("iteration")) {
			isIteration = true;
			isStory = false;
			iteration = new Iteration(project_id, iteration_group);
		}

		if (name.equalsIgnoreCase("story")) {
			isIteration = false;
			isStory = true;
			story = new IncomingStory(db, project_id, currentIteration, iteration_group);
		}

		if (name.equalsIgnoreCase("notes")) {
			isIteration = false;
			isStory = false;
		}

	}

	public void endElement(String uri, String name, String qName) throws SAXException {

		if (name.equalsIgnoreCase("iteration")) {
			isIteration = false;
		}

		if (name.equalsIgnoreCase("story")) {
			story.save();
			story = null;

			isStory = false;
		}

		if ((iteration !=null) && (iteration.isDataComplete())) {
			db.insertIteration(iteration);
			currentIteration = iteration.getNumber();
			iteration = null;
		}

		currentElementName = "";
	}

	public void characters(char ch[], int start, int length) {

		String chars = (new String(ch).substring(start, start + length));

		if ((isStory) && (story != null)) {
			if (checkAndFillString(story, currentElementName, StoryData.NAME, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.ID, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.ESTIMATE, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.STORY_TYPE, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.LABELS, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.DESCRIPTION, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.CURRENT_STATE, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.ACCEPTED_AT, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.CREATED_AT, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.REQUESTED_BY, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.OWNED_BY, chars)) return;
			if (checkAndFillString(story, currentElementName, StoryData.DEADLINE, chars)) return;
		}

		if ((isIteration) && (iteration != null)) {
			//<id type="integer">1</id>
			//<number type="integer">1</number>
			//<start type="datetime">2009/03/16 00:00:00 UTC</start>
			//<finish type="datetime">2009/03/23 00:00:00 UTC</finish>

			if (currentElementName.equalsIgnoreCase("number")) {
				// Log.e("name",chars);
				iteration.addNumber(chars);
			}

			if (currentElementName.equalsIgnoreCase("id")) {
				// Log.e("id",chars);
				iteration.addId(chars);
			}

			if (currentElementName.equalsIgnoreCase("start")) {
				// Log.e("id",chars);
				iteration.addStart(chars);
			}

			if (currentElementName.equalsIgnoreCase("finish")) {
				// Log.e("id",chars);
				iteration.addFinish(chars);
			}
		}
	}
}
