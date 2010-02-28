package me.linnemann.ptmobile.adapter;

import me.linnemann.ptmobile.R;
import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import me.linnemann.ptmobile.pivotaltracker.Activity;
import me.linnemann.ptmobile.pivotaltracker.Project;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivitiesCursorAdapter extends CursorAdapter {

	public ActivitiesCursorAdapter(Context context, ActivitiesCursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		if (view instanceof LinearLayout) {
			Activity activity = ((ActivitiesCursor) cursor).getActivity();
			Project project = activity.getProject();
			
			((TextView) view.findViewById(R.id.textProjectAL)).setText(project.getName().getUIString());
			((TextView) view.findViewById(R.id.textDescriptionAL)).setText(activity.getDescription().getUIString());
			((TextView) view.findViewById(R.id.textWhenAL)).setText(activity.getOccuredAt().getUIString());
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		   final LayoutInflater inflater = LayoutInflater.from(context); 
		   final View row = inflater.inflate (R.layout.activities_row, parent, false);
           return row; 
	}
}