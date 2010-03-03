package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

	private static final String TAG ="Login";
	
	public static final String PREFS_KEY_TOKEN ="APIKEY"; 
	
	private static final String KEY_TOKEN = "APItoken"; // internal Todo cleanup

	private static Button btnLogin;
	private static EditText etxtUsername;
	private static EditText etxtPassword;
	private static SharedPreferences settings;	
	
	private ProgressDialog dialog;
	private Activity ctx;

	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            
            if ((msg.what >=0) && (msg.getData().getString(KEY_TOKEN) != null)) {
            	String key = msg.getData().getString(KEY_TOKEN);
            	saveAPIKey(key);
            	finish();
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleAndLayout();
		
		ctx = this;
		btnLogin = (Button) findViewById(R.id.ButtonLogin);
		btnLogin.setOnClickListener(btnOnClick);

		etxtUsername = (EditText) findViewById(R.id.EditTextUsername);
		etxtPassword = (EditText) findViewById(R.id.EditTextPassword);
		settings = PreferenceManager.getDefaultSharedPreferences(ctx);
	}

	private void setTitleAndLayout() {
		setTitle("ptMobile "+getAppVersionName());
		setContentView(R.layout.login);
	}
	
	private String getAppVersionName() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			Log.w(TAG, "Exception while retrieving version name.",e);
			return "";
		}
	}
	
	private void saveAPIKey(String key) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(APIKeyPrefs.PREFS_KEY_TOKEN, key.trim());
		editor.commit();
	}
	
	private final Button.OnClickListener btnOnClick = new Button.OnClickListener() {

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
					} catch (Exception e) { 
						Log.w(TAG,"Exception while logging in: "+e.getMessage());
						handler.sendEmptyMessage(-1);	
					} 
				} 
			}.start(); 
		}
	};
}
