package me.linnemann.ptmobile.pivotaltracker.value;

public enum StoryType implements TrackerValue {
	FEATURE, BUG, CHORE, RELEASE;
	
	public String getValueAsString() {
		return name().toLowerCase();
	}
	
	public String getUIString() {
		return getMixedCaseName();
	}
	
	private String getMixedCaseName() {
		return name().substring(0,1).toUpperCase() + name().substring(1).toLowerCase();
	}

	public Object getValue() {
		return this;
	}

	public boolean isEmpty() {
		return false;
	}
	
	public String getXMLWrappedValue(String xmlTag) {
		return "<" + xmlTag + ">" + getValueAsString() + "</"+xmlTag+">";
	}
}
