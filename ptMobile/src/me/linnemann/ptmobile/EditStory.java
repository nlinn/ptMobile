package me.linnemann.ptmobile;

import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditStory extends Activity {

	private Story story;
	private PivotalTracker tracker;
	private EditText name, description;
	
	private ArrayAdapter<CharSequence> storyTypeAdapter, stateAdapter, pointsAdapter;
	private Spinner storyTypeSpinner, stateSpinner, pointsSpinner;
	
	private int estimateArrayRessource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.story_edit);
		
		Integer story_id = getIntent().getExtras().getInt("story_id");
		Integer project_id = getIntent().getExtras().getInt("project_id");
		tracker = new PivotalTracker(this);
		
		if ((story_id != null) && (story_id > 0)) {
			setTitle("Edit Story");
			story = tracker.getStory(story_id);
		} else {	
			setTitle("Add Story");
			story = tracker.getEmptyStoryForProject(project_id);
		}
		
		
		name = (EditText) findViewById(R.id.editNameSE); 
		name.setText(story.getName().getUIString());
		
		description = (EditText) findViewById(R.id.editDescriptionSE); 
		description.setText(story.getDescription().getUIString());
		    
		estimateArrayRessource = getEstimateArrayRessource(story);
		
		initStoryType(story);
		initState(story);
		initPoints(story, estimateArrayRessource);
		
		((Button) findViewById(R.id.btn3SE)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				story.changeName(name.getText().toString());
				story.changeDescription(description.getText().toString());
				story.changeEstimate(getEstimateFromSpinner());
				story.changeStoryType(getStoryTypeFromSpinner());
				story.changeCurrentState(getStateFromSpinner());
				
				tracker.commitChanges(story);
			}
		});
		
		storyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView av,View v,int i, long l) {
				
				Log.i("EditStory", storyTypeSpinner.getSelectedItem().toString());
				StoryType type = StoryType.valueOf(storyTypeSpinner.getSelectedItem().toString().toUpperCase());

				story.changeStoryType(type);
				initState(story);
				initPoints(story, estimateArrayRessource);
			}
			
			public void onNothingSelected(AdapterView av) {
				//?
			}
		});
	}
	
	private StoryType getStoryTypeFromSpinner() {
		StoryType type = StoryType.valueOf(storyTypeSpinner.getSelectedItem().toString().toUpperCase());
		return type;
	}
	
	private Estimate getEstimateFromSpinner() {
		String points = pointsSpinner.getSelectedItem().toString();
		return Estimate.valueOfBeautifiedString(points);
	}
	
	private State getStateFromSpinner() {
		String state = stateSpinner.getSelectedItem().toString();
		return State.valueOfBeautifiedString(state);
	}
	
	private void initStoryType(Story story) {
		storyTypeSpinner = (Spinner) findViewById(R.id.spnTypeSE);
		storyTypeAdapter = createAdapterForSpinner(storyTypeSpinner, R.array.storytypes);
		
		int position = storyTypeAdapter.getPosition(story.getStoryType().getUIString());
		storyTypeSpinner.setSelection(position);
	}
	
	private void initState(Story story) {
		stateSpinner = (Spinner) findViewById(R.id.spnStateSE);
		
		int statesRessource = getStateArrayRessource(story.getStoryType());
		
		stateAdapter = createAdapterForSpinner(stateSpinner, statesRessource);
		
		int position = stateAdapter.getPosition(story.getCurrentState().getUIString());
		stateSpinner.setSelection(position);
	}
	
	private void initPoints(Story story, int estimateArrayRessource) {
		pointsSpinner = (Spinner) findViewById(R.id.spnPointsSE);
		pointsAdapter = createAdapterForSpinner(pointsSpinner , estimateArrayRessource);
		
		Estimate estimate = story.getEstimate();
		int position = pointsAdapter.getPosition(estimate.getUIString());
		pointsSpinner.setSelection(position);
		enableEstimateIfFeature(story.getStoryType());
	}
	
	private ArrayAdapter<CharSequence> createAdapterForSpinner(Spinner spinner, int arrayFromResource) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, arrayFromResource, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		return adapter;
	}
	
	private int getStateArrayRessource(StoryType type) {
		if (StoryType.CHORE.equals(type))
			return R.array.chore_states;
		if (StoryType.RELEASE.equals(type))
			return R.array.release_states;
		if (StoryType.BUG.equals(type))
			return R.array.bug_states;
		
		return R.array.feature_states;
	}

	private int getEstimateArrayRessource(Story story) {
		Integer project_id = story.getProjectId().getValue();
		ProjectsCursor pc = tracker.getProject(project_id);
		String pointscale = pc.getPointScale();
		pc.close();
		
		if ("0,1,2,3".equals(pointscale)) return R.array.points_linear;
		if ("0,1,2,4,8".equals(pointscale)) return R.array.points_powerof2;
		return R.array.points_fibonacci;
	}
	
	private void enableEstimateIfFeature(StoryType type) {
		boolean enabled = type.equals(StoryType.FEATURE);
		
		if (!enabled) {
			pointsAdapter = createAdapterForSpinner(pointsSpinner , R.array.points_empty);
			pointsSpinner.setSelection(0);
		}
			
		pointsSpinner.setEnabled(enabled);
	}
}
