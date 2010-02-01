package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.ArrayList;
import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.value.State;
import android.util.Log;

public class ReleaseLifecycle implements Lifecycle {

	private static final String TAG ="ReleaseLifecycle";
	
	public List<Transition> getAvailableTransitions(State state) {
		ArrayList<Transition> list = emptyList();
		switch(state) {
			case UNSCHEDULED:
				// -- no transitions from here
				break;
			case UNSTARTED:
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
