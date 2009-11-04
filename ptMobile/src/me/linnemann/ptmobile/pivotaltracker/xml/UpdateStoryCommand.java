package me.linnemann.ptmobile.pivotaltracker.xml;

import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryField;

public class UpdateStoryCommand {

	private static final String URL="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories/STORY_ID";

	private Story story;
	
	public UpdateStoryCommand(Story story) {
		this.story = story;
	}
	
	public URL getURL() {
		String url = URL.replaceAll("PROJECT_ID", story.getData(StoryField.PROJECT_ID))
					.replaceAll("STORY_ID", story.getData(StoryField.ID));
		
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			Log.e("UpdateStoryCommand","Bad URL: "+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public String getXMLString() {
		
		StringBuilder xml = new StringBuilder("<story>");
		
		if (story.getModifiedFields().contains(StoryField.LABELS)) {
			xml.append("<labels>");
			xml.append(story.getData(StoryField.LABELS));
			xml.append("</labels>");
		}

		if (story.getModifiedFields().contains(StoryField.CURRENT_STATE)) {
			xml.append("<current_state>");
			xml.append(story.getData(StoryField.CURRENT_STATE));
			xml.append("</current_state>");
		}
		
		xml.append("</story>");
		
		return xml.toString();
	}
	
	public byte[] getXMLBytes() {
		return getXMLString().getBytes();
	}
}
