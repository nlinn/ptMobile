package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class APIKeyPrefs extends Activity {

	public static final String PREFS_KEY_TOKEN ="APIKEY"; 
	
	private static final String KEY_TOKEN = "APItoken"; // internal Todo cleanup

	private static Button btnFetch;
	private static Button btnSave;
	private static EditText etxtApiKey;
	private static EditText etxtUsername;
	private static EditText etxtPassword;
	private static SharedPreferences settings;	
	
	private ProgressDialog dialog;
	private Activity ctx;

	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            
            if (msg.getData().getString(KEY_TOKEN) != null) {
            	etxtApiKey.setText(msg.getData().getString(KEY_TOKEN));
            } else {
            	AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            	builder.setMessage("Error retrieving API key.")
            	       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            	                dialog.dismiss();
            	           }
            	       });
            	AlertDialog alert = builder.create();
            	alert.show();
            }
        }
    };
	
	private final Button.OnClickListener btnFetchOnClick = new Button.OnClickListener() {

		public void onClick(View v) {

			dialog = ProgressDialog.show(ctx, "", 
                    "Loading. Please wait...", true);
			
			new Thread() { 
				public void run() { 
					try{ 
						String token = new PivotalTracker(ctx).fetchAPIToken(etxtUsername.getText().toString().trim(),
								etxtPassword.getText().toString().trim());

						// --- very verbose msg sending :(	
						Bundle data = new Bundle();
						data.putString(KEY_TOKEN, token);
						Message msg = new Message();
						msg.setData(data);
						handler.sendMessage(msg);	
					} catch (Exception e) {  } 
				} 
			}.start(); 
		}
	};

	private final Button.OnClickListener btnSaveOnClick = new Button.OnClickListener() {

		public void onClick(View v) {
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(PREFS_KEY_TOKEN, etxtApiKey.getText().toString().trim());
			editor.commit();
			
			ctx.finish();
		}

	};


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Preferences");
		setContentView(R.layout.preferences);

		ctx = this;
		btnFetch = (Button) findViewById(R.id.ButtonFetch);
		btnFetch.setOnClickListener(btnFetchOnClick);

		btnSave = (Button) findViewById(R.id.ButtonSave);
		btnSave.setOnClickListener(btnSaveOnClick);

		etxtApiKey = (EditText) findViewById(R.id.EditTextAPIKey);
		etxtUsername = (EditText) findViewById(R.id.EditTextUsername);
		etxtPassword = (EditText) findViewById(R.id.EditTextPassword);


		settings = PreferenceManager.getDefaultSharedPreferences(ctx);
		etxtApiKey.setText(settings.getString(PREFS_KEY_TOKEN, "-"));
	}

	

}
