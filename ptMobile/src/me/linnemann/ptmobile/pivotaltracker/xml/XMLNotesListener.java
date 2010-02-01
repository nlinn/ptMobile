package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.IncomingNote;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.fields.NoteData;

public class XMLNotesListener implements XMLStackListener {

	IncomingNote note;
	private DBAdapter db;
	private StoryContext story;
	
	public XMLNotesListener(DBAdapter db, StoryContext story) {
		this.db = db;
		this.story = story;
		initNote();
	}
	
	private void initNote() {
		note = new IncomingNote(db);
	}
	
	public void elementPoppedFromStack() {
		addMetaDataToNote();
		note.save();
		initNote();
	}
	
	private void addMetaDataToNote() {
		note.addDataForKey(NoteData.PROJECT_ID.getDBFieldName(), story.getProjectId().toString());
		note.addDataForKey(NoteData.STORY_ID.getDBFieldName(), story.getStoryId().toString());
	}

	public void handleSubElement(String element, String data) {
		note.addDataForKey(element, data);		
	}
}