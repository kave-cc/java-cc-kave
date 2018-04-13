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
package cc.kave.rsse.calls.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
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
import cc.kave.rsse.calls.analysis.CallsitePruning;
import cc.kave.rsse.calls.analysis.PointsToUsageExtractor;
import cc.kave.rsse.calls.model.usages.DefinitionType;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.IUsageSite;
import cc.kave.rsse.calls.model.usages.UsageSiteType;

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
				List<IUsage> usages = usageExtractor.extract(paFactory.create().compute(context));

				assertEquals(3, usages.size()); // S(A), B, C
				for (IUsage usage : usages) {
					String usageTypeName = usage.getType().getName();

					assertEquals("entry1", usage.getMethodContext().getName());
					assertEquals("S", usage.getMethodContext().getDeclaringType().getName());
					assertEquals("S", usage.getClassContext().getName());

					if (usageTypeName.equals("S")) {
						assertEquals(DefinitionType.THIS, usage.getDefinition().getType());

						List<IUsageSite> callsites = usage.getUsageSites();
						assertEquals(1, callsites.size());
						IUsageSite callsite = callsites.iterator().next();
						assertEquals(UsageSiteType.CALL_RECEIVER, callsite.getType());
						assertEquals("fromS", callsite.getMember(IMethodName.class).getName());
						assertEquals("S", callsite.getMember(IMethodName.class).getDeclaringType().getName());
					} else if (usageTypeName.equals("B")) {
						assertEquals(DefinitionType.MEMBER_ACCESS, usage.getDefinition().getType());

						List<IUsageSite> callsites = usage.getUsageSites();
						assertEquals(3, callsites.size());
						for (IUsageSite callsite : callsites) {
							String methodName = callsite.getMember(IMethodName.class).getName();
							if (methodName.equals("m1") || methodName.equals("m2")) {
								assertEquals(UsageSiteType.CALL_RECEIVER, callsite.getType());
								assertEquals("B", callsite.getMember(IMethodName.class).getDeclaringType().getName());
							} else if (methodName.equals("entry2")) {
								assertEquals(UsageSiteType.CALL_PARAMETER, callsite.getType());
								assertEquals("C", callsite.getMember(IMethodName.class).getDeclaringType().getName());
								assertEquals(0, callsite.getArgIndex());
							} else {
								Assert.fail();
							}
						}
					} else if (usageTypeName.equals("C")) {
						assertEquals(DefinitionType.RETURN_VALUE, usage.getDefinition().getType());
						assertEquals("fromS", usage.getDefinition().getMember(IMethodName.class).getName());

						List<IUsageSite> callsites = usage.getUsageSites();
						assertEquals(1, callsites.size());
						IUsageSite callsite = callsites.iterator().next();
						assertEquals(UsageSiteType.CALL_RECEIVER, callsite.getType());
						assertEquals("entry2", callsite.getMember(IMethodName.class).getName());
					} else {
						Assert.fail();
					}
				}
			} else if (typeName.equals("C")) {
				List<IUsage> usages = usageExtractor.extract(paFactory.create().compute(context));

				assertEquals(3, usages.size()); // B, C, D
				for (IUsage usage : usages) {
					String usageTypeName = usage.getType().getName();

					assertEquals("C", usage.getClassContext().getName());

					if (usageTypeName.equals("B")) {
						assertEquals("entry2", usage.getMethodContext().getName());
						assertEquals("C", usage.getMethodContext().getDeclaringType().getName());
						assertEquals(DefinitionType.METHOD_PARAMETER, usage.getDefinition().getType());
						assertEquals(0, usage.getDefinition().getArgIndex());

						List<IUsageSite> callsites = usage.getUsageSites();
						assertEquals(1, callsites.size());
						IUsageSite callsite = callsites.iterator().next();
						assertEquals(UsageSiteType.CALL_RECEIVER, callsite.getType());
						assertEquals("m3", callsite.getMember(IMethodName.class).getName());
					} else if (usageTypeName.equals("C")) {
						assertEquals("entry2", usage.getMethodContext().getName());
						assertEquals(DefinitionType.THIS, usage.getDefinition().getType());

						List<IUsageSite> callsites = usage.getUsageSites();
						assertEquals(1, callsites.size());
						IUsageSite callsite = callsites.iterator().next();
						assertEquals(UsageSiteType.CALL_RECEIVER, callsite.getType());
						assertEquals("entry3", callsite.getMember(IMethodName.class).getName());
					} else if (usageTypeName.equals("D")) {
						assertEquals("entry3", usage.getMethodContext().getName());
						assertEquals(DefinitionType.NEW, usage.getDefinition().getType());

						List<IUsageSite> callsites = usage.getUsageSites();
						assertEquals(2, callsites.size());
						for (IUsageSite callsite : callsites) {
							assertEquals(UsageSiteType.CALL_RECEIVER, callsite.getType());
							assertThat(callsite.getMember(IMethodName.class).getName(), Matchers.isOneOf("m4", "m5"));
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

		List<IUsage> usages = usageExtractor.extract(pointsToAnalysis.compute(context));
		for (IUsage usage : usages) {
			String usageTypeName = usage.getType().getName();
			String methodContextName = usage.getMethodContext().getName();
			assertEquals("CopyTo", methodContextName);

			if (usageTypeName.equals(CORE_STRING_TYPE.getName())) {
				assertEquals(DefinitionType.METHOD_PARAMETER, usage.getDefinition().getType());
				assertEquals(0, usage.getDefinition().getArgIndex());

				List<IUsageSite> callsites = usage.getUsageSites();
				assertEquals(1, callsites.size());
				IUsageSite callsite = callsites.iterator().next();
				assertEquals(UsageSiteType.CALL_PARAMETER, callsite.getType());
				assertEquals(0, callsite.getArgIndex());
				IMethodName method = callsite.getMember(IMethodName.class);
				assertTrue(method.isInit());
				assertEquals("FileStream", method.getDeclaringType().getName());

			} else if (usageTypeName.equals("FileStream")) {
				IDefinition definitionSite = usage.getDefinition();
				assertEquals(DefinitionType.NEW, definitionSite.getType());
				assertTrue(definitionSite.getMember(IMethodName.class).isConstructor());
				assertEquals("FileStream", definitionSite.getMember(IMethodName.class).getDeclaringType().getName());

				List<IUsageSite> callsites = usage.getUsageSites();
				assertEquals(3, callsites.size()); // Read, Write, Close
				for (IUsageSite callsite : callsites) {
					assertEquals(UsageSiteType.CALL_RECEIVER, callsite.getType());
					assertThat(callsite.getMember(IMethodName.class).getName(),
							Matchers.isOneOf("Read", "Write", "Close"));
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

		List<IUsage> usages = usageExtractor.extract(pointsToAnalysis.compute(context));
		int fileStreamUsages = 0;
		int stringUsages = 0;
		for (IUsage usage : usages) {
			String usageTypeName = usage.getType().getName();

			if (usageTypeName.equals("FileStream")) {
				++fileStreamUsages;
				// analysis cannot infer that the FileStream created in OpenSource is the same
				// as the one returned by
				// the method call in CopyTo
				assertThat(usage.getDefinition().getType(),
						Matchers.isOneOf(DefinitionType.NEW, DefinitionType.RETURN_VALUE));

				List<IUsageSite> callsites = usage.getUsageSites();
				assertEquals(2, callsites.size());
				assertEquals(callsites.stream().map(c -> c.getType()).collect(Collectors.toSet()),
						Sets.newHashSet(UsageSiteType.CALL_RECEIVER));
				assertThat(
						callsites.stream().map(c -> c.getMember(IMethodName.class).getName())
								.collect(Collectors.toSet()),
						Matchers.isOneOf(Sets.newHashSet("Read", "Close"), Sets.newHashSet("Write", "Close")));
			} else if (usageTypeName.equals(CORE_STRING_TYPE.getName())) {
				++stringUsages;
				assertThat(usage.getDefinition().getType(),
						Matchers.isOneOf(DefinitionType.METHOD_PARAMETER, DefinitionType.MEMBER_ACCESS));

				List<IUsageSite> callsites = usage.getUsageSites();
				assertEquals(1, callsites.size());
				IUsageSite callsite = callsites.iterator().next();
				assertEquals(UsageSiteType.CALL_PARAMETER, callsite.getType());
				assertTrue(callsite.getMember(IMethodName.class).isConstructor());
				assertEquals("FileStream", callsite.getMember(IMethodName.class).getDeclaringType().getName());
			}
		}

		assertEquals(2, stringUsages);
		assertEquals(2, fileStreamUsages);
	}
}