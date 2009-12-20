package me.linnemann.ptmobile.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import me.linnemann.ptmobile.About;
import me.linnemann.ptmobile.Preferences;
import me.linnemann.ptmobile.R;

public class MainMenu {

	public static final boolean SHOW_MENU = true;
	public static final boolean FINISH_PROCESSING = true;
	public static final int DEFAULT_MENU_GROUP = Menu.NONE;
	public static final int DEFAULT_MENU_ORDER = Menu.NONE;
	
	private static final int MENU_REFRESH = 10000123;
	private static final int MENU_PREFERENCES = 10000124;
	private static final int MENU_ABOUT = 10000125;

	private RefreshableActivity activity;
	private Context context;

	public MainMenu(RefreshableActivity activity, Context context) {
		this.activity = activity;
		this.context = context;
	}

	public void addMenuItemsToMenu(Menu menu) {
		addMenuItemWithIcon(menu, MENU_REFRESH, R.string.menu_refesh, R.drawable.ic_menu_refresh);
		addMenuItemWithIcon(menu, MENU_PREFERENCES, R.string.menu_prefs, android.R.drawable.ic_menu_preferences);
		addMenuItemWithIcon(menu, MENU_ABOUT, R.string.menu_about, android.R.drawable.ic_menu_help);
	}

	private void addMenuItemWithIcon(Menu parentMenu, int menuId, int name, int icon) {
		MenuItem menu = parentMenu.add(	DEFAULT_MENU_GROUP,
									   	menuId,
									   	DEFAULT_MENU_ORDER,
										name);
		menu.setIcon(icon);
	}
	
	public void performMenuAction(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_REFRESH:
				executeRefresh();
				break;
			case MENU_PREFERENCES:
				showPreferences();
				break;
			case MENU_ABOUT:
				showAbout();
				break;
			default:
				throw new RuntimeException("trying to perform action on nonexisting menu");
		}
	}

	private void executeRefresh() {
		activity.refresh();
	}

	private void showPreferences() {
		showActivityFromClass(Preferences.class);
	}

	private void showAbout() {
		showActivityFromClass(About.class);
	}

	private void showActivityFromClass(Class<? extends Activity> activityToStart) {
		activity.startActivity(new Intent(context, activityToStart));
	}
}
