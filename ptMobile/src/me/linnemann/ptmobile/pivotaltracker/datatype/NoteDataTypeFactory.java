package me.linnemann.ptmobile.pivotaltracker.datatype;

import me.linnemann.ptmobile.pivotaltracker.Note;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;

public class NoteDataTypeFactory extends DataTypeFactory {
	
	public NoteDataTypeFactory() {
		super(NoteDataType.KNOWNTYPES);
	}

	@Override
	public TrackerEntity getEmptyEntity() {
		return Note.emptyNote();
	}
}