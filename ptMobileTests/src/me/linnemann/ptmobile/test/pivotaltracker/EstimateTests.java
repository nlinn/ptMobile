package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.value.Estimate;
import android.test.AndroidTestCase;

public class EstimateTests extends AndroidTestCase {

	public void test_createEstimate_valuesOfBeautifiedStrings() {
		assertEquals(Estimate.NO_ESTIMATE, Estimate.valueOfBeautifiedString("No estimate"));
		assertEquals(Estimate.UNESTIMATED, Estimate.valueOfBeautifiedString("Unestimated"));
		assertEquals(Estimate.POINTS_0, Estimate.valueOfBeautifiedString("0 points"));
		assertEquals(Estimate.POINTS_1, Estimate.valueOfBeautifiedString("1 point"));
		assertEquals(Estimate.POINTS_2, Estimate.valueOfBeautifiedString("2 points"));
		assertEquals(Estimate.POINTS_3, Estimate.valueOfBeautifiedString("3 points"));
		assertEquals(Estimate.POINTS_4, Estimate.valueOfBeautifiedString("4 points"));
		assertEquals(Estimate.POINTS_5, Estimate.valueOfBeautifiedString("5 points"));
		assertEquals(Estimate.POINTS_8, Estimate.valueOfBeautifiedString("8 points"));
	}

	public void test_createEstimate_valuesOfInts() {
		assertEquals(Estimate.NO_ESTIMATE, Estimate.valueOfNumeric(-2));
		assertEquals(Estimate.UNESTIMATED, Estimate.valueOfNumeric(-1));
		assertEquals(Estimate.POINTS_0, Estimate.valueOfNumeric(0));
		assertEquals(Estimate.POINTS_1, Estimate.valueOfNumeric(1));
		assertEquals(Estimate.POINTS_2, Estimate.valueOfNumeric(2));
		assertEquals(Estimate.POINTS_3, Estimate.valueOfNumeric(3));
		assertEquals(Estimate.POINTS_4, Estimate.valueOfNumeric(4));
		assertEquals(Estimate.POINTS_5, Estimate.valueOfNumeric(5));
		assertEquals(Estimate.POINTS_8, Estimate.valueOfNumeric(8));
	}

	public void test_estimateprovides_correctValues() {
		assertEquals(Estimate.NO_ESTIMATE.getValueAsString(), "-2");
		assertEquals(Estimate.UNESTIMATED.getValueAsString(), "-1");
		assertEquals(Estimate.POINTS_0.getValueAsString(), "0");
		assertEquals(Estimate.POINTS_1.getValueAsString(), "1");
		assertEquals(Estimate.POINTS_2.getValueAsString(), "2");
		assertEquals(Estimate.POINTS_3.getValueAsString(), "3");
		assertEquals(Estimate.POINTS_4.getValueAsString(), "4");
		assertEquals(Estimate.POINTS_5.getValueAsString(), "5");
		assertEquals(Estimate.POINTS_8.getValueAsString(), "8");
	}
}
