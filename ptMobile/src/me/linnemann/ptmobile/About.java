package me.linnemann.ptmobile;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * activity showing my about text with version info in headline
 * @author nlinn
 */
public class About extends Activity {

	private static final String TAG = "About";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleAndLayout();
		setAboutTextHeadline();
	}

	private void setTitleAndLayout() {
		setTitle("About ptMobile");
		setContentView(R.layout.about);
	}

	private void setAboutTextHeadline() {
		TextView aboutHeadlineWidget = findAboutHeadlineWidget();
		String aboutHeadline = getAboutHeadlineWithCurrentVersion();
		aboutHeadlineWidget.setText(aboutHeadline);
	}

	private TextView findAboutHeadlineWidget() {
		return (TextView) this.findViewById(R.id.textVersionAbout);
	}

	private String getAboutHeadlineWithCurrentVersion() {
		String aboutHeadline =  getResources().getString(R.string.about_headline);  
		String appVersionName = getAppVersionName();
		return aboutHeadline.replaceAll("\\$VERSION\\$", appVersionName);
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
}

