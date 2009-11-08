package me.linnemann.ptmobile.pivotaltracker.xml;

import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;

public class UpdateStoryCommand {

	private static final String URL="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories/STORY_ID";

	private Story story;
	
	public UpdateStoryCommand(Story story) {
		this.story = story;
	}
	
	public URL getURL() {
		String url = URL.replaceAll("PROJECT_ID", story.getData(StoryData.PROJECT_ID))
					.replaceAll("STORY_ID", story.getData(StoryData.ID));
		
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
		
		if (story.getModifiedFields().contains(StoryData.LABELS)) {
			xml.append("<labels>");
			xml.append(story.getData(StoryData.LABELS));
			xml.append("</labels>");
		}

		if (story.getModifiedFields().contains(StoryData.CURRENT_STATE)) {
			xml.append("<current_state>");
			xml.append(story.getData(StoryData.CURRENT_STATE));
			xml.append("</current_state>");
		}
		
		xml.append("</story>");
		
		return xml.toString();
	}
	
	public byte[] getXMLBytes() {
		return getXMLString().getBytes();
	}
}
