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
package cc.recommenders.mining.calls;

import static cc.recommenders.mining.calls.MiningOptions.newMiningOptions;
import static cc.recommenders.mining.calls.MiningOptions.Algorithm.CANOPY;
import static cc.recommenders.mining.calls.MiningOptions.Algorithm.KMEANS;
import static cc.recommenders.mining.calls.MiningOptions.DistanceMeasure.COSINE;
import static cc.recommenders.mining.calls.MiningOptions.DistanceMeasure.MANHATTAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.utils.LocaleUtils;

import com.google.common.collect.Lists;

public class MiningOptionsTest {

	private static final double DELTA = 0.0001;

	private MiningOptions sut;

	@Before
	public void setup() {
		LocaleUtils.setDefaultLocale();
		sut = new MiningOptions();
	}

	@Test
	public void miningOptionsHaveDefaultValues() {
		assertEquals(CANOPY, sut.getAlgorithm());
		assertEquals(COSINE, sut.getDistanceMeasure());
		assertEquals(0.1, sut.getConvergenceThreshold(), DELTA);
		assertEquals(10, sut.getNumberOfIterations());
		assertEquals(10, sut.getClusterCount());
		assertEquals(2.0, sut.getT1(), DELTA);
		assertEquals(1.0, sut.getT2(), DELTA);
		assertEquals(1.0, sut.getWeightClassContext(), DELTA);
		assertEquals(1.0, sut.getWeightMethodContext(), DELTA);
		assertEquals(1.0, sut.getWeightDefinition(), DELTA);
		assertEquals(1.0, sut.getWeightParameterSites(), DELTA);
		assertEquals(false, sut.isInitUsedAsCall());
		assertFalse(sut.isFeatureDropping());
	}

	private MiningOptions buildWithAllNonDefaultValues() {
		MiningOptions miningOptions = new MiningOptions();
		miningOptions.setAlgorithm(KMEANS);
		miningOptions.setDistanceMeasure(MANHATTAN);
		miningOptions.setFeatureDropping(true);
		miningOptions.setConvergenceThreshold(0.3);
		miningOptions.setNumberOfIterations(20);
		miningOptions.setClusterCount(20);
		miningOptions.setT1(4.0);
		miningOptions.setT2(2.0);
		miningOptions.setWeightClassContext(0.5);
		miningOptions.setWeightMethodContext(0.5);
		miningOptions.setWeightDefinition(0.5);
		miningOptions.setWeightParameterSites(0.5);
		miningOptions.setInitUsedAsCall(true);
		return miningOptions;
	}

	@Test
	public void initCanBeUsedAsCall() {
		sut.setInitUsedAsCall(true);
		assertTrue(sut.isInitUsedAsCall());

		String defOpts = new MiningOptions().toString();
		assertTrue(defOpts.contains("-INIT"));
		sut = MiningOptions.newMiningOptions(defOpts);
		assertFalse(sut.isInitUsedAsCall());

		sut.setInitUsedAsCall(true);
		String newOpts = sut.toString();
		assertTrue(newOpts.contains("+INIT"));
		sut = MiningOptions.newMiningOptions(newOpts);
		assertTrue(sut.isInitUsedAsCall());
	}

	@Test(expected = AssertionException.class)
	public void checkForNull() {
		MiningOptions.newMiningOptions(null);
	}

	@Test
	public void shortcutForSetting() {
		String options = "CANOPY[0.32; 0.21]+MANHATTAN+W[0.12; 0.23; 0.34; 0.45]-INIT-DROP";
		MiningOptions a = new MiningOptions();
		MiningOptions b = new MiningOptions();

		a.setFrom(MiningOptions.newMiningOptions(options));
		b.setFrom(options);
		assertEquals(a, b);
	}

	@Test
	public void algorithmCanBeSet() {
		sut.setAlgorithm(KMEANS);
		assertEquals(KMEANS, sut.getAlgorithm());
	}

	@Test
	public void distanceMeasureCanBeSet() {
		sut.setDistanceMeasure(MANHATTAN);
		assertEquals(MANHATTAN, sut.getDistanceMeasure());
	}

	@Test
	public void convergenceThresholdCanBeSet() {
		sut.setConvergenceThreshold(0.2);
		assertEquals(0.2, sut.getConvergenceThreshold(), DELTA);
	}

	@Test
	public void iterationsCanBeSet() {
		sut.setNumberOfIterations(15);
		assertEquals(15, sut.getNumberOfIterations());
	}

	@Test
	public void kCanBeSet() {
		sut.setClusterCount(13);
		assertEquals(13, sut.getClusterCount());
	}

	@Test
	public void t1CanBeSet() {
		sut.setT1(0.12);
		assertEquals(0.12, sut.getT1(), DELTA);
	}

	@Test
	public void t2CanBeSet() {
		sut.setT2(0.23);
		assertEquals(0.23, sut.getT2(), DELTA);
	}

	@Test
	public void weightClassContextCanBeSet() {
		sut.setWeightClassContext(0.34);
		assertEquals(0.34, sut.getWeightClassContext(), DELTA);
	}

	@Test
	public void weightMethodContextCanBeSet() {
		sut.setWeightMethodContext(0.56);
		assertEquals(0.56, sut.getWeightMethodContext(), DELTA);
	}

	@Test
	public void weightDefinitionCanBeSet() {
		sut.setWeightDefinition(0.67);
		assertEquals(0.67, sut.getWeightDefinition(), DELTA);
	}

	@Test
	public void weightParameterSitesCanBeSet() {
		sut.setWeightParameterSites(0.78);
		assertEquals(0.78, sut.getWeightParameterSites(), DELTA);
	}

	@Test
	public void featureDroppingCanBeSet() {
		sut.setFeatureDropping(true);
		assertTrue(sut.isFeatureDropping());
	}

	@Test
	public void featureDroppingIsCorrectlyParsedAndSerialized() {
		assertMiningOptions("CALLGROUP+MANHATTAN+W[0.12; 0.23; 0.34; 0.45]-INIT+DROP");
	}

	@Test
	public void serializingAndParsingForCallgroup() {
		assertMiningOptions("CALLGROUP+MANHATTAN+W[0.12; 0.23; 0.34; 0.45]-INIT-DROP");
	}

	@Test
	public void serializingAndParsingForCanopy() {
		assertMiningOptions("CANOPY[0.32; 0.21]+MANHATTAN+W[0.12; 0.23; 0.34; 0.45]-INIT-DROP");
	}

	@Test
	public void serializingAndParsingForKmeans() {
		assertMiningOptions("KMEANS[0.21; 13; 17]+COSINE+W[0.12; 0.23; 0.34; 0.45]-INIT-DROP");
	}

	@Test
	public void serializingAndParsingForCombined() {
		assertMiningOptions("COMBINED[6.00; 3.50; 0.13; 14]+COSINE+W[0.12; 0.23; 0.34; 0.45]-INIT-DROP");
	}

	@Test
	public void serializingAndParsingForBMN() {
		assertMiningOptions("BMN+MANHATTAN+W[0.12; 0.23; 0.34; 0.45]-INIT-DROP");
	}

	@Test
	public void differentOrderOfParameters() {
		MiningOptions a = newMiningOptions("CALLGROUP-INIT+MANHATTAN-DROP+W[0.12; 0.23; 0.34; 0.45]");
		MiningOptions b = newMiningOptions("CALLGROUP-DROP+MANHATTAN-INIT+W[0.12; 0.23; 0.34; 0.45]");
		assertEquals(a, b);
	}

	@Test(expected = AssertionException.class)
	public void exceptionOnUnknownAlgorithm() {
		assertMiningOptions("CALLGROUP2+W[0.12; 0.23; 0.34; 0.45]");
	}

	@Test(expected = AssertionException.class)
	public void exceptionOnIncorrectCanopyDefinition() {
		assertMiningOptions("CANOPY[0.34]+W[0.12; 0.23; 0.34; 0.45]");
	}

	@Test(expected = AssertionException.class)
	public void exceptionOnIncorrectKmeansDefinition() {
		assertMiningOptions("KMEANS[0.34; 123]+W[0.12; 0.23; 0.34; 0.45]");
	}

	@Test(expected = AssertionException.class)
	public void exceptionOnIncorrectCombinedDefinition() {
		assertMiningOptions("COMBINED[0.34]+W[0.12; 0.23; 0.34; 0.45]");
	}

	@Test(expected = AssertionException.class)
	public void exceptionOnIncorrectWeightDefinition() {
		assertMiningOptions("CALLGROUP+W[0.12; 0.23; 0.34]");
	}

	@Test
	public void otherRandomTest() {
		MiningOptions sut = newMiningOptions("CANOPY[2.00; 1.00]+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP");
		assertEquals(0, sut.getWeightClassContext(), 0.0001);
		assertEquals(0, sut.getWeightMethodContext(), 0.0001);
		assertEquals(0, sut.getWeightDefinition(), 0.0001);
		assertEquals(0, sut.getWeightParameterSites(), 0.0001);
	}

	@Test
	public void allNonDefaultValuesDoesNotEqualWithDefaultValues() {
		MiningOptions other = buildWithAllNonDefaultValues();
		assertFalse(sut.equals(other));
	}

	@Test
	public void allNonDefaultValuesHasDifferentHashCodeThanWithDefaultValues() {
		MiningOptions other = buildWithAllNonDefaultValues();
		assertFalse(sut.hashCode() == other.hashCode());
	}

	@Test
	public void informalCanopyFormatCanBeParsed() {
		MiningOptions sut = newMiningOptions("CANOPY[0;0]+COSINE+W[0;0;0;0]-DROP+METHOD-CLASS-INIT-DEFINITION-PARAMS");
		assertEquals(MiningOptions.Algorithm.CANOPY, sut.getAlgorithm());
		assertEquals(MiningOptions.DistanceMeasure.COSINE, sut.getDistanceMeasure());
		assertEquals(0, sut.getT1(), DELTA);
		assertEquals(0, sut.getT2(), DELTA);
		assertEquals(0, sut.getWeightClassContext(), DELTA);
		assertEquals(0, sut.getWeightMethodContext(), DELTA);
		assertEquals(0, sut.getWeightDefinition(), DELTA);
		assertEquals(0, sut.getWeightParameterSites(), DELTA);
	}

	@Test
	public void informalKmeansFormatCanBeParsed() {
		MiningOptions sut = newMiningOptions("KMEANS[0;1;2]+COSINE+W[0;0;0;0]+DROP-INIT+METHOD-CLASS-DEFINITION-PARAMS");
		assertEquals(MiningOptions.Algorithm.KMEANS, sut.getAlgorithm());
		assertEquals(MiningOptions.DistanceMeasure.COSINE, sut.getDistanceMeasure());
		assertEquals(0, sut.getConvergenceThreshold(), DELTA);
		assertEquals(1, sut.getNumberOfIterations());
		assertEquals(2, sut.getClusterCount());
		assertEquals(0, sut.getWeightMethodContext(), DELTA);
		assertEquals(0, sut.getWeightDefinition(), DELTA);
		assertEquals(0, sut.getWeightParameterSites(), DELTA);
	}

	@Test
	public void informalCombinedFormatCanBeParsed() {
		MiningOptions sut = newMiningOptions("COMBINED[0;0;0;0]+COSINE+W[0;0;0;0]+DROP+METHOD-CLASS-DEFINITION-INIT-PARAMS");
		assertEquals(MiningOptions.Algorithm.COMBINED, sut.getAlgorithm());
		assertEquals(MiningOptions.DistanceMeasure.COSINE, sut.getDistanceMeasure());
		assertEquals(0, sut.getT1(), DELTA);
		assertEquals(0, sut.getT2(), DELTA);
		assertEquals(0, sut.getConvergenceThreshold(), DELTA);
		assertEquals(0, sut.getNumberOfIterations());
		assertEquals(0, sut.getWeightMethodContext(), DELTA);
		assertEquals(0, sut.getWeightDefinition(), DELTA);
		assertEquals(0, sut.getWeightParameterSites(), DELTA);
	}

	@Test
	public void parametersCanBeParsedInAnyOrder() {
		MiningOptions sut = newMiningOptions("COMBINED[0;0;0;0]+METHOD-CLASS-INIT-DEFINITION-PARAMS+COSINE+W[0;0;0;0]-INIT-DROP");
		assertEquals(MiningOptions.Algorithm.COMBINED, sut.getAlgorithm());
		assertEquals(MiningOptions.DistanceMeasure.COSINE, sut.getDistanceMeasure());
		assertEquals(0, sut.getT1(), DELTA);
		assertEquals(0, sut.getT2(), DELTA);
		assertEquals(0, sut.getConvergenceThreshold(), DELTA);
		assertEquals(0, sut.getNumberOfIterations());
		assertEquals(0, sut.getWeightMethodContext(), DELTA);
		assertEquals(0, sut.getWeightDefinition(), DELTA);
		assertEquals(0, sut.getWeightParameterSites(), DELTA);
	}

	@Test
	public void testEquals() {
		MiningOptions a = MiningOptions
				.newMiningOptions("CANOPY[2.00; 1.00]+COSINE+W[0.20; 0.20; 0.26; 0.15]-INIT+DROP");
		MiningOptions b = MiningOptions
				.newMiningOptions("CANOPY[2.00; 1.00]+COSINE+W[0.20; 0.20; 0.26; 0.15]-INIT+DROP");
		assertTrue(a.equals(b) && b.equals(a));
	}

	@Test
	public void testHashCode() {
		MiningOptions a = MiningOptions
				.newMiningOptions("CANOPY[2.00; 1.00]+COSINE+W[0.20; 0.20; 0.26; 0.15]-INIT+DROP");
		MiningOptions b = MiningOptions
				.newMiningOptions("CANOPY[2.00; 1.00]+COSINE+W[0.20; 0.20; 0.26; 0.15]-INIT+DROP");
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void setFromIsEqual() {
		MiningOptions other = buildWithAllNonDefaultValues();
		assertTrue(sut.setFrom(other).equals(other));
	}

	@Test
	public void setFromSameHashCode() {
		MiningOptions other = buildWithAllNonDefaultValues();
		assertTrue(sut.setFrom(other).hashCode() == other.hashCode());
	}

	@Test
	public void equalsWorksInCollections() {
		List<MiningOptions> list = Lists.newLinkedList();
		MiningOptions mo = buildWithAllNonDefaultValues();
		MiningOptions equalMo = buildWithAllNonDefaultValues();
		list.add(mo);
		assertTrue(list.contains(equalMo));
	}

	private static void assertMiningOptions(String expected) {
		MiningOptions sut = newMiningOptions(expected);
		String actual = sut.toString();
		assertEquals(expected, actual);
	}
}