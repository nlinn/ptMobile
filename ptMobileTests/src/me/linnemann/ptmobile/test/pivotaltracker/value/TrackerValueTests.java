package me.linnemann.ptmobile.test.pivotaltracker.value;

import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.IntegerValue;
import me.linnemann.ptmobile.pivotaltracker.value.StringValue;
import android.test.AndroidTestCase;

public class TrackerValueTests extends AndroidTestCase {

	public void test_stringValue_equalsValueWithSameString() {
		StringValue a = new StringValue("string");
		StringValue b = new StringValue("string");
		assertEquals(a,b);
	}

	public void test_integerValue_equalsValueWithSameInteger() {
		IntegerValue a = new IntegerValue(1);
		IntegerValue b = new IntegerValue(1);
		assertEquals(a,b);
	}
	
	public void test_stringValueWithNull_isEmpty() {
		StringValue a = new StringValue(null);
		assertTrue(a.isEmpty());
	}

	public void test_integerValueWithNull_isEmpty() {
		IntegerValue a = new IntegerValue(null);
		assertTrue(a.isEmpty());
	}

	public void test_stringValueWithEmptyString_isEmpty() {
		StringValue a = new StringValue("");
		assertTrue(a.isEmpty());
	}

	public void test_estimateValue_isEmptyIfNo_Estimate() {
		assertTrue(Estimate.NO_ESTIMATE.isEmpty());
	}
}
