package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.EntityFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.Note;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.datatype.NoteDataType;
import me.linnemann.ptmobile.pivotaltracker.datatype.NoteDataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;

public class XMLNotesListener implements XMLStackListener {

	private DBAdapter db;
	private StoryContext story;
	
	private DataTypeFactory factory;
	private EntityFromAPIBuilder builder;
	
	public XMLNotesListener(DBAdapter db, StoryContext story) {
		this.db = db;
		this.story = story;
		this.factory = new NoteDataTypeFactory();
		initBuilder();
	}
	
	private void initBuilder() {
		builder = new EntityFromAPIBuilder(factory);
	}
	
	public void elementPoppedFromStack() {
		Note note = (Note) builder.getEntity();
		addMetaDataToNote(note);
		db.insertEntity(note);
		initBuilder();
	}
	
	private void addMetaDataToNote(Note note) {
		note.putDataAndTrackChanges(NoteDataType.PROJECT_ID, new Numeric(story.getProjectId()));
		note.putDataAndTrackChanges(NoteDataType.STORY_ID, new Numeric(story.getStoryId()));
	}

	public void handleSubElement(String element, String data) {
		builder.add(element, data);	
	}
}