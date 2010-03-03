package me.linnemann.ptmobile;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TabHost;

/**
 * Start Activity: the initial screen, shown project and news tab
 * @author nlinn
 *
 */
public class Start extends TabActivity {

	private static final String TAG = "Start";
	private TabHost tabHost;
	private Context ctx = this;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		showLoginIfNoAPIKey();

		tabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.storytabs, tabHost.getTabContentView());
		setupTabs();
	}

	private void showLoginIfNoAPIKey() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
		String apikey = settings.getString(APIKeyPrefs.PREFS_KEY_TOKEN, "");
		Log.i(TAG,apikey);
		if (apikey.length() <= 0) {
			goToLoginActivity();
		}
	}

	private void goToLoginActivity() {
		Intent i = new Intent(this, Login.class);
		startActivity(i);
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