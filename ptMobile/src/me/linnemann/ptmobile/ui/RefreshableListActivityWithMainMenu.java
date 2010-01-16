package me.linnemann.ptmobile.ui;

import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class RefreshableListActivityWithMainMenu extends ListActivity implements RefreshableActivity {

	public static final boolean SHOW_ADD_MENU = true;
	public static final boolean HIDE_ADD_MENU = !SHOW_ADD_MENU;
	private MainMenu mainMenu;
	
	public RefreshableListActivityWithMainMenu(boolean showAddMenu) {
		super();
		mainMenu = new MainMenu(this, showAddMenu);
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
	public abstract void addStory();
}
