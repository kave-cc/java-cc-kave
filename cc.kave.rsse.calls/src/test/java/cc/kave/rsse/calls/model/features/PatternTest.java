/**
 * Copyright (c) 2011 Sebastian Proksch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.model.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.utils.LocaleUtils;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;

public class PatternTest {

	private static final double DOUBLE_TRESHOLD = 0.001;
	private Pattern sut;

	@Before
	public void setup() {
		LocaleUtils.setDefaultLocale();
		sut = new Pattern();
	}

	@Test
	public void defaultValues() {
		Assert.assertEquals("", sut.getName());
		Assert.assertEquals(0, sut.getNumberOfObservations());
	}

	@Test
	public void nameCanBeSet() {
		sut.setName("abc");
		String actual = sut.getName();
		String expected = "abc";
		assertEquals(expected, actual);
	}

	@Test
	public void numberOfObservationsCanBeSet() {
		int expected = 1234;
		sut.setNumberOfObservations(expected);
		int actual = sut.getNumberOfObservations();
		assertEquals(expected, actual);
	}

	@Test
	public void unsetValuesHaveZeroPropability() {
		double actual = sut.getProbability(f("anUnsetFeature"));
		Assert.assertEquals(0.0, actual, DOUBLE_TRESHOLD);
	}

	@Test
	public void setValuesHaveExpectedPropability() {

		for (int i = 0; i < 50; i++) {
			sut.setProbability(f("feature" + i), i / 100.0);
		}

		for (int i = 0; i < 50; i++) {
			double expected = i / 100.0;
			double actual = sut.getProbability(f("feature" + i));
			Assert.assertEquals(expected, actual, DOUBLE_TRESHOLD);
		}
	}

	@Test
	public void toStringIsCorrect() {
		String method = "LType.method()V";

		sut.setName("pN");
		sut.setProbability(f(method), 0.123);
		sut.setNumberOfObservations(678);

		String actual = sut.toString();
		String expected = "[pattern 'pN' (678x):\n\t0.123 : " + method + "\n]";

		assertEquals(expected, actual);
	}

	@Test
	public void patternsCanBeCloned() {
		IFeature a = f("a");
		IFeature b = f("b");
		IFeature c = f("c");

		Pattern orig = new Pattern();
		orig.setName("p1");
		orig.setNumberOfObservations(13);
		orig.setProbability(a, 0.1);
		orig.setProbability(b, 0.2);
		orig.setProbability(c, 0.7);

		Pattern clone = orig.clone("other");

		assertEquals("other", clone.getName());
		assertEquals(orig.getNumberOfObservations(), clone.getNumberOfObservations());
		assertEquals(clone.getProbability(a), clone.getProbability(a), DOUBLE_TRESHOLD);
		assertEquals(clone.getProbability(b), clone.getProbability(b), DOUBLE_TRESHOLD);
		assertEquals(clone.getProbability(c), clone.getProbability(c), DOUBLE_TRESHOLD);
	}

	@Test
	public void equalObjectsAreDetected() {
		Pattern a = getExamplePattern();
		Pattern b = getExamplePattern();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalObjectsAreDetected_DELTA() {
		Assert.fail("consider DELTA in equals implementation");
	}

	@Test
	public void differentObjectsAreDetected() {
		Pattern a = getExamplePattern();
		Pattern b = getExamplePattern();
		b.setName("other");
		assertFalse(a.equals(b));
		assertFalse(a.hashCode() == b.hashCode());
	}

	private static Pattern getExamplePattern() {
		Pattern p = new Pattern();
		p.setName("aName");
		p.setNumberOfObservations(13);
		p.setProbability(f("a feature"), 0.9);
		return p;
	}

	private static IFeature f(String string) {
		throw new RuntimeException("implement me");
	}
}