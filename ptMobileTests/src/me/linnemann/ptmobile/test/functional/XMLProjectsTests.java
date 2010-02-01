package me.linnemann.ptmobile.test.functional;

import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLProjectsListener;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStack;
import me.linnemann.ptmobile.pivotaltracker.xml.XMLStackHandler;
import me.linnemann.ptmobile.test.pivotaltracker.DBAdapterMock;
import android.content.ContentValues;
import android.test.AndroidTestCase;

public class XMLProjectsTests extends AndroidTestCase {

	private static final String TAG = "ProjectsXMLTests";

	private XMLStackHandler xah;
	private DBAdapterMock db;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		db = new DBAdapterMock();

		XMLProjectsListener projectListener = new XMLProjectsListener(db);
		XMLStack stack = new XMLStack();
		stack.addListener("projects.project", projectListener);

		xah = new XMLStackHandler(stack);
	}

	public void test_parseProject_rightSize() throws Exception {
		xah.parse(XMLActivitiesHandlerTests.class.getResourceAsStream("projectsResponse.xml"));
		List<Project> projects = db.getProjects();

		assertEquals(6, projects.size());	// results expected from demo file
	}
	
	public void test_firstProject_correctCurrentVelocity() {
		Project project = getFirstProject();
		assertEquals(new Integer(16), project.getCurrentVelocity().getValue());
	}
	
	private Project getFirstProject() {
		xah.parse(XMLActivitiesHandlerTests.class.getResourceAsStream("projectsResponse.xml"));
		List<Project> projects = db.getProjects();
		return projects.get(0);
	}
	
	public void test_firstProject_correctName() {
		Project project = getFirstProject();
		assertEquals("BaR", project.getName().getValueAsString());
	}
	
	public void test_firstProject_correctWeekStartDay() {
		Project project = getFirstProject();
		assertEquals("Monday", project.getWeekStartDay().getValueAsString());
	}

	public void test_firstProject_correctId() {
		Project project = getFirstProject();
		assertEquals(new Integer(48422), project.getId().getValue());
	}

	public void test_firstProjectIterationLength() {
		Project project = getFirstProject();
		assertEquals("2", project.getIterationLength().getValueAsString()); // TODO convert to IntValue
	}

	public void test_firstProjectPointScale() {
		Project project = getFirstProject();
		assertEquals("0,1,2,3,5,8", project.getPointScale().getValueAsString()); 
	}	

	public void test_firstProjectLabels() {
		Project project = getFirstProject();
		String labels = "(poe),account,agg,autotopup,beta,data,deb,delta,freeze,fwf,gamma,invoice,mantopup,mbilling,misc,order,portout,replacement,tkg";
		assertEquals(labels, project.getLabels().getValueAsString());
	}
}