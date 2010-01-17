package me.linnemann.ptmobile.pivotaltracker.xml;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;

public abstract class StoryCommand extends RESTXMLCommand {
	
	private Story story;
	private String url;
	
	public StoryCommand(Story story, String url) {
		this.story = story;
		this.url = url;
	}
	
	public Story getStory() {
		return story;
	}
	
	public URL getURL() {
		try {
			return new URL(compiledURLString());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Unexpected: "+e.getMessage(),e);
		}
	}
	
	private String compiledURLString() {
		String compiledUrl = url.replaceAll("PROJECT_ID", story.getProjectId().getValueAsString());
		
		if (!story.getId().isEmpty()) {
			compiledUrl= compiledUrl.replaceAll("STORY_ID", story.getId().getValueAsString());	
		}
		
		return compiledUrl;
	}
	
	public String getXMLString() {
		StringBuilder xml = new StringBuilder("<story>");
		
		Set<StoryData> modified = story.getModifiedFields();
		
		if (modified.contains(StoryData.LABELS)) {
			xml.append("<labels>");
			xml.append(story.getLabels().getValueAsString());
			xml.append("</labels>");
		}

		if (modified.contains(StoryData.CURRENT_STATE)) {
			xml.append("<current_state>");
			xml.append(story.getCurrentState().getValueAsString());
			xml.append("</current_state>");
		}
		
		if ((StoryType.FEATURE.equals(story.getStoryType())) && (modified.contains(StoryData.ESTIMATE))) {
			xml.append("<estimate type=\"integer\">");
			xml.append(story.getEstimate().getValueAsString());
			xml.append("</estimate>");
		}
		
		if (modified.contains(StoryData.NAME)) {
			xml.append("<name>");
			xml.append(story.getName().getValueAsString());
			xml.append("</name>");
		}
		
		if (modified.contains(StoryData.DESCRIPTION)) {
			xml.append("<description>");
			xml.append(story.getDescription().getValueAsString());
			xml.append("</description>");
		}
		
		if (modified.contains(StoryData.STORY_TYPE)) {
			xml.append("<story_type>");
			xml.append(story.getStoryType().getValueAsString());
			xml.append("</story_type>");
		}
		
		xml.append("</story>");
		
		Log.i("StoryCommand",xml.toString());
		
		return xml.toString();
	}
}
