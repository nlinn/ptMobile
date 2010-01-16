package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.ContentValueProvider;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Story;
import android.content.ContentValues;
import android.test.AndroidTestCase;

public class PivotaltrackerTests extends AndroidTestCase {
	
	private PivotalTracker tracker;
	
	public void setUp() {
		tracker = new PivotalTracker(this.getContext());
	}
	
	public void test_getEmptyStory_isNotModified() {
		Story story = tracker.getEmptyStoryForProject(TestData.ANY_PROJECT_ID);
		assertTrue(story.getModifiedFields().isEmpty());
	}
	
	public void test_getEmptyStory_providesEmptyContentValues() {
		Story story = tracker.getEmptyStoryForProject(TestData.ANY_PROJECT_ID);
		ContentValueProvider provider = new ContentValueProvider(story);
		provider.fill();
		ContentValues cv = provider.getValues();
		
		assertNotNull(cv);
		assertEquals(0,cv.size());
	}

	public void test_getEmptyStory_hasEmptyID() {
		Story story = tracker.getEmptyStoryForProject(TestData.ANY_PROJECT_ID);
		assertTrue(story.getId().isEmpty());
	}

}