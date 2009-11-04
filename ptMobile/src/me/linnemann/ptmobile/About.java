package me.linnemann.ptmobile;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class About extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("About ptMobile");
		setContentView(R.layout.about);

		TextView tv = (TextView) this.findViewById(R.id.textVersionAbout);
		PackageManager manager = this.getPackageManager();
		PackageInfo info;
		String newText = "";
		try {
			Log.i("about", "trying");
			info = manager.getPackageInfo(this.getPackageName(), 0);
			newText = tv.getText().toString().replaceAll("\\$VERSION\\$", info.versionName);
			Log.i("about", newText);
		} catch (NameNotFoundException e) {
			newText = tv.getText().toString().replaceAll("\\$VERSION\\$", "");
			e.printStackTrace();
		}

		tv.setText(newText);
	}
}
