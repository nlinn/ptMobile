package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.pivotaltracker.fields.ProjectDataType;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import android.test.AndroidTestCase;

public class ProjectTests extends AndroidTestCase {

	public void test_protocol_https() {
		Project project = Project.emptyProject();
		project.putDataAndTrackChanges(ProjectDataType.USE_HTTPS, new Text("true"));
		String protocol = project.getProtocol();
		assertEquals("https://", protocol);
	}

	public void test_protocol_http() {
		Project project = Project.emptyProject();
		project.putDataAndTrackChanges(ProjectDataType.USE_HTTPS, new Text("false"));
		String protocol = project.getProtocol();
		assertEquals("http://", protocol);
	}

}
