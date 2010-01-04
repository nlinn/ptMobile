package me.linnemann.ptmobile.test.pivotaltracker.lifecycle;

import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.StoryType;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Lifecycle;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.LifecycleFactoryImpl;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import android.test.AndroidTestCase;
import android.util.Log;

public class LifecycleFactoryImplTests extends AndroidTestCase {

	private static final String TAG = "LifecycleFactoryImplTests";
	private LifecycleFactoryImpl factory;
	
	public void setUp() {
		factory = new LifecycleFactoryImpl();
	}
	
	public void test_lifecycleOfStoryWithoutType_startingStateIsCorrect() {
		StoryImpl story = new StoryImpl(StoryType.UNKNOWN);
		Lifecycle lifecycle = factory.getRemainingLifecycle(story.getStoryType(), story.getCurrentState());
		assertEquals(State.UNSTARTED, lifecycle.getState());
	}

	public void test_lifecycleStoryWithoutType_startingStateHasNoTransitions() {
		StoryImpl story = new StoryImpl(StoryType.UNKNOWN);
		Lifecycle lifecycle = factory.getRemainingLifecycle(story.getStoryType(), story.getCurrentState());
		List<Transition> transitions = lifecycle.getAvailableTransitions();
		assertTrue(transitions.isEmpty());
	}
	
	public void test_featureStateEngine_transitionsWork() {
		StoryImpl story = new StoryImpl(StoryType.FEATURE);
		
		Lifecycle lifecycle = story.getLifecycle();
		
		assertEquals(State.UNSTARTED, lifecycle.getState()); // --- initial state
		assertTrue(lifecycle.getAvailableTransitions().size() == 0); // features is not estimated -> no transition
		
		// --- "start" unstarted -> started
		story.changeEstimate(1);	// estimating story is expected to "unlock" transition
		testBugAndFeatureLifecycle(story); // rest of livecycle
	}
	
	/**
	 * testing the standard livecycle (same for features and bugs)
	 * 
	 * @param story Feature or Bug in state "started"
	 */
	private void testBugAndFeatureLifecycle(Story story) {
		assertEquals(State.UNSTARTED, story.getCurrentState());
			
		// --- "start" unstarted -> started
		testSimpleTransion(story,"start", State.STARTED);

		// --- "finish" started -> finished
		testSimpleTransion(story,"finish", State.FINISHED);
		
		// --- "deliver" finished --> delivered
		testSimpleTransion(story,"deliver", State.DELIVERED);
		
		// --- "accept" delivered -> accepted or
		// --- "reject" delivered -> rejected
		Lifecycle lifecycle = story.getLifecycle();
		List<Transition> transitions = lifecycle.getAvailableTransitions();
		assertTrue(transitions.size() == 2); // 2 transitions expected
		assertEquals(new Transition("accept", null), transitions.get(0)); 
		assertEquals(new Transition("reject", null), transitions.get(1));
		
		assertTrue(lifecycle.doTransition((Transition) transitions.get(0))); // transition
		assertEquals(State.ACCEPTED, story.getCurrentState()); // new state "accepted"
		
		// TODO CHECK REJECT TRANSITION!
		
		// --- no transitions from here
		assertTrue(lifecycle.getAvailableTransitions().size() == 0);
	}
	
	public void test_bugStateEngine_transitionsWork() {
		StoryImpl story = new StoryImpl(StoryType.BUG);
		assertEquals(State.UNSTARTED, story.getCurrentState()); // --- initial state
		testBugAndFeatureLifecycle(story); // rest of livecycle
	}
	
	public void test_choreStateEngine_transitionsWork() {
		StoryImpl story = new StoryImpl(StoryType.CHORE);
		
		assertEquals(State.UNSTARTED,story.getCurrentState()); // --- initial state

		// --- "start" unstarted -> started
		testSimpleTransion(story,"start", State.STARTED);
						
		// --- "finish" started -> accepted
		testSimpleTransion(story,"finish", State.ACCEPTED);
		
		// --- no transitions from here
		assertTrue(story.getLifecycle().getAvailableTransitions().size() == 0);
	}
	
	public void test_releaseStateEngine_transitionsWork() {
		StoryImpl s = new StoryImpl(StoryType.RELEASE);
		
		assertEquals(State.UNSTARTED, s.getCurrentState()); // --- initial state

		// --- "finish" transition: unstarted -> accepted
		testSimpleTransion(s,"finish", State.ACCEPTED);
		
		// --- no transitions from here
		assertTrue(s.getLifecycle().getAvailableTransitions().size() == 0);
	}
	
	/**
	 * helper doing a standard test where 1 possible transition
	 * is expected
	 * 
	 * this method is perfoming the transion, so your story will be modified!
	 * 
	 * @param story Story with current_state to test from
	 * @param expectedTransition transition you expect to be available from current state
	 * @param expectedState leading state
	 */
	private void testSimpleTransion(Story story, String expectedTransition, State expectedState) {
		Log.d(TAG,"Story "+story.getStoryType()+", "+story.getCurrentState());
		Log.d(TAG,"Testing "+expectedTransition+" -> "+expectedState);
		Lifecycle lifecycle = story.getLifecycle();
		List<Transition> trans = lifecycle.getAvailableTransitions();
		Log.d(TAG,"Found transitions "+trans.toString());
		assertTrue(trans.size() == 1);	// only one transition expected!
		assertEquals(expectedTransition,((Transition) trans.get(0)).toString()); 
		assertTrue(lifecycle.doTransition((Transition) trans.get(0))); // transition
		assertEquals(expectedState,story.getCurrentState()); 
	}
	
	public void test_releaseLifecycle_Construction() {
		StoryImpl s = new StoryImpl(StoryType.RELEASE);
		
		assertEquals(State.UNSTARTED,s.getCurrentState());
		
		State st = s.getLifecycle().getState();
		assertEquals("unstarted",st.getName());
		Transition t = st.getTransitions().get(0);
		assertEquals("finish", t.getName());
		State st2 = t.resultingState();
		assertEquals("accepted",st2.getName());
		assertTrue(st2.getTransitions().size() == 0);
	}
	
	public void test_featureLifecycle_Construction() {
		StoryImpl s = new StoryImpl(StoryType.FEATURE);
		
		assertEquals(State.UNSTARTED,s.getCurrentState());
		
		State st = s.getLifecycle().getState();
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
	
	public void test_bugLifecycle_Construction() {
		StoryImpl s = new StoryImpl(StoryType.BUG);
		
		assertEquals(State.UNSTARTED,s.getCurrentState());
		
		State st = s.getLifecycle().getState();
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
}
