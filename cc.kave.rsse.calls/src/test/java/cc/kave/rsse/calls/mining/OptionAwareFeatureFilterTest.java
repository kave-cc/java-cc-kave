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
package cc.kave.rsse.calls.mining;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.rsse.calls.mining.OptionAwareFeatureFilter;
import cc.kave.rsse.calls.mining.QueryOptions;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;

public class OptionAwareFeatureFilterTest {

	private QueryOptions qOpts;
	private IFeature feature;

	private OptionAwareFeatureFilter sut;

	@Before
	public void setup() {
		qOpts = new QueryOptions();
		sut = new OptionAwareFeatureFilter(qOpts);
	}

	@Test
	public void callFeature() {
		feature = mock(UsageSiteFeature.class);
		assertApply();
	}

	@Test
	public void classFeature() {
		feature = mock(ClassContextFeature.class);
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
		feature = mock(MethodContextFeature.class);
		qOpts.useMethodContext = false;
		assertNoApply();
		qOpts.useMethodContext = true;
		assertApply();
	}

	@Test
	public void parameterFeature() {
		// feature = mock(ParameterFeature.class);
		qOpts.useParameterSites = false;
		assertNoApply();
		qOpts.useParameterSites = true;
		assertApply();
	}

	@Test
	public void superMethodFeature() {
		// feature = mock(SuperMethodFeature.class);
		assertNoApply();
	}

	@Test
	public void typeFeature() {
		feature = mock(TypeFeature.class);
		assertApply();
	}

	@Test(expected = AssertionException.class)
	public void unknownFeature() {
		feature = mock(IFeature.class);
		sut.apply(feature);
	}

	private void assertApply() {
		assertTrue(sut.apply(feature));
	}

	private void assertNoApply() {
		assertFalse(sut.apply(feature));
	}
}