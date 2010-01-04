package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.ContentValueProvider;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.StoryType;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;
import android.content.ContentValues;
import android.test.AndroidTestCase;

public class ContentValueProviderTests extends AndroidTestCase {

	private static Integer ANY_ID = 12384932;
	private static Integer ANY_ITERATION_NUMBER = 38;
	private static Integer ANY_PROJECT_ID = 8938;
	private static Integer ANY_ESTIMATE = 8;
	private static StoryType ANY_STORYTYPE = StoryType.CHORE;
	private static String ANY_LABELS = "one,two,three";
	private static State ANY_STATE = State.UNSTARTED;
	private static String ANY_DESCRIPTION = "Just a Description.";
	private static String ANY_DEADLINE = "02.02.2010";
	private static String ANY_REQUESTED_BY = "Mr. Requester";
	private static String ANY_OWNED_BY = "Mrs. Owner";
	private static String ANY_CREATED_AT = "28.10.2009";
	private static String ANY_ACCEPTED_AT = "24.12.2010";
	private static String ANY_ITERATIONGROUP = "done";
	private static String ANY_NAME = "My Name";
	
	private ContentValueProvider provider;
	private Story story;
	
	public void setUp() {
		story = new StoryImpl();
		provider = new ContentValueProvider(story);
	}
	
	public void test_storyContentValues_provideTimestamp() {
		story.changeId(ANY_ID);
		provider.fill();
		ContentValues values = provider.getValues();
		assertTrue(values.containsKey("updatetimestamp"));
	}
	
	public void test_storyWithMultipleChanges_providesMultipleValues() {
		story.changeId(ANY_ID);
		story.changeEstimate(ANY_ESTIMATE);
		provider = new ContentValueProvider(story);
		provider.fill();
		ContentValues values = provider.getValues();
		assertEquals(2, values.size());
	}
	
	public void test_storyWithMultipleChanges_providesCorrectValues() {
		story.changeId(ANY_ID);
		story.changeEstimate(ANY_ESTIMATE);
		story.changeDescription(ANY_DESCRIPTION);
		provider.fill();
		
		checkContentValue(StoryData.ID, ANY_ID.toString());
		checkContentValue(StoryData.ESTIMATE, ANY_ESTIMATE.toString());
		checkContentValue(StoryData.DESCRIPTION, ANY_DESCRIPTION);
	}
	
	public void test_resettedModifiedFields_provideNoData() {
		story.changeAcceptedAt(ANY_ACCEPTED_AT);
		story.changeDescription(ANY_DESCRIPTION);
		story.changeName(ANY_NAME);
		story.resetModifiedFieldsTracking();
		provider = new ContentValueProvider(story);
		provider.fill();
		ContentValues values = provider.getValues();
		assertEquals(0, values.size());
	}
	
	public void test_storyId_providedWhenChanged() {
		story.changeId(ANY_ID);
		fillAndCheckContentValue(StoryData.ID, ANY_ID.toString());
	}

	private void fillAndCheckContentValue(StoryData storyData, String expectedValue) {
		provider.fill();
		checkContentValue(storyData, expectedValue);
	}
	
	private void checkContentValue(StoryData storyData, String expectedValue) {
		String dataFromValues = (String) provider.getValues().get(storyData.getDBFieldName());
		assertEquals(expectedValue, dataFromValues);
	}

	public void test_storyIterationNumber_providedWhenChanged() {
		story.changeIterationNumber(ANY_ITERATION_NUMBER);
		fillAndCheckContentValue(StoryData.ITERATION_NUMBER, ANY_ITERATION_NUMBER.toString());	
	}

	public void test_storyProjectId_providedWhenChanged() {
		story.changeProjectId(ANY_PROJECT_ID);
		fillAndCheckContentValue(StoryData.PROJECT_ID, ANY_PROJECT_ID.toString());	
	}

	public void test_storyEstimate_providedWhenChanged() {
		story.changeEstimate(ANY_ESTIMATE);
		fillAndCheckContentValue(StoryData.ESTIMATE, ANY_ESTIMATE.toString());	
	}
	
	public void test_storyType_providedWhenChanged() {
		story.changeStoryType(ANY_STORYTYPE);
		fillAndCheckContentValue(StoryData.STORY_TYPE, ANY_STORYTYPE.toString());	
	}

	public void test_storyLabels_providedWhenChanged() {
		story.changeLabels(ANY_LABELS);
		fillAndCheckContentValue(StoryData.LABELS, ANY_LABELS);	
	}

	public void test_storyCurrentState_providedWhenChanged() {
		story.changeCurrentState(ANY_STATE);
		fillAndCheckContentValue(StoryData.CURRENT_STATE, ANY_STATE.toString());	
	}
	
	public void test_storyDescription_providedWhenChanged() {
		story.changeDescription(ANY_DESCRIPTION);
		fillAndCheckContentValue(StoryData.DESCRIPTION, ANY_DESCRIPTION);	
	}

	public void test_storyDeadline_providedWhenChanged() {
		story.changeDeadline(ANY_DEADLINE);
		fillAndCheckContentValue(StoryData.DEADLINE, ANY_DEADLINE);
	}

	public void test_storyRequestedBy_providedWhenChanged() {
		story.changeRequestedBy(ANY_REQUESTED_BY);
		fillAndCheckContentValue(StoryData.REQUESTED_BY, ANY_REQUESTED_BY);
	}

	public void test_storyOwnedBy_providedWhenChanged() {
		story.changeOwnedBy(ANY_OWNED_BY);
		fillAndCheckContentValue(StoryData.OWNED_BY, ANY_OWNED_BY);
	}

	public void test_storyCreatedAt_providedWhenChanged() {
		story.changeCreatedAt(ANY_CREATED_AT);
		fillAndCheckContentValue(StoryData.CREATED_AT, ANY_CREATED_AT);
	}
	
	public void test_storyAcceptedAt_providedWhenChanged() {
		story.changeAcceptedAt(ANY_ACCEPTED_AT);
		fillAndCheckContentValue(StoryData.ACCEPTED_AT, ANY_ACCEPTED_AT);
	}

	public void test_storyIterationGroup_providedWhenChanged() {
		story.changeIterationGroup(ANY_ITERATIONGROUP);
		fillAndCheckContentValue(StoryData.ITERATION_GROUP, ANY_ITERATIONGROUP);
	}

	public void test_storyName_providedWhenChanged() {
		story.changeName(ANY_NAME);
		fillAndCheckContentValue(StoryData.NAME, ANY_NAME);
	}
}