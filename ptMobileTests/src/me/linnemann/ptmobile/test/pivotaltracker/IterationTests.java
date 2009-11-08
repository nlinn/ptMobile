package me.linnemann.ptmobile.test.pivotaltracker;

import android.test.AndroidTestCase;
import android.util.Log;

public class IterationTests extends AndroidTestCase {

	private static final String TAG = "IterationTests";
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();

        // Do some initial setup here
        Log.d(TAG, "In setUp");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Do some clean up here
        Log.d(TAG, "In tearDown");
    }

    // Test createNote
    public void testCreateNote() {
        assertTrue("default failure", 0 == 0);
    }
	
}
