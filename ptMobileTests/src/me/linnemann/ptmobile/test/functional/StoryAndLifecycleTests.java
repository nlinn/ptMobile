package me.linnemann.ptmobile.test.functional;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.StateWithTransitions;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.test.pivotaltracker.TestData;
import android.test.AndroidTestCase;

public class StoryAndLifecycleTests extends AndroidTestCase {

	public void test_estimatedUnstartedFeature_hasTransitionStart() {
		
		Story story = new Story();
		story.changeStoryType(StoryType.FEATURE);
		story.changeEstimate(TestData.ANY_ESTIMATE);
		story.changeCurrentState(State.UNSTARTED);
		story.resetModifiedDataTracking();
		

		Transition start  = story.getTransitions().get(0);
		
		assertEquals("start", start.getName());
	}
	
}
