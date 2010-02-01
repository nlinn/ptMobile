package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.StateWithTransitions;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.test.AndroidTestCase;

public class StoryTests extends AndroidTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "StoryTests";
	
	private StoryImpl story;
	
	public void setUp() {
		story = new StoryImpl(StoryType.FEATURE); 	
	}
	
	public void test_stateAfterCreation_isUnscheduled() {
		assertEquals(State.UNSCHEDULED, story.getCurrentState());
	}
	
	public void test_modifiedFieldsAfterCreation_isEmpty() {
		assertTrue(story.getModifiedData().isEmpty());
	}
	
	public void test_resetModifiedFields_works() {
		story.changeEstimate(TestData.ANY_ESTIMATE);
		story.changeName(TestData.ANY_NAME);
		story.resetModifiedDataTracking();
		assertTrue(story.getModifiedData().isEmpty());
	}
	
	public void test_chore_needsNoEstimate() {
		story.changeStoryType(StoryType.CHORE);
		assertFalse(story.needsEstimate());
	}
	
	public void test_release_needsNoEstimate() {
		story.changeStoryType(StoryType.RELEASE);
		assertFalse(story.needsEstimate());
	}
	
	public void test_bug_needsNoEstimate() {
		story.changeStoryType(StoryType.BUG);
		assertFalse(story.needsEstimate());
	}
	
	public void test_feature_needsEstimate() {
		story.changeStoryType(StoryType.FEATURE);
		assertTrue(story.needsEstimate());
	}
	
	public void test_feature_needsNoEstimateAfterEstimating() {
		story.changeStoryType(StoryType.FEATURE);
		story.changeEstimate(TestData.ANY_ESTIMATE);
		assertFalse(story.needsEstimate());
	}
	
	public void test_story_nameNotModifiedIfSameContentIsSet() {
		
		String sameNameTwice = TestData.ANY_NAME;
		
		story.changeName(sameNameTwice);
		story.resetModifiedDataTracking();
		story.changeName(sameNameTwice);
		
		assertEquals(0,story.getModifiedData().size());
	}
	
	public void test_story_positionOneIsFirstInIteration() {
		story.changePosition(1);
		assertTrue(story.isFirstInIteration());
	}
}

