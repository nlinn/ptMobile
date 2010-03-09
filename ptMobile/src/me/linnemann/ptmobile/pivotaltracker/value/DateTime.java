package me.linnemann.ptmobile.pivotaltracker.value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class DateTime implements TrackerValue {

	private Date date;
	private String original_date;
	private static SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss 'UTC'");
	private static SimpleDateFormat LOCAL_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static SimpleDateFormat OUT_FORMAT_UI = new SimpleDateFormat("dd MMM yyyy, HH:mm");
	private static SimpleDateFormat OUT_FORMAT_UI_SHORT = new SimpleDateFormat("dd MMM yyyy");
	
	static {
		Calendar cal = Calendar.getInstance();
		OUT_FORMAT_UI.setTimeZone(cal.getTimeZone());
		OUT_FORMAT_UI_SHORT.setTimeZone(cal.getTimeZone());
		UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
		LOCAL_FORMAT.setTimeZone(cal.getTimeZone());
	}
	
	public static DateTime getEmptyValue() {
		return new DateTime();
	}
	
	private DateTime() {
		original_date = "";
		date = null;
	}
	
	public DateTime(String from) {
		try {
			if (from.indexOf("UTC") > 1) {
				parseUTCFormat(from);
			} else {
				parseLocalFormat(from);
			}
			original_date = from.trim();
		} catch (ParseException e) {
			throw(new RuntimeException("Cannot parse date: "+from));
		}
	}
	
	private void parseUTCFormat(String from) throws ParseException {
		Log.v("DateTime","parsing UTC format");
		date = UTC_FORMAT.parse(from.trim());
	}
	
	private void parseLocalFormat(String from) throws ParseException {
		Log.v("DateTime","parsing local format: "+from.substring(0,19));
		date = LOCAL_FORMAT.parse(from.substring(0,19));
	}
	

	public String getUIString() {
		if (isEmpty()) {
			return "";
		} else {
			return OUT_FORMAT_UI.format(date);
		}
	}

	public String getUIStringShortDate() {
		if (isEmpty()) {
			return "";
		} else {
			return OUT_FORMAT_UI_SHORT.format(date);
		}
	}

	
	public Object getValue() {
		return date;
	}

	public String getValueAsString() {
		if (isEmpty()) {
			return "";
		} else {
			return original_date;
		}
	}

	public boolean isEmpty() {
		return (date == null);
	}

	public String getXMLWrappedValue(String xmlTag) {
		if (isEmpty()) {
			return "";
		} else {
			return "<" + xmlTag + " type=\"datetime\">" + getValueAsString() + "</"+xmlTag+">";
		}
	}
}