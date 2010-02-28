package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.Story;

public class UpdateStoryCommand extends StoryCommand {

	private static final String URL="www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories/STORY_ID";
	
	public UpdateStoryCommand(Story story, String protocol) {
		super(story, URL, protocol);
	}
	
	@Override
	public String getPUTorPOST() {
		return "PUT";
	}
}
