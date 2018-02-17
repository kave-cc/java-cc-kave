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
package cc.kave.rsse.calls.extraction.features;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.rsse.calls.extraction.features.OptionAwareFeaturePredicate;
import cc.kave.rsse.calls.options.QueryOptions;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.SuperMethodFeature;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;

public class OptionAwareFeaturePredicateTest {

	private QueryOptions qOpts;
	private UsageFeature feature;

	private OptionAwareFeaturePredicate sut;

	@Before
	public void setup() {
		qOpts = new QueryOptions();
		sut = new OptionAwareFeaturePredicate(qOpts);
	}

	@Test
	public void callFeature() {
		feature = mock(CallFeature.class);
		assertApply();
	}

	@Test
	public void classFeature() {
		feature = mock(ClassFeature.class);
		qOpts.useClassContext = false;
		assertNoApply();
		qOpts.useClassContext = true;
		assertApply();
	}

	@Test
	public void definitionFeature() {
		feature = mock(DefinitionFeature.class);
		qOpts.useDefinition = false;
		assertNoApply();
		qOpts.useDefinition = true;
		assertApply();
	}

	@Test
	public void firstMethodFeature() {
		feature = mock(FirstMethodFeature.class);
		qOpts.useMethodContext = false;
		assertNoApply();
		qOpts.useMethodContext = true;
		assertApply();
	}

	@Test
	public void parameterFeature() {
		feature = mock(ParameterFeature.class);
		qOpts.useParameterSites = false;
		assertNoApply();
		qOpts.useParameterSites = true;
		assertApply();
	}

	@Test
	public void superMethodFeature() {
		feature = mock(SuperMethodFeature.class);
		assertNoApply();
	}

	@Test
	public void typeFeature() {
		feature = mock(TypeFeature.class);
		assertApply();
	}

	@Test(expected = AssertionException.class)
	public void unknownFeature() {
		feature = mock(UsageFeature.class);
		sut.apply(feature);
	}

	private void assertApply() {
		assertTrue(sut.apply(feature));
	}

	private void assertNoApply() {
		assertFalse(sut.apply(feature));
	}
}