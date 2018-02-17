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
package cc.kave.commons.evaluation;

import static cc.kave.commons.evaluation.Measure.dropAfterTotalRecall;
import static cc.kave.commons.evaluation.Measure.newMeasure;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.evaluation.Measure;

public class MeasureTest {

	private static final double DOUBLE_TRESHOLD = 0.0001;

	private static final double PRECISION = 1.0 / 3.0;
	private static final double RECALL = 1.0 / 2.0;

	private Set<String> expecteds;
	private Set<String> proposeds;

	@Before
	public void setup() {
		expecteds = newHashSet("a", "b");
		proposeds = newHashSet("a", "c", "d");
	}

	@Test
	public void precisionIsCorrect() {
		Measure m = newMeasure(expecteds, proposeds);
		double actual = m.getPrecision();
		double expected = PRECISION;

		assertEquals(expected, actual, DOUBLE_TRESHOLD);
	}

	@Test
	public void recallIsCorrect() {
		Measure m = newMeasure(expecteds, proposeds);
		double actual = m.getRecall();
		double expected = RECALL;

		assertEquals(expected, actual, DOUBLE_TRESHOLD);
	}

	@Test
	public void f1IsCorrect() {
		Measure m = newMeasure(expecteds, proposeds);
		double actual = m.getF1();
		double expected = 2 * PRECISION * RECALL / (PRECISION + RECALL);

		assertEquals(expected, actual, DOUBLE_TRESHOLD);
	}

	@Test
	public void valuesAreCorrectEvenIfNoMatch() {
		Measure m = newMeasure(newHashSet("a"), newHashSet("b"));
		assertEquals(0.0, m.getRecall(), DOUBLE_TRESHOLD);
		assertEquals(0.0, m.getPrecision(), DOUBLE_TRESHOLD);
		assertEquals(0.0, m.getF1(), DOUBLE_TRESHOLD);
	}

	@Test
	public void valuesAreCorrectEvenIfNoExpectation() {
		Set<String> noExpectations = newHashSet();
		Measure m = newMeasure(noExpectations, newHashSet("a"));
		assertEquals(1.0, m.getRecall(), DOUBLE_TRESHOLD);
		assertEquals(0.0, m.getPrecision(), DOUBLE_TRESHOLD);
		assertEquals(0.0, m.getF1(), DOUBLE_TRESHOLD);
	}

	@Test
	public void valuesAreCorrectEvenIfNoProposal() {
		Set<String> noProposals = newHashSet();
		Measure m = newMeasure(newHashSet("a"), noProposals);
		assertEquals(0.0, m.getRecall(), DOUBLE_TRESHOLD);
		assertEquals(1.0, m.getPrecision(), DOUBLE_TRESHOLD);
		assertEquals(0.0, m.getF1(), DOUBLE_TRESHOLD);
	}

	@Test(expected = RuntimeException.class)
	public void negativeBetasCauseException() {
		Measure m = newMeasure(expecteds, proposeds);
		m.getF(-1);
	}

	@Test
	public void entriesCanBeDroppedOnTotalRecall() {
		Set<String> missing = Sets.newLinkedHashSet();
		missing.add("a");
		missing.add("b");
		missing.add("c");

		Set<String> proposed = Sets.newLinkedHashSet();
		proposed.add("a");
		proposed.add("X");
		proposed.add("c");
		proposed.add("Y");
		proposed.add("b");
		proposed.add("Z");

		Set<String> expected = Sets.newLinkedHashSet();
		expected.add("a");
		expected.add("X");
		expected.add("c");
		expected.add("Y");
		expected.add("b");

		Set<String> actual = dropAfterTotalRecall(missing, proposed);

		assertEquals(expected, actual);
	}
}