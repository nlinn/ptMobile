package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.StoryType;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;
import android.test.AndroidTestCase;

public class StoryTests extends AndroidTestCase {

	private static final Integer ANY_ESTIMATE = 8; 
	private static final String ANY_NAME = "yo mama"; 
	
	@SuppressWarnings("unused")
	private static final String TAG = "StoryTests";
	
	private StoryImpl story;
	
	public void setUp() {
		story = new StoryImpl(StoryType.FEATURE); 	
	}
	
	public void test_stateAfterCreation_isUnstarted() {
		assertEquals(State.UNSTARTED, story.getCurrentState());
	}
	
	public void test_modifiedFieldsAfterCreation_isEmpty() {
		assertTrue(story.getModifiedFields().isEmpty());
	}
	
	public void test_resetModifiedFields_works() {
		story.changeEstimate(ANY_ESTIMATE);
		story.changeName(ANY_NAME);
		story.resetModifiedFieldsTracking();
		assertTrue(story.getModifiedFields().isEmpty());
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
		story.changeEstimate(ANY_ESTIMATE);
		assertFalse(story.needsEstimate());
	}
}

