package me.linnemann.ptmobile.test.functional;

import java.io.InputStream;
import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.Iteration;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;
import me.linnemann.ptmobile.pivotaltracker.datatype.IterationDataType;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLIterationListener;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLNotesListener;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStack;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStackHandler;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStoriesListener;
import me.linnemann.ptmobile.test.pivotaltracker.DBAdapterMock;
import me.linnemann.ptmobile.test.pivotaltracker.TestData;
import android.test.AndroidTestCase;

public class XMLIterationTests extends AndroidTestCase {

	private XMLStackHandler xah;
	private DBAdapterMock db;
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        db = new DBAdapterMock();
        
        XMLIterationListener iterationListener = new XMLIterationListener(db, TestData.ANY_PROJECT_ID, TestData.ANY_ITERATIONGROUP);
        XMLStoriesListener storyListener = new XMLStoriesListener(db, iterationListener);
        XMLNotesListener noteListener = new XMLNotesListener(db, storyListener);
        
        XMLStack stack = new XMLStack();
		stack.addListener("iterations.iteration", iterationListener);
		stack.addListener("iterations.iteration.stories.story", storyListener);
		stack.addListener("iterations.iteration.stories.story.notes.note", noteListener);
		
        xah = new XMLStackHandler(stack);
    }
	
	public void test_parseIteration_CorrectStoryCountInResult() throws Exception {
		List<Story> receivedStories = getReceivedStories();
		assertEquals(3, receivedStories.size());
	}
	
	
	private List<Story> getReceivedStories() throws Exception {
		xah.parse(streamFromXMLFile());
		List<Story> receivedStories = db.getStories();
		return receivedStories;
	}
	
	private List<Iteration> getReceivedIterations() throws Exception {
		xah.parse(streamFromXMLFile());
		List<Iteration> receivedIterations = db.getIterations();
		return receivedIterations;
	}
	
	private InputStream streamFromXMLFile() {
		return StoryFromIceboxXMLTests.class.getResourceAsStream("iterationResponse.xml");
	}
	
	public void test_firstStory_hasCorrectData() throws Exception {
		List<Story> receivedStories = getReceivedStories();
		Story story = receivedStories.get(0);
		
		assertEquals(new Integer(1), story.getPosition().getValue());
		assertEquals(TestData.ANY_PROJECT_ID, story.getProjectId().getValue());
		assertEquals(TestData.ANY_ITERATIONGROUP, story.getIterationGroup().getValueAsString());
		assertEquals(new Integer(36), story.getIterationNumber().getValue());
		assertEquals(new Integer(2256873), story.getId().getValue());
		assertEquals(StoryType.FEATURE, story.getStoryType());
		assertEquals(Estimate.POINTS_2, story.getEstimate());
		assertEquals(State.UNSTARTED, story.getCurrentState());
		assertEquals("description with \"quotes\" and 'single'", story.getDescription().getValue());
		assertEquals("Story with \"quotes\" and 'single'", story.getName().getValue());
		assertEquals("Niels Linnemann", story.getRequestedBy().getValue());
		assertEquals("2010/01/21 18:36:23 UTC", story.getCreatedAt().getValueAsString());
	}
	
	public void test_firstIteration_hasCorrectData() throws Exception {
		List<Iteration> receivedIterations = getReceivedIterations();
		Iteration iteration = receivedIterations.get(0);
		
		assertEquals(new Integer(35), iteration.getId().getValue());
		assertEquals(new Integer(35), iteration.getNumber().getValue());
		assertEquals(TestData.ANY_PROJECT_ID, iteration.getProjectId().getValue());
		assertEquals(TestData.ANY_ITERATIONGROUP, iteration.getIterationGroup().getValue());
		assertEquals("2010/01/25 00:00:00 UTC", iteration.getStart().getValueAsString());
		assertEquals("2010/02/01 00:00:00 UTC", iteration.getFinish().getValueAsString());
	}
}
