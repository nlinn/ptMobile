package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

public class StoriesInTabs extends TabActivity {

	private PivotalTracker tracker;
	private String project_id;
	private TabHost tabHost;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.storytabs, tabHost.getTabContentView());
		setupTabs();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (this.tracker != null) this.tracker.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		tracker = new PivotalTracker(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (this.tracker != null) this.tracker.pause();
	}

	private void setupTabs() {
		Intent done = new Intent(this, Stories.class);
		Intent current = new Intent(this, Stories.class);
		Intent backlog = new Intent(this, Stories.class);		

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			project_id = extras.getString("project_id");
			setTitle("Project "+extras.getString("project_name"));

			done.putExtra("project_id", project_id);
			done.putExtra("filter", "done");

			current.putExtra("project_id", project_id);
			current.putExtra("filter", "current");

			backlog.putExtra("project_id", project_id);
			backlog.putExtra("filter", "backlog");
		}

		tabHost.clearAllTabs();

		tabHost.addTab(tabHost.newTabSpec("tab_current")
				.setIndicator("current")
				.setContent(current));

		tabHost.addTab(tabHost.newTabSpec("tab_backlog")
				.setIndicator("backlog")
				.setContent(backlog));

		tabHost.addTab(tabHost.newTabSpec("tab_done")
				.setIndicator("done")
				.setContent(done));

	}

}
