package me.linnemann.ptmobile.ui;

import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class RefreshableListActivityWithMainMenu extends ListActivity implements RefreshableActivity {

	private MainMenu mainMenu;
	
	public RefreshableListActivityWithMainMenu() {
		super();
		mainMenu = new MainMenu(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mainMenu.addMenuItemsToMenu(menu);
		return MainMenu.SHOW_MENU;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mainMenu.performMenuAction(item);
		return MainMenu.FINISH_PROCESSING;
	}
	
	public abstract void refresh();
}
