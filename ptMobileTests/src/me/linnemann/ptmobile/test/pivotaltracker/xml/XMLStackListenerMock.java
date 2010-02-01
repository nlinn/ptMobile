package me.linnemann.ptmobile.test.pivotaltracker.xml;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.xml.XMLStackListener;

public class XMLStackListenerMock implements XMLStackListener {

	private static final String TAG = "XMLStackListenerMock";
	private HashMap<String, String> elements = new HashMap<String, String>();
	private boolean elementPoppedFromStack;
	
	public void handleSubElement(String element, String data) {
		Log.i(TAG,"handleSubElement: "+element+" with data: "+data);
		elements.put(element, data);
	}
	
	public Map<String,String> getElements() {
		return elements;
	}

	public void elementPoppedFromStack() {
		Log.i(TAG,"elementPoppedFromStack!");
		elementPoppedFromStack = true;
	}
	
	public boolean isElementPoppedFromStack() {
		return elementPoppedFromStack;
	}
}
