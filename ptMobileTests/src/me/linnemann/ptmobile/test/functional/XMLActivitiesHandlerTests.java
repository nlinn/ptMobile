package me.linnemann.ptmobile.test.functional;

import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.Activity;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;
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

    public void testActivitiesDataPassedToDB() throws Exception {
    	Activity activity;
    	
    	xah.parse(XMLActivitiesHandlerTests.class.getResourceAsStream("activitiesV3Response.xml"));
    	List<TrackerEntity> result = db.getEntities();
    	
    	assertEquals(new Integer(10), new Integer(result.size()));
    	
    	// --- first activity
    	activity = (Activity) result.get(0);
    	assertEquals(new Integer(13813364), activity.getId().getValue());
    	assertEquals(new Integer(48422), activity.getProjectId().getValue());
    	assertEquals(new Integer(782), activity.getVersion().getValue());
    	assertEquals("2010/02/28 15:29:37 UTC", activity.getOccuredAt().getValueAsString());
    	assertEquals("Niels Linnemann accepted \"Im CS-Client benutzen alle existierenden Webflows den internen Browser\"", activity.getDescription().getValue());
    	assertEquals("Niels Linnemann", activity.getAuthor().getValue());
    	assertEquals("story_update", activity.getEventType().getValue());
    }
	
}
