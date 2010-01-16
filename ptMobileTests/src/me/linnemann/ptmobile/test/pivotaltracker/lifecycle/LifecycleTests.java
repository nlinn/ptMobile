package me.linnemann.ptmobile.test.pivotaltracker.lifecycle;

import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.lifecycle.Lifecycle;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.LifecycleFactoryImpl;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.StateWithTransitions;
import me.linnemann.ptmobile.pivotaltracker.lifecycle.Transition;
import me.linnemann.ptmobile.pivotaltracker.value.State;
import me.linnemann.ptmobile.pivotaltracker.value.StoryType;
import android.test.AndroidTestCase;

public class LifecycleTests extends AndroidTestCase {
	
	LifecycleFactoryImpl factory;
	
	public void setUp() {
		factory = new LifecycleFactoryImpl();
	}
	
	public void test_feature_unstartedToStart() {
		Lifecycle lifecycle = factory.getLifecycle(StoryType.FEATURE);
		assertFirstTrans(lifecycle,"start",State.UNSTARTED);
	}

	public void test_feature_startedToFinish() {
		Lifecycle lifecycle = factory.getLifecycle(StoryType.FEATURE);
		assertFirstTrans(lifecycle,"finish",State.STARTED);
	}
	
	public void test_feature_finishedToDeliver() {
		Lifecycle lifecycle = factory.getLifecycle(StoryType.FEATURE);
		assertFirstTrans(lifecycle,"deliver",State.FINISHED);
	}

	public void test_feature_deliveredToAcceptReject() {
		Lifecycle lifecycle = factory.getLifecycle(StoryType.FEATURE);
		assertFirstTrans(lifecycle,"accept",State.DELIVERED);
		assertSecondTrans(lifecycle,"reject",State.DELIVERED);
	}
	
	public void test_release_unstartedToFinish() {
		Lifecycle lifecycle = factory.getLifecycle(StoryType.RELEASE);
		assertFirstTrans(lifecycle,"finish",State.UNSTARTED);
	}
	
	private void assertFirstTrans(Lifecycle lifecycle, String transName, State fromState) {
		assertTransAtPos(lifecycle, transName, fromState, 0);
	}
	
	private void assertSecondTrans(Lifecycle lifecycle, String transName, State fromState) {
		assertTransAtPos(lifecycle, transName, fromState, 1);
	}
	
	private void assertTransAtPos(Lifecycle lifecycle, String transName, State fromState, int pos) {
		List<Transition> trans = lifecycle.getAvailableTransitions(fromState);
		assertEquals(transName,trans.get(pos).getName());
	}
}
