package me.linnemann.ptmobile.test;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite
 */
public class AllTests extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class)
        .includeAllPackagesUnderHere()
        .build();
    }

}