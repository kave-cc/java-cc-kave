/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;

public class TypeBasedAnalysisTest {

	private PointsToQuery createQuery(ITypeName type) {
		return new PointsToQuery(null, type, null, null);
	}

	@Test
	public void testStreamTest() {
		TestSSTBuilder builder = new TestSSTBuilder();
		Context context = builder.createStreamTest();

		TypeBasedAnalysis pointerAnalysis = new TypeBasedAnalysis();
		pointerAnalysis.compute(context);

		Set<AbstractLocation> fileStreamLocations = pointerAnalysis.query(createQuery(builder.getFileStreamType()));
		assertEquals(1, fileStreamLocations.size());

		Set<AbstractLocation> stringLocations = pointerAnalysis.query(createQuery(builder.getStringType()));
		assertEquals(1, stringLocations.size());

		// file stream and string should not have the same location
		assertNotEquals(fileStreamLocations.iterator().next(), stringLocations.iterator().next());

		// querying for System.Void should not return any locations
		assertTrue(pointerAnalysis.query(createQuery(builder.getVoidType())).isEmpty());
	}
}