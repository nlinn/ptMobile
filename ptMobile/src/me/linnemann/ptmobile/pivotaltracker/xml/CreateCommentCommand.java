package me.linnemann.ptmobile.pivotaltracker.xml;

import java.net.MalformedURLException;
import java.net.URL;

import me.linnemann.ptmobile.pivotaltracker.Story;

public class CreateCommentCommand extends RESTXMLCommand {
	
	protected static String URL="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories/STORY_ID/notes";

	private Story story;
	private String comment;
	
	public CreateCommentCommand(Story story, String comment) {
		this.story = story;
		this.comment = comment;
	}
	
	public URL getURL() {
		try {
			return new URL(compiledURLString());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Unexpected: "+e.getMessage(),e);
		}
	}
	
	private String compiledURLString() {
		String url = URL.replaceAll("PROJECT_ID", story.getProjectId().toString())
		.replaceAll("STORY_ID", story.getId().toString());
		return url;
	}
	
	public String getXMLString() {
		
		StringBuilder xml = new StringBuilder("<note><text>");
		xml.append(comment);
		xml.append("</text></note>");
		
		return xml.toString();
	}
}
