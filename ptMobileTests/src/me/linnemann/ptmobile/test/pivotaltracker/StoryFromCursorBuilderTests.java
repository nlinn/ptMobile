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
		assertEquals(TestData.ANY_ID, story.getId());
	}
	
	public void test_builtStory_hasProjectId() {
		assertEquals(TestData.ANY_PROJECT_ID, story.getProjectId());
	}
	
	public void test_builtStory_hasName() {
		assertEquals(TestData.ANY_NAME, story.getName());
	}
	
	public void test_builtStory_hasDescription() {
		assertEquals(TestData.ANY_DESCRIPTION, story.getDescription());
	}
	
	public void test_builtStory_hasDeadline() {
		assertEquals(TestData.ANY_DEADLINE, story.getDeadline());
	}

	public void test_builtStory_hasAcceptedAt() {
		assertEquals(TestData.ANY_ACCEPTED_AT, story.getAcceptedAt());
	}

	public void test_builtStory_hasCreatedAt() {
		assertEquals(TestData.ANY_CREATED_AT, story.getCreatedAt());
	}

	public void test_builtStory_hasIterationGroup() {
		assertEquals(TestData.ANY_ITERATIONGROUP, story.getIterationGroup());
	}
	
	public void test_builtStory_hasLabels() {
		assertEquals(TestData.ANY_LABELS, story.getLabels());
	}
	
	public void test_builtStory_hasOwnedBy() {
		assertEquals(TestData.ANY_OWNED_BY, story.getOwnedBy());
	}
	
	public void test_builtStory_hasRequestedBy() {
		assertEquals(TestData.ANY_REQUESTED_BY, story.getRequestedBy());
	}

	public void test_builtStory_hasEstimate() {
		assertEquals(TestData.ANY_ESTIMATE, story.getEstimate());
	}

	public void test_builtStory_hasIterationNumber() {
		assertEquals(TestData.ANY_ITERATION_NUMBER, story.getIterationNumber());
	}

	public void test_builtStory_hasCurrentState() {
		assertEquals(TestData.ANY_STATE, story.getCurrentState());
	}

	public void test_builtStory_hasStoryType() {
		assertEquals(TestData.ANY_STORYTYPE, story.getStoryType());
	}
	
	public void test_builtStory_hasNoModifiedFlags() {
		assertEquals(0,story.getModifiedFields().size());
	}
}
