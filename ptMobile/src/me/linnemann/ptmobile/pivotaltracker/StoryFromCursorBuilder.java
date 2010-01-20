package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.util.Log;

/**
 * creates story objects from a cursor (db)
 * story data is not marked "modified" after creating from db
 * 
 * @author nlinn
 *
 */
public class StoryFromCursorBuilder implements StoryBuilder {

	private static final String TAG = "StoryFromCursorBuilder";
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
		
		story.changeCurrentState(cursor.getCurrentState());
		

		if (StoryType.RELEASE.equals(story.getStoryType())) {
			story.changeEstimate(Estimate.NO_ESTIMATE);
		} else {
			story.changeEstimate(cursor.getEstimate());
		}

		
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
		story.changePosition(cursor.getStoryPosition());

		story.resetModifiedFieldsTracking(); // important!
	}
	
	private void initStoryType() {
		String typeFromData = cursor.getStoryType().toUpperCase();
		StoryType type = StoryType.valueOf(typeFromData);

		Log.i(TAG, "from db:"+typeFromData);
		Log.i(TAG, "type value of:"+type);
		
		story.changeStoryType(type);
	}
}