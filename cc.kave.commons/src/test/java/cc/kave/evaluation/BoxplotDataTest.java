/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.evaluation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.util.MathUtils;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;

public class BoxplotDataTest {

	// needed because of strange Percentile.evaluate implementation
	private static final double TEST_PRECISION = Boxplot.DATA_PRECISION / 10.0;

	private BoxplotData sut;

	@Before
	public void setup() {
		initSortedSut();
	}

	private void initSortedSut() {
		sut = new BoxplotData();
		long numValues = Math.round(1 / TEST_PRECISION);
		for (int i = 1; i <= numValues; i++) {
			sut.add(i * TEST_PRECISION);
		}
	}

	private void initUnsortedSut() {
		sut = new BoxplotData();
		long numValues = Math.round(1 / TEST_PRECISION);
		for (int i = 1; i <= numValues / 2; i++) {
			long j = numValues + 1 - i;
			sut.add(i * TEST_PRECISION);
			sut.add(j * TEST_PRECISION);
		}
	}

	@Test
	public void instantiateWithData() {
		double[] expecteds = new double[] { 0.1, 0.2, 0.3 };
		double[] actuals = BoxplotData.from(expecteds).getRawValues();
		assertArrayEquals(expecteds, actuals, 0.0001);
	}

	@Test
	public void correctBoxplotIsCreated() {
		Boxplot actual = sut.getBoxplot();
		Boxplot expected = new Boxplot(1000000, 0.5, 0.05, 0.25, 0.5, 0.75, 0.95);
		assertEquals(expected, actual);
	}

	@Test
	public void canWorkWithUnsortedValues() {
		initUnsortedSut();
		correctBoxplotIsCreated();
	}

	@Test
	public void aNewBoxplotDoesNotHaveData() {
		sut = new BoxplotData();
		assertFalse(sut.hasData());
	}

	@Test
	public void boxplotHasDataOnceTheFirstDataIsAdded() {
		sut = new BoxplotData();
		sut.add(0.5);
		assertTrue(sut.hasData());
	}

	@Test
	public void rawVarianceCalculationWorks() {
		sut = new BoxplotData();
		sut.add(0.1);
		sut.add(0.2);
		sut.add(0.3);
		double actual = sut.getVarianceRaw();
		double expected = calculateVariance(0.1, 0.2, 0.3);
		assertEquals(expected, actual, TEST_PRECISION);
	}

	@Test
	public void integrationTestForVarianceCalculation() {
		Variance actual = createVariance(0.1, 0.3, 0.6);
		assertEquals(3, actual.getNumberOfInput());
		assertEquals(0.33333, actual.getMean(), TEST_PRECISION);
		assertEquals(calculateVariance(0.1, 0.3, 0.6), actual.getVariance(), TEST_PRECISION);
	}

	@Test
	public void anotherIntegrationTestForVarianceCalculation() {
		Variance actual = createVariance(0.1, 0.3, 0.6, 0.2);
		assertEquals(4, actual.getNumberOfInput());
		assertEquals(0.3, actual.getMean(), TEST_PRECISION);
		assertEquals(calculateVariance(0.1, 0.3, 0.6, 0.2), actual.getVariance(), TEST_PRECISION);
	}

	@Test(expected = AssertionException.class)
	public void callsOnEmptyBoxplotCauseExceptionForBoxplot() {
		new BoxplotData().getBoxplot();
	}

	@Test(expected = AssertionException.class)
	public void callsOnEmptyBoxplotCauseExceptionForVariance() {
		new BoxplotData().getVariance();
	}

	@Test
	public void dataCanBeAddedAfterTmpCacheIsEstablished() {
		sut = new BoxplotData();
		sut.add(0.0);
		sut.getRawValues();
		sut.add(1.0);
		double[] actuals = sut.getRawValues();
		double[] expecteds = new double[] { 0.0, 1.0 };
		assertArrayEquals(expecteds, actuals, TEST_PRECISION);
	}

	@Test
	public void dataCanBeRequested() {
		sut = new BoxplotData();
		sut.add(0.0);
		sut.add(0.345);
		sut.add(0.123);
		sut.add(1.0);
		double[] actuals = sut.getRawValues();
		double[] expecteds = new double[] { 0.0, 0.345, 0.123, 1.0 };
		assertArrayEquals(expecteds, actuals, TEST_PRECISION);
	}

	@Test
	public void multipleValuesCanBeAdded() {
		sut = new BoxplotData();
		sut.addAll(new double[] { 0.123, 0.234 });
		double[] actuals = sut.getRawValues();
		double[] expecteds = new double[] { 0.123, 0.234 };
		assertArrayEquals(expecteds, actuals, TEST_PRECISION);
	}

	@Test
	public void canBeMerged() {
		sut = new BoxplotData();
		sut.add(0.0);
		BoxplotData other = new BoxplotData();
		other.add(1.0);
		other.add(4.0);
		sut.addAll(other);
		double[] actuals = sut.getRawValues();
		double[] expecteds = new double[] { 0.0, 1.0, 4.0 };
		assertArrayEquals(expecteds, actuals, TEST_PRECISION);
	}

	@Test
	public void equality_default() {
		BoxplotData a = new BoxplotData();
		BoxplotData b = new BoxplotData();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_afterAddingValues() {
		BoxplotData a = new BoxplotData();
		a.add(0.1);
		BoxplotData b = new BoxplotData();
		b.add(0.1);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_differentValues() {
		BoxplotData a = new BoxplotData();
		a.add(0.1);
		BoxplotData b = new BoxplotData();
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	private Variance createVariance(double... values) {
		sut = new BoxplotData();
		for (double v : values) {
			sut.add(v);
		}
		return sut.getVariance();
	}

	private double calculateVariance(double... values) {
		double mean = 0;
		double numEntries = values.length;
		for (double value : values) {
			mean += value;
		}
		mean = mean / numEntries;

		double variance = 0.0;
		for (double value : values) {
			double diff = value - mean;
			variance += diff * diff / (numEntries - 1.0);
		}

		return MathUtils.round(variance, Boxplot.DATA_SCALE);

		// double variance = StatUtils.variance(values);
		// return MathUtils.round(variance, Boxplot.DATA_SCALE);
	}
}