package me.linnemann.ptmobile.adapter;

import me.linnemann.ptmobile.R;
import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import me.linnemann.ptmobile.ui.OutputStyler;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * StoriesCursorAdapter populates ListView with views per row
 * Views are filled with data from StoriesCursor
 * 
 * @author nlinn
 *
 */
public class StoriesCursorAdapter extends CursorAdapter {

	//private static final String TAG = "StoriesCursorAdapter";
	
	public StoriesCursorAdapter(Context context, StoriesCursorImpl c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context ctx, Cursor c) {
		
		Story story = ((StoriesCursor) c).getStory();
		
		if (view instanceof RelativeLayout) {
			setName(view, ctx, story);
			setLabels(view, ctx, story);
			setStoryTypeIcon(view, ctx, story);
			setEstimate(view, ctx, story);
			setDescriptionImage(view,ctx, story);
			setBackground(view,ctx, story);
			setIteration(view, ctx, story, (StoriesCursor) c);
			setNextTag(view, ctx, story);
		}
	}


	private void setIteration(View view, Context ctx, Story story, StoriesCursor c) {
		TextView tv = (TextView) view.findViewById(R.id.textIterationStory);
	
		if ((!story.getIterationGroup().getValue().equalsIgnoreCase("icebox")) &&
		 (story.isFirstInIteration())) {
			if (tv != null) {
				tv.setText(OutputStyler.getIterationAsText(c));				
				tv.setVisibility(View.VISIBLE);
			}
		} else {
			tv.setVisibility(View.GONE);
		}
	}

	private void setName(View view, Context ctx, Story story) {
		TextView tv = (TextView) view.findViewById(R.id.textNameStory);
		tv.setText(story.getName().getUIString());
	}

	private void setLabels(View view, Context ctx, Story story) {
		TextView tv = (TextView) view.findViewById(R.id.textLabelsStory);
		Text labels = story.getLabels();

		if (!labels.isEmpty()) {
			tv.setText(labels.getUIString());
			tv.setVisibility(View.VISIBLE);
		} else {
			tv.setVisibility(View.INVISIBLE);
		}
	}

	private void setBackground(View view, Context ctx, Story story) {
		final State state = story.getCurrentState();

		switch(state) {
			case STARTED:
				view.setBackgroundResource(R.drawable.list_view_selector_feature_started);
				break;
			case FINISHED:
				view.setBackgroundResource(R.drawable.list_view_selector_feature_started);
				break;
			case DELIVERED:
				view.setBackgroundResource(R.drawable.list_view_selector_feature_started);
				break;
			case ACCEPTED:
				view.setBackgroundResource(R.drawable.list_view_selector_feature_accepted);
				break;
			case UNSCHEDULED:
				view.setBackgroundResource(R.drawable.list_view_selector_feature_unscheduled);
				break;
			default:
				view.setBackgroundResource(R.drawable.list_view_selector_feature);
		}
		
		// -- no matter what state, release is always same color
		TextView tv = (TextView) view.findViewById(R.id.textNameStory);
		if (StoryType.RELEASE.equals(story.getStoryType())) {
			view.setBackgroundResource(R.drawable.list_view_selector_release);
			tv.setTextColor(Color.WHITE);
		} else {
			tv.setTextColor(Color.BLACK);
		}
	}

	private void setEstimate(View view, Context ctx, Story story) {

		Estimate estimate = story.getEstimate();
		
		ImageView iv = (ImageView) view.findViewById(R.id.imagePointsStory);
		if (!Estimate.NO_ESTIMATE.equals(estimate)) {
			iv.setVisibility(View.VISIBLE);
			Drawable d = ctx.getResources().getDrawable(R.drawable.pointsnoestimate);
			if (Estimate.POINTS_0.equals(estimate)) d = ctx.getResources().getDrawable(R.drawable.points0);
			if (Estimate.POINTS_1.equals(estimate)) d = ctx.getResources().getDrawable(R.drawable.points1);
			if (Estimate.POINTS_2.equals(estimate)) d = ctx.getResources().getDrawable(R.drawable.points2);
			if (Estimate.POINTS_3.equals(estimate)) d = ctx.getResources().getDrawable(R.drawable.points3);
			if (Estimate.POINTS_4.equals(estimate)) d = ctx.getResources().getDrawable(R.drawable.points4);
			if (Estimate.POINTS_5.equals(estimate)) d = ctx.getResources().getDrawable(R.drawable.points5);
			if (Estimate.POINTS_8.equals(estimate)) d = ctx.getResources().getDrawable(R.drawable.points8);
			iv.setImageDrawable(d);
		} else {		
			iv.setVisibility(View.GONE);
		}
	}

	private void setNextTag(View view, Context ctx, Story story) {

		ImageView iv = (ImageView) view.findViewById(R.id.imageNextStory);
		
		if (story.getTransitions().size() > 0){
			iv.setVisibility(View.VISIBLE);

			if (story.getTransitions().get(0).getName().equals("start")) {
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.start));
			}

			if (story.getTransitions().get(0).getName().equals("finish")) {
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.finish));
			}

			if (story.getTransitions().get(0).getName().equals("deliver")) {
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.deliver));
			}

			if (story.getTransitions().get(0).getName().equals("accept")) {
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.acceptreject));
			} 
			
			if (story.getTransitions().get(0).getName().equals("restart")) {
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.restart));
			} 

		}else {
			iv.setVisibility(View.INVISIBLE);
		}
	}


	private void setStoryTypeIcon(View view, Context ctx, Story story) {
		ImageView iv = (ImageView) view.findViewById(R.id.imageTypeStory);
		final StoryType type = story.getStoryType();

		switch(type) {
			case FEATURE:
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.feature));
				break;
			case CHORE:
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.chore_icon));
				break;
			case BUG:
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.bug_icon));
				break;
			case RELEASE:
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.release_icon));
				break;
		}		
	}

	private void setDescriptionImage(View view, Context ctx, Story story) {
		ImageView iv = (ImageView) view.findViewById(R.id.imageInfosStory);
		Text description = story.getDescription();
		
		if (!description.isEmpty()) {
			iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.story_flyover_icon));
			iv.setVisibility(View.VISIBLE);
		} else {
			iv.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context); 
		return inflater.inflate (R.layout.stories_row_with_header, parent, false); 
	}

}
