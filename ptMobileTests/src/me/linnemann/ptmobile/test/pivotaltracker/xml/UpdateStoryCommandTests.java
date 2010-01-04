package me.linnemann.ptmobile.test.pivotaltracker.xml;

import java.net.URL;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.StoryType;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;
import me.linnemann.ptmobile.pivotaltracker.xml.UpdateStoryCommand;
import android.test.AndroidTestCase;
import android.util.Log;

public class UpdateStoryCommandTests extends AndroidTestCase {

	private final static String TAG="UpdateStoryCommandTest";
	private Story story;
	
	public void setUp() {
		story = new StoryImpl();
	}
	
	public void testGenerateCorrectURL() {
		story.changeId(222222);
		story.changeProjectId(1111);
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		
		URL url = usc.getURL();
		Log.d(TAG, url.toString());
		assertEquals("http://www.pivotaltracker.com/services/v2/projects/1111/stories/222222", url.toString());
	}

	public void test_changedLabels_resultsInXML() {
		String expectedXML = "<story><labels>label1,label2</labels></story>";

		story.changeLabels("label1,label2");
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		assertEquals(expectedXML, usc.getXMLString());
	}
	
	public void test_changedEstimate_resultsInXML() {
		String expectedXML = "<story><estimate type=\"integer\">8</estimate></story>";
		
		story.changeEstimate(new Integer(8));
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		assertEquals(expectedXML, usc.getXMLString());
	}
	
	
	public void test_changedState_resultsInXML() {
		String expectedXML = "<story><current_state>started</current_state></story>";

		story.changeCurrentState(State.STARTED);		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		assertEquals(expectedXML, usc.getXMLString());
	}
	
	public void test_changedType_resultsInXML() {
		String expectedXML = "<story><story_type>chore</story_type></story>";

		story.changeStoryType(StoryType.CHORE);		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		assertEquals(expectedXML, usc.getXMLString());
	}
	
	public void test_changedName_resultsInXML() {
		String expectedXML = "<story><name>My Name</name></story>";

		story.changeName("My Name");		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		assertEquals(expectedXML, usc.getXMLString());
	}
	
	public void test_changedDescription_resultsInXML() {
		String expectedXML = "<story><description>My simple description.</description></story>";

		story.changeDescription("My simple description.");		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		assertEquals(expectedXML, usc.getXMLString());
	}
}
