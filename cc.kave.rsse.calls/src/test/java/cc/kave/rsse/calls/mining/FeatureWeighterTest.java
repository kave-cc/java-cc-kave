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
package cc.kave.rsse.calls.mining;

import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstant;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.utils.OptionsBuilder;

public class FeatureWeighterTest {

	private static final double W_CLASS = 0.1;
	private static final double W_METHOD = 0.2;
	private static final double W_DEFINITION = 0.3;
	private static final double W_PARAM = 0.4;

	private FeatureWeighter sut;
	private Options options;

	@Before
	public void setup() {
		options = OptionsBuilder.bmn().cCtx(W_CLASS).mCtx(W_METHOD).def(W_DEFINITION).params(W_PARAM).get();
		sut = new FeatureWeighter(options);
	}

	@Test
	public void classContextIsWeighted() {
		ClassContextFeature feature = new ClassContextFeature(mock(ITypeName.class));
		double actual = sut.getWeight(feature);
		double expected = W_CLASS;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void classContextIsUnweighted() {
		ClassContextFeature feature = new ClassContextFeature(mock(ITypeName.class));
		double actual = sut.getUnweighted(feature, 0.1);
		double expected = 0.1 / W_CLASS;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void methodContextIsWeighted() {
		MethodContextFeature feature = new MethodContextFeature(mock(IMethodName.class));
		double actual = sut.getWeight(feature);
		double expected = W_METHOD;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void methodContextIsUnweighted() {
		MethodContextFeature feature = new MethodContextFeature(mock(IMethodName.class));
		double actual = sut.getUnweighted(feature, 0.1);
		double expected = 0.1 / W_METHOD;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void definitionIsWeighted() {
		DefinitionFeature feature = new DefinitionFeature(definedByConstant());
		double actual = sut.getWeight(feature);
		double expected = W_DEFINITION;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void definitionIsUnweighted() {
		DefinitionFeature feature = new DefinitionFeature(definedByConstant());
		double actual = sut.getUnweighted(feature, 0.1);
		double expected = 0.1 / W_DEFINITION;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void callAreWeightedNormalized() {
		fail();
		// UsageSiteFeature feature = new UsageSiteFeature(mock(IMethodName.class));
		// double actual = sut.getWeight(feature);
		// double expected = 1.0;
		// assertEquals(expected, actual, 0.001);
	}

	// @Test
	// public void paramIsWeighted() {
	// ParameterFeature feature = new ParameterFeature(mock(IMethodName.class), 0);
	// double actual = sut.getWeight(feature);
	// double expected = W_PARAM;
	// assertEquals(expected, actual, 0.001);
	// }
	//
	// @Test
	// public void paramIsUnweighted() {
	// ParameterFeature feature = new ParameterFeature(mock(IMethodName.class), 0);
	// double actual = sut.getUnweighted(feature, 0.1);
	// double expected = 0.1 / W_PARAM;
	// assertEquals(expected, actual, 0.001);
	// }
}