package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.Story;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectLabels extends ListActivity {

	public static final String KEY_LABELS = "labels";
	
	private static final String TAG = "SelectLabels";
	
	private PivotalTracker tracker;
	private Story story;
	private ListView listView;
	private String[] labels;
	
	final OnItemClickListener listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			updateActivityResult();
		}
		
	};
	
	private void updateActivityResult() {
		String chosenLabels = getChosenLabelsAsCSV();
		Intent i = new Intent();
		Bundle result = new Bundle();
		result.putString(KEY_LABELS, chosenLabels);
		i.putExtras(result);
		this.setResult(RESULT_OK, i);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Edit Labels");

		Integer story_id = getIntent().getExtras().getInt("story_id");

		tracker = new PivotalTracker(this);
		story = tracker.getStory(story_id);
		Project project = story.getProject();

		labels = project.getLabels().getValue().split(",");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, labels);

		setListAdapter(adapter);
		
		listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(listener);
		
		checkExistingLabels();
		updateActivityResult();	// in case user does not change anything
	}
	
	private String getChosenLabelsAsCSV() {
		String csv="";
		SparseBooleanArray chosen = listView.getCheckedItemPositions();
		for(int i=0; i<chosen.size(); i++) {
			if (chosen.valueAt(i)) {
				csv = addToCSV(csv,chosen.keyAt(i));
			}
		}
		Log.i(TAG,csv);
		return csv;
	}
	
	private String addToCSV(String csv, int i) {
		if (csv.length() > 0) {
			csv += ",";
		}
		csv += labels[i];
		return csv;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (tracker != null)
			tracker.pause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (tracker != null)
			tracker.pause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (tracker != null)
			tracker.pause();
	}
	
	private void checkExistingLabels() {
		String[] labelsInStory = story.getLabels().getValue().split(",");
		
		for (int i=0; i< labelsInStory.length;i++) {
			for (int ii=0; ii <labels.length; ii++) {
				boolean check = labelsInStory[i].equalsIgnoreCase(labels[ii]);
				if (check) {
					listView.setItemChecked(ii, check);
				}
			}
		}
	}
}
