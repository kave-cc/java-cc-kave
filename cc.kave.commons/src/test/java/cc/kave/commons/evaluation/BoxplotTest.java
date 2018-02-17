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

import static cc.kave.commons.evaluation.Boxplot.DATA_PRECISION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.evaluation.Boxplot;
import cc.kave.commons.utils.LocaleUtils;

public class BoxplotTest {

	private Boxplot sut;

	@Before
	public void setup() {
		LocaleUtils.setDefaultLocale();
	}
	
	@Test
	public void valuesAreAccessable() {
		sut = new Boxplot(13, 0.3, 0.1, 0.2, 0.3, 0.4, 0.5);
		assertEquals(13, sut.getNumValues());
		assertEquals(0.3, sut.getMean(), DATA_PRECISION);
		assertEquals(0.1, sut.getLowerWhisker(), DATA_PRECISION);
		assertEquals(0.2, sut.getLowerQuartil(), DATA_PRECISION);
		assertEquals(0.3, sut.getMedian(), DATA_PRECISION);
		assertEquals(0.4, sut.getUpperQuartil(), DATA_PRECISION);
		assertEquals(0.5, sut.getUpperWhisker(), DATA_PRECISION);
	}

	@Test
	public void valuesAreRoundedOnInput() {
		Boxplot a = new Boxplot(100, 0.2999999, 0.0999999, 0.1999999, 0.2999999, 0.3999999, 0.4999999);
		Boxplot b = new Boxplot(100, 0.3000001, 0.1000001, 0.2000001, 0.3000001, 0.4000001, 0.5000001);
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void identicalBoxPlotsAreEqualAndShareHashcodes() {
		Boxplot a = new Boxplot(11, 0.3, 0.1, 0.2, 0.3, 0.4, 0.5);
		Boxplot b = new Boxplot(11, 0.3, 0.1, 0.2, 0.3, 0.4, 0.5);

		assertNotSame(a, b);
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void differentBoxPlotsAreNotEqualAndDontShareHashcodes() {
		Boxplot a = new Boxplot(11, 0.0, 0.1, 0.2, 0.3, 0.4, 0.5);
		Boxplot b = new Boxplot(13, 1.0, 0.0, 0.1, 0.2, 0.3, 0.4);

		assertNotSame(a, b);
		assertFalse(a.equals(b));
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void meaningfulToStringImplementation() {
		Boxplot a = new Boxplot(9, 0.0, 0.1, 0.2, 0.3, 0.399999, 0.500001);
		String actual = a.toString();
		String expected = "[9 values (avg: 0.000) - 0.10; 0.20; 0.30; 0.40; 0.50]";
		assertEquals(expected, actual);
	}
}