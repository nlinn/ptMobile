package me.linnemann.ptmobile.ui;

import java.util.Locale;
import java.util.TimeZone;

import android.test.AndroidTestCase;

public class OutputStylerTests  extends AndroidTestCase {

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
	
	public void testActivityPMDateisParsedCorrectly() {
		String input = "12/18/2009 01:04 PM";
		String expectedOutput = "18 Dec 2009, 14:04";
		String formattedOutput = OutputStyler.getDateWithTimeForActivity(input);
		assertEquals(expectedOutput, formattedOutput);
	}
	
	public void testActivityAMDateisParsedCorrectly() {
		String input = "12/18/2009 08:50 AM";
		String expectedOutput = "18 Dec 2009, 09:50";
		String formattedOutput = OutputStyler.getDateWithTimeForActivity(input);
		assertEquals(expectedOutput, formattedOutput);
	}
	
	public void testActivityDate_FallbackIfUnparsable() {
		String input = "not parsable";
		String expectedOutput = "not parsable UTC"; // fallback is input plus " UTC" info.
		String formattedOutput = OutputStyler.getDateWithTimeForActivity(input);
		assertEquals(expectedOutput, formattedOutput);
	}
}