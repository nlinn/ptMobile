package me.linnemann.ptmobile.pivotaltracker.value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class DateTime implements TrackerValue {

	private Date date;
	private static SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss 'UTC'");
	private static SimpleDateFormat OUT_FORMAT_UI = new SimpleDateFormat("dd MMM yyyy, HH:mm");
	private static SimpleDateFormat OUT_FORMAT_UI_SHORT = new SimpleDateFormat("dd MMM yyyy");
	
	static {
		Calendar cal = Calendar.getInstance();
		OUT_FORMAT_UI.setTimeZone(cal.getTimeZone());
		OUT_FORMAT_UI_SHORT.setTimeZone(cal.getTimeZone());
		UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	public static DateTime getEmptyValue() {
		return new DateTime();
	}
	
	private DateTime() {
	}
	
	public DateTime(String from) {
		try {
			date = UTC_FORMAT.parse(from.trim());
			Log.v("DateTime",date.toLocaleString());
		} catch (ParseException e) {
			throw(new RuntimeException("Cannot parse date: "+from));
		}
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
			return UTC_FORMAT.format(date);
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
