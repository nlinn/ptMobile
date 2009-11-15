package me.linnemann.ptmobile.test.pivotaltracker.xml;

import java.net.URL;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.xml.UpdateStoryCommand;
import android.test.AndroidTestCase;
import android.util.Log;

public class UpdateStoryCommandTests extends AndroidTestCase {

	private final static String TAG="UpdateStoryCommandTest";
	
	public void testGenerateCorrectURL() {
		Story story = new StoryImpl();
		story.setData(StoryData.PROJECT_ID,"1111");
		story.setData(StoryData.ID,"222222");
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		
		URL url = usc.getURL();
		Log.d(TAG, url.toString());
		assertEquals("http://www.pivotaltracker.com/services/v2/projects/1111/stories/222222", url.toString());
	}

	public void testUpdateLabels() {
		String expectedXML = "<story><labels>label1,label2</labels></story>";
		
		Story story = new StoryImpl();
		story.setData(StoryData.PROJECT_ID,"1111");
		story.setData(StoryData.ID,"222222");
		story.setData(StoryData.LABELS,"label123,label456");
		story.setData(StoryData.LABELS,"label1,label2"); // 2nd set is expected to override labels
		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		
		assertEquals(expectedXML, usc.getXMLString());
	}
	
	public void testUpdateEstimate() {
		String expectedXML = "<story><estimate type=\"integer\">8</estimate></story>";
		
		Story story = new StoryImpl();
		story.setData(StoryData.PROJECT_ID,"1111");
		story.setData(StoryData.ID,"222222");
		story.setEstimate(new Integer(8));
		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		
		assertEquals(expectedXML, usc.getXMLString());
	}
	
	
	public void testUpdateState() {
		String expectedXML = "<story><current_state>started</current_state></story>";

		Story story = new StoryImpl();
		story.setData(StoryData.PROJECT_ID,"1111");
		story.setData(StoryData.ID,"222222");
		
		story.setData(StoryData.STORY_TYPE, "bug");
		story.setData(StoryData.CURRENT_STATE,"started");
		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		
		assertEquals(expectedXML, usc.getXMLString());
	}
}
