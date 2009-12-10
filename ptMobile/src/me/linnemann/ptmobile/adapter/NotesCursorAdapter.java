package me.linnemann.ptmobile.adapter;

import me.linnemann.ptmobile.R;
import me.linnemann.ptmobile.cursor.ActivitiesCursor;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotesCursorAdapter extends CursorAdapter {
	
	public NotesCursorAdapter(Context context, ActivitiesCursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		if (view instanceof LinearLayout) {
			((TextView) view.findViewById(R.id.textProjectAL)).setText( ((ActivitiesCursor) cursor).getProject());
			((TextView) view.findViewById(R.id.textDescriptionAL)).setText( ((ActivitiesCursor) cursor).getDescription());
			((TextView) view.findViewById(R.id.textWhenAL)).setText( ((ActivitiesCursor) cursor).getWhen());
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		   final LayoutInflater inflater = LayoutInflater.from(context); 
           
		   final View row = inflater.inflate (R.layout.activities_row, parent, false);
           return row; 
	}
}