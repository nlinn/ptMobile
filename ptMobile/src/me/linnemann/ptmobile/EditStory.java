package me.linnemann.ptmobile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EditStory extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("Add Story");
		setContentView(R.layout.story_edit);
		
		Spinner s = (Spinner) findViewById(R.id.spnTypeSE);
	    ArrayAdapter adapter = ArrayAdapter.createFromResource(
	            this, R.array.storytypes, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    s.setAdapter(adapter);
	}
	
}
