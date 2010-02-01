package me.linnemann.ptmobile.test.pivotaltracker;

import java.io.InputStream;

import me.linnemann.ptmobile.pivotaltracker.adapter.APIAdapter;
import me.linnemann.ptmobile.pivotaltracker.xml.RESTXMLCommand;
import me.linnemann.ptmobile.test.functional.StoryFromIceboxXMLTests;

public class APIAdapterMock implements APIAdapter {

	private InputStream icebox, done, activities, projects, current, backlog;
	
	public void setIceboxStream(InputStream in) {
		this.icebox = in;
	}
	
	public void setBacklogStream(InputStream in) {
		this.backlog = in;
	}
	
	public void setCurrentStream(InputStream in) {
		this.current = in;
	}
	
	public void setDoneStream(InputStream in) {
		this.done = in;
	}
	
	public InputStream getActivitiesStream() {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getBacklogStream(Integer projectId) {
		return backlog;
	}

	public InputStream getCurrentStream(Integer projectId) {
		return current;
	}

	public InputStream getDoneStream(Integer projectId) {
		return done;
	}

	public InputStream getIceboxStream(Integer projectId) {
		return icebox;
	}

	public InputStream getProjectsStream() {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getStreamForCommand(RESTXMLCommand command) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTimeoutInMillis() {
		// TODO Auto-generated method stub
		return 0;
	}

	public InputStream getTokenStream(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
