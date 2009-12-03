package me.linnemann.ptmobile.pivotaltracker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import me.linnemann.ptmobile.pivotaltracker.xml.UpdateStoryCommand;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLActivitiesHandler;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLProjectsHandler;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStoriesHandler;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLTokenHandler;

import org.xml.sax.SAXException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * APIAdapter is connecting to pivotaltracker.com to load/save data. The adapter is interfacing with
 * the local db, it is not meant to serve data a UI
 * 
 * @author niels
 *
 */
public class APIAdapter {

	private static final String TAG="APIAdapter";
	private static final int TIMEOUT_MILLIS = 20000; // http read timeout
	private static final String TRACKER_TOKEN_NAME = "X-TrackerToken";

	private static final String URL_PROJECTS ="http://www.pivotaltracker.com/services/v2/projects";
	private static final String URL_ACTIVITIES ="http://www.pivotaltracker.com/services/v2/activities";
	//private static final String URL_STORIES ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories";
	private static final String URL_ITERATIONS ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations";
	private static final String URL_ITERATIONS_CURRENT ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations/current";
	private static final String URL_ITERATIONS_DONE ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations/done?offset=-3";
	private static final String URL_ICEBOX ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories?filter=state%3Aunscheduled";
	private static final String URL_ITERATIONS_BACKLOG ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations/backlog";
	private static final String URL_TOKEN ="https://www.pivotaltracker.com/services/tokens/active";

	private String apikey;

	private DBAdapter db;
	private RESTSupport rest;

	public APIAdapter(Context ctx, DBAdapter db) {
		this.db = db;

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
		apikey = settings.getString("APIKEY", "");
		rest = new RESTSupport(TIMEOUT_MILLIS);
	}

	/**
	 * connects to pivotaltracker to retrieve a API token
	 * catches all exceptions and returns null if unsuccessful
	 * 
	 * @param username
	 * @param password
	 * @return token or null if unsuccessful
	 */
	public String getAPIToken(String username, String password) {

		String token=null;

		try {
			token = new XMLTokenHandler().getToken(loadToken(username, password));
			//token = new XMLTokenHandler().getToken(inputStreamWithToken(username, password));
			Log.i(APIAdapter.class.toString(),"token: "+token);
		} catch (IOException e) {
			Log.e(APIAdapter.class.getSimpleName(),"getAPIToken failed, IO: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(APIAdapter.class.getSimpleName(),"getAPIToken failed, ParserConfig: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(APIAdapter.class.getSimpleName(),"getAPIToken failed, SAX: "+e.getMessage());
		}

		return token;
	}

	public boolean updateProjects() {
		boolean successful=false;

		db.deleteAllProjects();	// Todo: bad boy! make sure to delete only if API update is successful!

		try {
			new XMLProjectsHandler(db).go(loadURL(URL_PROJECTS));
			db.saveProjectsUpdatedTimestamp();
			successful = true;
		} catch (IOException e) {
			Log.e(APIAdapter.class.getSimpleName(),"updateProjects, IO: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(APIAdapter.class.getSimpleName(),"updateProjects, ParserConfig: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(APIAdapter.class.getSimpleName(),"updateProjects, SAX: "+e.getMessage());
		}

		return successful;
	}

	public boolean updateActivities() {
		boolean successful=false;

		db.deleteAllActivities();	// Todo: bad boy! make sure to delete only if API update is successful!

		try {
			new XMLActivitiesHandler(db).go(loadURL(URL_ACTIVITIES));
			db.saveActivitiesUpdatedTimestamp();
			successful = true;
		} catch (IOException e) {
			Log.e(APIAdapter.class.getSimpleName(),"updateActivities, IO: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(APIAdapter.class.getSimpleName(),"updateActivities, ParserConfig: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(APIAdapter.class.getSimpleName(),"updateActivities, SAX: "+e.getMessage());
		}

		return successful;
	}

	/**
	 * 
	 * @param project_id
	 * @param iteration_group current, backlog, done
	 * @return
	 */
	public boolean updateStoriesForProject(String project_id, String iteration_group) {
		boolean successful=false;

		Log.i(TAG,"udpateStoriesInProject "+project_id + " group "+iteration_group);

		String url="";
		
		if (iteration_group.equalsIgnoreCase("current")) {
			url = URL_ITERATIONS_CURRENT.replaceAll("PROJECT_ID", project_id); // create project specific url
		}
		if (iteration_group.equalsIgnoreCase("done")) {
			url = URL_ITERATIONS_DONE.replaceAll("PROJECT_ID", project_id); // create project specific url
		}
		if (iteration_group.equalsIgnoreCase("backlog")) {
			url = URL_ITERATIONS_BACKLOG.replaceAll("PROJECT_ID", project_id); // create project specific url
		}
		if (iteration_group.equalsIgnoreCase("icebox")) {
			url = URL_ICEBOX.replaceAll("PROJECT_ID", project_id); // create project specific url
		}
		
		Log.i(TAG,url);

		long timestamp = System.currentTimeMillis();
		
		try {
			new XMLStoriesHandler(db, project_id, iteration_group).go(loadURL(url));
			db.saveStoriesUpdatedTimestamp(project_id, iteration_group);
			db.deleteStoriesInProject(project_id, timestamp, iteration_group);
			successful=true;	
		} catch (IOException e) {
			Log.e(APIAdapter.class.getSimpleName(),"updateStoriesInProject, IO: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(APIAdapter.class.getSimpleName(),"updateStoriesInProject, ParserConfig: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(APIAdapter.class.getSimpleName(),"updateStoriesInProject, SAX: "+e.getMessage());
		}
		return successful;
	}

	public void editStory(Story story) {
		
		UpdateStoryCommand usc = new UpdateStoryCommand(story);

		Map<String,String> properties = new HashMap<String,String>();
		properties.put(TRACKER_TOKEN_NAME,apikey);
		properties.put("Content-type","application/xml");

		try {
			ByteArrayInputStream body = new ByteArrayInputStream(usc.getXMLBytes());

			InputStream is = rest.doPUT(usc.getURL(), properties, body);
			Log.d(TAG,"RESP: "+rest.textFromURL(is));

			is.close();
		} catch (IOException e) {
			Log.e(TAG,"editStory, IO: "+e.getMessage());
		}
	}
	
	private InputStream loadToken(final String username,final String password) throws IOException {
		return rest.doGET(new URL(URL_TOKEN), null, username, password);
	}

	private InputStream loadURL(String urlString) throws IOException {
		Map<String,String> properties = new HashMap<String,String>();
		properties.put(TRACKER_TOKEN_NAME,apikey);
		return rest.doGET(new URL(urlString), properties);
	}

}
