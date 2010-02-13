package me.linnemann.ptmobile.pivotaltracker.xml;

import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.Story;

public class CreateCommentCommand extends RESTXMLCommand {
	
	protected static String URL="www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories/STORY_ID/notes";

	private static final String TAG = "CreateCommentCommand";
	private Story story;
	private String comment;
	private String protocol;
	
	public CreateCommentCommand(Story story, String comment, String protocol) {
		this.story = story;
		this.comment = comment;
		this.protocol =protocol;
	}
	
	public URL getURL() {
		try {
			return new URL(compiledURLString());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Unexpected: "+e.getMessage(),e);
		}
	}
	
	private String compiledURLString() {
		String url = protocol+URL.replaceAll("PROJECT_ID", story.getProjectId().getValueAsString())
		.replaceAll("STORY_ID", story.getId().getValueAsString());
		return url;
	}
	
	public String getXMLString() {
		
		StringBuilder xml = new StringBuilder("<note><text>");
		xml.append(comment);
		xml.append("</text></note>");
		
		Log.d(TAG,xml.toString());
		
		return xml.toString();
	}

	@Override
	public String getPUTorPOST() {
		return "POST";
	}
}
