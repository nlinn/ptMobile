package me.linnemann.ptmobile.test.pivotaltracker;

import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.APIAdapter;
import me.linnemann.ptmobile.pivotaltracker.fields.ActivityField;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLActivitiesHandler;
import android.content.ContentValues;
import android.test.AndroidTestCase;
import android.util.Log;

public class XMLActivitiesHandlerTests extends AndroidTestCase {

	private static final String TAG = "XMLActivitiesHandlerTests";
	
	private XMLActivitiesHandler xah;
	private DBAdapterMock db;
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();

        // Do some initial setup here
        Log.d(TAG, "In setUp");
        db = new DBAdapterMock();
        xah = new XMLActivitiesHandler(db);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Do some clean up here
        Log.d(TAG, "In tearDown");
    }

    // Test createNote
    public void testActivitiesDataPassedToDB() throws Exception {

    	ContentValues cv;
    	
    	xah.go(XMLActivitiesHandlerTests.class.getResourceAsStream("activitiesResponse.xml"));
    	List<ContentValues> result = db.getContentValuesList();
    	
    	assertTrue(result.size() == 2);	// 2 results expected from demo file
    	
    	// --- first activity
    	cv = result.get(0);
    	assertEquals("1031", cv.getAsString(ActivityField.ID.getDBFieldName()));
    	assertEquals(new Long(1031), cv.getAsLong(ActivityField.ID.getDBFieldName()));
    	assertEquals("Sample Project", cv.getAsString(ActivityField.PROJECT.getDBFieldName()));
    	assertEquals("More power to shields", cv.getAsString(ActivityField.STORY.getDBFieldName()));
    	assertEquals("James Kirk accepted \"More power to shields\"", cv.getAsString(ActivityField.DESCRIPTION.getDBFieldName()));
    	assertEquals("James Kirk", cv.getAsString(ActivityField.AUTHOR.getDBFieldName()));
    	assertEquals("06/01/2009 08:22 AM", cv.getAsString(ActivityField.WHEN.getDBFieldName()));
    	
    	// --- second activity
    	cv = result.get(1);
    	assertEquals("1030", cv.getAsString(ActivityField.ID.getDBFieldName()));
    	assertEquals(new Long(1030), cv.getAsLong(ActivityField.ID.getDBFieldName()));
    	assertEquals("Another Sample Project", cv.getAsString(ActivityField.PROJECT.getDBFieldName()));
    	assertEquals("Warp speed", cv.getAsString(ActivityField.STORY.getDBFieldName()));
    	assertEquals("Montgomery Scott rejected \"Warp speend\" with comments: \"Warp speed isn't working\"", cv.getAsString(ActivityField.DESCRIPTION.getDBFieldName()));
    	assertEquals("Montgomery Scott", cv.getAsString(ActivityField.AUTHOR.getDBFieldName()));
    	assertEquals("06/01/2009 12:22 AM", cv.getAsString(ActivityField.WHEN.getDBFieldName()));
    }
	
}
