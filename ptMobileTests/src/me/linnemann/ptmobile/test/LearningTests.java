package me.linnemann.ptmobile.test;

import android.test.AndroidTestCase;

public class LearningTests extends AndroidTestCase {

	public void testReplaceCorrectPattern() {
	
		String testString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>This project requires SSL access";
	
		String output = testString.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
		assertEquals("This project requires SSL access",output);
	}

	
}
