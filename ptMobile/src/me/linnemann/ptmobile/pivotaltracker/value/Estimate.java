package me.linnemann.ptmobile.pivotaltracker.value;

public enum Estimate implements TrackerValue {

	NO_ESTIMATE(-2, "No estimate"), UNESTIMATED(-1, "Unestimated"), POINTS_0(0, "0 points"), POINTS_1(1,"1 point"), 
	POINTS_2(2,"2 points"), POINTS_3(3,"3 points"), POINTS_4(4,"4 points"),
	POINTS_5(5,"5 points"), POINTS_8(8,"8 points");
	
	private final Integer numeric;
	private String beautifiedString;
	
	Estimate(Integer numeric, String beautifiedString) {
		this.numeric = numeric;
		this.beautifiedString = beautifiedString;
	}
	
	public String getValueAsString() {
		return numeric.toString();
	}
	
	public static Estimate valueOfBeautifiedString(String beautifiedString) {
		for (Estimate toCheck : Estimate.values()) {
			if (toCheck.beautifiedString.equalsIgnoreCase(beautifiedString)) {
				return toCheck;
			}
		}
		throw new RuntimeException("Unable to find Estimate for String: "+beautifiedString);
	}
	
	public static Estimate valueOfNumeric(Integer numeric) {
		for (Estimate toCheck : Estimate.values()) {
			if (toCheck.numeric.equals(numeric)) {
				return toCheck;
			}
		}
		throw new RuntimeException("Unable to find Estimate for Integer: "+numeric);
	}

	public String getUIString() {
		return beautifiedString;
	}

	public Object getValue() {
		return this;
	}

	public boolean isEmpty() {
		return this.equals(NO_ESTIMATE);
	}
	
	public String getXMLWrappedValue(String xmlTag) {
		if (!this.equals(NO_ESTIMATE)) {
			return "<" + xmlTag + " type=\"integer\">" + getValueAsString() + "</"+xmlTag+">";
		} else {
			return "";
		}
	}
}
