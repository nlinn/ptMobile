package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.IncomingIteration;
import me.linnemann.ptmobile.pivotaltracker.IncomingNote;
import me.linnemann.ptmobile.pivotaltracker.IncomingStory;
import me.linnemann.ptmobile.pivotaltracker.fields.IterationData;
import me.linnemann.ptmobile.pivotaltracker.fields.NoteData;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLStoriesHandler extends XMLBaseHandler {
	// --- TODO: code duplication: check how to use XMLNotesHandler for notes
	private static final String TAG="XMLStoriesHandler";
	
	private static final int STORY = 1;
	private static final int ITERATION = 2;
	private static final int NOTE = 3;
	
	private String project_id;
	private IncomingStory story;
	private IncomingIteration iteration;
	private IncomingNote note;
	private String iteration_group;
	private int parseWhat;
	
	public XMLStoriesHandler(DBAdapter db, String project_id, String iteration_group) {
		super(db);
		this.project_id = project_id;
		this.iteration_group = iteration_group;
	}

	public void startElement(String uri, String name, String qName, Attributes attr) {
		super.startElement(uri, name, qName, attr);

		if (name.equalsIgnoreCase("iteration")) {
			parseWhat = ITERATION;
			iteration = new IncomingIteration(db, project_id, iteration_group);
		}

		if (name.equalsIgnoreCase("story")) {
			parseWhat = STORY;
			
			String itnumber = null;
			if (iteration != null) {
				itnumber = iteration.getIterationNumber();
			}
			
			story = new IncomingStory(db, project_id, itnumber, iteration_group);
		}

		if (name.equalsIgnoreCase("note")) {
			parseWhat = NOTE;
			note = new IncomingNote(db, project_id, story.getStoryId());
		}
	}

	public void endElement(String uri, String name, String qName) throws SAXException {
		super.endElement(uri, name, qName);
		
		if (name.equalsIgnoreCase("iteration")) {
			iteration.save();
			iteration = null;
		}

		if (name.equalsIgnoreCase("story")) {
			story.save();
			story = null;
		}
		
		if (name.equalsIgnoreCase("note")) {
			note.save();
			note = null;
		}
	}

	public void characters(char ch[], int start, int length) {

		String chars = (new String(ch).substring(start, start + length));

		if (parseWhat == STORY) {
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

		if (parseWhat == ITERATION) {
			if (checkAndFillString(iteration, currentElementName, IterationData.NUMBER, chars)) return;
			if (checkAndFillString(iteration, currentElementName, IterationData.ID, chars)) return;
			if (checkAndFillString(iteration, currentElementName, IterationData.START, chars)) return;
			if (checkAndFillString(iteration, currentElementName, IterationData.FINISH, chars)) return;
		}
		
		if (parseWhat == NOTE) {
			if (checkAndFillString(note, currentElementName, NoteData.ID, chars)) return;
			if (checkAndFillString(note, currentElementName, NoteData.TEXT, chars)) return;
			if (checkAndFillString(note, currentElementName, NoteData.AUTHOR, chars)) return;
			if (checkAndFillString(note, currentElementName, NoteData.NOTED_AT, chars)) return;
		}
	}
}
