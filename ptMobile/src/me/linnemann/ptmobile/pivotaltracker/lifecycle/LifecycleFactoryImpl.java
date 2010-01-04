package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.ArrayList;
import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.StoryType;
import android.util.Log;

/**
 * LifecycleFactoryImpl is meant to create story type specific lifecycles
 * 
 * A lifecycle consists out of states a story can have and transitions to get to another
 * state. E.g. A story is "unstarted" and the transition "start" results in the new state "started"
 * 
 * @author nlinn
 */
public class LifecycleFactoryImpl {

	private static final String TAG = "LifecycleFactoryImpl";
	
	public Lifecycle getRemainingLifecycle(StoryType type, State startingState) {
		LifecycleImpl lifecycle;
		switch (type) {
			case FEATURE:
				lifecycle = prepareLifecycleBugFeature(startingState);
				if (State.UNSTARTED.equals(lifecycle.getState()))
					lifecycle.blockStartTransition();
				break;
			case BUG:
				lifecycle = prepareLifecycleBugFeature(startingState);
				break;
			case RELEASE:
				lifecycle = prepareLifecycleRelease(startingState);
				break;
			case CHORE:
				lifecycle = prepareLifecycleChore(startingState);
				break;
			default:
				lifecycle = new LifecycleImpl(startingState); // empty lifecycle
		}
		
		return lifecycle;
	}
	
	private LifecycleImpl prepareLifecycleChore(State startingState) {
		Log.d(TAG,"preparing lifecycle for chore");

		State accepted = new State("accepted");
		if (accepted.equals(startingState))
			return new LifecycleImpl(accepted);
		
		Transition finish = new Transition("finish",accepted);
		State started = new State("started",finish);
		if (started.equals(startingState))
			return new LifecycleImpl(started);
		
		Transition start = new Transition("start",started);
		State unstarted = new State("unstarted",start);
		return new LifecycleImpl(unstarted);
	}
	
	/**
	 * prepares the _remaining_ lifecycle of your story
	 * starts with stories current state (thus its the remaining lifecycle)
	 * 
	 * @return current state in lifecycle
	 */
	private LifecycleImpl prepareLifecycleRelease(State startingState) {
		Log.d(TAG,"preparing lifecycle for release");
	
		State accepted = new State("accepted");
		if (accepted.equals(startingState)) 
			return new LifecycleImpl(accepted);
		
		Transition finish = new Transition("finish",accepted);
		State unstarted = new State("unstarted",finish);
		return new LifecycleImpl(unstarted);
	}
	
	/**
	 * prepares the _remaining_ lifecycle of your story
	 * starts with stories current state (thus its the remaining lifecycle)
	 * 
	 * @return current state in lifecycle
	 */
	private LifecycleImpl prepareLifecycleBugFeature(State startingState) {
		Log.d(TAG,"preparing lifecycle for bug/feature");

		State accepted = new State("accepted");
		if (accepted.equals(startingState))
			return new LifecycleImpl(accepted);
		
		State rejected = new State("rejected");
		if (rejected.equals(startingState))
			return new LifecycleImpl(rejected);

		List<Transition> acceptreject = new ArrayList<Transition>();
		acceptreject.add(new Transition("accept", accepted));
		acceptreject.add(new Transition("reject", rejected));

		State delivered = new State("delivered",acceptreject);
		if (delivered.equals(startingState))
			return new LifecycleImpl(delivered);
		
		Transition deliver = new Transition("deliver",delivered);
		State finished = new State("finished",deliver);
		if (finished.equals(startingState)) 
			return new LifecycleImpl(finished);
		
		Transition finish = new Transition("finish",finished);
		State started = new State("started",finish);
		if (started.equals(startingState))
			return new LifecycleImpl(started);
		
		Transition start = new Transition("start",started);
		State unstarted = new State("unstarted",start);
		return new LifecycleImpl(unstarted);
	}
}
