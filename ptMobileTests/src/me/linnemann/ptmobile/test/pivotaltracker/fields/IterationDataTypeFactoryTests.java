package me.linnemann.ptmobile.test.pivotaltracker.fields;

import me.linnemann.ptmobile.pivotaltracker.datatype.IterationDataTypeFactory;
import android.test.AndroidTestCase;

public class IterationDataTypeFactoryTests extends AndroidTestCase {

	private IterationDataTypeFactory factory;
	
	public void setUp() {
		factory = new IterationDataTypeFactory();
	}
	
	public void test_factoryknowsType_iterationType() {
		assertTrue(factory.knownTypes.containsKey("id"));
		assertTrue(factory.knownTypes.containsKey("number"));
		assertTrue(factory.knownTypes.containsKey("start"));
		assertTrue(factory.knownTypes.containsKey("finish"));
		assertTrue(factory.knownTypes.containsKey("project_id"));
	}
	
}

