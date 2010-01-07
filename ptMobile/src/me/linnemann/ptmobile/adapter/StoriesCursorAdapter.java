package me.linnemann.ptmobile.adapter;

import me.linnemann.ptmobile.R;
import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.cursor.StoriesCursorImpl;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Lifecycle;
import me.linnemann.ptmobile.ui.OutputStyler;
import android.content.Context;
import android.database.Cursor;
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

	public StoriesCursorAdapter(Context context, StoriesCursorImpl c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context ctx, Cursor c) {
		if (view instanceof RelativeLayout) {
			setName(view, ctx, (StoriesCursor) c);
			setLabels(view, ctx, (StoriesCursor) c);
			setTypeImage(view, ctx, (StoriesCursor) c);
			setEstimate(view, ctx, (StoriesCursor) c);
			setDescriptionImage(view,ctx,(StoriesCursor) c);
			setState(view,ctx,(StoriesCursor) c);
			setIteration(view, ctx, (StoriesCursor) c);
			setNextTag(view, ctx, (StoriesCursor) c);
		}
	}


	private void setIteration(View view, Context ctx, StoriesCursor c) {
		TextView tv = (TextView) view.findViewById(R.id.textIterationStory);

		if (c.isIterationStarter()) {

			if (tv != null) {
				tv.setText(OutputStyler.getIterationAsText(c));				
				tv.setVisibility(View.VISIBLE);
			}
		} else {
			tv.setVisibility(View.GONE);
		}
	}

	private void setName(View view, Context ctx, StoriesCursor c) {
		TextView tv = (TextView) view.findViewById(R.id.textNameStory);
		String name = c.getName();
		tv.setText(name);
	}

	private void setLabels(View view, Context ctx, StoriesCursor c) {
		TextView tv = (TextView) view.findViewById(R.id.textLabelsStory);
		String labels = c.getLabels();

		if ((labels != null)  && (labels.length() > 0)) {
			tv.setText(labels);
			tv.setVisibility(View.VISIBLE);
		} else {
			tv.setVisibility(View.INVISIBLE);
		}
	}


	private void setState(View view, Context ctx, StoriesCursor c) {
		String state = c.getCurrentState();

		if ("started".equalsIgnoreCase(state)) {
			view.setBackgroundResource(R.drawable.list_view_selector_feature_started);
		}
		if ("finished".equalsIgnoreCase(state)) {
			view.setBackgroundResource(R.drawable.list_view_selector_feature_started);
		}
		if ("delivered".equalsIgnoreCase(state)) {
			view.setBackgroundResource(R.drawable.list_view_selector_feature_started);
		}
		if ("accepted".equalsIgnoreCase(state)) {
			view.setBackgroundResource(R.drawable.list_view_selector_feature_accepted);
		}
	}

	private void setEstimate(View view, Context ctx, StoriesCursor c) {
		//	TextView tv = (TextView) view.findViewById(R.id.textEstimateStory);
		//	tv.setText(OutputStyler.getEstimateAsText(c));

		ImageView iv = (ImageView) view.findViewById(R.id.imagePointsStory);
		if ((c.getEstimate() != null) && (c.getEstimate() > -2)) {
			iv.setVisibility(View.VISIBLE);
			Drawable d = ctx.getResources().getDrawable(R.drawable.pointsnoestimate);
			if (c.getEstimate() == 0) d = ctx.getResources().getDrawable(R.drawable.points0);
			if (c.getEstimate() == 1) d = ctx.getResources().getDrawable(R.drawable.points1);
			if (c.getEstimate() == 2) d = ctx.getResources().getDrawable(R.drawable.points2);
			if (c.getEstimate() == 3) d = ctx.getResources().getDrawable(R.drawable.points3);
			if (c.getEstimate() == 4) d = ctx.getResources().getDrawable(R.drawable.points4);
			if (c.getEstimate() == 5) d = ctx.getResources().getDrawable(R.drawable.points5);
			if (c.getEstimate() == 8) d = ctx.getResources().getDrawable(R.drawable.points8);
			iv.setImageDrawable(d);
		} else {		
			iv.setVisibility(View.GONE);
		}
	}

	private void setNextTag(View view, Context ctx, StoriesCursor c) {

		ImageView iv = (ImageView) view.findViewById(R.id.imageNextStory);

		Story s = c.getStory();
		Lifecycle lifecycle = s.getLifecycle();

		if (lifecycle.getAvailableTransitions().size() > 0){
			iv.setVisibility(View.VISIBLE);

			if (lifecycle.getAvailableTransitions().get(0).getName().equals("start")) {
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.start));
			}

			if (lifecycle.getAvailableTransitions().get(0).getName().equals("finish")) {
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.finish));
			}

			if (lifecycle.getAvailableTransitions().get(0).getName().equals("deliver")) {
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.deliver));
			}

			if (lifecycle.getAvailableTransitions().get(0).getName().equals("accept")) {
				iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.acceptreject));
			} 

		}else {
			iv.setVisibility(View.INVISIBLE);
		}
	}


	private void setTypeImage(View view, Context ctx, StoriesCursor c) {
		ImageView iv = (ImageView) view.findViewById(R.id.imageTypeStory);
		String type = c.getStoryType();

		if (type.equalsIgnoreCase("feature")) {
			iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.feature));
			view.setBackgroundResource(R.drawable.list_view_selector_feature);
		}
		if (type.equalsIgnoreCase("chore")) {
			iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.chore_icon));
			view.setBackgroundResource(R.drawable.list_view_selector_feature);
		}
		if (type.equalsIgnoreCase("bug")) {
			iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.bug_icon));
			view.setBackgroundResource(R.drawable.list_view_selector_feature);
		}
		if (type.equalsIgnoreCase("release")) {
			iv.setImageDrawable(ctx.getResources().getDrawable(R.drawable.release_icon));
			view.setBackgroundResource(R.drawable.list_view_selector_release);
		}
	}

	private void setDescriptionImage(View view, Context ctx, StoriesCursor c) {
		ImageView iv = (ImageView) view.findViewById(R.id.imageInfosStory);

		if (c.hasDescription()) {
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
