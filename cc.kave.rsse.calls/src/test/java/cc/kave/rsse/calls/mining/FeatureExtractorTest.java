/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.rsse.calls.mining;

import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_CCF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_DF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_MCF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_TF;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCast;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCatchParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstant;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByLoopHeader;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccessToField;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByReturnValue;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.fieldAccess;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.IUsageSite;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.utils.OptionsBuilder;

public class FeatureExtractorTest {

	private Options opts;

	@Before
	public void setup() {
		opts = enableAll().get();
	}

	private static OptionsBuilder enableAll() {
		return new OptionsBuilder("...").cCtx(true).mCtx(true).def(true).calls(true).params(true).members(true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void null_crashForSingleUsages() {
		new FeatureExtractor(opts).extract((IUsage) null);
	}

	@Test
	public void null_ignoredInMultiUsages() {
		List<IUsage> usages = new LinkedList<>();
		usages.add(null);
		List<List<IFeature>> actuals = new FeatureExtractor(opts).extract(usages);
		List<List<IFeature>> expecteds = new LinkedList<>();
		assertEquals(expecteds, actuals);
	}

	@Test
	public void null_unsetFieldsAreSetToUnknown() {
		List<IFeature> fs = new LinkedList<>();
		fs.add(UNKNOWN_TF);
		fs.add(UNKNOWN_CCF);
		fs.add(UNKNOWN_MCF);
		fs.add(UNKNOWN_DF);

		assertFeatures(new Usage(), fs);
	}

	@Test
	public void null_ignoreSites() {
		List<IFeature> fs = new LinkedList<>();
		fs.add(UNKNOWN_TF);
		fs.add(UNKNOWN_CCF);
		fs.add(UNKNOWN_MCF);
		fs.add(UNKNOWN_DF);

		Usage u = new Usage();
		u.usageSites.add(null);

		assertFeatures(u, fs);
	}

	@Test
	public void type() {
		ITypeName type = newType("p:int");
		assertType(type, new TypeFeature(type));
	}

	@Test
	public void type_disabled() {
		// impossible, TypeFeatures are always kept
	}

	@Test
	public void type_local() {
		ITypeName type = newType("T, P");
		assertType(type, UNKNOWN_TF);
	}

	@Test
	public void cctx() {
		ITypeName type = newType("p:int");
		assertCCtx(type, new ClassContextFeature(type));
	}

	@Test
	public void cctx_disabled() {
		opts = enableAll().cCtx(false).get();
		ITypeName type = newType("p:int");
		assertCCtx(type, UNKNOWN_CCF);
	}

	@Test
	public void cctx_local() {
		ITypeName type = newType("T, P");
		assertCCtx(type, UNKNOWN_CCF);
	}

	@Test
	public void mctx() {
		IMethodName m = Names.newMethod("[p:void] [p:int].m([p:int] p)");
		assertMCtx(m, new MethodContextFeature(m));
	}

	@Test
	public void mctx_disabled() {
		opts = enableAll().mCtx(false).get();
		IMethodName m = newMethod("[p:void] [p:int].m()");
		assertMCtx(m, UNKNOWN_MCF);
	}

	@Test
	public void mctx_local() {
		IMethodName m = newMethod("[p:void] [T, P].m()");
		assertMCtx(m, UNKNOWN_MCF);
	}

	@Test
	public void def() {
		IDefinition d = definedByReturnValue("[p:bool] [p:int].m()");
		assertDef(d, new DefinitionFeature(d));
	}

	@Test
	public void def_disabled() {
		opts = enableAll().def(false).get();
		IDefinition d = definedByReturnValue("[p:bool] [p:int].m()");
		assertDef(d, UNKNOWN_DF);
	}

	@Test
	public void def_local_any() {
		IDefinition d = definedByConstant();
		assertDef(d, new DefinitionFeature(d));
	}

	@Test
	public void def_localMethod() {
		IDefinition d = definedByReturnValue("[p:bool] [T, P].m()");
		assertDef(d, UNKNOWN_DF);
	}

	@Test
	public void def_localField() {
		IDefinition d = definedByMemberAccessToField("[p:bool] [T, P]._f");
		assertDef(d, UNKNOWN_DF);
	}

	@Test
	public void def_nonLocalField() {
		IDefinition d = definedByMemberAccessToField("[p:bool] [p:int]._f");
		assertDef(d, new DefinitionFeature(d));
	}

	@Test
	public void def_local_loopHeader() {
		IDefinition d = definedByLoopHeader();
		assertDef(d, new DefinitionFeature(d));
	}

	@Test
	public void def_local_catchParam() {
		IDefinition d = definedByCatchParameter();
		assertDef(d, new DefinitionFeature(d));
	}

	@Test
	public void def_local_cast() {
		IDefinition d = definedByCast();
		assertDef(d, new DefinitionFeature(d));
	}

	@Test
	public void def_outParam() {
		IDefinition d = Definitions.definedByOutParameter("[p:void] [p:object].m(out [p:char] c)");
		assertDef(d, new DefinitionFeature(d));
	}

	@Test
	public void def_outParam_local() {
		IDefinition d = Definitions.definedByOutParameter("[p:void] [T,P].m(out [p:int] p)");
		assertDef(d, UNKNOWN_DF);
	}

	@Test
	public void usCall() {
		IUsageSite us = call("[p:void] [p:int].m()");
		assertSites(asList(us), asList(new UsageSiteFeature(us)));
	}

	@Test
	public void usCall_disabled() {
		opts = enableAll().calls(false).get();
		IUsageSite us = call("[p:void] [p:int].m()");
		assertSites(asList(us), asList());
	}

	@Test
	public void usCall_local() {
		IUsageSite us = call("[p:void] [T, P].m()");
		assertSites(asList(us), asList());
	}

	@Test
	public void usParam() {
		IUsageSite us = callParameter("[p:void] [p:int].m([p:int] p)", 0);
		assertSites(asList(us), asList(new UsageSiteFeature(us)));
	}

	@Test
	public void usParam_disabled() {
		opts = enableAll().params(false).get();
		IUsageSite us = callParameter("[p:void] [p:int].m([p:int] p)", 0);
		assertSites(asList(us), asList());
	}

	@Test
	public void usParam_local() {
		IUsageSite us = callParameter("[p:void] [T, P].m([p:int] p)", 0);
		assertSites(asList(us), asList());
	}

	@Test
	public void usMember() {
		IUsageSite us = fieldAccess("[p:void] [p:int]._f");
		assertSites(asList(us), asList(new UsageSiteFeature(us)));
	}

	@Test
	public void usMember_disabled() {
		opts = enableAll().members(false).get();
		IUsageSite us = fieldAccess("[p:void] [p:int]._f");
		assertSites(asList(us), asList());
	}

	@Test
	public void usMember_local() {
		IUsageSite us = fieldAccess("[p:void] [T, P]._f");
		assertSites(asList(us), asList());
	}

	@Test
	public void us_repeatedEntriesArePreserved() {
		IUsageSite us1 = call("[p:void] [p:int].m()");
		IUsageSite us2 = callParameter("[p:void] [p:int].m([p:int] p)", 0);
		IUsageSite us3 = fieldAccess("[p:void] [p:int]._f");
		UsageSiteFeature usf1 = new UsageSiteFeature(us1);
		UsageSiteFeature usf2 = new UsageSiteFeature(us2);
		UsageSiteFeature usf3 = new UsageSiteFeature(us3);
		assertSites(asList(us1, us2, us3, us1, us2, us3), asList(usf1, usf2, usf3, usf1, usf2, usf3));
	}

	private void assertFeatures(Usage u, List<IFeature> expected) {
		FeatureExtractor sut = new FeatureExtractor(opts);
		List<IFeature> actual = sut.extract(u);
		Assert.assertEquals(expected, actual);

		List<List<IFeature>> expecteds = new LinkedList<>();
		expecteds.add(expected);
		Assert.assertEquals(expecteds, sut.extract(asList(u)));
		expecteds.add(expected);
		Assert.assertEquals(expecteds, sut.extract(asList(u, u)));
	}

	private void assertType(ITypeName type, TypeFeature tf) {
		List<IFeature> fs = new LinkedList<>();
		fs.add(tf);
		fs.add(UNKNOWN_CCF);
		fs.add(UNKNOWN_MCF);
		fs.add(UNKNOWN_DF);

		Usage u = new Usage();
		u.type = type;

		assertFeatures(u, fs);
	}

	private void assertCCtx(ITypeName t, ClassContextFeature ccf) {
		List<IFeature> fs = new LinkedList<>();
		fs.add(UNKNOWN_TF);
		fs.add(ccf);
		fs.add(UNKNOWN_MCF);
		fs.add(UNKNOWN_DF);

		Usage u = new Usage();
		u.classCtx = t;

		assertFeatures(u, fs);
	}

	private void assertMCtx(IMethodName m, MethodContextFeature mcf) {
		List<IFeature> fs = new LinkedList<>();
		fs.add(UNKNOWN_TF);
		fs.add(UNKNOWN_CCF);
		fs.add(mcf);
		fs.add(UNKNOWN_DF);

		Usage u = new Usage();
		u.methodCtx = m;

		assertFeatures(u, fs);
	}

	private void assertDef(IDefinition d, DefinitionFeature df) {
		List<IFeature> fs = new LinkedList<>();
		fs.add(UNKNOWN_TF);
		fs.add(UNKNOWN_CCF);
		fs.add(UNKNOWN_MCF);
		fs.add(df);

		Usage u = new Usage();
		u.definition = d;

		assertFeatures(u, fs);
	}

	private void assertSites(List<IUsageSite> sites, List<UsageSiteFeature> usfs) {
		List<IFeature> fs = new LinkedList<>();
		fs.add(UNKNOWN_TF);
		fs.add(UNKNOWN_CCF);
		fs.add(UNKNOWN_MCF);
		fs.add(UNKNOWN_DF);
		fs.addAll(usfs);

		Usage u = new Usage();
		u.usageSites.addAll(sites);

		assertFeatures(u, fs);
	}
}