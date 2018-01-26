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
package org.apache.mahout.commons.distance;

import static org.junit.Assert.assertEquals;

import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.math.AbstractVector;
import org.apache.mahout.math.CardinalityException;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.junit.Test;

public class ManhattanDistanceMeasureTests {

	private static final double PRECISION = 0.000000001;
	private DistanceMeasure sut;
	private AbstractVector v1;
	private AbstractVector v2;

	@Test
	public void equalVectors() {
		sut = new ManhattanDistanceMeasure();
		v1 = v(1, 0.2);
		v2 = v(1, 0.2);

		double actual = sut.distance(v1, v2);
		double expected = 0;
		assertEquals(expected, actual, PRECISION);
	}

	@Test(expected = CardinalityException.class)
	public void cardinalityDoesMatter() {
		sut = new ManhattanDistanceMeasure();
		v1 = v(1, 1, 0.1, 0);
		v2 = v(0, 1, 0.2);
		sut.distance(v1, v2);
	}

	@Test
	public void differencesAreJustSummedUp() {
		sut = new ManhattanDistanceMeasure();
		v1 = v(1, 1, 0.1);
		v2 = v(0, 1, 0.2);

		double actual = sut.distance(v1, v2);
		double expected = 1.1;
		assertEquals(expected, actual, PRECISION);
	}

	private AbstractVector v(double... entries) {
		AbstractVector v = new RandomAccessSparseVector(entries.length);
		for (int i = 0; i < entries.length; i++) {
			v.set(i, entries[i]);
		}
		return v;
	}
}