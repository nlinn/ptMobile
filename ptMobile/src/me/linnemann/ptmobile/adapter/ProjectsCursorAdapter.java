package me.linnemann.ptmobile.adapter;

import me.linnemann.ptmobile.OutputStyler;
import me.linnemann.ptmobile.R;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.pivotaltracker.PivotalTracker;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProjectsCursorAdapter extends CursorAdapter {

	public ProjectsCursorAdapter(Context context, ProjectsCursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		if (view instanceof LinearLayout) {
			((TextView) view.findViewById(R.id.textProject)).setText( ((ProjectsCursor) cursor).getName());

			PivotalTracker tracker = new PivotalTracker(context);
			int vel = tracker.getVelocityForProject(((ProjectsCursor) cursor).getId());
			tracker.pause();
			((TextView) view.findViewById(R.id.textVelocityPL)).setText(OutputStyler.getVelocityAsText(vel));
		}

		  
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		   final LayoutInflater inflater = LayoutInflater.from(context); 
           
		   final View row = inflater.inflate (R.layout.projects_row, parent, false);
		   //Log.i("newView","newView_1");
		   //final TextView view = (TextView) row.findViewById(R.id.textProject); 
		   //Log.i("newView","newView_2");
           //view.setText( ((ProjectsCursor) cursor).getName()); 
           //Log.i("newView","newView_ok");
           return row; 
	}

}
