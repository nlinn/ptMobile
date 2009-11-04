package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TabHost;

public class Start extends TabActivity {

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
		Intent projects = new Intent(this, Projects.class);
		Intent activities = new Intent(this, Activities.class);

		tabHost.clearAllTabs();

		tabHost.addTab(tabHost.newTabSpec("tab_projects")
				.setIndicator("projects")
				.setContent(projects));

		tabHost.addTab(tabHost.newTabSpec("tab_activities")
				.setIndicator("activity feed")
				.setContent(activities));

	}

}

