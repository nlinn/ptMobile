package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryField;
import android.test.AndroidTestCase;

public class StoryTests extends AndroidTestCase {

	private static final String TAG = "StoryTests";
	
	public void testModifiedFieldsInfo() {
		
		Story s = new StoryImpl();
		assertTrue(s.getModifiedFields().isEmpty());
		
		s.setEstimate(new Integer(1));
		assertTrue(s.getModifiedFields().contains(StoryField.ESTIMATE));
		
		s.toNextState();
		assertTrue(s.getModifiedFields().contains(StoryField.ESTIMATE));
		assertTrue(s.getModifiedFields().contains(StoryField.CURRENT_STATE));
		
		// Todo: add changes when implementing new data
		
		// --- and now reset
		s.resetModifiedFieldsTracking();
		assertTrue(s.getModifiedFields().isEmpty());
	}
	
	
	public void testFeatureStateEngine() {
	
		Story s = new StoryImpl();
		
		assertEquals("unstarted",s.getData(StoryField.CURRENT_STATE)); // --- initial state
		assertEquals(null,s.getNextState()); // --- next state only if estimated
		assertFalse(s.toNextState()); // --- next state only if estimated

		// --- unstarted -> started
		s.setEstimate(1);
		assertEquals("started",s.getNextState());
		assertTrue(s.toNextState());
		assertEquals("started",s.getData(StoryField.CURRENT_STATE));
		
		// --- started -> finished
		assertEquals("finished",s.getNextState());
		assertTrue(s.toNextState());
		assertEquals("finished",s.getData(StoryField.CURRENT_STATE));
		
		// --- finished --> delivered
		assertEquals("delivered",s.getNextState());
		assertTrue(s.toNextState());
		assertEquals("delivered",s.getData(StoryField.CURRENT_STATE));
		
		// --- delivered -> accepted
		assertEquals("accepted",s.getNextState());
		assertTrue(s.toNextState());
		assertEquals("accepted",s.getData(StoryField.CURRENT_STATE));
		
		// --- accepted is end
		assertEquals(null,s.getNextState());
		assertFalse(s.toNextState());
	}
}
