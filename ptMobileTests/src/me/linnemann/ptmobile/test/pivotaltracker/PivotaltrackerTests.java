package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Story;
import android.test.AndroidTestCase;

public class PivotaltrackerTests extends AndroidTestCase {
	
	private PivotalTracker tracker;
	
	public void setUp() {
		tracker = new PivotalTracker(this.getContext());
	}
	
	public void test_getEmptyStory_isNotModified() {
		Story story = tracker.getEmptyStoryForProject(TestData.ANY_PROJECT_ID);
		assertTrue(story.getModifiedData().isEmpty());
	}
	
	public void test_getEmptyStory_hasEmptyID() {
		Story story = tracker.getEmptyStoryForProject(TestData.ANY_PROJECT_ID);
		assertTrue(story.getId().isEmpty());
	}

}