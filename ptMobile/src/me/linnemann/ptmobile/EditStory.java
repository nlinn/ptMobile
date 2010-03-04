package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.ui.SimpleErrorDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

		try {
			tracker.commitChanges(story);
			Toast toast = Toast.makeText(getApplicationContext(), "update complete", Toast.LENGTH_SHORT);
			toast.show();
			finish();
		} catch (RuntimeException e) {
			SimpleErrorDialog dialog = new SimpleErrorDialog("error updating story: "+e.getMessage(), this);
			dialog.show();
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