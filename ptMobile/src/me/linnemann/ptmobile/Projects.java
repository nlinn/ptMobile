package me.linnemann.ptmobile;

import me.linnemann.ptmobile.adapter.ProjectsCursorAdapter;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Projects extends ListActivity {
	public static final int REFRESH = Menu.FIRST;
	private static final int PREFERENCES_ID = Menu.FIRST + 3;
	private static final int FLUSH_ID = Menu.FIRST + 4;
	private static final int ABOUT_ID = Menu.FIRST + 5;
	private static final int PROJECT_DETAILS_ID = Menu.FIRST + 6;
	private static final int STORIES_ID = Menu.FIRST + 7;

	private ProjectsCursor pc;
	private PivotalTracker tracker;
	private Context ctx;
	private boolean alreadyCheckedForUpdates;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			getParent().setProgressBarIndeterminateVisibility(false);

			// --- >0 rc is failure
			if (msg.what > 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setMessage("Updating data failed.")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), "update complete", Toast.LENGTH_SHORT);
				toast.show();	
			}

			fillData();
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ctx = this; // reference for handler
		setContentView(R.layout.projects_list);
		this.getListView().setBackgroundResource(android.R.color.white);
		this.getListView().setCacheColorHint(0);
		this.getListView().setDivider(this.getResources().getDrawable(R.drawable.darkgray1x1));// .getDivider().

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
		tracker = new PivotalTracker(this);
		fillData(); // show projects
		Log.i("Projects","needs update?");
		// --- refresh if update timestamp is too old (or never set)
		if(tracker.projectsNeedUpdate()) {
			Log.i("Projects","needs update!");
			refresh();
		} else {
			if (!alreadyCheckedForUpdates) {
				alreadyCheckedForUpdates = true;
				new UpdateHelper(this);				
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, REFRESH, 0, R.string.menu_refesh).setIcon(android.R.drawable.ic_menu_upload);
		menu.add(0, PREFERENCES_ID, 0, R.string.menu_prefs).setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(0, FLUSH_ID, 0, "debug: flush local data");
		menu.add(0, ABOUT_ID, 0, "About").setIcon(android.R.drawable.ic_menu_help);
		return result;
	}


	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case REFRESH:
			refresh();
			return true;
		case PREFERENCES_ID:
			startActivity(new Intent(this,Preferences.class));
			return true;
		case FLUSH_ID:
			tracker.flush();
			fillData();
			return true;
		case ABOUT_ID:
			startActivity(new Intent(this,About.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
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
		
		Intent i = new Intent(this, StoriesInTabs.class);
		i.putExtra("project_id", pc.getId());
		i.putExtra("project_name", pc.getName());

		startActivity(i);
	}
	
	/**
	 * Starting Project Details Activity with project at current position of cursor
	 * make shure cursor points to correct position
	 * 
	 * @param pc
	 */
	private void startProjectDetailsActivity(ProjectsCursor pc) {
		
		Log.i("pos","pos: "+pc.getPosition());

		Intent i = new Intent(this, ProjectDetails.class);
		i.putExtra("project_id", pc.getId());
		startActivity(i);
	}
	
	private void refresh() {

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);

		// --- try if API-Token is set
		if (settings.getString(APIKeyPrefs.PREFS_KEY_TOKEN, "").length() > 4) {
			getParent().setProgressBarIndeterminateVisibility(true);
			new Thread() { 
				public void run() { 
					if (tracker.updateProjects()) {
						handler.sendEmptyMessage(0);	// success
					} else {
						handler.sendEmptyMessage(1);	// failed
					}
				} 
			}.start();
		} else {
			Log.i("Stories","update canceled, no valid APItoken");
		}
	}
}
