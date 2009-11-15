package me.linnemann.ptmobile.test.pivotaltracker;

import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.state.State;
import me.linnemann.ptmobile.pivotaltracker.state.Transition;
import android.test.AndroidTestCase;
import android.util.Log;

public class StoryTests extends AndroidTestCase {

	private static final String TAG = "StoryTests";
	
	public void testModifiedFieldsInfo() {
		
		Story s = new StoryImpl();
		assertTrue(s.getModifiedFields().isEmpty());
		
		s.setEstimate(new Integer(1));
		assertTrue(s.getModifiedFields().contains(StoryData.ESTIMATE));

		// TODO: add changes when implementing new data
		
		// --- and now reset
		s.resetModifiedFieldsTracking();
		assertTrue(s.getModifiedFields().isEmpty());
	}
	
	public void testNeedsEstimate() {
		StoryImpl s = new StoryImpl();
		// --- chore
		s.setData(StoryData.STORY_TYPE, "chore");
		assertFalse(s.needsEstimate()); // never needs to be estimated

		// --- bug
		s.setData(StoryData.STORY_TYPE, "bug");
		assertFalse(s.needsEstimate()); // never needs to be estimated

		// --- release
		s.setData(StoryData.STORY_TYPE, "release");
		assertFalse(s.needsEstimate()); // never needs to be estimated

		// --- feature
		s.setData(StoryData.STORY_TYPE, "feature");
		assertTrue(s.needsEstimate()); // Feature needs estimate
		s.setEstimate(new Integer(0)); // estimating
		assertFalse(s.needsEstimate()); // now it should not need estimate
	}
	
	public void testLifecycleConstructionRelease() {
		StoryImpl s = new StoryImpl();
		s.setData(StoryData.STORY_TYPE, "release");
		
		assertEquals("unstarted",s.getData(StoryData.CURRENT_STATE));
		
		State st = s.getCurrentState();
		assertEquals("unstarted",st.getName());
		Transition t = st.getTransitions().get(0);
		assertEquals("finish", t.getName());
		State st2 = t.resultingState();
		assertEquals("accepted",st2.getName());
		assertTrue(st2.getTransitions().size() == 0);
	}
	
	public void testLifecycleConstructionFeature() {
		StoryImpl s = new StoryImpl();
		s.setData(StoryData.STORY_TYPE, "feature");
		
		assertEquals("unstarted",s.getData(StoryData.CURRENT_STATE));
		
		State st = s.getCurrentState();
		assertEquals("unstarted",st.getName());
		Transition t = st.getTransitions().get(0);
		assertEquals("start", t.getName());
		
		State st2 = t.resultingState();
		assertEquals("started",st2.getName());
		Transition t2 =st2.getTransitions().get(0);
		assertEquals("finish", t2.getName());
		
		State st3 = t2.resultingState();
		assertEquals("finished",st3.getName());
		Transition t3 =st3.getTransitions().get(0);
		assertEquals("deliver", t3.getName());
	}
	
	public void testLifecycleConstructionBug() {
		StoryImpl s = new StoryImpl();
		s.setData(StoryData.STORY_TYPE, "bug");
		
		assertEquals("unstarted",s.getData(StoryData.CURRENT_STATE));
		
		State st = s.getCurrentState();
		assertEquals("unstarted",st.getName());
		Transition t = st.getTransitions().get(0);
		assertEquals("start", t.getName());
		
		State st2 = t.resultingState();
		assertEquals("started",st2.getName());
		Transition t2 =st2.getTransitions().get(0);
		assertEquals("finish", t2.getName());
		
		State st3 = t2.resultingState();
		assertEquals("finished",st3.getName());
		Transition t3 =st3.getTransitions().get(0);
		assertEquals("deliver", t3.getName());
	}
	
	public void testFeatureStateEngine() {
		Story s = new StoryImpl();
		s.setData(StoryData.STORY_TYPE, "feature");
		
		assertEquals("unstarted",s.getData(StoryData.CURRENT_STATE)); // --- initial state
		assertTrue(s.getAvailableTransitions().size() == 0); // features is not estimated -> no transition
		
		// --- "start" unstarted -> started
		s.setEstimate(1);	// estimating story is expected to "unlock" transition
		testStandardLifecycle(s); // rest of livecycle
	}
	
	public void testBugStateEngine() {
		Story s = new StoryImpl();
		s.setData(StoryData.STORY_TYPE, "bug");
		
		assertEquals("unstarted",s.getData(StoryData.CURRENT_STATE)); // --- initial state

		testStandardLifecycle(s); // rest of livecycle
	}
	
	public void testChoreStateEngine() {
		Story s = new StoryImpl();
		s.setData(StoryData.STORY_TYPE, "chore");
		
		assertEquals("unstarted",s.getData(StoryData.CURRENT_STATE)); // --- initial state

		// --- "start" unstarted -> started
		testSimpleTransion(s,"start","started");
						
		// --- "finish" started -> accepted
		testSimpleTransion(s,"finish","accepted");
		
		// --- no transitions from here
		assertTrue(s.getAvailableTransitions().size() == 0);
	}
	
	public void testReleaseStateEngine() {
		Story s = new StoryImpl();
		s.setData(StoryData.STORY_TYPE, "release");
		
		assertEquals("unstarted",s.getData(StoryData.CURRENT_STATE)); // --- initial state

		// --- "finish" transition: unstarted -> accepted
		testSimpleTransion(s,"finish","accepted");
		
		// --- no transitions from here
		Log.d(TAG,"transitions from "+s.getData(StoryData.CURRENT_STATE)+": "+s.getAvailableTransitions().toString());
		assertTrue(s.getAvailableTransitions().size() == 0);
	}

	/**
	 * helper doing a standard test where 1 possible transition
	 * is expected
	 * 
	 * this method is perfoming the transion, so your story will be modified!
	 * 
	 * @param s Story with current_state to test from
	 * @param expectedTransition transition you expect to be available from current state
	 * @param expectedState leading state
	 */
	private void testSimpleTransion(Story s, String expectedTransition, String expectedState) {
		Log.d(TAG,"Story "+s.getData(StoryData.STORY_TYPE)+", "+s.getData(StoryData.CURRENT_STATE));
		Log.d(TAG,"Testing "+expectedTransition+" -> "+expectedState);
		List<Transition> trans = s.getAvailableTransitions();
		Log.d(TAG,"Found transitions "+trans.toString());
		assertTrue(trans.size() == 1);	// only one transition expected!
		assertEquals(expectedTransition,((Transition) trans.get(0)).toString()); 
		assertTrue(s.doTransition((Transition) trans.get(0))); // transition
		assertEquals(expectedState,s.getData(StoryData.CURRENT_STATE)); 
	}
	
	/**
	 * testing the standard livecycle (same for features and bugs)
	 * 
	 * @param s Feature or Bug in state "started"
	 */
	private void testStandardLifecycle(Story s) {
		assertEquals("unstarted",s.getData(StoryData.CURRENT_STATE));
			
		// --- "start" unstarted -> started
		testSimpleTransion(s,"start","started");


		// --- "finish" started -> finished
		testSimpleTransion(s,"finish","finished");
		
		// --- "deliver" finished --> delivered
		testSimpleTransion(s,"deliver","delivered");
		
		// --- "accept" delivered -> accepted or
		// --- "reject" delivered -> rejected
		List<Transition> ts = s.getAvailableTransitions();
		assertTrue(ts.size() == 2); // 2 transitions expected
		assertEquals("accept",((Transition) ts.get(0)).toString()); // 1st entry 
		assertEquals("reject",((Transition) ts.get(1)).toString()); // 2nd entry
		
		assertTrue(s.doTransition((Transition) ts.get(0))); // transition
		assertEquals("accepted",s.getData(StoryData.CURRENT_STATE)); // new state "accepted"
		
		// TODO CHECK REJECT TRANSITION!
		
		// --- no transitions from here
		assertTrue(s.getAvailableTransitions().size() == 0);
	}
}

