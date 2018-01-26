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

import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.AbstractVector;
import org.apache.mahout.math.CardinalityException;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.junit.Test;

public class CosineDistanceMeasureTest {

	private static final double PRECISION = 0.001;
	private DistanceMeasure sut;
	private AbstractVector v1;
	private AbstractVector v2;

	@Test
	public void equalVectors() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 0.2);
		v2 = v(1, 0.2);

		assertDistance(0);
	}

	@Test(expected = CardinalityException.class)
	public void cardinalityDoesMatter() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 0);
		v2 = v(0, 1);
		sut.distance(v1, v2);
	}

	@Test
	public void complexCalculation() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 0);
		v2 = v(0, 1);

		assertDistance(1);
	}

	@Test
	public void assertDistances_1() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1);
		v2 = v(1, 0);
		assertDistance(0.292);
		v1 = v(1, 1, 1);
		v2 = v(1, 1, 0);
		assertDistance(0.183);
		v1 = v(1, 1, 1, 1);
		v2 = v(1, 1, 1, 0);
		assertDistance(0.133);
		v1 = v(1, 1, 1, 1, 1);
		v2 = v(1, 1, 1, 1, 0);
		assertDistance(0.105);
	}

	@Test
	public void assertDistances_2() {
		sut = new CosineDistanceMeasure();
		v1 = v(0, 1);
		v2 = v(1, 0);
		assertDistance(1);
		v1 = v(1, 0, 1);
		v2 = v(1, 1, 0);
		assertDistance(0.5);
		v1 = v(1, 1, 0, 1);
		v2 = v(1, 1, 1, 0);
		assertDistance(0.333);
		v1 = v(1, 1, 1, 0, 1);
		v2 = v(1, 1, 1, 1, 0);
		assertDistance(0.25);
	}

	@Test
	public void valueDoesNotChangeIfLengthOfVectorIsNotAffected() {
		sut = new CosineDistanceMeasure();
		v1 = v(0, 1, 0);
		v2 = v(0, 0, 1);

		assertDistance(1);
	}

	@Test
	public void valueChangesIfLengthOfVectorIsAffected() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 0);
		v2 = v(1, 0, 1);

		assertDistance(0.5);
	}

	@Test
	public void ex0() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 0, 0.3, 0.3);
		v2 = v(0, 0, 0.3, 0.3);

		assertDistance(0.609);
	}
	@Test
	public void ex1() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 0, 0.3, 0.3);
		v2 = v(1, 0, 0, 0.3, 0.3);

		assertDistance(0.264);
	}

	@Test
	public void ex2() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 1, 0, 0.3, 0.3);
		v2 = v(1, 1, 0, 0, 0.3, 0.3);

		assertDistance(0.172);
	}

	@Test
	public void ex3() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 1, 0, 0.3, 0.0, 0.3);
		v2 = v(1, 1, 1, 0, 0.0, 0.3, 0.3);

		assertDistance(0.028);
	}

	@Test
	public void ex4() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 1, 1, 0, 0.3, 0.0, 0.3);
		v2 = v(1, 1, 1, 1, 0, 0.0, 0.3, 0.3);

		assertDistance(0.022);
	}

	@Test
	public void ex5() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 1, 1, 0, 0.3, 0.0, 0.3);
		v2 = v(1, 1, 1, 0, 0, 0.0, 0.3, 0.3);

		assertDistance(0.152);
	}

	@Test
	public void ex6() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 0, 1, 0, 0.3, 0.0, 0.3);
		v2 = v(1, 1, 1, 0, 0, 0.0, 0.3, 0.3);

		assertDistance(0.343);
	}

	@Test
	public void ex6b() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 0, 1, 0.3, 0.0, 0.3, 0.0);
		v2 = v(1, 1, 1, 0, 0.0, 0.3, 0.0, 0.3);

		assertDistance(0.371);
	}

	@Test
	public void ex7() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 1, 0, 0, 0, 0.0, 0.3, 0.0, 0.3);
		v2 = v(0, 0, 0, 1, 1, 1, 0.3, 0.0, 0.3, 0.0);

		assertDistance(1);
	}

	@Test
	public void ex8() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 1, 0, 0, 0.0, 0.3, 0.0, 0.3);
		v2 = v(1, 0, 0, 1, 1, 0.3, 0.0, 0.3, 0.0);

		assertDistance(0.686);
	}

	@Test
	public void ex9() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 1, 0.0, 0.3, 0.0, 0.3);
		v2 = v(1, 1, 1, 0.3, 0.0, 0.3, 0.0);

		assertDistance(0.056);
	}

	@Test
	public void maxDistance_1() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 0);
		v2 = v(0, 1);

		assertDistance(1);
	}

	@Test
	public void maxDistance_2() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 0, 0);
		v2 = v(0, 0, 1, 1);

		assertDistance(1);
	}

	@Test
	public void maxDistance_3() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 0);
		v2 = v(0, 0, 1);

		assertDistance(1);
	}

	@Test
	public void maxDistance_4() {
		sut = new CosineDistanceMeasure();
		v1 = v(1, 1, 1, 1, 1, 0, 0, 0, 0, 0);
		v2 = v(0, 0, 0, 0, 0, 1, 1, 1, 1, 1);

		assertDistance(1);
	}

	private AbstractVector v(double... entries) {
		AbstractVector v = new RandomAccessSparseVector(entries.length);
		for (int i = 0; i < entries.length; i++) {
			v.set(i, entries[i]);
		}
		return v;
	}

	private void assertDistance(double expected) {
		double actual = sut.distance(v1, v2);
		assertEquals(expected, actual, PRECISION);
	}
}