package me.linnemann.ptmobile;

import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public abstract class AddEditStoryBase extends Activity {

	protected EditText name, description;

	private ArrayAdapter<CharSequence> storyTypeAdapter, stateAdapter, pointsAdapter;
	protected Spinner storyTypeSpinner, stateSpinner, pointsSpinner;

	protected PivotalTracker tracker;
	int layout;
	
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

	protected StoryType getStoryTypeFromSpinner() {
		StoryType type = StoryType.valueOf(storyTypeSpinner.getSelectedItem().toString().toUpperCase());
		return type;
	}

	protected Estimate getEstimateFromSpinner() {
		String points = pointsSpinner.getSelectedItem().toString();
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

	protected void initPoints(Story story) {
		int estimateArrayRessource = getEstimateArrayRessource(story);

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

}
