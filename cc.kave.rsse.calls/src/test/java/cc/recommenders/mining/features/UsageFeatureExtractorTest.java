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

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.features.UsageFeatureExtractor;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSiteKind;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ClassFeature;
import cc.recommenders.usages.features.DefinitionFeature;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.ParameterFeature;
import cc.recommenders.usages.features.TypeFeature;
import cc.recommenders.usages.features.UsageFeature;

import com.google.common.collect.Sets;

public class UsageFeatureExtractorTest {

	public UsageFeatureExtractor sut;
	private static ICoReMethodName aCallSite;
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
		Usage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		UsageFeature expected = new ClassFeature(usage.getClassContext());
		assertTrue(features.contains(expected));
	}

	@Test
	public void methodContextFeatureIsCreated() {
		Usage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		UsageFeature expected = new FirstMethodFeature(usage.getMethodContext());
		assertTrue(features.contains(expected));
	}

	@Test
	public void initDefinitionFeatureIsCreated() {
		Usage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		UsageFeature expected = new DefinitionFeature(usage.getDefinitionSite());
		assertTrue(features.contains(expected));
	}

	@Test
	public void allCallSiteFeaturesAreCreated() {
		Usage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		for (CallSite site : usage.getAllCallsites()) {
			UsageFeature expected;
			if (site.getKind().equals(CallSiteKind.PARAMETER)) {
				ICoReMethodName targetMethod = site.getMethod();
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
		Usage usage = createInitUsage("Blubb");
		List<UsageFeature> features = sut.extract(usage);

		int firstIdx = features.indexOf(new CallFeature(aCallSite));
		int lastIdx = features.lastIndexOf(new CallFeature(aCallSite));

		assertEquals(firstIdx, lastIdx);
	}

	@Test
	public void featureExtractionWorksForMultipleUsages() {
		List<Usage> usages = newArrayList(createInitUsage("Blubb"), createInitUsage("Blubb2"));
		List<List<UsageFeature>> extract = sut.extract(usages);
		assertEquals(2, extract.size());

		assertTrue(extract.get(0).contains(new TypeFeature(CoReTypeName.get("Lorg/bla/Blubb"))));
		assertTrue(extract.get(1).contains(new TypeFeature(CoReTypeName.get("Lorg/bla/Blubb2"))));
	}

	@Test
	public void initIsNotAlwaysAddedAsCall() {

		miningOptions.setInitUsedAsCall(false);

		List<Usage> usages = newArrayList(createInitUsage("Blubb"));
		List<UsageFeature> actuals = assertSingle(sut.extract(usages));

		CallFeature unexpected = new CallFeature(createDefinitionSite("Blubb").getMethod());

		assertFalse(actuals.contains(unexpected));
	}

	@Test
	public void initIsAddedAsCallIfRequested() {

		miningOptions.setInitUsedAsCall(true);

		List<Usage> usages = newArrayList(createInitUsage("Blubb"));
		List<UsageFeature> actuals = assertSingle(sut.extract(usages));

		CallFeature expected = new CallFeature(createDefinitionSite("Blubb").getMethod());

		assertTrue(actuals.contains(expected));
	}

	private static Usage createInitUsage(String typeName) {

		aCallSite = CoReMethodName.get("Lorg/blubb/Bla.method()V");

		Set<CallSite> sites = Sets.newLinkedHashSet();
		sites.add(CallSites.createReceiverCallSite("Lorg/blubb/Bla.method()V"));
		sites.add(CallSites.createParameterCallSite("Lorg/blubb/Bla.method2()V", 1));
		sites.add(CallSites.createReceiverCallSite("Lorg/blubb/Bla.method2()V"));
		sites.add(CallSites.createParameterCallSite("Lorg/blubb/Bla.method2()V", 1));

		Query q = new Query();
		q.setType(CoReTypeName.get("Lorg/bla/" + typeName));
		q.setClassContext(CoReTypeName.get("Lorg/bla/SuperBlubb"));
		q.setMethodContext(CoReMethodName.get("Lorg/bla/First.method()V"));
		q.setDefinition(DefinitionSites.createDefinitionByConstructor("Lorg/bla/Blubb.<init>()V"));
		q.setAllCallsites(sites);

		return q;
	}

	private static DefinitionSite createDefinitionSite(String typeName) {
		return DefinitionSites.createDefinitionByConstructor("Lorg/bla/" + typeName + ".<init>()V");
	}

	private static <T> List<T> assertSingle(List<List<T>> list) {
		assertEquals(1, list.size());
		return list.get(0);
	}
}