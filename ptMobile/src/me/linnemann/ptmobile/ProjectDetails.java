package me.linnemann.ptmobile;

import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ProjectDetails extends Activity {

	private ProjectsCursor c;
	private PivotalTracker tracker;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Project");
		setContentView(R.layout.project_details);

		String project_id = getIntent().getExtras().getString("project_id");
		Log.i("ProjectDetails","called for project: "+project_id);
		
		tracker = new PivotalTracker(this);
		c = tracker.getProject(project_id);
		
		findTextView(R.id.textNamePD).setText(c.getName());
		findTextView(R.id.textIterationLengthPD).setText(OutputStyler.getIterationLengthAsText(c));
		findTextView(R.id.textWeekStartDayPD).setText(c.getWeekStartDay());
		findTextView(R.id.textPointScalePD).setText(c.getPointScale());
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
