package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.datatype.NoteDataType;
import me.linnemann.ptmobile.pivotaltracker.value.DateTime;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;

public class Note extends TrackerEntity {
	
	public static Note emptyNote() {
		return new Note();
	}
	
	public Numeric getId() {
		return (Numeric) data.get(NoteDataType.ID);
	}
	
	public Numeric getProjectId() {
		return (Numeric) data.get(NoteDataType.PROJECT_ID);
	}
	
	public Numeric getStoryId() {
		return (Numeric) data.get(NoteDataType.STORY_ID);
	}
	
	public Text getText() {
		return (Text) data.get(NoteDataType.TEXT);
	}
	
	public Text getAuthor() {
		return (Text) data.get(NoteDataType.AUTHOR);
	}
	
	public DateTime getNotedAt() {
		return (DateTime) data.get(NoteDataType.NOTED_AT);
	}
	
	@Override
	public String getTableName() {
		return "notes";
	}

	public String getUIString() {
		return getText().getUIString() + "\n" + getAuthor().getUIString() + ", "+ getNotedAt().getUIString();
	}
}
