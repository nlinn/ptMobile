package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.ui.SimpleErrorDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
		initPoints(story);
	}
	
	public void onOkButtonClick() {
		story.changeName(name.getText().toString());
		story.changeDescription(description.getText().toString());
		story.changeEstimate(getEstimateFromSpinner());
		story.changeStoryType(getStoryTypeFromSpinner());
		
		try {
			tracker.commitChanges(story);
			Toast toast = Toast.makeText(getApplicationContext(), "story added to icebox", Toast.LENGTH_SHORT);
			toast.show();
			finish();
		} catch (RuntimeException e) {
			SimpleErrorDialog dialog = new SimpleErrorDialog("error adding story: "+e.getMessage(), this);
			dialog.show();
		}	
	}
	
	public void onStoryTypeSelected(String selectedItem) {
		Log.i("EditStory", selectedItem);
		StoryType type = StoryType.valueOf(selectedItem.toUpperCase());

		story.changeStoryType(type);
		initPoints(story);
	}
	
}
