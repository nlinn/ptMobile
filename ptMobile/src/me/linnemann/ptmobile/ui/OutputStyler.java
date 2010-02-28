package me.linnemann.ptmobile.ui;

import me.linnemann.ptmobile.pivotaltracker.Project;

public class OutputStyler {

	public static String getIterationLengthAsText(Project project) {

		String s = "";
		String iterationLength = project.getIterationLength().getValueAsString();

		if (iterationLength.equals("1")) {
			s= iterationLength+" week";
		} else {
			s= iterationLength+" weeks";
		}

		return s;
	}

	public static String getTransitionContextLabel(String transitionName) {
		return transitionName.substring(0, 1).toUpperCase() +
		transitionName.substring(1) + " Story";
	}

	public static String getVelocityAsText(Project project) {
		String s = "";
		Integer v = project.getCurrentVelocity().getValue();

		if ((v == null) || (v < 1)) {
			s= s+" n/a";
		} else if (v == 1) {
			s= s+"1 point";
		} else {
			s= s+v+" points";
		}

		return s;
	}
}