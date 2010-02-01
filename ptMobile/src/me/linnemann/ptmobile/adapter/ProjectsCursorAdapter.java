package me.linnemann.ptmobile.adapter;

import me.linnemann.ptmobile.R;
import me.linnemann.ptmobile.cursor.ProjectsCursor;
import me.linnemann.ptmobile.pivotaltracker.Project;
import me.linnemann.ptmobile.ui.OutputStyler;
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
			Project project = ((ProjectsCursor) cursor).getProject();
			
			((TextView) view.findViewById(R.id.textProject)).setText(project.getName().getUIString());
			((TextView) view.findViewById(R.id.textVelocityPL)).setText("Velocity: "+OutputStyler.getVelocityAsText(project));
		}

		  
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		   final LayoutInflater inflater = LayoutInflater.from(context); 
           
		   final View row = inflater.inflate (R.layout.projects_row, parent, false);
           return row; 
	}

}
