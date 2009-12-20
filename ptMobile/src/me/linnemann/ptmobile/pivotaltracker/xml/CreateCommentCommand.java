package me.linnemann.ptmobile.pivotaltracker.xml;

import java.net.MalformedURLException;
import java.net.URL;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import android.util.Log;

public class CreateCommentCommand {
	private static final String URL="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories/STORY_ID/notes";

	private Story story;
	private String comment;
	
	public CreateCommentCommand(Story story, String comment) {
		this.story = story;
		this.comment = comment;
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
		
		StringBuilder xml = new StringBuilder("<note><text>");
		xml.append(comment);
		xml.append("</text></note>");
		
		return xml.toString();
	}
	
	public byte[] getXMLBytes() {
		return getXMLString().getBytes();
	}
}
