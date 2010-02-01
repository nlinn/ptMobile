package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.value.State;

public class ChoreLifecycle implements Lifecycle {

	private static final String TAG ="ChoreLifecycle";
	
	public List<Transition> getAvailableTransitions(State state) {
		ArrayList<Transition> list = emptyList();
		switch(state) {
			case UNSCHEDULED:
				// -- no transitions from here
				break;
			case UNSTARTED:
				list.add(new Transition("start",State.STARTED));
				break;
			case STARTED:	
				list.add(new Transition("finish",State.ACCEPTED));
				break;
			case ACCEPTED:
				// -- no transitions from here
				break;
			default:
				Log.w(TAG, "Unexpected State: "+state);
		}
		return list;
	}

	private ArrayList<Transition> emptyList() {
		return new ArrayList<Transition>();
	}
}
