package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.IncomingIteration;
import me.linnemann.ptmobile.pivotaltracker.IncomingNote;
import me.linnemann.ptmobile.pivotaltracker.StoryFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.fields.IterationData;
import me.linnemann.ptmobile.pivotaltracker.fields.NoteData;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

public class XMLStoriesHandler extends XMLBaseHandler {
	@SuppressWarnings("unused")
	private static final String TAG="XMLStoriesHandler";
	
	private static final int STORY = 1;
	private static final int ITERATION = 2;
	private static final int NOTE = 3;
	
	private Integer project_id;
	private IncomingIteration iteration;
	private IncomingNote note;
	private String iteration_group;
	private int parseWhat;
	private StringBuilder elementData;
	private StoryFromAPIBuilder storyBuilder;
	private String iteration_number;
	private Integer story_id;
	
	public XMLStoriesHandler(DBAdapter db, Integer project_id, String iteration_group) {
		super(db);
		this.project_id = project_id;
		this.iteration_group = iteration_group;
		this.storyBuilder = new StoryFromAPIBuilder();
		this.iteration_number = "";
	}

	public void startElement(String uri, String name, String qName, Attributes attr) {
		super.startElement(uri, name, qName, attr);

		elementData = new StringBuilder();
		
		if (name.equalsIgnoreCase("iteration")) {
			parseWhat = ITERATION;
			iteration = new IncomingIteration(db, project_id, iteration_group);
		}

		if (name.equalsIgnoreCase("story")) {
			parseWhat = STORY;
			
			if (iteration != null) {
				iteration_number = iteration.getIterationNumber();
			}
			
			initStoryBuilder();
		}

		if (name.equalsIgnoreCase("note")) {
			parseWhat = NOTE;
			note = new IncomingNote(db, project_id, story_id);
		}
	}

	// --- Note: uri and qname seem to be always null on android 1.5
	public void endElement(String uri, String name, String qName) throws SAXException {
		
		
		if (parseWhat == STORY) {
			Log.d(TAG, "curr: "+currentElementName);
			storyBuilder.add(currentElementName, elementData.toString());
			
			if ((currentElementName != null) && (currentElementName.equalsIgnoreCase("id"))) {
				story_id = new Integer(elementData.toString());
			}
		}
		
		if (name.equalsIgnoreCase("iteration")) {
			iteration.save();
			iteration = null;
		}

		if (name.equalsIgnoreCase("story")) {
			db.insertStory(storyBuilder.getStory());
		}
		
		if (name.equalsIgnoreCase("note")) {
			note.save();
			note = null;
		}
		
		super.endElement(uri, name, qName);
	}

	private void initStoryBuilder() {
		storyBuilder.clear();
		storyBuilder.add(StoryData.PROJECT_ID.toString(), project_id.toString());
		storyBuilder.add(StoryData.ITERATION_NUMBER.toString(), iteration_number.toString());
		storyBuilder.add(StoryData.ITERATION_GROUP.toString(), iteration_group);
	}
	
	public void characters(char ch[], int start, int length) {

		elementData.append(ch, start, length);
		String chars = (new String(ch).substring(start, start + length));
		
		Log.i(TAG,chars);

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
