package me.linnemann.ptmobile.test.pivotaltracker.value;

import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;
import android.test.AndroidTestCase;

public class TrackerValueTests extends AndroidTestCase {

	public void test_stringValue_equalsValueWithSameString() {
		Text a = new Text("string");
		Text b = new Text("string");
		assertEquals(a,b);
	}

	public void test_integerValue_equalsValueWithSameInteger() {
		Numeric a = new Numeric(1);
		Numeric b = new Numeric(1);
		assertEquals(a,b);
	}
	
	public void test_stringValueWithNull_isEmpty() {
		Text a = new Text(null);
		assertTrue(a.isEmpty());
	}

	public void test_integerValueWithNull_isEmpty() {
		Numeric a = Numeric.getEmptyValue();
		assertTrue(a.isEmpty());
	}

	public void test_stringValueWithEmptyString_isEmpty() {
		Text a = new Text("");
		assertTrue(a.isEmpty());
	}

	public void test_estimateValue_isEmptyIfNo_Estimate() {
		assertTrue(Estimate.NO_ESTIMATE.isEmpty());
	}
}
