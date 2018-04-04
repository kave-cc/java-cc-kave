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
package cc.kave.rsse.calls.extraction.features;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.options.MiningOptions;
import cc.kave.rsse.calls.usages.DefinitionSite;
import cc.kave.rsse.calls.usages.DefinitionSites;
import cc.kave.rsse.calls.usages.IUsage;
import cc.kave.rsse.calls.usages.Usage;
import cc.kave.rsse.calls.usages.UsageAccess;
import cc.kave.rsse.calls.usages.UsageAccessType;
import cc.kave.rsse.calls.usages.UsageAccesses;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;

public class UsageFeatureExtractorTest {

	public UsageFeatureExtractor sut;
	private static IMethodName aCallSite;
	private MiningOptions miningOptions;

	@Before
	public void setup() {
		miningOptions = new MiningOptions();
		sut = new UsageFeatureExtractor(miningOptions);
	}

	@Test
	public void featuresCanBeExtracted() {
		List<UsageFeature> features = sut.extract(createInitUsage("Blubb"));
		assertNotNull(features);
	}

	@Test
	public void classContextFeatureIsCreated() {
		IUsage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		UsageFeature expected = new ClassFeature(usage.getClassContext());
		assertTrue(features.contains(expected));
	}

	@Test
	public void methodContextFeatureIsCreated() {
		IUsage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		UsageFeature expected = new FirstMethodFeature(usage.getMethodContext());
		assertTrue(features.contains(expected));
	}

	@Test
	public void initDefinitionFeatureIsCreated() {
		IUsage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		UsageFeature expected = new DefinitionFeature(usage.getDefinitionSite());
		assertTrue(features.contains(expected));
	}

	@Test
	public void allCallSiteFeaturesAreCreated() {
		IUsage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		for (UsageAccess site : usage.getAllAccesses()) {
			UsageFeature expected;
			if (site.getKind().equals(UsageAccessType.CALL_PARAMETER)) {
				IMethodName targetMethod = site.getMethod();
				int argIndex = site.getArgIndex();
				expected = new ParameterFeature(targetMethod, argIndex);
			} else {
				expected = new CallFeature(site.getMethod());
			}
			assertTrue(features.contains(expected));
		}
	}

	@Test
	public void callSiteFeaturesAreNotStoredTwice() {
		IUsage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		int firstIdx = features.indexOf(new CallFeature(aCallSite));
		int lastIdx = features.lastIndexOf(new CallFeature(aCallSite));

		assertEquals(firstIdx, lastIdx);
	}

	@Test
	public void featureExtractionWorksForMultipleUsages() {
		List<IUsage> usages = newArrayList(createInitUsage("Blubb"), createInitUsage("Blubb2"));
		List<List<UsageFeature>> extract = sut.extract(usages);
		assertEquals(2, extract.size());

		assertTrue(extract.get(0).contains(new TypeFeature(Names.newType("org.bla.Blubb, P"))));
		assertTrue(extract.get(1).contains(new TypeFeature(Names.newType("org.bla.Blubb2, P"))));
	}

	@Test
	public void initIsNotAlwaysAddedAsCall() {

		miningOptions.setInitUsedAsCall(false);

		List<IUsage> usages = newArrayList(createInitUsage("Blubb"));
		List<UsageFeature> actuals = assertSingle(sut.extract(usages));

		CallFeature unexpected = new CallFeature(createDefinitionSite("Blubb").getMethod());

		assertFalse(actuals.contains(unexpected));
	}

	@Test
	public void initIsAddedAsCallIfRequested() {

		miningOptions.setInitUsedAsCall(true);

		List<IUsage> usages = newArrayList(createInitUsage("Blubb"));
		List<UsageFeature> actuals = assertSingle(sut.extract(usages));

		CallFeature expected = new CallFeature(createDefinitionSite("Blubb").getMethod());

		assertTrue(actuals.contains(expected));
	}

	private static IUsage createInitUsage(String typeName) {

		aCallSite = Names.newMethod("Lorg/blubb/Bla.method()V");

		Usage q = new Usage();
		q.setType(Names.newType("org.bla." + typeName + ", P"));
		q.setClassContext(Names.newType("org.bla.SuperBlubb,P"));
		q.setMethodContext(Names.newMethod("[p:void] [org.bla.First].method()"));
		q.setDefinition(DefinitionSites.createDefinitionByConstructor("[p:void] [org.bla.Blubb]..ctor()"));
		q.accesses.add(UsageAccesses.createCallReceiver("Lorg/blubb/Bla.method()V"));
		q.accesses.add(UsageAccesses.createCallParameter("Lorg/blubb/Bla.method2()V", 1));
		q.accesses.add(UsageAccesses.createCallReceiver("Lorg/blubb/Bla.method2()V"));
		q.accesses.add(UsageAccesses.createCallParameter("Lorg/blubb/Bla.method2()V", 1));

		return q;
	}

	private static DefinitionSite createDefinitionSite(String typeName) {
		return DefinitionSites.createDefinitionByConstructor("[p:void] [org.bla." + typeName + "]..ctor()");
	}

	private static <T> List<T> assertSingle(List<List<T>> list) {
		assertEquals(1, list.size());
		return list.get(0);
	}
}