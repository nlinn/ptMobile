package me.linnemann.ptmobile;

import java.util.List;

import me.linnemann.ptmobile.adapter.MyAdapter;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.Story;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectLabels extends ListActivity {

	private Story story;
	private ListView listView;
	private String[] labels;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Edit Labels");

		Integer story_id = getIntent().getExtras().getInt("story_id");

		PivotalTracker tracker = new PivotalTracker(this);
		story = tracker.getStory(story_id);
		Project project = story.getProject();

		labels = project.getLabels().getValue().split(",");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, labels);

//		ArrayAdapter<String> adapter =new MyAdapter(this, R.layout.labels_row, labels);
		
		setListAdapter(adapter);
		
		listView = getListView();
		
		
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		checkExistingLabels();
		
		SparseBooleanArray chosen = listView.getCheckedItemPositions();
		for(int i=0; i<chosen.size(); i++) {
			Log.d("selection", "index: "+i+"; key: "+chosen.keyAt(i)+"; value: "+chosen.valueAt(i)+"; "+labels[chosen.keyAt(i)]);
			// if the item is selected by the user, we display it on the TextView.
			//if(chosen.valueAt(i)) {
			//	selection.append(items[chosen.keyAt(i)]+"");
			//}
		}
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
