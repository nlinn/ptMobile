package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TabHost;

/**
 * Start Activity: the initial screen, shown project and news tab
 * @author nlinn
 *
 */
public class Start extends TabActivity {

	private TabHost tabHost;
	private Context ctx = this;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		tabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.storytabs, tabHost.getTabContentView());
		setupTabs();

		//showDialog(1);
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






	protected Dialog onCreateDialog(int id) {


		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.dialog_login, null);
		return new AlertDialog.Builder(Start.this)
		.setIcon(null)
		.setTitle("Login")
		.setView(textEntryView)
		.setPositiveButton("login", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				final EditText usernameWidget = (EditText) ((AlertDialog) dialog).findViewById(R.id.usernameEdit);
				final EditText passwordWidget = (EditText) ((AlertDialog) dialog).findViewById(R.id.passwordEdit);

				final String username = usernameWidget.getText().toString().trim();
				final String password = passwordWidget.getText().toString().trim();

				setProgressBarIndeterminateVisibility(true);

				new Thread() { 
					public void run() { 
						try {      

							String token = new PivotalTracker(ctx).fetchAPIToken(username,password);

							SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
							SharedPreferences.Editor editor = settings.edit();
							editor.putString(APIKeyPrefs.PREFS_KEY_TOKEN, token);
							editor.commit();
							
							setProgressBarIndeterminateVisibility(false);

							//QoS.sendSuccessMessageToHandler(handler);
						} catch (RuntimeException e) {
							Log.w("Start",e.getMessage());
							//QoS.sendErrorMessageToHandler(e, handler);
						}
					} 
				}.start();
			}

		})
		.create();
	}
}

