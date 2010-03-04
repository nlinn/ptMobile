package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.qos.QoS;
import android.os.Bundle;
import android.util.Log;

public class AddStory extends AddEditStoryBase {

	private Story story;
	private Integer project_id;
	
	public AddStory() {
		super(R.layout.story_add);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Add Story");
		project_id = getIntent().getExtras().getInt("project_id");
		story = tracker.getEmptyStoryForProject(project_id);
		
		initNameAndDescription(story);
		initStoryType(story);
		initEstimate(story);
	}
	

	public Story getStory() {
		return story;
	}
	
	public void onOkButtonClick() {
		story.changeName(name.getText().toString());
		story.changeDescription(description.getText().toString());
		story.changeEstimate(getEstimateFromSpinner());
		story.changeStoryType(getStoryTypeFromSpinner());
		
		QoS qos = new QoS(this);
		qos.setOkMessage("story added to icebox");
		qos.setErrorMessage("error adding story");
			
		try {
			tracker.commitChanges(story);
			qos.sendSuccessMessageToHandler();
			finish();
		} catch (RuntimeException e) {
			qos.sendErrorMessageToHandler(e);
		}	
	}
	
	public void onStoryTypeSelected(String selectedItem) {
		Log.i("EditStory", selectedItem);
		StoryType type = StoryType.valueOf(selectedItem.toUpperCase());

		story.changeStoryType(type);
		initEstimate(story);
	}
	
}
