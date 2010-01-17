package me.linnemann.ptmobile;

import java.util.List;

import me.linnemann.ptmobile.cursor.IterationCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.pivotaltracker.value.StringValue;
import me.linnemann.ptmobile.ui.OutputStyler;
import me.linnemann.ptmobile.ui.SimpleErrorDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.Toast;

public class StoryDetails extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "StoryDetails";
	
	private TextView name;
	private TextView type;
	private TextView estimateWidget;
	private TextView descriptionWidget;
	private TextView labels;
	private TextView owner;
	private TextView requested;	
	private TextView deadlineWidget;
	private TextView state;
	private TextView iteration;
	private ImageView image;
	private TextView comments;
	
	
	private Story story;
	private PivotalTracker tracker;
	private Integer story_id;
	private Context ctx;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ctx = this;
		
		setTitle("Story");
		setContentView(R.layout.story_details);

		name = (TextView) this.findViewById(R.id.textNameStoryDetails);
		type = (TextView) this.findViewById(R.id.textTypeStoryDetails);
		labels = (TextView) this.findViewById(R.id.textLabelsStoryDetails);
		estimateWidget = (TextView) this.findViewById(R.id.textEstimateStoryDetails);
		descriptionWidget = (TextView) this.findViewById(R.id.textDescriptionStoryDetails);
		owner = (TextView) this.findViewById(R.id.textOwnedByStoryDetails);
		requested = (TextView) this.findViewById(R.id.textRequestedByStoryDetails);
		deadlineWidget = (TextView) this.findViewById(R.id.textDeadlineStoryDetails);
		state = (TextView) this.findViewById(R.id.textStateStoryDetails);
		image = (ImageView) this.findViewById(R.id.imageTypeStoryDetails);
		iteration = (TextView) this.findViewById(R.id.textIterationStoryDetails);
		comments = (TextView) this.findViewById(R.id.textCommentsSD);
		
		story_id = getIntent().getExtras().getInt("story_id");
	}
	
	private void updateView() {
		
		story = tracker.getStory(story_id);
		
		name.setText(story.getName().getUIString());
		
		type.setText(story.getStoryType().getUIString());
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
		
		showDescription();
		
		state.setText(story.getCurrentState().getUIString());
		
		showEstimateIfNotEmpty();
		
		
		if (!story.getLabels().isEmpty()) {
			labels.setText(story.getLabels().getUIString());
		} else {
			labels.setVisibility(View.INVISIBLE);
		}
		
		showDeadlineIfNotEmpty();
		
		if (!story.getRequestedBy().isEmpty()) {
			requested.setText("Requested by "+story.getRequestedBy().getUIString());
		} else {
			requested.setVisibility(View.INVISIBLE);
		}
		
		if (!story.getOwnedBy().isEmpty()) {
			owner.setText("Owned by "+story.getOwnedBy().getUIString());
		} else {
			owner.setVisibility(View.INVISIBLE);
		}
		
		if (!story.getIterationNumber().isEmpty()) {
			IterationCursor ic = tracker.getIterationCursor(story.getProjectId().getValue(), story.getIterationNumber().getValue());
			iteration.setText(OutputStyler.getIterationAsText(ic));	
			ic.close();
		} else {
			iteration.setVisibility(View.GONE);
		}
		
		comments.setText(tracker.getCommentsAsString(story_id));
		setUpButtons();
	}

	private void showEstimateIfNotEmpty() {
		Estimate estimate = story.getEstimate();
		if (!estimate.isEmpty()) {
			estimateWidget.setText(estimate.getUIString());
		} else {
			estimateWidget.setVisibility(View.INVISIBLE);
		}
	}

	private void showDeadlineIfNotEmpty() {
		StringValue deadline = story.getDeadline();
		if (!deadline.isEmpty()) {
			deadlineWidget.setText("Deadline: "+OutputStyler.getShortDate(deadline.getUIString()));
		} else {
			deadlineWidget.setVisibility(View.INVISIBLE);
		}
	}

	private void showDescription() {
		StringValue description = story.getDescription();
		if (!description.isEmpty()) {
			descriptionWidget.setText(description.getUIString());
		} else {
			descriptionWidget.setText("(no description)");
		}
	}
	
	private void setUpButtons() {
		
		Button btn1,btn2,btn3;
		btn1 = (Button) findViewById(R.id.btn1SD);
		btn2 = (Button) findViewById(R.id.btn2SD);
		btn3 = (Button) findViewById(R.id.btn3SD);
		btn1.setVisibility(View.INVISIBLE);
		btn2.setVisibility(View.INVISIBLE);
		btn3.setVisibility(View.INVISIBLE);
		
		final List<Transition> trans = story.getTransitions();
		
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
					story.applyTransition(trans.get(0));
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
					story.applyTransition(trans.get(1));
					tracker.commitChanges(story);
					updateView();
				}  
			});
			btn2.setVisibility(View.VISIBLE);
		} else {
			btn2.setText("edit");
			btn2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {  
					Intent i = new Intent(ctx, EditStory.class);
					i.putExtra("story_id", story.getId().getValue());
					startActivity(i);
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
		Intent i = new Intent(this,ChangeEstimate.class);
		i.putExtra("story_id", story.getId().getValue());
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
                		
                		try {
                			tracker.addComment(story, edit.getText().toString());
                			Toast toast = Toast.makeText(getApplicationContext(), "update complete", Toast.LENGTH_SHORT);
                			toast.show();
                			updateView();
                		} catch (RuntimeException e) {
                			SimpleErrorDialog errordlg = new SimpleErrorDialog("error adding comment: "+e.getMessage(), ctx);
                			errordlg.show();
                		}	
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
