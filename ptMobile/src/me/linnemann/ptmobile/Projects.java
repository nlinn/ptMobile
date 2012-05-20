package me.linnemann.ptmobile;

import me.linnemann.ptmobile.adapter.ProjectsCursorAdapter;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.qos.QoS;
import me.linnemann.ptmobile.qos.QoSMessageHandler;
import me.linnemann.ptmobile.ui.RefreshableListActivityWithMainMenu;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Projects extends RefreshableListActivityWithMainMenu implements QoSMessageHandler {

	private static final int PROJECT_DETAILS_ID = Menu.FIRST + 6;
	private static final int STORIES_ID = Menu.FIRST + 7;

	private ProjectsCursor pc;
	private PivotalTracker tracker;
	private Context ctx;
	private boolean alreadyCheckedForUpdates;
	
	public Projects() {
		super(RefreshableListActivityWithMainMenu.HIDE_ADD_MENU);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ctx = this; // reference for handler
		setContentView(R.layout.projects_list);
		this.getListView().setBackgroundResource(android.R.color.white);
		this.getListView().setCacheColorHint(0);
		this.getListView().setDivider(this.getResources().getDrawable(R.drawable.darkgray1x1));

		registerForContextMenu(getListView());
		setTitle("Projects");
		Log.i("me.linnemann.ptmobile","onCreate finished");
	}

	 @Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.setHeaderTitle("Project");
			menu.add(0, STORIES_ID, 0, R.string.menu_showstories);
	        menu.add(0, PROJECT_DETAILS_ID, 0, R.string.menu_showprojectdetails);
		}
	
	public void onResume() {
		super.onResume();
		tracker = PivotalTracker.getInstance(this);
		populateList(); // show projects
		
		if (!alreadyCheckedForUpdates) { // TODO maybe better to store a timestamp in prefs? am i checking too often?
			Log.i("Projects","checking for updates");
			alreadyCheckedForUpdates = true;
			new UpdateHelper(this);				
		}
		
		// --- refresh if update timestamp is too old (or never set)
		if(tracker.projectsNeedUpdate()) {
			Log.i("Projects","needs update!");
			refresh();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		switch(item.getItemId()) {
		case STORIES_ID:        	
			pc.moveToPosition((int) info.position);
			startStoriesActivity(pc);
			return true;
		case PROJECT_DETAILS_ID:        	
			pc.moveToPosition((int) info.position);
			startProjectDetailsActivity(pc);
			return true;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		pc.moveToPosition(position);
		startStoriesActivity(pc);
	}

	private void populateList() {
		pc = tracker.getProjectsCursor();
		startManagingCursor(pc);
		ProjectsCursorAdapter pa = new ProjectsCursorAdapter(this, pc);
		setListAdapter(pa);
	}

	/**
	 * Starting Stories in Tabs Activity with project at current position of cursor
	 * make shure cursor points to correct position
	 * 
	 * @param pc
	 */
	private void startStoriesActivity(ProjectsCursor pc) {

		Log.i("pos stories","pos: "+pc.getPosition());
		
		Project project = pc.getProject();
		
		Intent i = new Intent(this, StoriesInTabs.class);
		i.putExtra("project_id", project.getId().getValue());
		i.putExtra("project_name", project.getName().getValueAsString());

		startActivity(i);
	}
	
	/**
	 * Starting Project Details Activity with project at current position of cursor
	 * make shure cursor points to correct position
	 * 
	 * @param pc
	 */
	private void startProjectDetailsActivity(ProjectsCursor pc) {
		
		Log.v("pos","pos: "+pc.getPosition());
		Project project = pc.getProject();
		
		Intent i = new Intent(this, ProjectDetails.class);
		i.putExtra("project_id", project.getId().getValue());
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
						tracker.updateProjects();						
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
		populateList();
		return QoS.HANDLE_EVENT;
	}
}
