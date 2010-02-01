package me.linnemann.ptmobile.pivotaltracker.adapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import me.linnemann.ptmobile.pivotaltracker.RESTSupport;
import me.linnemann.ptmobile.pivotaltracker.xml.RESTXMLCommand;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class APIAdapterImpl implements APIAdapter {

	private static final String TAG = "APIAdapterImpl";

	private static final int TIMEOUT_MILLIS = 20000; // http read timeout
	private static final String TRACKER_TOKEN_NAME = "X-TrackerToken";

	private static final String URL_PROJECTS ="http://www.pivotaltracker.com/services/v3/projects";
	private static final String URL_ACTIVITIES ="http://www.pivotaltracker.com/services/v2/activities";
	private static final String URL_ITERATIONS_CURRENT ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations/current";
	private static final String URL_ITERATIONS_DONE ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations/done?offset=-3";
	private static final String URL_ICEBOX ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/stories?filter=state%3Aunscheduled";
	private static final String URL_ITERATIONS_BACKLOG ="http://www.pivotaltracker.com/services/v2/projects/PROJECT_ID/iterations/backlog";
	private static final String URL_TOKEN ="https://www.pivotaltracker.com/services/tokens/active";

	private String apikey;
	private RESTSupport rest;

	public APIAdapterImpl(Context ctx) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
		apikey = settings.getString("APIKEY", "");
		rest = new RESTSupport(TIMEOUT_MILLIS);
	}

	public int getTimeoutInMillis() {
		return TIMEOUT_MILLIS;
	}

	private InputStream loadURL(String urlString) {
		Map<String,String> properties = new HashMap<String,String>();
		properties.put(TRACKER_TOKEN_NAME,apikey);

		try {
			InputStream in = rest.doGET(new URL(urlString), properties);
			return in;
		} catch(IOException e) {
			Log.e(TAG,"IOException: "+e.getMessage());
			throw new RuntimeException("IOException while loading stream: "+e.getMessage());
		} 
	}

	public InputStream getActivitiesStream() {
		String url = URL_ACTIVITIES;
		return loadURL(url);
	}

	public InputStream getProjectsStream() {
		String url = URL_PROJECTS;
		return loadURL(url);
	}

	public InputStream getTokenStream(final String username,final String password) {
		try {
			return rest.doGET(new URL(URL_TOKEN), null, username, password);
		} catch(IOException e) {
			Log.e(TAG,"IOException: "+e.getMessage());
			throw new RuntimeException("IOException while loading stream: "+e.getMessage());
		} 
	}

	public InputStream getDoneStream(Integer project_id) {
		String url = URL_ITERATIONS_DONE.replaceAll("PROJECT_ID", project_id.toString());
		return loadURL(url);
	}

	public InputStream getCurrentStream(Integer project_id) {
		String url = URL_ITERATIONS_CURRENT.replaceAll("PROJECT_ID", project_id.toString());
		return loadURL(url);
	}

	public InputStream getBacklogStream(Integer project_id) {
		String url = URL_ITERATIONS_BACKLOG.replaceAll("PROJECT_ID", project_id.toString());
		return loadURL(url);
	}

	public InputStream getIceboxStream(Integer project_id) {
		String url = URL_ICEBOX.replaceAll("PROJECT_ID", project_id.toString());
		return loadURL(url);
	}

	public InputStream getStreamForCommand(RESTXMLCommand command) {
		Map<String,String> properties = new HashMap<String,String>();
		properties.put(TRACKER_TOKEN_NAME,apikey);
		properties.put("Content-type","application/xml");

		try {
			ByteArrayInputStream body = new ByteArrayInputStream(command.getXMLBytes());

			InputStream is = null;
			if (command.getPUTorPOST().equalsIgnoreCase("PUT")) {
				is = rest.doPUT(command.getURL(), properties, body); 
			} else {
				is = rest.doPOST(command.getURL(), properties, body);
			}
			return is;
		} catch (IOException e) {
			Log.e(TAG,"IO: "+e.getMessage());
			throw new RuntimeException("IOException while put/post: "+e.getMessage());
		}
	}
}
