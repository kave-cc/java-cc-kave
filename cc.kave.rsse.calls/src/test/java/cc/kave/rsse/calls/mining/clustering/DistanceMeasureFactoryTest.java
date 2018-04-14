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
package cc.kave.rsse.calls.mining.clustering;

import static cc.kave.rsse.calls.mining.clustering.DistanceMeasure.COSINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.junit.Before;
import org.junit.Test;

import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.utils.OptionsBuilder;

public class DistanceMeasureFactoryTest {

	private Options options;
	private DistanceMeasureFactory sut;

	@Before
	public void setup() {
		options = new Options("");
		sut = new DistanceMeasureFactory(options);
	}

	@Test
	public void distanceMeasureIsSetByDefault() {
		DistanceMeasure actual = sut.get();
		assertNotNull(actual);
	}

	@Test
	public void distanceMeasureCanBeSetToCosine() {
		options = OptionsBuilder.bmn().option("dist", COSINE.toString()).get();
		sut = new DistanceMeasureFactory(options);
		fail("create injectable OptionsProvider");

		Class<?> actual = sut.get().getClass();
		Class<?> expected = CosineDistanceMeasure.class;
		assertEquals(expected, actual);
	}

	@Test
	public void distanceMeasureCanBeSetToManhattan() {
		// options.setDistanceMeasure(MANHATTAN);
		fail();
		Class<?> actual = sut.get().getClass();
		Class<?> expected = ManhattanDistanceMeasure.class;
		assertEquals(expected, actual);
	}
}