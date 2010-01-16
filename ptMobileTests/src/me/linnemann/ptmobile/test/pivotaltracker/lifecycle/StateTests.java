package me.linnemann.ptmobile.test.pivotaltracker.lifecycle;

import me.linnemann.ptmobile.pivotaltracker.lifecycle.StateWithTransitions;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import android.test.AndroidTestCase;

public class StateTests extends AndroidTestCase {

	public void test_createdState_hasNoTransitions() {
		StateWithTransitions s = new StateWithTransitions(State.STARTED);
		
		assertEquals(State.STARTED, s.getState());
		assertNotNull(s.getTransitions());
		assertTrue(s.getTransitions().size() == 0);
	}
}
