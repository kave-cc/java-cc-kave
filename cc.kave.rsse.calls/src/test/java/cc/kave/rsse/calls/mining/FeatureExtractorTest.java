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

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.IUsageSite;
import cc.kave.rsse.calls.model.usages.UsageSiteType;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSites;

public class FeatureExtractorTest {

	public FeatureExtractor sut;
	private static IMethodName aCallSite;
	private MiningOptions miningOptions;

	@Before
	public void setup() {
		miningOptions = new MiningOptions();
		sut = new FeatureExtractor(miningOptions);
	}

	@Test
	public void featuresCanBeExtracted() {
		List<IFeature> features = sut.extract(createInitUsage("Blubb"));
		assertNotNull(features);
	}

	@Test
	public void classContextFeatureIsCreated() {
		IUsage usage = createInitUsage("Blubb");
		List<IFeature> features = sut.extract(usage);

		IFeature expected = new ClassContextFeature(usage.getClassContext());
		assertTrue(features.contains(expected));
	}

	@Test
	public void methodContextFeatureIsCreated() {
		IUsage usage = createInitUsage("Blubb");
		List<IFeature> features = sut.extract(usage);

		IFeature expected = new MethodContextFeature(usage.getMethodContext());
		assertTrue(features.contains(expected));
	}

	@Test
	public void initDefinitionFeatureIsCreated() {
		IUsage usage = createInitUsage("Blubb");
		List<IFeature> features = sut.extract(usage);

		IFeature expected = new DefinitionFeature(usage.getDefinition());
		assertTrue(features.contains(expected));
	}

	@Test
	public void allCallSiteFeaturesAreCreated() {
		IUsage usage = createInitUsage("Blubb");
		List<IFeature> features = sut.extract(usage);

		for (IUsageSite site : usage.getUsageSites()) {
			IFeature expected = null;
			if (site.getType().equals(UsageSiteType.CALL_PARAMETER)) {
				IMethodName targetMethod = site.getMember(IMethodName.class);
				int argIndex = site.getArgIndex();
				// expected = new ParameterFeature(targetMethod, argIndex);
			} else {
				expected = new UsageSiteFeature(site);
			}
			assertTrue(features.contains(expected));
		}
	}

	@Test
	public void callSiteFeaturesAreNotStoredTwice() {
		IUsage usage = createInitUsage("Blubb");
		List<IFeature> features = sut.extract(usage);
		fail();
		// int firstIdx = features.indexOf(new UsageSiteFeature(aCallSite));
		// int lastIdx = features.lastIndexOf(new UsageSiteFeature(aCallSite));

		// assertEquals(firstIdx, lastIdx);
	}

	@Test
	public void featureExtractionWorksForMultipleUsages() {
		List<IUsage> usages = newArrayList(createInitUsage("Blubb"), createInitUsage("Blubb2"));
		List<List<IFeature>> extract = sut.extract(usages);
		assertEquals(2, extract.size());

		assertTrue(extract.get(0).contains(new TypeFeature(Names.newType("org.bla.Blubb, P"))));
		assertTrue(extract.get(1).contains(new TypeFeature(Names.newType("org.bla.Blubb2, P"))));
	}

	@Test
	public void initIsNotAlwaysAddedAsCall() {

		miningOptions.setInitUsedAsCall(false);

		List<IUsage> usages = newArrayList(createInitUsage("Blubb"));
		List<IFeature> actuals = assertSingle(sut.extract(usages));

		fail();
		// UsageSiteFeature unexpected = new
		// UsageSiteFeature(createDefinitionSite("Blubb"));

		// assertFalse(actuals.contains(unexpected));
	}

	@Test
	public void initIsAddedAsCallIfRequested() {

		miningOptions.setInitUsedAsCall(true);

		List<IUsage> usages = newArrayList(createInitUsage("Blubb"));
		List<IFeature> actuals = assertSingle(sut.extract(usages));
		fail();
		// UsageSiteFeature expected = new
		// UsageSiteFeature(createDefinitionSite("Blubb").getMember(IMethodName.class));
		//
		// assertTrue(actuals.contains(expected));
	}

	private static IUsage createInitUsage(String typeName) {

		aCallSite = Names.newMethod("Lorg/blubb/Bla.method()V");

		Usage q = new Usage();
		q.type = Names.newType("org.bla." + typeName + ", P");
		q.classCtx = Names.newType("org.bla.SuperBlubb,P");
		q.methodCtx = Names.newMethod("[p:void] [org.bla.First].method()");
		q.definition = Definitions.definedByConstructor("[p:void] [org.bla.Blubb]..ctor()");
		q.usageSites.add(UsageSites.call("Lorg/blubb/Bla.method()V"));
		q.usageSites.add(UsageSites.callParameter("Lorg/blubb/Bla.method2()V", 1));
		q.usageSites.add(UsageSites.call("Lorg/blubb/Bla.method2()V"));
		q.usageSites.add(UsageSites.callParameter("Lorg/blubb/Bla.method2()V", 1));

		return q;
	}

	private static IDefinition createDefinitionSite(String typeName) {
		return Definitions.definedByConstructor("[p:void] [org.bla." + typeName + "]..ctor()");
	}

	private static <T> List<T> assertSingle(List<List<T>> list) {
		assertEquals(1, list.size());
		return list.get(0);
	}
}