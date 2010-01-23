package me.linnemann.ptmobile.test.functional;

import java.io.InputStream;
import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStoriesHandler;
import me.linnemann.ptmobile.test.pivotaltracker.DBAdapterMock;
import me.linnemann.ptmobile.test.pivotaltracker.TestData;
import android.test.AndroidTestCase;

public class IterationXMLTests extends AndroidTestCase {
	
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
		assertEquals(3, receivedStories.size());
	}
	
	private List<Story> getReceivedStoriesForSimpleStoryXML() throws Exception {
		handler.go(streamFromSimpleStoryXML());
		List<Story> receivedStories = db.getStories();
		return receivedStories;
	}
	
	private InputStream streamFromSimpleStoryXML() {
		return SimpleStoryXMLTests.class.getResourceAsStream("iterationResponse.xml");
	}
}
