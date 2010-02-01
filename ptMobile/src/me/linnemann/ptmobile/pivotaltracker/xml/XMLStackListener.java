package me.linnemann.ptmobile.pivotaltracker.xml;

public interface XMLStackListener {
	public void handleSubElement(String element, String data);
	public void elementPoppedFromStack();
}
