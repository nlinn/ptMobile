package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;

/**
 * creates story objects from a cursor (db)
 * story data is not marked "modified" after creating from db
 * 
 * @author nlinn
 *
 */
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
		story.changeAcceptedAt(cursor.getAcceptedAt());
		story.changeIterationNumber(cursor.getIterationNumber());
		story.changeIterationGroup(cursor.getIterationGroup());
		story.changeRequestedBy(cursor.getRequestedBy());
		story.changeOwnedBy(cursor.getOwnedBy());
		story.changeDeadline(cursor.getDeadline());
		story.changeCreatedAt(cursor.getCreatedAt());

		story.resetModifiedFieldsTracking(); // important!
	}
	
	private void initStoryType() {
		StoryType type = StoryType.valueOf(cursor.getStoryType().toUpperCase());
		story.changeStoryType(type);
	}

	private void initInitialState() {
		State state = new State(cursor.getCurrentState());
		story.changeInitialState(state);
	}
}