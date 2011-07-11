package me.linnemann.ptmobile;

import me.linnemann.ptmobile.adapter.StoriesCursorAdapter;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.qos.QoS;
import me.linnemann.ptmobile.qos.QoSMessageHandler;
import me.linnemann.ptmobile.ui.OutputStyler;
import me.linnemann.ptmobile.ui.RefreshableListActivityWithMainMenu;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Stories extends RefreshableListActivityWithMainMenu implements QoSMessageHandler {

	private static final int STORYDETAILS_ID = Menu.FIRST + 5;
	private static final int TRANS_1_ID = Menu.FIRST + 6;
	private static final int TRANS_2_ID = Menu.FIRST + 7;
	private static final int ESTIMATE_ID = Menu.FIRST + 8;
	private static final int EDITSTORY_ID = Menu.FIRST + 9;

	private static final String TAG = "Stories";

	private PivotalTracker tracker;
	private StoriesCursorImpl c;
	private Integer project_id;
	private Transition transition_1, transition_2;
	private Story selectedStory;
	private String iteration_group;

	public Stories() {
		super(RefreshableListActivityWithMainMenu.SHOW_ADD_MENU);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stories_list);
		registerForContextMenu(getListView());
		this.getListView().setCacheColorHint(0);
		this.getListView().setDivider(this.getResources().getDrawable(R.drawable.darkgray1x1));// .getDivider().

		this.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Log.i(TAG,"Project ID from Extras: "+extras.getInt("project_id"));

			project_id=extras.getInt("project_id");
			iteration_group=extras.getString("filter");
			setTitle("Project "+extras.getString("project_name") + " "+iteration_group);
		}
		Log.i(TAG,"onCreate finished");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		c.moveToPosition(position);

		Intent i = new Intent(this, StoryDetails.class);
		putStoryIdToExtra(i);

		startActivity(i);
	}

	private void putStoryIdToExtra(Intent i) {
		i.putExtra("story_id", c.getStory().getId().getValue());
	}

	private void updateList(Integer project_id) {
		Log.v(TAG,"update list for project: "+project_id);
		c = tracker.getStoriesCursor(project_id,iteration_group);
		startManagingCursor(c);
		StoriesCursorAdapter sa = new StoriesCursorAdapter(this, c);
		setListAdapter(sa);
	}

	// --- CONTEXT MENU ----------------------------------------	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("Story");

		menu.add(0, STORYDETAILS_ID, 0, R.string.menu_showstorydetails);

		menu.add(0, EDITSTORY_ID, 0, R.string.menu_editstory);


		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			Log.e(TAG, "bad menuInfo", e);
			return;
		}

		c.moveToPosition(info.position);

		selectedStory = c.getStory();

		if (selectedStory.getTransitions().size() > 0) {
			transition_1 = selectedStory.getTransitions().get(0);
			menu.add(0, TRANS_1_ID, 0, OutputStyler.getTransitionContextLabel(transition_1.getName()));
		}

		if (selectedStory.getTransitions().size() > 1) {
			transition_2 = selectedStory.getTransitions().get(1);
			menu.add(0, TRANS_2_ID, 0, OutputStyler.getTransitionContextLabel(transition_2.getName()));
		}

		if (selectedStory.needsEstimate()) {
			menu.add(0, ESTIMATE_ID, 0, "Estimate Story");
		}
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent i;
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		c.moveToPosition((int) info.position);
		switch(item.getItemId()) {
		case STORYDETAILS_ID:        	
			i = new Intent(this, StoryDetails.class);
			putStoryIdToExtra(i);
			startActivity(i);
			return true;
		case EDITSTORY_ID:        	
			i = new Intent(this, EditStory.class);
			putStoryIdToExtra(i);
			startActivity(i);
			return true;
		case TRANS_1_ID:        	
			selectedStory.applyTransition(transition_1);
			tracker.commitChanges(selectedStory);
			updateList(project_id);
			return true;
		case TRANS_2_ID:        	
			selectedStory.applyTransition(transition_2);
			tracker.commitChanges(selectedStory);
			updateList(project_id);
			return true;
		case ESTIMATE_ID: 
			i = new Intent(this, ChangeEstimate.class);
			putStoryIdToExtra(i);
			startActivity(i);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onResume() {
		Log.v(TAG,"onResume");
		super.onResume();
		tracker = PivotalTracker.getInstance(this);
		updateList(project_id);
		if (tracker.storiesNeedUpdate(project_id, iteration_group)) {
			refresh();
		}
	}

	public void refresh() {

		final QoS qos = new QoS(this,this);
		qos.setErrorMessage("error updating data");
		qos.setOkMessage("update complete");
	

		if (getParent() != null) {
			getParent().setProgressBarIndeterminateVisibility(true);
		}

		new Thread() { 
			public void run() { 
				if (getParent() != null) {
					getParent().setProgressBarIndeterminateVisibility(true);
				}

				try {
					tracker.updateStoriesForProject(project_id, iteration_group);
					qos.sendSuccessMessageToHandler();
				} catch (RuntimeException e) {
					qos.sendErrorMessageToHandler(e);
				}
			} 
		}.start(); 
	}

	@Override
	public void addStory() {
		Intent i = new Intent(this, AddStory.class);
		i.putExtra("project_id", project_id);
		startActivity(i);
	}

	public boolean onQoSERROR() {
		if (getParent() != null) {
			getParent().setProgressBarIndeterminateVisibility(false);
		}
		return QoS.HANDLE_EVENT;
	}

	public boolean onQoSOK() {
		if (getParent() != null) {
			getParent().setProgressBarIndeterminateVisibility(false);
		}
		updateList(project_id);
		return QoS.HANDLE_EVENT;
	}
}
