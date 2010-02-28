package me.linnemann.ptmobile.test.functional;

import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;
import me.linnemann.ptmobile.pivotaltracker.adapter.PivotalAPI;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.test.pivotaltracker.APIAdapterMock;
import me.linnemann.ptmobile.test.pivotaltracker.DBAdapterMock;
import me.linnemann.ptmobile.test.pivotaltracker.TestData;
import android.test.AndroidTestCase;

public class StoryFromIceboxXMLTests extends AndroidTestCase {

	private DBAdapterMock db;
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        db = new DBAdapterMock();
        
        APIAdapterMock adapter = new APIAdapterMock();
        adapter.setMockStream(StoryFromIceboxXMLTests.class.getResourceAsStream("storySimple.xml"));
        
        PivotalAPI api = new PivotalAPI(this.getContext(), db, adapter);
        api.readStories(TestData.ANY_PROJECT_ID, "icebox");
    }

	public void test_simpleStoryExample_correctStoryCountInResult() throws Exception {
		List<Story> receivedStories = getReceivedStoriesForSimpleStoryXML();
		assertEquals(5, receivedStories.size());
	}
	
	private List<Story> getReceivedStoriesForSimpleStoryXML() throws Exception {
		List<Story> receivedStories = db.getStories();
		return receivedStories;
	}
	
	public void test_simpleStoryExample_correctId() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(new Integer(777190), story.getId().getValue());
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
		assertEquals(Estimate.POINTS_5, story.getEstimate());
	}
	
	public void test_simpleStoryExample_correctState() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(State.ACCEPTED, story.getCurrentState());
	}

	public void test_simpleStoryExample_correctDescription() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("", story.getDescription().getValue());
	}

	public void test_simpleStoryExample_correctName() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("PO can update list of stories via pivotal API", story.getName().getValue());
	}

	public void test_simpleStoryExample_correctRequestedBy() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("Niels Linnemann", story.getRequestedBy().getValue());
	}

	public void test_simpleStoryExample_correctOwnedBy() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("Niels Linnemann", story.getOwnedBy().getValue());
	}

	public void test_simpleStoryExample_correctLabels() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("view", story.getLabels().getValue());
	}
	
	public void test_simpleStoryExample_correctProjectId() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(TestData.ANY_PROJECT_ID, story.getProjectId().getValue());
	}
	
	public void test_simpleStoryExample_correctIterationGroup() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("icebox", story.getIterationGroup().getValue());
	}
	
	public void test_simpleStoryExample_correctIterationNumber() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertTrue(null, story.getIterationNumber().isEmpty());
	}
	
	public void test_simpleStoryExample_correctCreatedAt() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("2009/06/07 11:52:35 UTC", story.getCreatedAt().getValueAsString());
	}
	
	public void test_simpleStoryExample_correctAcceptedAt() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals("2009/06/07 12:13:35 UTC", story.getAcceptedAt().getValueAsString());
	}
	
	public void test_simpleStoryExample_positionOne() throws Exception {
		Story story = getFirstStoryOfReceivedStories();
		assertEquals(new Integer(1), story.getPosition().getValue());
	}
	// TODO: Missing fields URL

}
