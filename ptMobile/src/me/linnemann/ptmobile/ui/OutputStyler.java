package me.linnemann.ptmobile.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.Story;

public class OutputStyler {

	private static SimpleDateFormat from_db = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//12/12/2009 03:09 PM
	private static SimpleDateFormat from_db_activity = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
	private static SimpleDateFormat out_w_time = new SimpleDateFormat("dd MMM yyyy, HH:mm");
	private static SimpleDateFormat out = new SimpleDateFormat("dd MMM yyyy");

	static {
		Calendar cal = Calendar.getInstance();
		
		out_w_time.setTimeZone(cal.getTimeZone());
		from_db.setTimeZone(TimeZone.getTimeZone("UTC"));
		from_db_activity.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public static String getIterationAsText(StoriesCursor c) {
		
		if (c.getIterationNumber() == null) return "";
		
		StringBuilder s = new StringBuilder();
		s.append(c.getIterationNumber());
		s.append(" |  ");
		try {
			s.append(out.format(from_db.parse(c.getIterationStart())));
			s.append(" - ");
			s.append(out.format(from_db.parse(c.getIterationFinish())));		
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
			s.append(out.format(from_db.parse(c.getStart())));
			s.append(" - ");
			s.append(out.format(from_db.parse(c.getFinish())));		
		} catch (ParseException e) {
			Log.e("OutputStyler",e.getMessage());
		}

		return s.toString();
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
	
	public static String getCommentAsText(String text, String author, String noted_at) {
	
		try {
			return text + "\n" + author + ", "+ out_w_time.format(from_db.parse(noted_at));
		} catch (ParseException e) {
			Log.e("OutputStyler",e.getMessage());
			return "";
		}
	}
	
	/**
	 * 
	 * @param in activity style date (API v2 specific?)
	 * @return formatted date with time in local timezone
	 */
	public static String getDateWithTimeForActivity(String in) {
		try {
			return out_w_time.format(from_db_activity.parse(in));
		} catch (ParseException e) {
			Log.e("OutputStyler getDateWithTimeForActivity",e.getMessage());
			// --- i had strange problems with my galaxy parsing dates (emulator was fine)
			// --- so i return the original date with "UTC" info (this sucks)
			return in + " UTC"; 
		}
	}
	
	public static String getShortDate(String in) {
		try {
			return out.format(from_db.parse(in));
		} catch (ParseException e) {
			Log.e("OutputStyler getShortDate",e.getMessage());
			// --- i had strange problems with my galaxy parsing dates (emulator was fine)
			// --- so i return the original date with "UTC" info (this sucks)
			return in + " UTC";
		}
	}
}
