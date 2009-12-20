package me.linnemann.ptmobile.ui;

import android.content.Intent;

public interface RefreshableActivity {

	public void refresh();
	public void startActivity(Intent intent);
}
