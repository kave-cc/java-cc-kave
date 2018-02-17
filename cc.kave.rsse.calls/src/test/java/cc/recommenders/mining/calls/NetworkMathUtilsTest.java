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
package cc.recommenders.mining.calls;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class NetworkMathUtilsTest {
	private static final double DELTA = 0.0000001;

	@Test
	public void roundToDefaultPrecision() {
		double actual = NetworkMathUtils.roundToDefaultPrecision(0.123456789);
		double expected = 0.123457;
		assertEquals(expected, actual, DELTA);
	}

	@Test
	public void safeDivMinMax() {
		double actual = NetworkMathUtils.safeDivMaxMin(2, 3);
		Assert.assertEquals(2d / 3d, actual, DELTA);
	}

	@Test
	public void safeDivMinMax_greaterThanMax() {
		double actual = NetworkMathUtils.safeDivMaxMin(2, 1);
		Assert.assertEquals(0.999999, actual, DELTA);
	}

	@Test
	public void safeDivMinMax_smallerThanMin() {
		double actual = NetworkMathUtils.safeDivMaxMin(1, 100000000);
		Assert.assertEquals(0.000001, actual, DELTA);
	}

	@Test
	public void safeDivMinMax_divByZero() {
		double actual = NetworkMathUtils.safeDivMaxMin(1, 0);
		Assert.assertEquals(0.999999, actual, DELTA);
	}
}