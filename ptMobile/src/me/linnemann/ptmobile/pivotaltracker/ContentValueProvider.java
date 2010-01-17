package me.linnemann.ptmobile.pivotaltracker;

import java.util.Set;

import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;
import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;
import android.content.ContentValues;

/**
 * this class deals with converting data into ContentValues. ContentValues can easily be stored
 * in db
 * 
 * @author nlinn
 *
 */
public class ContentValueProvider {

	private static final String UPDATETIMESTAMP_KEY = "updatetimestamp";
	private ContentValues values;
	private Story story;
	private Set<StoryData> modified;
	
	public ContentValueProvider(Story story) {
		this.values = new ContentValues();
		this.story = story;
		modified = story.getModifiedFields();
	}
	
	public ContentValues getValues() {
		return values;
	}
	
	public void fill() {
		putStoryDataIfModified(StoryData.ESTIMATE, story.getEstimate());
		putStoryDataIfModified(StoryData.NAME, story.getName());
		putStoryDataIfModified(StoryData.DESCRIPTION, story.getDescription());
		putStoryDataIfModified(StoryData.PROJECT_ID, story.getProjectId());
		putStoryDataIfModified(StoryData.CURRENT_STATE, story.getCurrentState()); 
		putStoryDataIfModified(StoryData.DEADLINE, story.getDeadline());
		putStoryDataIfModified(StoryData.ACCEPTED_AT, story.getAcceptedAt());
		putStoryDataIfModified(StoryData.CREATED_AT, story.getCreatedAt());
		putStoryDataIfModified(StoryData.LABELS, story.getLabels());
		putStoryDataIfModified(StoryData.OWNED_BY, story.getOwnedBy());
		putStoryDataIfModified(StoryData.REQUESTED_BY, story.getRequestedBy());
		putStoryDataIfModified(StoryData.STORY_TYPE, story.getStoryType());
		putStoryDataIfModified(StoryData.ITERATION_GROUP, story.getIterationGroup());
		putStoryDataIfModified(StoryData.ITERATION_NUMBER, story.getIterationNumber());
		putStoryDataIfModified(StoryData.POSITION, story.getPosition());
		
		addUpdateTimestampIfNotEmpty();
		addIDIfNotEmpty();
	}
	
	private void putStoryDataIfModified(StoryData key, TrackerValue value) {
		if (modified.contains(key))
			putStoryData(key,value);
	}
	
	private void putStoryData(StoryData key, TrackerValue value) {
		values.put(key.getDBFieldName(), value.getValueAsString());
	}
	
	private void addUpdateTimestampIfNotEmpty() {
		if (values.size() > 0)
			values.put(UPDATETIMESTAMP_KEY, Long.toString(System.currentTimeMillis()));
	}
	
	private void addIDIfNotEmpty() {
		if (values.size() > 0)
			values.put(StoryData.ID.getDBFieldName(), story.getId().getValueAsString());
	}
}
