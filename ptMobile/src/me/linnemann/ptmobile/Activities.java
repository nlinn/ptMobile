package me.linnemann.ptmobile;

import me.linnemann.ptmobile.adapter.ActivitiesCursorAdapter;
import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.pivotaltracker.Activity;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.qos.QoS;
import me.linnemann.ptmobile.qos.QoSMessageHandler;
import me.linnemann.ptmobile.ui.RefreshableListActivityWithMainMenu;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * Activity showing a ListView with the latest news from pivotaltracker
 * 
 * @author nlinn
 */
public class Activities extends RefreshableListActivityWithMainMenu implements QoSMessageHandler {

	private ActivitiesCursor pc;
	private PivotalTracker tracker;
	private Context ctx;
	private boolean alreadyCheckedForUpdates;

	public Activities() {
		super(RefreshableListActivityWithMainMenu.HIDE_ADD_MENU);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		ctx = this; // reference for handler
		setContentView(R.layout.activities_list);
		this.getListView().setBackgroundResource(android.R.color.white);
		this.getListView().setCacheColorHint(0);
		this.getListView().setDivider(this.getResources().getDrawable(R.drawable.darkgray1x1));// .getDivider().

		registerForContextMenu(getListView());

		setTitle("Activities");

		Log.i("me.linnemann.ptmobile","onCreate finished");
	}


	public void onResume() {
		super.onResume();
		tracker = new PivotalTracker(this);
		fillData(); // show list
		Log.i("Activities","needs update?");
		// --- refresh if update timestamp is too old (or never set)
		if(tracker.activitiesNeedUpdate()) {
			Log.i("Activities","needs update!");
			refresh();
		} else {
			if (!alreadyCheckedForUpdates) {
				alreadyCheckedForUpdates = true;
				new UpdateHelper(this);				
			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		pc.moveToPosition(position);
		startStoriesActivity(pc);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (pc !=null) pc.close();
		if (tracker != null) tracker.pause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (pc !=null) pc.close();
		if (tracker != null) tracker.pause();
	}

	private void fillData() {
		pc = tracker.getActivitiesCursor();
		startManagingCursor(pc);
		ActivitiesCursorAdapter pa = new ActivitiesCursorAdapter(this, pc);
		setListAdapter(pa);
	}


	/**
	 * Starting Stories in Tabs Activity with project at current position of cursor
	 * make shure cursor points to correct position
	 * 
	 * @param pc
	 */
	private void startStoriesActivity(ActivitiesCursor c) {
		Activity activity = c.getActivity();
		
		Integer projectId =  activity.getProjectId().getValue();
		String projectName = tracker.getProject(projectId).getName().getUIString();
		
		Intent i = new Intent(this, StoriesInTabs.class);
		i.putExtra("project_id", projectId);
		i.putExtra("project_name", projectName);
		startActivity(i);
	}

	public void refresh() {
		
		final QoS qos = new QoS(ctx,this);
		qos.setErrorMessage("error updating data");
		qos.setOkMessage("update complete");

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);

		// --- try if API-Token is set
		if (settings.getString(APIKeyPrefs.PREFS_KEY_TOKEN, "").length() > 4) {

			getParent().setProgressBarIndeterminateVisibility(true);

			new Thread() { 
				public void run() { 
					try {
						tracker.updateActivities();
						qos.sendSuccessMessageToHandler();
					} catch (RuntimeException e) {
						qos.sendErrorMessageToHandler(e);
					}
				} 
			}.start();
		} else {
			Log.i("Stories","update canceled, no valid APItoken");
		}
	}

	@Override
	public void addStory() {
		// nothing
	}

	public boolean onQoSERROR() {
		getParent().setProgressBarIndeterminateVisibility(false);
		return QoS.HANDLE_EVENT;
	}

	public boolean onQoSOK() {
		getParent().setProgressBarIndeterminateVisibility(false);
		fillData();
		return QoS.HANDLE_EVENT;
	}
}

