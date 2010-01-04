package me.linnemann.ptmobile.test.pivotaltracker.lifecycle;

import me.linnemann.ptmobile.pivotaltracker.StoryType;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Lifecycle;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.LifecycleFactoryImpl;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.State;
import android.test.AndroidTestCase;

public class LifecycleTests extends AndroidTestCase {
	
	LifecycleFactoryImpl factory;
	
	public void setUp() {
		factory = new LifecycleFactoryImpl();
	}
	
	public void test_unstartedFeature_isBlocked() {
		Lifecycle lifecycle = factory.getRemainingLifecycle(StoryType.FEATURE, State.UNSTARTED);
		assertEquals(0,lifecycle.getAvailableTransitions().size());
	}
	
	public void test_unstartedBug_isNotBlocked() {
		Lifecycle lifecycle = factory.getRemainingLifecycle(StoryType.BUG, State.UNSTARTED);
		assertEquals(1,lifecycle.getAvailableTransitions().size());
	}
	
	public void test_unstartedChore_isNotBlocked() {
		Lifecycle lifecycle = factory.getRemainingLifecycle(StoryType.CHORE, State.UNSTARTED);
		assertEquals(1,lifecycle.getAvailableTransitions().size());
	}
	
	public void test_unstartedRelease_isNotBlocked() {
		Lifecycle lifecycle = factory.getRemainingLifecycle(StoryType.RELEASE, State.UNSTARTED);
		assertEquals(1,lifecycle.getAvailableTransitions().size());
	}
}
