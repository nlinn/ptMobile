package me.linnemann.ptmobile.test.functional;

import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.Activity;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLActivityListener;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStack;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStackHandler;
import me.linnemann.ptmobile.test.pivotaltracker.DBAdapterMock;
import android.test.AndroidTestCase;
import android.util.Log;

public class XMLActivitiesHandlerTests extends AndroidTestCase {

	private static final String TAG = "XMLActivitiesHandlerTests";
	
	private XMLStackHandler xah;
	private DBAdapterMock db;
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        db = new DBAdapterMock();
        
        XMLActivityListener activitiesListener = new XMLActivityListener(db);
		XMLStack stack = new XMLStack();
		stack.addListener("activities.activity", activitiesListener);
        
        xah = new XMLStackHandler(stack);
    }

    @Override
    protected void tearDown() throws Exception {
       super.tearDown();
       Log.d(TAG, "In tearDown");
    }

    // Test createNote
    public void testActivitiesDataPassedToDB() throws Exception {

    	Activity activity;
    	
    	xah.parse(XMLActivitiesHandlerTests.class.getResourceAsStream("activitiesResponse.xml"));
    	List<Activity> result = db.getActivities();
    	
    	assertTrue(result.size() == 2);	// 2 results expected from demo file
    	
    	// --- first activity
    	activity = result.get(0);
    	assertEquals(new Integer(1031), activity.getId().getValue());
    	assertEquals("Sample Project", activity.getProject().getValue());
    	assertEquals("More power to shields", activity.getStory().getValue());
    	assertEquals("James Kirk accepted \"More power to shields\"", activity.getDescription().getValue());
    	assertEquals("James Kirk", activity.getAuthor().getValue());
    	assertEquals("06/01/2009 08:22 AM", activity.getWhen().getValue());
    	
    	
    	// --- second activity
    	/*
    	activity = result.get(1);
    	assertEquals("1030", cv.getAsString(ActivityData.ID.getDBFieldName()));
    	assertEquals(new Long(1030), cv.getAsLong(ActivityData.ID.getDBFieldName()));
    	assertEquals("Another Sample Project", cv.getAsString(ActivityData.PROJECT.getDBFieldName()));
    	assertEquals("Warp speed", cv.getAsString(ActivityData.STORY.getDBFieldName()));
    	assertEquals("Montgomery Scott rejected \"Warp speend\" with comments: \"Warp speed isn't working\"", cv.getAsString(ActivityData.DESCRIPTION.getDBFieldName()));
    	assertEquals("Montgomery Scott", cv.getAsString(ActivityData.AUTHOR.getDBFieldName()));
    	assertEquals("06/01/2009 12:22 AM", cv.getAsString(ActivityData.WHEN.getDBFieldName()));
    	*/
    }
	
}
