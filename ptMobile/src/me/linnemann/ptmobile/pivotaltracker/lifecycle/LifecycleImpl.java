package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class LifecycleImpl implements Lifecycle {
	
	private static final String TAG = "LifecycleImpl";

	private boolean isBlockedStart;
	private State state;
	
	public LifecycleImpl(State initialState) {
		state = initialState;
		unblockStartTransition(); // default, since only features need blocking
	}
	
	public void blockStartTransition() {
		isBlockedStart = true;
	}
	
	public void unblockStartTransition() {
		isBlockedStart = false;
	}
	
	public List<Transition> getAvailableTransitions() {
		Transition start = new Transition("start", null);
		List<Transition> transitions = state.getTransitions();
		if ((isBlockedStart) && (transitions.contains(start))) {
			return new ArrayList<Transition>(); // empty list if start is blocked
		}
		return transitions;
	}

	public boolean doTransition(Transition trans) {
		if (getAvailableTransitions().contains(trans)) {
			Log.d(TAG,"applying transition: "+trans.getName());
			state = trans.resultingState();
			Log.d(TAG,"state is now: "+state.getName());
		//	setData(StoryData.CURRENT_STATE, state.getName());
			return true;
		} else {
			Log.e(TAG,"transition not found: "+trans.getName());
			return false;
		}
	}
	
	public State getState() {
		return state;
	}
}
