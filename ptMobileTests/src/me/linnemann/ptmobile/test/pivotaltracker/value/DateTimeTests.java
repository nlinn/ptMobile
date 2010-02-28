package me.linnemann.ptmobile.test.pivotaltracker.value;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import me.linnemann.ptmobile.pivotaltracker.value.DateTime;

public class DateTimeTests extends TestCase {

	private TimeZone systemDefaultTimeZone;
	private Locale systemDefaultLocale;
	
	public void setUp() {
		systemDefaultTimeZone = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
		systemDefaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.US);
	}
	
	public void tearDown() {
		TimeZone.setDefault(systemDefaultTimeZone);
		Locale.setDefault(systemDefaultLocale);
	}
	
	public void test_dateTimeCreated_pivotalUTCFormat() {
		
		DateTime dt = new DateTime("2009/06/07 12:13:35 UTC");
		
		assertFalse(dt.isEmpty());
		assertEquals("07 Jun 2009, 13:13", dt.getUIString());
		assertEquals("07 Jun 2009", dt.getUIStringShortDate());
		assertEquals("2009/06/07 12:13:35 UTC", dt.getValueAsString());
		
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.GERMAN);
		c.set(2009,Calendar.JUNE,7,12,13,35);
		
		assertEquals(c.getTime().toString(), dt.getValue().toString());
		
	}
}
