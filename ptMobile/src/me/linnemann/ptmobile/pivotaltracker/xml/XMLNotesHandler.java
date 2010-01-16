package me.linnemann.ptmobile.pivotaltracker.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import me.linnemann.ptmobile.pivotaltracker.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.IncomingNote;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.fields.NoteData;

/**
 * parses xml for note elements and stores data via IncomingNote
 * 
 * @author nlinn
 */
public class XMLNotesHandler extends XMLBaseHandler {

	private IncomingNote note;
	private Integer story_id;
	private Integer project_id;
	
	public XMLNotesHandler(final DBAdapter db, final Story story) {
		super(db);
		this.story_id = story.getId().getValue();
		this.project_id = story.getProjectId().getValue();
	}

	public void startElement(String uri, String name, String qName, Attributes attr) {
		super.startElement(uri, name, qName, attr);
		
		if (name.equalsIgnoreCase("note")) {
			note = new IncomingNote(db, project_id, story_id);
		}
	}

	public void endElement(String uri, String name, String qName) throws SAXException {
		super.endElement(uri, name, qName);

		if (name.equalsIgnoreCase("note")) {
			note.save();
			note = null;
		}
	}

	public void characters(char ch[], int start, int length) {

		String chars = (new String(ch).substring(start, start + length));

		if (checkAndFillString(note, currentElementName, NoteData.ID, chars)) return;
		if (checkAndFillString(note, currentElementName, NoteData.TEXT, chars)) return;
		if (checkAndFillString(note, currentElementName, NoteData.AUTHOR, chars)) return;
		if (checkAndFillString(note, currentElementName, NoteData.NOTED_AT, chars)) return;
	}
}
