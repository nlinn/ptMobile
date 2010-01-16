package me.linnemann.ptmobile.test.pivotaltracker.lifecycle;

import me.linnemann.ptmobile.pivotaltracker.lifecycle.LifecycleFactoryImpl;
import android.test.AndroidTestCase;

public class LifecycleFactoryImplTests extends AndroidTestCase {

	private static final String TAG = "LifecycleFactoryImplTests";
	private LifecycleFactoryImpl factory;
	
	public void setUp() {
		factory = new LifecycleFactoryImpl();
	}
		
}
