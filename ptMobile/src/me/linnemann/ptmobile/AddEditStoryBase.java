package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public abstract class AddEditStoryBase extends Activity {

	protected EditText name, description;
	protected TextView labels;

	private ArrayAdapter<CharSequence> storyTypeAdapter, stateAdapter, estimateAdapter;
	protected Spinner storyTypeSpinner, stateSpinner, estimateSpinner;

	protected PivotalTracker tracker;
	int layout;
	
	View.OnClickListener labelsClick = new View.OnClickListener() {
		public void onClick(View v) {
			showEditLabels();
		}
	};
	
	private void showEditLabels() {
		Intent i = new Intent(this, SelectLabels.class);
		i.putExtra("story_id", getStory().getId().getValue());
		startActivity(i);
	}
	
	public AddEditStoryBase(int layout) {
		this.layout = layout;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(layout);
		
		tracker = new PivotalTracker(this);
		setOKButtonListener();
	}

	protected void initNameAndDescription(Story story) {
		name = (EditText) findViewById(R.id.editNameSE);
		description = (EditText) findViewById(R.id.editDescriptionSE); 
		name.setText(story.getName().getUIString());
		description.setText(story.getDescription().getUIString());
	}

	protected void initLabels(Story story) {
		labels = (TextView) findViewById(R.id.textLabels);
		labels.setText(story.getLabels().getUIString());
		((Button) findViewById(R.id.btnEditLabels)).setOnClickListener(labelsClick);
	}
	
	protected StoryType getStoryTypeFromSpinner() {
		StoryType type = StoryType.valueOf(storyTypeSpinner.getSelectedItem().toString().toUpperCase());
		return type;
	}

	protected Estimate getEstimateFromSpinner() {
		String points = estimateSpinner.getSelectedItem().toString();
		return Estimate.valueOfBeautifiedString(points);
	}

	protected State getStateFromSpinner() {
		String state = stateSpinner.getSelectedItem().toString();
		return State.valueOfBeautifiedString(state);
	}

	protected void initStoryType(Story story) {
		storyTypeSpinner = (Spinner) findViewById(R.id.spnTypeSE);
		storyTypeAdapter = createAdapterForSpinner(storyTypeSpinner, R.array.storytypes);

		int position = storyTypeAdapter.getPosition(story.getStoryType().getUIString());
		storyTypeSpinner.setSelection(position);
		
		setStoryTypeListener();
	}

	protected void initState(Story story) {
		stateSpinner = (Spinner) findViewById(R.id.spnStateSE);

		int statesRessource = getStateArrayRessource(story.getStoryType());

		stateAdapter = createAdapterForSpinner(stateSpinner, statesRessource);

		int position = stateAdapter.getPosition(story.getCurrentState().getUIString());
		stateSpinner.setSelection(position);
	}

	protected void initEstimate(Story story) {
		int estimateArrayRessource = getEstimateArrayRessource(story);

		estimateSpinner = (Spinner) findViewById(R.id.spnPointsSE);
		estimateAdapter = createAdapterForSpinner(estimateSpinner , estimateArrayRessource);

		Estimate estimate = story.getEstimate();
		int position = estimateAdapter.getPosition(estimate.getUIString());
		estimateSpinner.setSelection(position);
//		enableEstimateIfFeature(story.getStoryType());
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
		Project project = tracker.getProject(project_id);
		String pointscale = project.getPointScale().getValueAsString();

		Log.i("ADDEDIT","estimate: "+story.getEstimate().getUIString());
		
		if (story.getEstimate().isEmpty()) return R.array.points_empty;
		if ("0,1,2,3".equals(pointscale)) return R.array.points_linear;
		if ("0,1,2,4,8".equals(pointscale)) return R.array.points_powerof2;
		return R.array.points_fibonacci;
	}

	private void setStoryTypeListener() {
		storyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> av,View v,int i, long l) {
				onStoryTypeSelected(storyTypeSpinner.getSelectedItem().toString());
			}

			public void onNothingSelected(AdapterView<?> av) {
				//?
			}
		});
	}

	private void setOKButtonListener() {
		((Button) findViewById(R.id.btn3SE)).setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				onOkButtonClick();
			}
		});
	}

	protected abstract void onStoryTypeSelected(String selectedItem);
	protected abstract void onOkButtonClick();
	protected abstract Story getStory();

}
