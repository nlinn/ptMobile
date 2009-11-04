package me.linnemann.ptmobile;

import me.linnemann.ptmobile.adapter.StoriesCursorAdapter;
import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Stories extends ListActivity {

	private static final int STORYDETAILS_ID = Menu.FIRST + 5;
	private static final int NEXTSTATE_ID = Menu.FIRST + 6;
	
	private static final String TAG = "Stories";
	
	private PivotalTracker tracker;
	private StoriesCursor c;
	private String project_id;
	private Dialog dialog;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			updateList(project_id);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTheme(R.style.ptMobileDefault);
		setContentView(R.layout.stories_list);
		registerForContextMenu(getListView());
		//this.getListView().setBackgroundResource(android.R.color.white);
		this.getListView().setCacheColorHint(0);
		this.getListView().setDivider(this.getResources().getDrawable(R.drawable.darkgray1x1));// .getDivider().

		this.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Log.i(Stories.class.toString(),"Project ID from Extras: "+extras.getString("project_id"));

			setTitle("Project "+extras.getString("project_name"));
			project_id=extras.getString("project_id");
		}
		Log.i(Stories.class.toString(),"onCreate finished");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		c.moveToPosition(position);

		Intent i = new Intent(this, StoryDetails.class);
		i.putExtra("story_id", c.getId());

		startActivity(i);
	}

	private void updateList(String project_id) {
		Log.i(Stories.class.toString(),"fillData2 called: "+project_id);

		if (getIntent().getExtras().getString("filter").equalsIgnoreCase("done")) {
			c = tracker.getStoriesCursorDone(project_id);
		} else if (getIntent().getExtras().getString("filter").equalsIgnoreCase("current")) {
			c = tracker.getStoriesCursorCurrent(project_id);
		} else if (getIntent().getExtras().getString("filter").equalsIgnoreCase("backlog")) {
			c = tracker.getStoriesCursorBacklog(project_id);
		} 

		startManagingCursor(c);
		StoriesCursorAdapter sa = new StoriesCursorAdapter(this, c);
		setListAdapter(sa);
	}

	// --- OPTIONS ------------------------------------------
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		updateFromTracker();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, R.string.menu_refesh).setIcon(android.R.drawable.ic_menu_upload);
		return result;
	}
	
	// --- CONTEXT MENU ----------------------------------------	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("Story");

		menu.add(0, STORYDETAILS_ID, 0, R.string.menu_showstorydetails);
		
		AdapterView.AdapterContextMenuInfo info;
        try {
             info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            Log.e(TAG, "bad menuInfo", e);
            return;
        }

        c.moveToPosition(info.position);
        
		menu.add(0, NEXTSTATE_ID, 0, "Set "+c.getStory().getNextState());
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		switch(item.getItemId()) {
		case STORYDETAILS_ID:        	
			c.moveToPosition((int) info.position);
			Intent i = new Intent(this, StoryDetails.class);
			i.putExtra("story_id", c.getId());
			startActivity(i);
			return true;
		case NEXTSTATE_ID:        	
			c.moveToPosition((int) info.position);
			
			return true;
		}

		return super.onContextItemSelected(item);
	}
	// -------------------------------------------
	@Override
	public void onResume() {
		super.onResume();
		tracker = new PivotalTracker(this);

		updateList(project_id);	// load stories
		if (tracker.storiesNeedUpdate(project_id)) {
			updateFromTracker();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (c !=null) c.close();
		if (tracker != null) tracker.pause();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (c !=null) c.close();
		if (tracker != null) tracker.pause();
	}

	private void updateFromTracker() {
		dialog = ProgressDialog.show(this, "", 
				"Updating. Please wait...", true);
		new Thread() { 
			public void run() { 
				try{ 
					tracker.updateStoriesForProject(project_id);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
				} 
			} 
		}.start(); 
	}
}
