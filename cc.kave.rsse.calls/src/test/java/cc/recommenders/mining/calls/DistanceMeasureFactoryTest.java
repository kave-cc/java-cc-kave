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

import static cc.recommenders.mining.calls.MiningOptions.DistanceMeasure.COSINE;
import static cc.recommenders.mining.calls.MiningOptions.DistanceMeasure.MANHATTAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.junit.Before;
import org.junit.Test;

public class DistanceMeasureFactoryTest {

	private MiningOptions options;
	private DistanceMeasureFactory sut;

	@Before
	public void setup() {
		options = new MiningOptions();
		sut = new DistanceMeasureFactory(options);
	}

	@Test
	public void distanceMeasureIsSetByDefault() {
		DistanceMeasure actual = sut.get();
		assertNotNull(actual);
	}

	@Test
	public void distanceMeasureCanBeSetToCosine() {
		options.setDistanceMeasure(COSINE);
		Class<?> actual = sut.get().getClass();
		Class<?> expected = CosineDistanceMeasure.class;
		assertEquals(expected, actual);
	}

	@Test
	public void distanceMeasureCanBeSetToManhattan() {
		options.setDistanceMeasure(MANHATTAN);
		Class<?> actual = sut.get().getClass();
		Class<?> expected = ManhattanDistanceMeasure.class;
		assertEquals(expected, actual);
	}
}