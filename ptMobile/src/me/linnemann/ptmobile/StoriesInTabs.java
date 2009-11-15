package me.linnemann.ptmobile;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TabHost;

public class StoriesInTabs extends TabActivity {

	private String project_id;
	private TabHost tabHost;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		tabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.storytabs, tabHost.getTabContentView());
		setupTabs();
	}

	@Override
	public void onStop() {
		super.onStop();
		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
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
