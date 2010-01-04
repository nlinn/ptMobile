package me.linnemann.ptmobile.pivotaltracker.xml;

import java.net.URL;

public abstract class RESTXMLCommand {

	public abstract URL getURL();
	public abstract String getXMLString();
	
	public byte[] getXMLBytes() {
		return getXMLString().getBytes();
	}
}
