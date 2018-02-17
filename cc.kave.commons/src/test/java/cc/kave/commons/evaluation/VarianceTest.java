/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.commons.evaluation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.evaluation.Variance;
import cc.kave.commons.exceptions.AssertionException;

public class VarianceTest {

	private static final double DELTA = Variance.DATA_PRECISION / 100;

	private Variance sut;

	@Test
	public void valuesAreProvidedInConstructor() {
		sut = new Variance(123, 0.2, 0.3);
		assertEquals(123, sut.getNumberOfInput());
		assertEquals(0.2, sut.getMean(), DELTA);
		assertEquals(0.3, sut.getVariance(), DELTA);
	}

	@Test
	public void inputIsRounded() {
		sut = new Variance(1, 0.199999, 0.300001);
		assertEquals(0.2, sut.getMean(), DELTA);
		assertEquals(0.3, sut.getVariance(), DELTA);
	}

	@Test(expected = AssertionException.class)
	public void numCannotBeZero() {
		sut = new Variance(0, 0.2, 0.3);
	}

	@Test(expected = AssertionException.class)
	public void numHasToBePositive() {
		sut = new Variance(-1, 0.2, 0.3);
	}

	@Test
	public void varianceCanBeZero() {
		sut = new Variance(1, 0.2, 0.0);
		double actual = sut.getVariance();
		double expected = 0;
		assertEquals(expected, actual, DELTA);
	}

	@Test(expected = AssertionException.class)
	public void varianceHasToBePositive() {
		sut = new Variance(0, 0.2, -0.1);
	}
}