package me.linnemann.ptmobile.pivotaltracker.xml;

import java.util.HashMap;
import java.util.LinkedList;

public class XMLStack {

	private StringBuilder elementData;
	
	public static final XMLStackListener IGNORE_LISTENER = new XMLStackListener() {
		public void handleSubElement(String element, String data) {
			// ignore;
		}

		public void elementPoppedFromStack() {
			// ignore;
		}
	};
	
	LinkedList<String> stack;
	HashMap<String,XMLStackListener> listeners;
	
	public XMLStack() {
		stack = new LinkedList<String>();
		listeners = new HashMap<String,XMLStackListener>();
		initElementData();
	}
	
	private void initElementData() {
		elementData = new StringBuilder("");
	}
	
	public void addData(char ch[], int start, int length) {
		elementData.append(ch, start, length);
	}
	
	public void addListener(String path, XMLStackListener listener) {
		listeners.put(path,listener);
	}
	
	public XMLStackListener getListener(String path) {
		if (listeners.containsKey(path)) {
			return listeners.get(path);
		} else {
			return XMLStack.IGNORE_LISTENER;
		}
	}
	
	public void startElement(String element) {
		stack.add(element);
		initElementData();
	}
	
	public void endElement(String element) {
		String last = stack.getLast();
		if (last.equals(element)) {
			String pathWithElement = getQualifiedPath();
			invokeElementPoppedFromStack(pathWithElement);
			
			stack.removeLast();
			
			String pathAfterRemovingElement = getQualifiedPath();
			invokeHandleSubElement(pathAfterRemovingElement, element, elementData);
		} else {
			throw new RuntimeException("element is not on top of stack");
		}
	}
	
	private void invokeElementPoppedFromStack(String element) {
		XMLStackListener listener = getListener(element);
		listener.elementPoppedFromStack();
	}
	
	private void invokeHandleSubElement(String parentElement, String subElement, StringBuilder data) {
		XMLStackListener listener = getListener(parentElement);
		listener.handleSubElement(subElement, data.toString());
	}
	
	
	public String getParent() {
		if (stack.size() < 2) {
			return "root";
		} else {
			return stack.get(stack.size()-1);
		}
	}
	
	public String getCurrent() {
		return stack.getLast();
	}
	
	public String getQualifiedPath() {
		StringBuilder path = new StringBuilder("");
		
		for (String element : stack) {
			path.append(element);
			path.append(".");
		}
		
		if (path.length() > 0)
			path.deleteCharAt(path.length()-1);
		
		return path.toString();
	}
}
