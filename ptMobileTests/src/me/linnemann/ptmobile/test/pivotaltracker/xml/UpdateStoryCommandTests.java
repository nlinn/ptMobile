package me.linnemann.ptmobile.test.pivotaltracker.xml;

import java.net.URL;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryImpl;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryField;
import me.linnemann.ptmobile.pivotaltracker.xml.UpdateStoryCommand;
import android.test.AndroidTestCase;
import android.util.Log;

public class UpdateStoryCommandTests extends AndroidTestCase {

	private final static String TAG="UpdateStoryCommandTest";
	
	public void testGenerateCorrectURL() {
		Story story = new StoryImpl();
		story.setData(StoryField.PROJECT_ID,"1111");
		story.setData(StoryField.ID,"222222");
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		
		URL url = usc.getURL();
		Log.d(TAG, url.toString());
		assertEquals("http://www.pivotaltracker.com/services/v2/projects/1111/stories/222222", url.toString());
	}

	public void testUpdateLabels() {
		String expectedXML = "<story><labels>label1,label2</labels></story>";
		
		Story story = new StoryImpl();
		story.setData(StoryField.PROJECT_ID,"1111");
		story.setData(StoryField.ID,"222222");
		story.setData(StoryField.LABELS,"label123,label456");
		story.setData(StoryField.LABELS,"label1,label2"); // 2nd set is expected to override labels
		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		
		assertEquals(expectedXML, usc.getXMLString());
	}
	
	public void testUpdateState() {
		String expectedXML = "<story><current_state>started</current_state></story>";

		Story story = new StoryImpl();
		story.setData(StoryField.PROJECT_ID,"1111");
		story.setData(StoryField.ID,"222222");
		story.setData(StoryField.PROJECT_ID,"1111");
		story.setData(StoryField.ID,"222222");
		story.setData(StoryField.CURRENT_STATE,"started");
		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);
		
		assertEquals(expectedXML, usc.getXMLString());
	}
}
