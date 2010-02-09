package me.linnemann.ptmobile.pivotaltracker.xml;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.fields.TrackerData;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;
import android.util.Log;

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
		Map<TrackerData,TrackerValue> modified = story.getModifiedData();
		
		for (TrackerData tranferable : modified.keySet()) {
			TrackerValue value = modified.get(tranferable);
			xml.append(tranferable.getXMLWrappedValue(value));
		}

		xml.append("</story>");
		Log.v("StoryCommand",xml.toString());
		
		return xml.toString();
	}
}
