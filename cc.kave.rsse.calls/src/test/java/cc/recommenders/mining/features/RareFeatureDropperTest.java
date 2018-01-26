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
package cc.recommenders.mining.features;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.recommenders.datastructures.Dictionary;

import com.google.common.collect.Lists;

public class RareFeatureDropperTest {

	private RareFeatureDropper<String> sut;
	private List<List<String>> features;
	private Dictionary<String> rawDict;
	private Dictionary<String> actual;
	private Dictionary<String> expected;

	@Before
	public void setup() {
		sut = new RareFeatureDropper<String>();
		expected = new Dictionary<String>();
		features = Lists.newLinkedList();
	}

	@Test
	public void defaultTreshold() {
		int actual = sut.getTreshold();
		int expected = 2;
		assertEquals(expected, actual);
	}

	@Test
	public void dropThresholdCanBeSet() {
		sut.setThreshold(3);
		int actual = sut.getTreshold();
		int expected = 3;
		assertEquals(expected, actual);
	}

	@Test(expected = AssertionException.class)
	public void illegalThreshold() {
		sut.setThreshold(1);
	}

	@Test
	public void featuresAreDroppedIfBelowThreshold() {
		usage("a", "b");
		usage("a");
		rawDict = dict("a", "b");

		assertDictionary("a");
	}

	@Test
	public void thresholdIsRespected() {
		sut.setThreshold(3);
		usage("a", "b", "c");
		usage("a", "b");
		usage("a");
		rawDict = dict("a");

		assertDictionary("a");
	}

	@Test
	public void featuresAreNotCountedTwicePerUsage() {
		usage("a", "b");
		usage("a", "c", "c");
		rawDict = dict("a", "b", "c");

		assertDictionary("a");
	}

	@Test
	public void noEmptyDictionaryWillBeCreated() {
		usage("a", "b");
		rawDict = dict("a", "b");

		Dictionary<String> actual = sut.dropRare(rawDict, features);
		assertEquals(1, actual.size());
	}

	@Test(expected = AssertionException.class)
	public void inputMustBeNonEmpty() {
		rawDict = dict();

		Dictionary<String> actual = sut.dropRare(rawDict, features);
		assertEquals(1, actual.size());
	}

	private void usage(String... fs) {
		List<String> usage = Lists.newLinkedList();
		for (String f : fs) {
			usage.add(f);
		}
		features.add(usage);
	}

	private Dictionary<String> dict(String... xs) {
		Dictionary<String> d = new Dictionary<String>();
		for (String x : xs) {
			d.add(x);
		}
		return d;
	}

	private void assertDictionary(String... fs) {
		Dictionary<String> actual = sut.dropRare(rawDict, features);
		Dictionary<String> expected = dict(fs);
		assertEquals(expected, actual);
	}
}