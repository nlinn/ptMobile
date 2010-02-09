package me.linnemann.ptmobile.pivotaltracker.fields;

import me.linnemann.ptmobile.pivotaltracker.value.TrackerValue;

public enum StoryData implements TrackerData {
	
	ID, NAME, ESTIMATE, STORY_TYPE, LABELS, CURRENT_STATE, DESCRIPTION, REQUESTED_BY, OWNED_BY,
	CREATED_AT, ACCEPTED_AT, DEADLINE, ITERATION_NUMBER, PROJECT_ID, ITERATION_GROUP, POSITION;
	
	public String getDBFieldName() {
		return toString().toLowerCase();
	}
	
	public String getXMLWrappedValue(TrackerValue value) {
		String xml = "";
		switch(this) {
			case LABELS:
				xml = "<labels>" + value.getValueAsString() + "</labels>";
				break;
			case CURRENT_STATE:
				xml = "<current_state>" + value.getValueAsString() + "</current_state>";
				break;
			case ESTIMATE:
				xml = "<estimate type=\"integer\">" + value.getValueAsString() + "</estimate>";
				break;
			case NAME:
				xml = "<name>" + value.getValueAsString() + "</name>";
				break;
			case DESCRIPTION:
				xml = "<description>" + value.getValueAsString() + "</description>";
				break;
			case STORY_TYPE:
				xml = "<story_type>" + value.getValueAsString() + "</story_type>";
				break;
		}
		return xml;
	}

	public TrackerValue getValueFromString(String from) {
		// TODO Auto-generated method stub
		return null;
	}
}