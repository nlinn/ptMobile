package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;

public class StoryFromCursorBuilder implements StoryBuilder {

	private StoriesCursor cursor;
	private StoryImpl story;
	
	public StoryFromCursorBuilder(StoriesCursor cursor) {
		this.cursor = cursor;
		this.story = new StoryImpl();
	}
	
	public StoryImpl getStory() {
		return story;
	}
	
	public void construct() {
		initStoryType();
		initInitialState();

		story.changeEstimate(cursor.getEstimate());
		story.changeId(new Integer(cursor.getId()));
		story.changeName(cursor.getName());
		story.changeProjectId(new Integer(cursor.getProjectId()));
		story.changeDescription(cursor.getDescription());
		story.changeLabels(cursor.getLabels());
		// TODO: add more fields (requested, owned, created, accepted, iterationnumber)
				
		story.resetModifiedFieldsTracking(); // important!
	}
	
	private void initStoryType() {
		StoryType type = StoryType.valueOf(cursor.getStoryType());
		story.changeStoryType(type);
	}

	private void initInitialState() {
		State state = new State(cursor.getCurrentState());
		story.changeInitialState(state);
	}
}