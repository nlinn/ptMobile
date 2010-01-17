package me.linnemann.ptmobile;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TabHost;

public class StoriesInTabs extends TabActivity {

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
		setProgressBarIndeterminateVisibility(false);
	}

	private void setupTabs() {
		
		Intent done = new Intent(this, Stories.class);
		Intent current = new Intent(this, Stories.class);
		Intent backlog = new Intent(this, Stories.class);
		Intent icebox = new Intent(this, Stories.class);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			setTitle("Project "+extras.getString("project_name"));
			// --- delegate extras to tabs	
			setExtras(extras, done, "done");
			setExtras(extras, backlog, "backlog");
			setExtras(extras, current, "current");
			setExtras(extras, icebox, "icebox");
		}

		tabHost.clearAllTabs();
		tabHost.addTab(tabHost.newTabSpec("tab_done")
				.setIndicator("done")
				.setContent(done));
		tabHost.addTab(tabHost.newTabSpec("tab_current")
				.setIndicator("current")
				.setContent(current));
		tabHost.addTab(tabHost.newTabSpec("tab_backlog")
				.setIndicator("backlog")
				.setContent(backlog));
		tabHost.addTab(tabHost.newTabSpec("tab_icebox")
				.setIndicator("icebox")
				.setContent(icebox));
		
		tabHost.setCurrentTab(1);
	}
	
	private void setExtras(Bundle extras, Intent intent, String iteration_group) {
		intent.putExtras(extras);
		intent.putExtra("filter", iteration_group);
	}	
}
