package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.qos.QoS;
import android.os.Bundle;
import android.util.Log;

public class EditStory extends AddEditStoryBase {

	private Story story;

	public EditStory() {
		super(R.layout.story_edit);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Integer story_id = getIntent().getExtras().getInt("story_id");

		setTitle("Edit Story");
		story = tracker.getStory(story_id);

		initNameAndDescription(story);
		initStoryType(story);
		initState(story);
		initEstimate(story);
		initLabels(story);
	}

	public Story getStory() {
		return story;
	}

	public void onOkButtonClick() {
		story.changeName(name.getText().toString());
		story.changeDescription(description.getText().toString());
		story.changeEstimate(getEstimateFromSpinner());
		story.changeStoryType(getStoryTypeFromSpinner());
		story.changeCurrentState(getStateFromSpinner());
		story.changeLabels(getLabelsFromTextView());

		QoS qos = new QoS(this);
		qos.setOkMessage("update complete");
		qos.setErrorMessage("error updating story");
		
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
		initState(story);
		initEstimate(story);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (tracker != null) tracker.pause();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (tracker != null) tracker.pause();
	}
	
	
}