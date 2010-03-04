package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.Story;

public class CreateStoryCommand extends StoryCommand {

	private static final String URL="www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories";
	
	public CreateStoryCommand(Story story, String protocol) {
		super(story,URL,protocol);

		if (story.getProjectId() == null) // TODO better make sure story.getprojectid never returns null
			throw new RuntimeException("trying to create story without project_id!");
	}
	
	@Override
	public String getPUTorPOST() {
		return "POST";
	}
}
