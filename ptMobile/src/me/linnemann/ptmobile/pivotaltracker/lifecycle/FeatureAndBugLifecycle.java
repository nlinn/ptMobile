package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import me.linnemann.ptmobile.pivotaltracker.value.State;

public class FeatureAndBugLifecycle implements Lifecycle {

	private static final String TAG ="FeatureAndBugLifecycle";
	
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
				list.add(new Transition("finish",State.FINISHED));
				break;
			case FINISHED:
				list.add(new Transition("deliver",State.DELIVERED));
				break;
			case DELIVERED:
				list.add(new Transition("accept",State.ACCEPTED));
				list.add(new Transition("reject",State.REJECTED));
				break;
			case ACCEPTED:
				// -- no transitions from here
				break;
			case REJECTED:	
				list.add(new Transition("restart",State.STARTED));
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