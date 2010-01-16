package me.linnemann.ptmobile;

import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.ui.OutputStyler;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ProjectDetails extends Activity {

	private ProjectsCursor c;
	private PivotalTracker tracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Project");
		setContentView(R.layout.project_details);

		Integer project_id = getIntent().getExtras().getInt("project_id");
		Log.i("ProjectDetails","called for project: "+project_id);
		
		tracker = new PivotalTracker(this);
		c = tracker.getProject(project_id);
		
		tracker.updateStoriesForProject(project_id, "done"); // --- update done stories for velocity
		

		findTextView(R.id.textNamePD).setText(c.getName());
		findTextView(R.id.textIterationLengthPD).setText(OutputStyler.getIterationLengthAsText(c));
		findTextView(R.id.textWeekStartDayPD).setText(c.getWeekStartDay());
		findTextView(R.id.textPointScalePD).setText(c.getPointScale());
		findTextView(R.id.textVelocityPD).setText(OutputStyler.getVelocityAsText(tracker.getVelocityForProject(project_id)));
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (c !=null) c.close();
		if (this.tracker != null) this.tracker.pause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (c !=null) c.close();
		if (this.tracker != null) this.tracker.pause();
	}
	
	private TextView findTextView(int id) {
		return (TextView) this.findViewById(id);
	}
}
