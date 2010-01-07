package me.linnemann.ptmobile.test.functional;

import java.io.InputStream;
import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryType;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStoriesHandler;
import me.linnemann.ptmobile.test.pivotaltracker.DBAdapterMock;
import me.linnemann.ptmobile.test.pivotaltracker.TestData;
import android.test.AndroidTestCase;

public class XMLStoriesHandlerTests extends AndroidTestCase {

	private XMLStoriesHandler handler;
	private DBAdapterMock db;
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        db = new DBAdapterMock();
        handler = new XMLStoriesHandler(db, TestData.ANY_PROJECT_ID, TestData.ANY_ITERATIONGROUP);
    }

	public void test_simpleStoryExample_oneStoryInResult() throws Exception {
		List<Story> receivedStories = getReceivedStoriesForSimpleStoryXML();
		assertEquals(1, receivedStories.size());
	}
	
	private List<Story> getReceivedStoriesForSimpleStoryXML() throws Exception {
		handler.go(streamFromSimpleStoryXML());
		List<Story> receivedStories = db.getStories();
		return receivedStories;
	}
	
	private InputStream streamFromSimpleStoryXML() {
		return XMLStoriesHandlerTests.class.getResourceAsStream("storySimple.xml");
	}
	
	public void test_simpleStoryExample_correctId() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(new Integer(777190), story.getId());
	}

	private Story getFirstStoryOfReceivedStories() throws Exception {
		List<Story> receivedStories = getReceivedStoriesForSimpleStoryXML();
		return receivedStories.get(0);
	}
	
	public void test_simpleStoryExample_correctStoryType() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(StoryType.FEATURE, story.getStoryType());
	}

	public void test_simpleStoryExample_correctEstimate() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(new Integer(5), story.getEstimate());
	}
	
	public void test_simpleStoryExample_correctState() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(State.ACCEPTED, story.getCurrentState());
	}

	public void test_simpleStoryExample_correctDescription() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("", story.getDescription());
	}

	public void test_simpleStoryExample_correctName() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("PO can update list of stories via pivotal API", story.getName());
	}

	public void test_simpleStoryExample_correctRequestedBy() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("Niels Linnemann", story.getRequestedBy());
	}

	public void test_simpleStoryExample_correctOwnedBy() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("Niels Linnemann", story.getOwnedBy());
	}

	public void test_simpleStoryExample_correctLabels() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("view", story.getLabels());
	}
	
	public void test_simpleStoryExample_correctProjectId() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(TestData.ANY_PROJECT_ID, story.getProjectId());
	}
	
	public void test_simpleStoryExample_correctIterationGroup() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(TestData.ANY_ITERATIONGROUP, story.getIterationGroup());
	}
	
	public void test_simpleStoryExample_correctIterationNumber() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(null, story.getIterationNumber());
	}
	
	public void test_simpleStoryExample_correctCreatedAt() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("2009-06-07 11:52:35", story.getCreatedAt());
	}
	
	public void test_simpleStoryExample_correctAcceptedAt() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("2009-06-07 12:13:35", story.getAcceptedAt());
	}
	// TODO: Missing fields acceptedAt, createdAt, URL

}
