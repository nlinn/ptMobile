package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.ArrayList;
import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
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
	private static LifecycleFactoryImpl factory = new LifecycleFactoryImpl();
	
	private Lifecycle featureBugLifeCycle;
	private Lifecycle choreLifeCycle;
	private Lifecycle releaseLifeCycle;
	
	public static Lifecycle getLifecycleForType(StoryType type) {
		return factory.getLifecycle(type);
	}
	
	public Lifecycle getLifecycle(StoryType type) {
		Lifecycle lifecycle;
		switch (type) {
			case FEATURE:
				if (featureBugLifeCycle == null)
					featureBugLifeCycle = prepareLifecycleBugFeature();
				lifecycle = featureBugLifeCycle;
				break;
			case BUG:
				if (featureBugLifeCycle == null)
					featureBugLifeCycle = prepareLifecycleBugFeature();
				lifecycle = featureBugLifeCycle;
				break;
			case RELEASE:
				if (releaseLifeCycle == null)
					releaseLifeCycle = prepareLifecycleRelease();
				lifecycle = releaseLifeCycle;
				break;
			case CHORE:
				if (choreLifeCycle == null)
					choreLifeCycle = prepareLifecycleChore();
				lifecycle = choreLifeCycle;
				break;
			default:
				throw new RuntimeException("Lifecycle not found for type: "+type);
		}
		
		return lifecycle;
	}
	
	private LifecycleImpl prepareLifecycleChore() {
		Log.d(TAG,"preparing lifecycle for chore");

		StateWithTransitions accepted = new StateWithTransitions(State.ACCEPTED);
		
		Transition finish = new Transition("finish",accepted);
		StateWithTransitions started = new StateWithTransitions(State.STARTED,finish);
		
		Transition start = new Transition("start",started);
		StateWithTransitions unstarted = new StateWithTransitions(State.UNSTARTED,start);
		return new LifecycleImpl(unstarted);
	}
	
	/**
	 * prepares the _remaining_ lifecycle of your story
	 * starts with stories current state (thus its the remaining lifecycle)
	 * 
	 * @return current state in lifecycle
	 */
	private LifecycleImpl prepareLifecycleRelease() {
		Log.d(TAG,"preparing lifecycle for release");
	
		StateWithTransitions accepted = new StateWithTransitions(State.ACCEPTED);
		Transition finish = new Transition("finish",accepted);
		StateWithTransitions unstarted = new StateWithTransitions(State.UNSTARTED,finish);
		return new LifecycleImpl(unstarted);
	}
	
	private LifecycleImpl prepareLifecycleBugFeature() {
		Log.d(TAG,"preparing lifecycle for bug/feature");

		StateWithTransitions accepted = new StateWithTransitions(State.ACCEPTED);
		StateWithTransitions rejected = new StateWithTransitions(State.REJECTED);

		List<Transition> acceptreject = new ArrayList<Transition>();
		acceptreject.add(new Transition("accept", accepted));
		acceptreject.add(new Transition("reject", rejected));

		StateWithTransitions delivered = new StateWithTransitions(State.DELIVERED, acceptreject);
		
		Transition deliver = new Transition("deliver",delivered);
		StateWithTransitions finished = new StateWithTransitions(State.FINISHED, deliver);
		
		Transition finish = new Transition("finish",finished);
		StateWithTransitions started = new StateWithTransitions(State.STARTED, finish);
		
		Transition start = new Transition("start",started);
		StateWithTransitions unstarted = new StateWithTransitions(State.UNSTARTED, start);
		return new LifecycleImpl(unstarted);
	}
}
