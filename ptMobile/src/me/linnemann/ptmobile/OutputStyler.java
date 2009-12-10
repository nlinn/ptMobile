package me.linnemann.ptmobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.util.Log;

import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursor;

public class OutputStyler {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat out = new SimpleDateFormat("dd MMM");



	public static String getIterationAsText(StoriesCursor c) {
		
		if (c.getIterationNumber() == null) return "";
		
		StringBuilder s = new StringBuilder(c.getIterationNumber());
		s.append(" |  ");
		try {
			s.append(out.format(sdf.parse(c.getIterationStart())));
			s.append(" - ");
			s.append(out.format(sdf.parse(c.getIterationFinish())));		
		} catch (ParseException e) {
			Log.e("OutputStyler",e.getMessage());
		}

		return s.toString();
	}

	// TODO code dupes!
	public static String getIterationAsText(IterationCursor c) {
		
		if (c.getNumber() == null) return "";
		
		StringBuilder s = new StringBuilder(c.getNumber());
		s.append(" |  ");
		try {
			s.append(out.format(sdf.parse(c.getStart())));
			s.append(" - ");
			s.append(out.format(sdf.parse(c.getFinish())));		
		} catch (ParseException e) {
			Log.e("OutputStyler",e.getMessage());
		}

		return s.toString();
	}
	
	public static String getEstimateAsText(StoriesCursor c) {

		String s = "";

		if (c.hasEstimate()) {
			if (c.getEstimate().equals(new Integer(1))) {
				s= c.getEstimate()+" point";
			} else {
				s= c.getEstimate()+" points";
			}
		} else if (c.getStoryType().equalsIgnoreCase("feature")) {
			s= "unestimated";
		}

		return s;
	}

	public static String getIterationLengthAsText(ProjectsCursor c) {

		String s = "";

		if (c.getIterationLength().equals("1")) {
			s= c.getIterationLength()+" week";
		} else {
			s= c.getIterationLength()+" weeks";
		}

		return s;
	}

	public static String getTransitionContextLabel(String transitionName) {
		return transitionName.substring(0, 1).toUpperCase() +
		transitionName.substring(1) + " Story";
	}
	
	public static String getVelocityAsText(int v) {

		String s = "";

		if (v < 1) {
			s= s+" n/a";
		} else if (v == 1) {
			s= s+"1 point";
		} else {
			s= s+v+" points";
		}

		return s;
	}
}
