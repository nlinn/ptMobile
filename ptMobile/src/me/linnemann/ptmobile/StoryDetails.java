package me.linnemann.ptmobile;

import java.util.List;


import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.state.Transition;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class StoryDetails extends Activity {

	private static final String TAG = "StoryDetails";
	
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
	private TextView comments;
	
	
	private StoriesCursor c;
	private PivotalTracker tracker;
	private String story_id;
	
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
		comments = (TextView) this.findViewById(R.id.textCommentsSD);
		
		story_id = getIntent().getExtras().getString("story_id");
	}
	
	private void updateView() {
		
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
		
		if (c.getIterationNumber() != null) {
			IterationCursor ic = tracker.getIterationCursor(c.getProjectId(), c.getIterationNumber());
			iteration.setText(OutputStyler.getIterationAsText(ic));		
		} else {
			iteration.setVisibility(View.GONE);
		}
		
		setComments();
		setUpButtons();
	}
	
	private void setComments() {

		comments.setText(tracker.getCommentsAsString(story_id));
	}
	
	private void setUpButtons() {
		
		Button btn1,btn2,btn3;
		btn1 = (Button) findViewById(R.id.btn1SD);
		btn2 = (Button) findViewById(R.id.btn2SD);
		btn3 = (Button) findViewById(R.id.btn3SD);
		btn1.setVisibility(View.INVISIBLE);
		btn2.setVisibility(View.INVISIBLE);
		btn3.setVisibility(View.INVISIBLE);
		
		final Story s = c.getStory();
		final List<Transition> trans = s.getAvailableTransitions();
		
		if (s.needsEstimate()) {
			btn1.setText("Estimate");
			btn1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {  
					showEstimateActivity();
				}  
			});
			btn1.setVisibility(View.VISIBLE);
		}
		
		if (trans.size() > 0) {
			btn1.setText(trans.get(0).getName());
			btn1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {  
					s.doTransition(trans.get(0));
					tracker.commitChanges(s);
					updateView();
				}  
			});
			btn1.setVisibility(View.VISIBLE);
		}
		
		if (trans.size() > 1) {
			btn2.setText(trans.get(1).getName());
			btn2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {  
					s.doTransition(trans.get(1));
					tracker.commitChanges(s);
					updateView();
				}  
			});
			btn2.setVisibility(View.VISIBLE);
		}
		
		btn3.setText("comment");
		btn3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {  
				showDialog(1);
			}  
		});
		btn3.setVisibility(View.VISIBLE);
	}
	
	private void showEstimateActivity() {
		Intent i = new Intent(this,Estimate.class);
		i.putExtra("story_id", c.getId());
		startActivity(i);
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
	
	@Override
	public void onResume() {
		super.onResume();
		tracker = new PivotalTracker(this);
		updateView();
	}

	protected Dialog onCreateDialog(int id) {
		LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog_comment, null);
        return new AlertDialog.Builder(StoryDetails.this)
        	.setIcon(null)
            .setTitle("Comments")
            .setView(textEntryView)
            .setPositiveButton("save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	
                	EditText edit = (EditText) findViewById(R.id.commentEdit);
                	// TODO edit throws NP... fix it!
                	if (edit.getText().length() > 0) {
                		tracker.addComment(c.getStory(), edit.getText().toString());
                	}
                	
                }
            })
            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // --- do nothing
                }
            })
            .create();
	}
	
	
}
