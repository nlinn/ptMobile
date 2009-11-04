package me.linnemann.ptmobile.adapter;

import me.linnemann.ptmobile.OutputStyler;
import me.linnemann.ptmobile.R;
import me.linnemann.ptmobile.cursor.StoriesCursor;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoriesCursorAdapter extends CursorAdapter {

	public StoriesCursorAdapter(Context context, StoriesCursor c) {
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
	}

	private void setEstimate(View view, Context ctx, StoriesCursor c) {
		TextView tv = (TextView) view.findViewById(R.id.textEstimateStory);
		tv.setText(OutputStyler.getEstimateAsText(c));
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
