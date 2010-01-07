package me.linnemann.ptmobile;

import java.util.List;


import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.StoryType;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.ui.OutputStyler;
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
import android.widget.TextView;

public class StoryDetails extends Activity {

	@SuppressWarnings("unused")
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
	
	
	private Story story;
	private PivotalTracker tracker;
	private Integer story_id;
	
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
		
		story_id = getIntent().getExtras().getInt("story_id");
	}
	
	private void updateView() {
		
		story = tracker.getStory(story_id);
		
		name.setText(story.getName());
		
		type.setText(story.getStoryType().toString());
		if (StoryType.FEATURE.equals(story.getStoryType())) {
			image.setImageDrawable(getResources().getDrawable(R.drawable.feature));
		}
		if (StoryType.CHORE.equals(story.getStoryType())) {
			image.setImageDrawable(getResources().getDrawable(R.drawable.chore_icon));
		}
		if (StoryType.RELEASE.equals(story.getStoryType())) {
			image.setImageDrawable(getResources().getDrawable(R.drawable.release_icon));
		}
		if (StoryType.BUG.equals(story.getStoryType())) {
			image.setImageDrawable(getResources().getDrawable(R.drawable.bug_icon));
		}
		
		description.setText(OutputStyler.getDescriptionText(story));
		
		state.setText(story.getCurrentState().toString());
		estimate.setText(OutputStyler.getEstimateText(story));
		
		
		if (hasLabels(story)) {
			labels.setText(story.getLabels());
		} else {
			labels.setVisibility(View.INVISIBLE);
		}
		
		if (hasDeadline(story)) {
			deadline.setText("Deadline: "+OutputStyler.getShortDate(story.getDeadline()));
		} else {
			deadline.setVisibility(View.INVISIBLE);
		}
		
		if (hasRequestedBy(story)) {
			requested.setText("Requested by "+story.getRequestedBy());
		} else {
			requested.setVisibility(View.INVISIBLE);
		}
		
		if (hasOwnedBy(story)) {
			owner.setText("Owned by "+story.getOwnedBy());
		} else {
			owner.setVisibility(View.INVISIBLE);
		}
		
		if (story.getIterationNumber() != null) {
			IterationCursor ic = tracker.getIterationCursor(story.getProjectId(), story.getIterationNumber());
			iteration.setText(OutputStyler.getIterationAsText(ic));	
			ic.close();
		} else {
			iteration.setVisibility(View.GONE);
		}
		
		comments.setText(tracker.getCommentsAsString(story_id));
		setUpButtons();
	}
	
	private boolean hasLabels(Story story) {
		String labels = story.getLabels();
		return notEmpty(labels);
	}

	private boolean hasDeadline(Story story) {
		String deadline = story.getDeadline();
		return notEmpty(deadline);
	}
	
	private boolean hasRequestedBy(Story story) {
		String requestedBy = story.getRequestedBy();
		return notEmpty(requestedBy);
	}
	
	private boolean hasOwnedBy(Story story) {
		String ownedBy = story.getOwnedBy();
		return notEmpty(ownedBy);
	}
	
	private boolean notEmpty(String value) {
		return ((value != null) && (value.length() > 0));
	}
	
	private void setUpButtons() {
		
		Button btn1,btn2,btn3;
		btn1 = (Button) findViewById(R.id.btn1SD);
		btn2 = (Button) findViewById(R.id.btn2SD);
		btn3 = (Button) findViewById(R.id.btn3SD);
		btn1.setVisibility(View.INVISIBLE);
		btn2.setVisibility(View.INVISIBLE);
		btn3.setVisibility(View.INVISIBLE);
		
		//final Story s = c.getStory();
		final List<Transition> trans = story.getLifecycle().getAvailableTransitions();
		
		if (story.needsEstimate()) {
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
					story.getLifecycle().doTransition(trans.get(0));
					tracker.commitChanges(story);
					updateView();
				}  
			});
			btn1.setVisibility(View.VISIBLE);
		}
		
		if (trans.size() > 1) {
			btn2.setText(trans.get(1).getName());
			btn2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {  
					story.getLifecycle().doTransition(trans.get(1));
					tracker.commitChanges(story);
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
		i.putExtra("story_id", story.getId());
		startActivity(i);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (this.tracker != null) this.tracker.pause();
	}
	
	@Override
	public void onDestroy() {
		super.onStop();
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
                	EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.commentEdit);
                	
                	if (edit.getText().length() > 0) {
                		// TODO: response may be "unsuccessful", error msg?
                		tracker.addComment(story, edit.getText().toString());
                		updateView();
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
