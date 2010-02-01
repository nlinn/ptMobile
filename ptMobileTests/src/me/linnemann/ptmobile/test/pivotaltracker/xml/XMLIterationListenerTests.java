package me.linnemann.ptmobile.test.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLIterationListener;
import me.linnemann.ptmobile.test.pivotaltracker.DBAdapterMock;
import me.linnemann.ptmobile.test.pivotaltracker.TestData;
import android.test.AndroidTestCase;

public class XMLIterationListenerTests extends AndroidTestCase {
	
	private XMLIterationListener xil;
	
	public void setUp() {
		DBAdapter db = new DBAdapterMock();
		xil = new XMLIterationListener(db, TestData.ANY_PROJECT_ID , TestData.ANY_ITERATIONGROUP);
	}
	
	public void test_createdlistener_returnsProjectId() {
		assertEquals(TestData.ANY_PROJECT_ID, xil.getProjectId());
	}

	public void test_createdlistener_returnsIterationGroup() {
		assertEquals(TestData.ANY_ITERATIONGROUP, xil.getIterationGroup());
	}

	public void test_createdlistener_returnsIterationNumber() {
		assertNull(xil.getIterationNumber());
	}
}
