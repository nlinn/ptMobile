package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.util.Log;

/**
 * creates a story object from pivotaltracker API
 * found story data is marked modified
 * 
 * @author nlinn
 *
 */
public class StoryFromAPIBuilder implements StoryBuilder {

	private static final String TAG = "StoryFromAPIBuilder";
	private Story story;

	public StoryFromAPIBuilder() {
		story = new StoryImpl();		
	}

	public void clear() {
		story = new StoryImpl();
	}

	public void construct() {
		// empty :(
	}

	public void add(String elementName, String elementData) {
		
		if (elementName == null) return; // handler spams me with null values -> ignore
		
		try {
			StoryData element = StoryData.valueOf(elementName.toUpperCase());
			add(element,elementData);
		} catch (IllegalArgumentException e) {
			Log.w(TAG, "ignoring unknown element: "+elementName);
		}
	}

	public void add(StoryData element, String elementData) {

		Log.d(TAG,element +": "+elementData);

		if (StoryData.PROJECT_ID.equals(element))
			story.changeProjectId(new Integer(elementData));
		
		if (StoryData.POSITION.equals(element))
			story.changePosition(new Integer(elementData));

		if (StoryData.ITERATION_NUMBER.equals(element)) {
			try {
				story.changeIterationNumber(new Integer(elementData));
			} catch (NumberFormatException e) {}
		}

		if (StoryData.ITERATION_GROUP.equals(element))
			story.changeIterationGroup(elementData);

		if (StoryData.ID.equals(element))
			story.changeId(new Integer(elementData));

		if (StoryData.STORY_TYPE.equals(element))
			story.changeStoryType(StoryType.valueOf(elementData.toUpperCase()));

		if (StoryData.ESTIMATE.equals(element)) {
			Log.i("API","found estimate: "+elementData);
			Integer estimateNumeric = new Integer(elementData);
			story.changeEstimate(Estimate.valueOfNumeric(estimateNumeric));
			Log.i("API","estimate after change: "+story.getEstimate().getUIString());
		}

		if (StoryData.CURRENT_STATE.equals(element))
			story.changeCurrentState(State.valueOf(elementData.toUpperCase()));

		if (StoryData.DESCRIPTION.equals(element))
			story.changeDescription(elementData);

		if (StoryData.NAME.equals(element))
			story.changeName(elementData);

		if (StoryData.REQUESTED_BY.equals(element))
			story.changeRequestedBy(elementData);

		if (StoryData.OWNED_BY.equals(element))
			story.changeOwnedBy(elementData);

		if (StoryData.LABELS.equals(element))
			story.changeLabels(elementData);

		if (StoryData.DEADLINE.equals(element))
			story.changeDeadline(convertToLocalFormat(elementData));	

		if (StoryData.CREATED_AT.equals(element))
			story.changeCreatedAt(convertToLocalFormat(elementData));

		if (StoryData.ACCEPTED_AT.equals(element))
			story.changeAcceptedAt(convertToLocalFormat(elementData));

	}

	private String convertToLocalFormat(String dateFromTracker) {
		return dateFromTracker.replaceAll("/", "-").replaceAll(" UTC","");
	}

	public Story getStory() {
		return story;
	}

}
