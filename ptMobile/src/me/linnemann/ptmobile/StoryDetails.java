package me.linnemann.ptmobile;

import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StoryDetails extends Activity {

	private TextView name;
	private TextView type;
	private TextView estimate;
	private TextView description;
	private TextView labels;
	private TextView owner;
	private TextView requested;	
	private TextView deadline;
	private TextView state;
	private TextView iteration;
	private ImageView image;
	
	
	private StoriesCursor c;
	private PivotalTracker tracker;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("Story");
		setContentView(R.layout.story_details);

		name = (TextView) this.findViewById(R.id.textNameStoryDetails);
		type = (TextView) this.findViewById(R.id.textTypeStoryDetails);
		labels = (TextView) this.findViewById(R.id.textLabelsStoryDetails);
		estimate = (TextView) this.findViewById(R.id.textEstimateStoryDetails);
		description = (TextView) this.findViewById(R.id.textDescriptionStoryDetails);
		owner = (TextView) this.findViewById(R.id.textOwnedByStoryDetails);
		requested = (TextView) this.findViewById(R.id.textRequestedByStoryDetails);
		deadline = (TextView) this.findViewById(R.id.textDeadlineStoryDetails);
		state = (TextView) this.findViewById(R.id.textStateStoryDetails);
		image = (ImageView) this.findViewById(R.id.imageTypeStoryDetails);
		iteration = (TextView) this.findViewById(R.id.textIterationStoryDetails);
		
		String story_id = getIntent().getExtras().getString("story_id");
		Log.i("StoryDetails","called for story: "+story_id);
		
		tracker = new PivotalTracker(this);
		
		c = tracker.getStory(story_id);
		
		name.setText(c.getName());
		
		type.setText(c.getStoryType());
		if (c.getStoryType().equalsIgnoreCase("feature")) {
			image.setImageDrawable(getResources().getDrawable(R.drawable.feature));
		}
		if (c.getStoryType().equalsIgnoreCase("chore")) {
			image.setImageDrawable(getResources().getDrawable(R.drawable.chore_icon));
		}
		if (c.getStoryType().equalsIgnoreCase("release")) {
			image.setImageDrawable(getResources().getDrawable(R.drawable.release_icon));
		}
		if (c.getStoryType().equalsIgnoreCase("bug")) {
			image.setImageDrawable(getResources().getDrawable(R.drawable.bug_icon));
		}
		
		if (c.hasDescription()) {
			description.setText(c.getDescription());
		} else {
			description.setText("(no description)");
		}
		
		state.setText(c.getCurrentState());
		estimate.setText(OutputStyler.getEstimateAsText(c));
		
		
		if (c.hasLabels()) {
			labels.setText(c.getLabels());
		} else {
			labels.setVisibility(View.INVISIBLE);
		}
		
		if (c.hasDeadline()) {
			deadline.setText("Deadline: "+c.getDeadline());
		} else {
			deadline.setVisibility(View.INVISIBLE);
		}
		
		if (c.hasRequestedBy()) {
			requested.setText("Requested by "+c.getRequestedBy());
		} else {
			requested.setVisibility(View.INVISIBLE);
		}
		
		if (c.hasOwnedBy()) {
			owner.setText("Owned by "+c.getOwnedBy());
		} else {
			owner.setVisibility(View.INVISIBLE);
		}
		
		iteration.setText(OutputStyler.getIterationAsText(c));
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (c !=null) c.close();
		if (this.tracker != null) this.tracker.pause();
	}
	
	@Override
	public void onDestroy() {
		super.onStop();
		if (c !=null) c.close();
		if (this.tracker != null) this.tracker.pause();
	}
}
