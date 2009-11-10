package state;

import me.linnemann.ptmobile.pivotaltracker.state.State;
import android.test.AndroidTestCase;

public class StateTests extends AndroidTestCase {

	public void testCreateStateWithoutTransition() {
		
		State s = new State("testname");
		
		assertEquals("testname",s.getName());
		assertNotNull(s.getTransitions());
		assertTrue(s.getTransitions().size() == 0);
	}
	
}
