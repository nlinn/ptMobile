package me.linnemann.ptmobile.pivotaltracker.lifecycle;

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

	public LifecycleFactoryImpl() {
		featureBugLifeCycle = new FeatureAndBugLifecycle();
		choreLifeCycle = new ChoreLifecycle();
		releaseLifeCycle = new ReleaseLifecycle();
	}
	
	public static Lifecycle getLifecycleForType(StoryType type) {
		return factory.getLifecycle(type);
	}
	
	public Lifecycle getLifecycle(StoryType type) {
		Lifecycle lifecycle=null;
		switch (type) {
			case FEATURE:
				lifecycle = featureBugLifeCycle;
				break;
			case BUG:
				lifecycle = featureBugLifeCycle;
				break;
			case RELEASE:
				lifecycle = releaseLifeCycle;
				break;
			case CHORE:
				lifecycle = choreLifeCycle;
				break;
			default:
				Log.w(TAG,"Lifecycle not found for type: "+type);
				throw new RuntimeException("Lifecycle not found for type: "+type);
		}
		
		return lifecycle;
	}
}
