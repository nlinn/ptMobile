package me.linnemann.ptmobile.test.pivotaltracker.fields;

import java.util.HashMap;

import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataType;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.datatype.NoteDataType;
import android.test.AndroidTestCase;

public class DataTypeFactoryTests extends AndroidTestCase {

	private DataTypeFactory factory;
	private HashMap<String, DataType> knownTypes;
	
	public void setUp() {
		knownTypes = new HashMap<String, DataType>();
		knownTypes.put("first", NoteDataType.NOTED_AT);
		
		factory = new DataTypeFactory(knownTypes) {
			
			@Override
			public TrackerEntity getEmptyEntity() {
				return null;
			}
		};
	}
	
	public void test_factoryinit_knownTypesArrayCorrect() {
		assertEquals(knownTypes, factory.knownTypes);
	}
	
	public void test_factoryinit_firstKnownTypeCorrect() {
		DataType type = factory.knownTypes.get("first");
		assertEquals(NoteDataType.NOTED_AT, type);
	}	
}
