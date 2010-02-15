package me.linnemann.ptmobile.test.pivotaltracker;

import java.io.InputStream;

import me.linnemann.ptmobile.pivotaltracker.adapter.APIAdapter;
import me.linnemann.ptmobile.pivotaltracker.xml.RESTXMLCommand;

public class APIAdapterMock implements APIAdapter {

	private InputStream mockstream;
	
	public void setMockStream(InputStream in) {
		this.mockstream = in;
	}
	
	public InputStream getActivitiesStream() {
		return mockstream;
	}

	public InputStream getBacklogStream(Integer projectId, String protocol) {
		return mockstream;
	}

	public InputStream getCurrentStream(Integer projectId, String protocol) {
		return mockstream;
	}

	public InputStream getDoneStream(Integer projectId, String protocol) {
		return mockstream;
	}

	public InputStream getIceboxStream(Integer projectId, String protocol) {
		return mockstream;
	}

	public InputStream getProjectsStream() {
		return mockstream;
	}

	public InputStream getStreamForCommand(RESTXMLCommand command) {
		return mockstream;
	}

	public int getTimeoutInMillis() {
		return 0;
	}

	public InputStream getTokenStream(String username, String password) {
		return mockstream;
	}

}
