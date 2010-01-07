package me.linnemann.ptmobile.pivotaltracker;

import android.util.Log;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;

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

	public void add(String elementName, String elementData) {
		
		Log.d(TAG,elementName +": "+elementData);
		
		if (isElement(StoryData.PROJECT_ID,elementName))
			story.changeProjectId(new Integer(elementData));

		if (isElement(StoryData.ITERATION_NUMBER,elementName)) {
			try {
				story.changeIterationNumber(new Integer(elementData));
			} catch (NumberFormatException e) {}
		}
		
		if (isElement(StoryData.ITERATION_GROUP,elementName))
			story.changeIterationGroup(elementData);
		
		if (isElement(StoryData.ID,elementName))
				story.changeId(new Integer(elementData));
		
		if (isElement(StoryData.STORY_TYPE,elementName))
			story.changeStoryType(StoryType.valueOf(elementData.toUpperCase()));

		if (isElement(StoryData.ESTIMATE,elementName))
			story.changeEstimate(new Integer(elementData));
		
		if (isElement(StoryData.CURRENT_STATE,elementName))
			story.changeCurrentState(new State(elementData));

		if (isElement(StoryData.DESCRIPTION,elementName))
			story.changeDescription(elementData);

		if (isElement(StoryData.NAME,elementName))
			story.changeName(elementData);
			
		if (isElement(StoryData.REQUESTED_BY,elementName))
			story.changeRequestedBy(elementData);
		
		if (isElement(StoryData.OWNED_BY,elementName))
			story.changeOwnedBy(elementData);

		if (isElement(StoryData.LABELS,elementName))
			story.changeLabels(elementData);
		
		if (isElement(StoryData.DEADLINE,elementName))
			story.changeDeadline(elementData);	
		
		if (isElement(StoryData.CREATED_AT,elementName)) 
			story.changeCreatedAt(convertToLocalFormat(elementData));

		if (isElement(StoryData.ACCEPTED_AT,elementName)) 
			story.changeAcceptedAt(convertToLocalFormat(elementData));
		
	}
	
	public boolean isElement(StoryData data, String elementName) {
		return (data.toString().equalsIgnoreCase(elementName));
	}
	
	public String convertToLocalFormat(String dateFromTracker) {
		return dateFromTracker.replaceAll("/", "-").replaceAll(" UTC","");
	}
	
	public Story getStory() {
		return story;
	}

}
