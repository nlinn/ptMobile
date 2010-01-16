package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryFromCursorBuilder;
import android.test.AndroidTestCase;

public class StoryFromCursorBuilderTests extends AndroidTestCase {

	private StoriesCursorMock cursor;
	private StoryFromCursorBuilder storyBuilder;
	private Story story;
	
	public void setUp() {
		cursor = new StoriesCursorMock();
		storyBuilder = new StoryFromCursorBuilder(cursor);
		storyBuilder.construct();
		story = storyBuilder.getStory();
	}
	
	public void test_builtStory_hasId() {
		assertEquals(TestData.ANY_ID, story.getId().getValue());
	}
	
	public void test_builtStory_hasProjectId() {
		assertEquals(TestData.ANY_PROJECT_ID, story.getProjectId().getValue());
	}
	
	public void test_builtStory_hasName() {
		assertEquals(TestData.ANY_NAME, story.getName().getValue());
	}
	
	public void test_builtStory_hasDescription() {
		assertEquals(TestData.ANY_DESCRIPTION, story.getDescription().getValue());
	}
	
	public void test_builtStory_hasDeadline() {
		assertEquals(TestData.ANY_DEADLINE, story.getDeadline().getValue());
	}

	public void test_builtStory_hasAcceptedAt() {
		assertEquals(TestData.ANY_ACCEPTED_AT, story.getAcceptedAt().getValue());
	}

	public void test_builtStory_hasCreatedAt() {
		assertEquals(TestData.ANY_CREATED_AT, story.getCreatedAt().getValue());
	}

	public void test_builtStory_hasIterationGroup() {
		assertEquals(TestData.ANY_ITERATIONGROUP, story.getIterationGroup().getValue());
	}
	
	public void test_builtStory_hasLabels() {
		assertEquals(TestData.ANY_LABELS, story.getLabels().getValue());
	}
	
	public void test_builtStory_hasOwnedBy() {
		assertEquals(TestData.ANY_OWNED_BY, story.getOwnedBy().getValue());
	}
	
	public void test_builtStory_hasRequestedBy() {
		assertEquals(TestData.ANY_REQUESTED_BY, story.getRequestedBy().getValue());
	}

	public void test_builtStory_hasEstimate() {
		assertEquals(TestData.ANY_ESTIMATE, story.getEstimate().getValue());
	}

	public void test_builtStory_hasIterationNumber() {
		assertEquals(TestData.ANY_ITERATION_NUMBER, story.getIterationNumber().getValue());
	}

	public void test_builtStory_hasCurrentState() {
		assertEquals(TestData.ANY_STATE, story.getCurrentState().getValue());
	}

	public void test_builtStory_hasStoryType() {
		assertEquals(TestData.ANY_STORYTYPE, story.getStoryType().getValue());
	}
	
	public void test_builtStory_hasNoModifiedFlags() {
		assertEquals(0,story.getModifiedFields().size());
	}
}
