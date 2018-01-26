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
package cc.recommenders.mining.features;

import static cc.recommenders.usages.DefinitionSites.createDefinitionByConstant;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.features.UsageFeatureWeighter;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ClassFeature;
import cc.recommenders.usages.features.DefinitionFeature;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.ParameterFeature;

public class UsageFeatureWeighterTest {

	private static final double W_CLASS = 0.1;
	private static final double W_METHOD = 0.2;
	private static final double W_DEFINITION = 0.3;
	private static final double W_PARAM = 0.4;

	private UsageFeatureWeighter sut;
	private MiningOptions options;

	@Before
	public void setup() {

		options = new MiningOptions();
		options.setWeightClassContext(W_CLASS);
		options.setWeightMethodContext(W_METHOD);
		options.setWeightDefinition(W_DEFINITION);
		options.setWeightParameterSites(W_PARAM);

		sut = new UsageFeatureWeighter(options);
	}

	@Test
	public void classContextIsWeighted() {
		ClassFeature feature = new ClassFeature(mock(ICoReTypeName.class));
		double actual = sut.getWeight(feature);
		double expected = W_CLASS;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void classContextIsUnweighted() {
		ClassFeature feature = new ClassFeature(mock(ICoReTypeName.class));
		double actual = sut.getUnweighted(feature, 0.1);
		double expected = 0.1 / W_CLASS;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void methodContextIsWeighted() {
		FirstMethodFeature feature = new FirstMethodFeature(mock(ICoReMethodName.class));
		double actual = sut.getWeight(feature);
		double expected = W_METHOD;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void methodContextIsUnweighted() {
		FirstMethodFeature feature = new FirstMethodFeature(mock(ICoReMethodName.class));
		double actual = sut.getUnweighted(feature, 0.1);
		double expected = 0.1 / W_METHOD;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void definitionIsWeighted() {
		DefinitionFeature feature = new DefinitionFeature(createDefinitionByConstant());
		double actual = sut.getWeight(feature);
		double expected = W_DEFINITION;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void definitionIsUnweighted() {
		DefinitionFeature feature = new DefinitionFeature(createDefinitionByConstant());
		double actual = sut.getUnweighted(feature, 0.1);
		double expected = 0.1 / W_DEFINITION;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void callAreWeightedNormalized() {
		CallFeature feature = new CallFeature(mock(ICoReMethodName.class));
		double actual = sut.getWeight(feature);
		double expected = 1.0;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void paramIsWeighted() {
		ParameterFeature feature = new ParameterFeature(mock(ICoReMethodName.class), 0);
		double actual = sut.getWeight(feature);
		double expected = W_PARAM;
		assertEquals(expected, actual, 0.001);
	}

	@Test
	public void paramIsUnweighted() {
		ParameterFeature feature = new ParameterFeature(mock(ICoReMethodName.class), 0);
		double actual = sut.getUnweighted(feature, 0.1);
		double expected = 0.1 / W_PARAM;
		assertEquals(expected, actual, 0.001);
	}
}