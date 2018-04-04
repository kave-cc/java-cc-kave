/**
 * Copyright 2016 Simon Reuß
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

import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.fieldReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.propertyReference;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.AnalysesProvider;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.rsse.calls.usages.DefinitionSite;
import cc.kave.rsse.calls.usages.DefinitionSites;
import cc.kave.rsse.calls.usages.IUsage;

@RunWith(Parameterized.class)
public class PointsToUsageExtractorTest extends UsageExtractionTest {

	@Parameters
	public static Collection<Object[]> data() {
		return AnalysesProvider.ANALYSES_AS_PARAMETERS;
	}

	private final PointsToAnalysisFactory analysisFactory;

	public PointsToUsageExtractorTest(PointsToAnalysisFactory analysisFactory) {
		this.analysisFactory = analysisFactory;
	}

	@Override
	protected PointsToAnalysis createAnalysis() {
		return analysisFactory.create();
	}

	private ITypeName enclosingType() {
		return type("ET");
	}

	private IMethodName enclosingMethod(ITypeName... parameters) {
		return method(enclosingType(), "Entry", parameters);
	}

	private List<IUsage> createDefinitionSiteUsage(IMethodName enclosingMethod, DefinitionSite definitionSite) {
		return Arrays.asList(usage(type("A"), enclosingMethod, enclosingType(), definitionSite,
				Sets.newHashSet(callSite(method(type("A"), "M1")))));
	}

	@Test
	public void expectConstantDefinitionSite() {
		// A x = constant
		// x = x.M1()

		IMethodDeclaration enclosingMethod = declareMethod(enclosingMethod(), true, declare("x", type("A")),
				assign("x", constantExpr()), exprStmt(invoke("x", method(type("A"), "M1"))));
		Context cxt = context(enclosingType(), ImmutableSet.of(enclosingMethod), Collections.emptySet(),
				Collections.emptySet());

		List<IUsage> expectedUsages = createDefinitionSiteUsage(enclosingMethod.getName(),
				DefinitionSites.createDefinitionByConstant());
		assertThat(extract(cxt), Matchers.is(expectedUsages));
	}

	@Test
	public void expectParameterDefinitionSite() {
		// p0.M1()

		IMethodDeclaration enclosingMethod = declareMethod(enclosingMethod(type("A")), true,
				exprStmt(invoke("p0", method(type("A"), "M1"))));
		Context cxt = context(enclosingType(), ImmutableSet.of(enclosingMethod), Collections.emptySet(),
				Collections.emptySet());

		List<IUsage> expectedUsages = createDefinitionSiteUsage(enclosingMethod.getName(),
				parameterDefinitionSite(enclosingMethod.getName(), 0));
		assertThat(extract(cxt), Matchers.is(expectedUsages));
	}

	@Test
	public void expectReturnDefinitionSite() {
		// A x = p0.GetA()
		// x.M1()

		IMethodDeclaration enclosingMethod = declareMethod(enclosingMethod(type("B")), true, declare("x", type("A")),
				assign("x", invoke("p0", method(type("A"), type("B"), "GetA"))),
				exprStmt(invoke("x", method(type("A"), "M1"))));
		Context cxt = context(enclosingType(), ImmutableSet.of(enclosingMethod), Collections.emptySet(),
				Collections.emptySet());

		List<IUsage> expectedUsages = createDefinitionSiteUsage(enclosingMethod.getName(),
				returnDefinitionSite(method(type("A"), type("B"), "GetA")));
		List<IUsage> extractedUsages = extract(cxt).stream().filter(u -> u.getType().getName().equals("A"))
				.collect(Collectors.toList());
		assertThat(extractedUsages, Matchers.is(expectedUsages));
	}

	@Test
	public void expectFieldDefinitionSite() {
		// A x = f0
		// x.M1()

		IMethodDeclaration enclosingMethod = declareMethod(enclosingMethod(), true, declare("x", type("A")),
				assign("x", refExpr(fieldReference(field(type("A"), enclosingType(), 0)))),
				exprStmt(invoke("x", method(type("A"), "M1"))));
		Context cxt = context(enclosingType(), ImmutableSet.of(enclosingMethod),
				ImmutableSet.of(declare(field(type("A"), enclosingType(), 0))), Collections.emptySet());

		List<IUsage> expectedUsages = createDefinitionSiteUsage(enclosingMethod.getName(),
				fieldDefinitionSite(field(type("A"), enclosingType(), 0)));
		assertThat(extract(cxt), Matchers.is(expectedUsages));
	}

	@Test
	public void expectPropertyDefinitionSite() {
		// A x = p0
		// x.M1()

		IMethodDeclaration enclosingMethod = declareMethod(enclosingMethod(), true, declare("x", type("A")),
				assign("x", refExpr(propertyReference(property(type("A"), enclosingType(), 0)))),
				exprStmt(invoke("x", method(type("A"), "M1"))));
		Context cxt = context(enclosingType(), ImmutableSet.of(enclosingMethod), Collections.emptySet(),
				ImmutableSet.of(declare(property(type("A"), enclosingType(), 0))));

		List<IUsage> expectedUsages = createDefinitionSiteUsage(enclosingMethod.getName(),
				propertyDefinitionSite(property(type("A"), enclosingType(), 0)));
		assertThat(extract(cxt), Matchers.is(expectedUsages));
	}
}