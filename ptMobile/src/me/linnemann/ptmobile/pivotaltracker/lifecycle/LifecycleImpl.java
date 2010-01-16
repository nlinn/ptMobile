package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.ArrayList;
import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.value.State;

import android.util.Log;

public class LifecycleImpl implements Lifecycle {
	
	private static final String TAG = "LifecycleImpl";

	private StateWithTransitions initialState;
	
	public LifecycleImpl(StateWithTransitions initialState) {
		this.initialState = initialState;
	}
	
	public List<Transition> getAvailableTransitions(State state) {

		if (state.equals(State.UNSCHEDULED)) {
			return new ArrayList<Transition>();
		}
		
		StateWithTransitions stateWithTransitions = findStateInLifecycleRecursivly(state, initialState);
		return stateWithTransitions.getTransitions();
	}
	
	private StateWithTransitions findStateInLifecycleRecursivly(State stateFromOutside, StateWithTransitions stateInLifeCycle) {

		Log.i(TAG,"checking: "+stateFromOutside+" against lifecycle: "+stateInLifeCycle);
		
		if (stateInLifeCycle.getState().equals(stateFromOutside))
			return stateInLifeCycle;

		
		StateWithTransitions foundState = null;
		List<Transition>transitions = stateInLifeCycle.getTransitions();
		
		for (Transition trans : transitions) {
				foundState = findStateInLifecycleRecursivly(stateFromOutside, trans.resultingState());
				if (foundState != null)
					break;
		}
		
		return foundState;

	}
}
