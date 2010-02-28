package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.ContentValueProvider;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataType;
import me.linnemann.ptmobile.pivotaltracker.datatype.StoryDataType;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.content.ContentValues;
import android.test.AndroidTestCase;

public class ContentValueProviderTests extends AndroidTestCase {

	
	private ContentValueProvider provider;
	private Story story;
	
	public void setUp() {
		story = new Story(StoryType.FEATURE);
		story.changeId(TestData.ANY_ID);
		story.resetModifiedDataTracking();
		provider = new ContentValueProvider(story);
	}
	
	public void test_storyContentValues_provideTimestamp() {
		story.changeEstimate(TestData.ANY_ESTIMATE);
		provider.fill();
		ContentValues values = provider.getValues();
		assertTrue(values.containsKey("updatetimestamp"));
	}
	
	public void test_storyContentValues_provideIDevenWhenNotChanged() {
		story.changeDescription(TestData.ANY_DESCRIPTION);
		provider.fill();
		ContentValues values = provider.getValues();
		assertEquals(TestData.ANY_ID.toString(), values.get("id"));
	}
	
	public void test_storyWithMultipleChanges_providesCorrectValues() {
		story.changeId(TestData.ANY_ID);
		story.changeEstimate(TestData.ANY_ESTIMATE);
		story.changeDescription(TestData.ANY_DESCRIPTION);
		provider.fill();
		
		checkContentValue(StoryDataType.ID, TestData.ANY_ID.toString());
		checkContentValue(StoryDataType.ESTIMATE, TestData.ANY_ESTIMATE.getValueAsString().toString());
		checkContentValue(StoryDataType.DESCRIPTION, TestData.ANY_DESCRIPTION);
	}

	private void fillAndCheckContentValue(DataType dataType, String expectedValue) {
		provider.fill();
		checkContentValue(dataType, expectedValue);
	}
	
	private void checkContentValue(DataType dataType, String expectedValue) {
		String dataFromValues = (String) provider.getValues().get(dataType.getDBColName());
		assertEquals(expectedValue, dataFromValues);
	}

	public void test_storyIterationNumber_providedWhenChanged() {
		story.changeIterationNumber(TestData.ANY_ITERATION_NUMBER);
		fillAndCheckContentValue(StoryDataType.ITERATION_NUMBER, TestData.ANY_ITERATION_NUMBER.toString());	
	}

	public void test_storyProjectId_providedWhenChanged() {
		story.changeProjectId(TestData.ANY_PROJECT_ID);
		fillAndCheckContentValue(StoryDataType.PROJECT_ID, TestData.ANY_PROJECT_ID.toString());	
	}

	public void test_storyEstimate_providedWhenChanged() {
		story.changeEstimate(TestData.ANY_ESTIMATE);
		fillAndCheckContentValue(StoryDataType.ESTIMATE, TestData.ANY_ESTIMATE.getValueAsString());	
	}
	
	public void test_storyType_providedWhenChanged() {
		story.changeStoryType(TestData.ANY_STORYTYPE);
		fillAndCheckContentValue(StoryDataType.STORY_TYPE, TestData.ANY_STORYTYPE.getValueAsString());	
	}

	public void test_storyLabels_providedWhenChanged() {
		story.changeLabels(TestData.ANY_LABELS);
		fillAndCheckContentValue(StoryDataType.LABELS, TestData.ANY_LABELS);	
	}

	public void test_storyCurrentState_providedWhenChanged() {
		story.changeCurrentState(TestData.ANY_STATE);
		fillAndCheckContentValue(StoryDataType.CURRENT_STATE, TestData.ANY_STATE.getValueAsString());	
	}
	
	public void test_storyDescription_providedWhenChanged() {
		story.changeDescription(TestData.ANY_DESCRIPTION);
		fillAndCheckContentValue(StoryDataType.DESCRIPTION, TestData.ANY_DESCRIPTION);	
	}

	public void test_storyDeadline_providedWhenChanged() {
		story.changeDeadline(TestData.ANY_DEADLINE);
		fillAndCheckContentValue(StoryDataType.DEADLINE,TestData.ANY_DEADLINE);
	}

	public void test_storyRequestedBy_providedWhenChanged() {
		story.changeRequestedBy(TestData.ANY_REQUESTED_BY);
		fillAndCheckContentValue(StoryDataType.REQUESTED_BY, TestData.ANY_REQUESTED_BY);
	}

	public void test_storyOwnedBy_providedWhenChanged() {
		story.changeOwnedBy(TestData.ANY_OWNED_BY);
		fillAndCheckContentValue(StoryDataType.OWNED_BY, TestData.ANY_OWNED_BY);
	}

	public void test_storyCreatedAt_providedWhenChanged() {
		story.changeCreatedAt(TestData.ANY_CREATED_AT);
		fillAndCheckContentValue(StoryDataType.CREATED_AT, TestData.ANY_CREATED_AT);
	}
	
	public void test_storyAcceptedAt_providedWhenChanged() {
		story.changeAcceptedAt(TestData.ANY_ACCEPTED_AT);
		fillAndCheckContentValue(StoryDataType.ACCEPTED_AT, TestData.ANY_ACCEPTED_AT);
	}

	public void test_storyIterationGroup_providedWhenChanged() {
		story.changeIterationGroup(TestData.ANY_ITERATIONGROUP);
		fillAndCheckContentValue(StoryDataType.ITERATION_GROUP, TestData.ANY_ITERATIONGROUP);
	}

	public void test_storyName_providedWhenChanged() {
		story.changeName(TestData.ANY_NAME);
		fillAndCheckContentValue(StoryDataType.NAME, TestData.ANY_NAME);
	}
}