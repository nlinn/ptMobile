package me.linnemann.ptmobile.pivotaltracker.value;


public enum State implements TrackerValue  {

	UNSCHEDULED("Not Yet Scheduled"), UNSTARTED("Not Yet Started"), STARTED("Started"), FINISHED("Finished"),
	DELIVERED("Delivered"), ACCEPTED("Accepted"), REJECTED("Rejected");

	private String beautifiedName;
	
	State(String beautifiedName) {
		this.beautifiedName = beautifiedName;
	}
	
	public static State valueOfBeautifiedString(String beautified) {
		for (State toCheck : State.values()) {
			if (toCheck.beautifiedName.equalsIgnoreCase(beautified)) {
				return toCheck;
			}
		}
		throw new RuntimeException("Unable to find State for String: "+beautified);
	}
	
	public String getUIString() {
		return beautifiedName;
	}

	public String getValueAsString() {
		return name().toLowerCase();
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
