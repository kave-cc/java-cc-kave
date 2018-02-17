/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.rsse.calls.extraction.usages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.pointsto.SimplePointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.ReferenceBasedAnalysis;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;
import cc.kave.rsse.calls.usages.CallSite;
import cc.kave.rsse.calls.usages.CallSiteKind;
import cc.kave.rsse.calls.usages.DefinitionSite;
import cc.kave.rsse.calls.usages.DefinitionSiteKind;
import cc.kave.rsse.calls.usages.Usage;

public class UsageExtractionTest2 {

	private static final ITypeName CORE_STRING_TYPE = Names.newType("p:string");

	@Test
	public void testPaperTest() {
		TestSSTBuilder builder = new TestSSTBuilder();
		SimplePointsToAnalysisFactory<TypeBasedAnalysis> paFactory = new SimplePointsToAnalysisFactory<>(
				TypeBasedAnalysis.class);
		PointsToUsageExtractor usageExtractor = new PointsToUsageExtractor();
		List<Context> contexts = builder.createPaperTest();

		for (Context context : contexts) {
			String typeName = context.getTypeShape().getTypeHierarchy().getElement().getName();
			if (typeName.equals("A")) {
				List<Usage> usages = usageExtractor.extract(paFactory.create().compute(context));

				assertEquals(3, usages.size()); // S(A), B, C
				for (Usage usage : usages) {
					String usageTypeName = usage.getType().getName();

					assertEquals("entry1", usage.getMethodContext().getName());
					assertEquals("S", usage.getMethodContext().getDeclaringType().getName());
					assertEquals("S", usage.getClassContext().getName());

					if (usageTypeName.equals("S")) {
						assertEquals(DefinitionSiteKind.THIS, usage.getDefinitionSite().getKind());

						Set<CallSite> callsites = usage.getAllCallsites();
						assertEquals(1, callsites.size());
						CallSite callsite = callsites.iterator().next();
						assertEquals(CallSiteKind.RECEIVER, callsite.getKind());
						assertEquals("fromS", callsite.getMethod().getName());
						assertEquals("S", callsite.getMethod().getDeclaringType().getName());
					} else if (usageTypeName.equals("B")) {
						assertEquals(DefinitionSiteKind.FIELD, usage.getDefinitionSite().getKind());

						Set<CallSite> callsites = usage.getAllCallsites();
						assertEquals(3, callsites.size());
						for (CallSite callsite : callsites) {
							String methodName = callsite.getMethod().getName();
							if (methodName.equals("m1") || methodName.equals("m2")) {
								assertEquals(CallSiteKind.RECEIVER, callsite.getKind());
								assertEquals("B", callsite.getMethod().getDeclaringType().getName());
							} else if (methodName.equals("entry2")) {
								assertEquals(CallSiteKind.PARAMETER, callsite.getKind());
								assertEquals("C", callsite.getMethod().getDeclaringType().getName());
								assertEquals(0, callsite.getArgIndex());
							} else {
								Assert.fail();
							}
						}
					} else if (usageTypeName.equals("C")) {
						assertEquals(DefinitionSiteKind.RETURN, usage.getDefinitionSite().getKind());
						assertEquals("fromS", usage.getDefinitionSite().getMethod().getName());

						Set<CallSite> callsites = usage.getAllCallsites();
						assertEquals(1, callsites.size());
						CallSite callsite = callsites.iterator().next();
						assertEquals(CallSiteKind.RECEIVER, callsite.getKind());
						assertEquals("entry2", callsite.getMethod().getName());
					} else {
						Assert.fail();
					}
				}
			} else if (typeName.equals("C")) {
				List<Usage> usages = usageExtractor.extract(paFactory.create().compute(context));

				assertEquals(3, usages.size()); // B, C, D
				for (Usage usage : usages) {
					String usageTypeName = usage.getType().getName();

					assertEquals("C", usage.getClassContext().getName());

					if (usageTypeName.equals("B")) {
						assertEquals("entry2", usage.getMethodContext().getName());
						assertEquals("C", usage.getMethodContext().getDeclaringType().getName());
						assertEquals(DefinitionSiteKind.PARAM, usage.getDefinitionSite().getKind());
						assertEquals(0, usage.getDefinitionSite().getArgIndex());

						Set<CallSite> callsites = usage.getAllCallsites();
						assertEquals(1, callsites.size());
						CallSite callsite = callsites.iterator().next();
						assertEquals(CallSiteKind.RECEIVER, callsite.getKind());
						assertEquals("m3", callsite.getMethod().getName());
					} else if (usageTypeName.equals("C")) {
						assertEquals("entry2", usage.getMethodContext().getName());
						assertEquals(DefinitionSiteKind.THIS, usage.getDefinitionSite().getKind());

						Set<CallSite> callsites = usage.getAllCallsites();
						assertEquals(1, callsites.size());
						CallSite callsite = callsites.iterator().next();
						assertEquals(CallSiteKind.RECEIVER, callsite.getKind());
						assertEquals("entry3", callsite.getMethod().getName());
					} else if (usageTypeName.equals("D")) {
						assertEquals("entry3", usage.getMethodContext().getName());
						assertEquals(DefinitionSiteKind.NEW, usage.getDefinitionSite().getKind());

						Set<CallSite> callsites = usage.getAllCallsites();
						assertEquals(2, callsites.size());
						for (CallSite callsite : callsites) {
							assertEquals(CallSiteKind.RECEIVER, callsite.getKind());
							assertThat(callsite.getMethod().getName(), Matchers.isOneOf("m4", "m5"));
						}
					} else {
						Assert.fail();
					}
				}
			} else {
				Assert.fail();
			}
		}
	}

	@Test
	public void testStreamTestTypeBased() {
		TestSSTBuilder builder = new TestSSTBuilder();
		PointsToAnalysis pointsToAnalysis = new TypeBasedAnalysis();
		PointsToUsageExtractor usageExtractor = new PointsToUsageExtractor();
		Context context = builder.createStreamTest();

		List<Usage> usages = usageExtractor.extract(pointsToAnalysis.compute(context));
		for (Usage usage : usages) {
			String usageTypeName = usage.getType().getName();
			String methodContextName = usage.getMethodContext().getName();
			assertEquals("CopyTo", methodContextName);

			if (usageTypeName.equals(CORE_STRING_TYPE.getName())) {
				assertEquals(DefinitionSiteKind.PARAM, usage.getDefinitionSite().getKind());
				assertEquals(0, usage.getDefinitionSite().getArgIndex());

				Set<CallSite> callsites = usage.getAllCallsites();
				assertEquals(1, callsites.size());
				CallSite callsite = callsites.iterator().next();
				assertEquals(CallSiteKind.PARAMETER, callsite.getKind());
				assertEquals(0, callsite.getArgIndex());
				IMethodName method = callsite.getMethod();
				assertTrue(method.isInit());
				assertEquals("FileStream", method.getDeclaringType().getName());

			} else if (usageTypeName.equals("FileStream")) {
				DefinitionSite definitionSite = usage.getDefinitionSite();
				assertEquals(DefinitionSiteKind.NEW, definitionSite.getKind());
				assertTrue(definitionSite.getMethod().isConstructor());
				assertEquals("FileStream", definitionSite.getMethod().getDeclaringType().getName());

				Set<CallSite> callsites = usage.getAllCallsites();
				assertEquals(3, callsites.size()); // Read, Write, Close
				for (CallSite callsite : callsites) {
					assertEquals(CallSiteKind.RECEIVER, callsite.getKind());
					assertThat(callsite.getMethod().getName(), Matchers.isOneOf("Read", "Write", "Close"));
				}

			} else {
				assertThat(usageTypeName, Matchers.isOneOf("Byte", "Int32"));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testStreamTestReferenceBased() {
		TestSSTBuilder builder = new TestSSTBuilder();
		PointsToAnalysis pointsToAnalysis = new ReferenceBasedAnalysis();
		PointsToUsageExtractor usageExtractor = new PointsToUsageExtractor();
		// do not prune usages that only have parameter call sites
		usageExtractor.setCallsitePruningBehavior(CallsitePruning.EMPTY_CALLSITES);
		Context context = builder.createStreamTest();

		List<Usage> usages = usageExtractor.extract(pointsToAnalysis.compute(context));
		int fileStreamUsages = 0;
		int stringUsages = 0;
		for (Usage usage : usages) {
			String usageTypeName = usage.getType().getName();

			if (usageTypeName.equals("FileStream")) {
				++fileStreamUsages;
				// analysis cannot infer that the FileStream created in OpenSource is the same
				// as the one returned by
				// the method call in CopyTo
				assertThat(usage.getDefinitionSite().getKind(),
						Matchers.isOneOf(DefinitionSiteKind.NEW, DefinitionSiteKind.RETURN));

				Set<CallSite> callsites = usage.getAllCallsites();
				assertEquals(2, callsites.size());
				assertEquals(callsites.stream().map(c -> c.getKind()).collect(Collectors.toSet()),
						Sets.newHashSet(CallSiteKind.RECEIVER));
				assertThat(callsites.stream().map(c -> c.getMethod().getName()).collect(Collectors.toSet()),
						Matchers.isOneOf(Sets.newHashSet("Read", "Close"), Sets.newHashSet("Write", "Close")));
			} else if (usageTypeName.equals(CORE_STRING_TYPE.getName())) {
				++stringUsages;
				assertThat(usage.getDefinitionSite().getKind(),
						Matchers.isOneOf(DefinitionSiteKind.PARAM, DefinitionSiteKind.FIELD));

				Set<CallSite> callsites = usage.getAllCallsites();
				assertEquals(1, callsites.size());
				CallSite callsite = callsites.iterator().next();
				assertEquals(CallSiteKind.PARAMETER, callsite.getKind());
				assertTrue(callsite.getMethod().isConstructor());
				assertEquals("FileStream", callsite.getMethod().getDeclaringType().getName());
			}
		}

		assertEquals(2, stringUsages);
		assertEquals(2, fileStreamUsages);
	}
}
