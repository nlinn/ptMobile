package me.linnemann.ptmobile.test.pivotaltracker.lifecycle;

import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;
import android.test.AndroidTestCase;

public class StateTests extends AndroidTestCase {

	public void test_createdState_hasNoTransitions() {
		State s = new State("testname");
		
		assertEquals("testname",s.getName());
		assertNotNull(s.getTransitions());
		assertTrue(s.getTransitions().size() == 0);
	}
	
	public void test_stateConstants_equalToSelfCreatedStates() {
		assertCreatedStateEqualsExpected(State.UNSTARTED,"unstarted");
		assertCreatedStateEqualsExpected(State.STARTED,"started");
		assertCreatedStateEqualsExpected(State.FINISHED,"finished");
		assertCreatedStateEqualsExpected(State.DELIVERED,"delivered");
		assertCreatedStateEqualsExpected(State.ACCEPTED,"accepted");
		assertCreatedStateEqualsExpected(State.REJECTED,"rejected");
	}
	
	private void assertCreatedStateEqualsExpected(State expectedState, String createStateFromThisName) {
		State stateFromName = new State(createStateFromThisName);
		assertEquals(expectedState, stateFromName);
	}
	
}
