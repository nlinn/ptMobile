package me.linnemann.ptmobile;

import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.ui.OutputStyler;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ProjectDetails extends Activity {

	private Project project;
	private PivotalTracker tracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Project");
		setContentView(R.layout.project_details);

		Integer project_id = getIntent().getExtras().getInt("project_id");
		Log.i("ProjectDetails","called for project: "+project_id);
		
		tracker = new PivotalTracker(this);
		project = tracker.getProject(project_id);
		
		findTextView(R.id.textNamePD).setText(project.getName().getUIString());
		findTextView(R.id.textIterationLengthPD).setText(OutputStyler.getIterationLengthAsText(project));
		findTextView(R.id.textWeekStartDayPD).setText(project.getWeekStartDay().getUIString());
		findTextView(R.id.textPointScalePD).setText(project.getPointScale().getUIString());
		findTextView(R.id.textVelocityPD).setText(OutputStyler.getVelocityAsText(project));
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (this.tracker != null) this.tracker.pause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (this.tracker != null) this.tracker.pause();
	}
	
	private TextView findTextView(int id) {
		return (TextView) this.findViewById(id);
	}
}
