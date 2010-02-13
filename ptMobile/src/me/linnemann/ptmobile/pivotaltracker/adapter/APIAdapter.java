package me.linnemann.ptmobile.pivotaltracker.adapter;

import java.io.InputStream;

import me.linnemann.ptmobile.pivotaltracker.xml.RESTXMLCommand;

public interface APIAdapter {

	public int getTimeoutInMillis();

	public InputStream getTokenStream(String username, String password);
	public InputStream getActivitiesStream();
	public InputStream getProjectsStream();
	public InputStream getDoneStream(Integer project_id, String protocol);
	public InputStream getCurrentStream(Integer project_id, String protocol);
	public InputStream getBacklogStream(Integer project_id, String protocol);
	public InputStream getIceboxStream(Integer project_id, String protocol);
	public InputStream getStreamForCommand(RESTXMLCommand command);
}
