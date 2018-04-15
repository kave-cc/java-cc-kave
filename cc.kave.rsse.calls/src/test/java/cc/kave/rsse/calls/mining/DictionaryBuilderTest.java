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

import static cc.kave.commons.model.naming.Names.getUnknownMethod;
import static cc.kave.commons.model.naming.Names.getUnknownType;
import static cc.kave.commons.model.naming.Names.newField;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.rsse.calls.mining.DictionaryBuilder.DUMMY_CCF;
import static cc.kave.rsse.calls.mining.DictionaryBuilder.DUMMY_DF;
import static cc.kave.rsse.calls.mining.DictionaryBuilder.DUMMY_MCF;
import static cc.kave.rsse.calls.mining.DictionaryBuilder.UNKNOWN_CCF;
import static cc.kave.rsse.calls.mining.DictionaryBuilder.UNKNOWN_DF;
import static cc.kave.rsse.calls.mining.DictionaryBuilder.UNKNOWN_MCF;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCatchParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByLoopHeader;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByReturnValue;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.fieldAccess;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.utils.OptionsBuilder;

public class DictionaryBuilderTest {

	private Options opts;

	private List<List<IFeature>> in;

	@Before
	public void setup() {
		opts = atLeast(1).get();
		in = Lists.newLinkedList();
	}

	private OptionsBuilder atLeast(int numAtLeast) {
		return new OptionsBuilder("...").atLeast(numAtLeast).cCtx(true).mCtx(true).def(true).calls(true).params(true)
				.members(true);
	}

	private void add(IFeature... fs) {
		in.add(new LinkedList<>(asList(fs)));
	}

	private void assertDict(IFeature... ts) {
		DictionaryBuilder sut = new DictionaryBuilder(opts);
		Dictionary<IFeature> actual = sut.build(in);
		Dictionary<IFeature> expected = new Dictionary<>();
		expected.addAll(asList(DUMMY_CCF, DUMMY_MCF, DUMMY_DF, UNKNOWN_CCF, UNKNOWN_MCF, UNKNOWN_DF));
		expected.addAll(asList(ts));
		assertEquals(expected, actual);
	}

	@Test
	public void alwaysContainsBasicFeatures() {
		// We remove features, so we need to make sure that the dictionary preserves
		// "alternatives" for exclusive feature types or some recommender would
		// fail to build their models.

		ITypeName dummyType = Names.newType("D,P");
		IMethodName dummyMethod = Names.newMethod("[R,P] [D,P].m()");

		assertEquals(new ClassContextFeature(getUnknownType()), UNKNOWN_CCF);
		assertEquals(new MethodContextFeature(getUnknownMethod()), UNKNOWN_MCF);
		assertEquals(new DefinitionFeature(definedByUnknown()), UNKNOWN_DF);

		assertEquals(new ClassContextFeature(dummyType), DUMMY_CCF);
		assertEquals(new MethodContextFeature(dummyMethod), DUMMY_MCF);
		assertEquals(new DefinitionFeature(definedByReturnValue(dummyMethod)), DUMMY_DF);

		assertDict(); // default are added automatically
	}

	@Test
	public void dropLocal_TypeIsNotDropped() {
		IFeature f1 = new TypeFeature(newType("p:object"));
		IFeature f2 = new TypeFeature(newType("T, P"));
		add(f1);
		add(f2);
		assertDict(f1, f2);
	}

	@Test
	public void dropLocal_ClassContexts() {
		IFeature f1 = new ClassContextFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("T, P"));
		add(f1);
		add(f2);
		assertDict(f1);
	}

	@Test
	public void dropLocal_MethodContexts() {
		IFeature f1 = new MethodContextFeature(newMethod("[p:void] [p:object].m()"));
		IFeature f2 = new MethodContextFeature(newMethod("[p:void] [T, P].m()"));
		add(f1);
		add(f2);
		assertDict(f1);
	}

	@Test
	public void dropLocal_Definitions_localMembers() {
		IFeature f1 = new DefinitionFeature(definedByReturnValue(newMethod("[p:int] [p:object].m()")));
		IFeature f2 = new DefinitionFeature(definedByReturnValue(newMethod("[p:int] [T, P].m()")));
		add(f1);
		add(f2);
		assertDict(f1);
	}

	@Test
	public void dropLocal_Definitions_loopHeader() {
		IFeature f1 = new DefinitionFeature(definedByLoopHeader(newType("p:object")));
		IFeature f2 = new DefinitionFeature(definedByLoopHeader(newType("T, P")));
		add(f1);
		add(f2);
		assertDict(f1);
	}

	@Test
	public void dropLocal_Definitions_catch() {
		IFeature f1 = new DefinitionFeature(definedByCatchParameter(newType("p:object")));
		IFeature f2 = new DefinitionFeature(definedByCatchParameter(newType("T, P")));
		add(f1);
		add(f2);
		assertDict(f1);
	}

	@Test
	public void dropLocal_UsagesSites() {
		IFeature f1 = new UsageSiteFeature(call(newMethod("[p:int] [p:object].m()")));
		IFeature f2 = new UsageSiteFeature(call(newMethod("[p:int] [T, P].m()")));
		add(f1);
		add(f2);
		assertDict(f1);
	}

	@Ignore
	@Test
	public void dropUnknown_thinkAboutWhatToDrop() {
		// everything with a "?" in the identifier?
		fail();
	}

	@Test
	public void add_1() {
		IFeature f1 = new ClassContextFeature(newType("p:object"));
		add(f1);
		assertDict(f1);
	}

	@Test
	public void add_2() {
		opts = atLeast(2).get();
		IFeature f1 = new ClassContextFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:string"));
		add(f1);
		add(f2);
		add(f2);
		assertDict(f2);
	}

	@Test
	public void add_doesNotCountTwicePerUsage() {
		opts = atLeast(2).get();
		IFeature f1 = new ClassContextFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:string"));
		add(f1, f1);
		add(f2);
		add(f2);
		assertDict(f2);
	}

	@Test
	public void add_neverDropTypeFeatures() {
		opts = atLeast(2).get();
		IFeature f1 = new TypeFeature(newType("p:object"));
		add(f1);
		assertDict(f1);
	}

	@Test
	public void dropDisabled_onlyClassCtx() {
		opts = atLeast(1).cCtx(true).mCtx(false).def(false).calls(false).params(false).members(false).get();
		IFeature f1 = new TypeFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:object"));
		IFeature f3 = new MethodContextFeature(newMethod("[p:void] [p:object].m()"));
		IFeature f4 = new DefinitionFeature(definedByReturnValue(newMethod("[p:int] [p:object].m()")));
		IFeature f5 = new UsageSiteFeature(call(newMethod("[p:int] [p:object].m()")));
		IFeature f6 = new UsageSiteFeature(callParameter(newMethod("[p:int] [p:object].m([p:bool] p)"), 1));
		IFeature f7 = new UsageSiteFeature(fieldAccess(newField("[p:int] [p:object]._f")));
		add(f1, f2, f3, f4, f5, f6, f7);
		assertDict(f1, f2);
	}

	@Test
	public void dropDisabled_onlyMethodCtx() {
		opts = atLeast(1).cCtx(false).mCtx(true).def(false).calls(false).params(false).members(false).get();
		IFeature f1 = new TypeFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:object"));
		IFeature f3 = new MethodContextFeature(newMethod("[p:void] [p:object].m()"));
		IFeature f4 = new DefinitionFeature(definedByReturnValue(newMethod("[p:int] [p:object].m()")));
		IFeature f5 = new UsageSiteFeature(call(newMethod("[p:int] [p:object].m()")));
		IFeature f6 = new UsageSiteFeature(callParameter(newMethod("[p:int] [p:object].m([p:bool] p)"), 1));
		IFeature f7 = new UsageSiteFeature(fieldAccess(newField("[p:int] [p:object]._f")));
		add(f1, f2, f3, f4, f5, f6, f7);
		assertDict(f1, f3);
	}

	@Test
	public void dropDisabled_onlyDef() {
		opts = atLeast(1).cCtx(false).mCtx(false).def(true).calls(false).params(false).members(false).get();
		IFeature f1 = new TypeFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:object"));
		IFeature f3 = new MethodContextFeature(newMethod("[p:void] [p:object].m()"));
		IFeature f4 = new DefinitionFeature(definedByReturnValue(newMethod("[p:int] [p:object].m()")));
		IFeature f5 = new UsageSiteFeature(call(newMethod("[p:int] [p:object].m()")));
		IFeature f6 = new UsageSiteFeature(callParameter(newMethod("[p:int] [p:object].m([p:bool] p)"), 1));
		IFeature f7 = new UsageSiteFeature(fieldAccess(newField("[p:int] [p:object]._f")));
		add(f1, f2, f3, f4, f5, f6, f7);
		assertDict(f1, f4);
	}

	@Test
	public void dropDisabled_onlyCalls() {
		opts = atLeast(1).cCtx(false).mCtx(false).def(false).calls(true).params(false).members(false).get();
		IFeature f1 = new TypeFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:object"));
		IFeature f3 = new MethodContextFeature(newMethod("[p:void] [p:object].m()"));
		IFeature f4 = new DefinitionFeature(definedByReturnValue(newMethod("[p:int] [p:object].m()")));
		IFeature f5 = new UsageSiteFeature(call(newMethod("[p:int] [p:object].m()")));
		IFeature f6 = new UsageSiteFeature(callParameter(newMethod("[p:int] [p:object].m([p:bool] p)"), 1));
		IFeature f7 = new UsageSiteFeature(fieldAccess(newField("[p:int] [p:object]._f")));
		add(f1, f2, f3, f4, f5, f6, f7);
		assertDict(f1, f5);
	}

	@Test
	public void dropDisabled_onlyParams() {
		opts = atLeast(1).cCtx(false).mCtx(false).def(false).calls(false).params(true).members(false).get();
		IFeature f1 = new TypeFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:object"));
		IFeature f3 = new MethodContextFeature(newMethod("[p:void] [p:object].m()"));
		IFeature f4 = new DefinitionFeature(definedByReturnValue(newMethod("[p:int] [p:object].m()")));
		IFeature f5 = new UsageSiteFeature(call(newMethod("[p:int] [p:object].m()")));
		IFeature f6 = new UsageSiteFeature(callParameter(newMethod("[p:int] [p:object].m([p:bool] p)"), 1));
		IFeature f7 = new UsageSiteFeature(fieldAccess(newField("[p:int] [p:object]._f")));
		add(f1, f2, f3, f4, f5, f6, f7);
		assertDict(f1, f6);
	}

	@Test
	public void dropDisabled_onlyMembers() {
		opts = atLeast(1).cCtx(false).mCtx(false).def(false).calls(false).params(false).members(true).get();
		IFeature f1 = new TypeFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:object"));
		IFeature f3 = new MethodContextFeature(newMethod("[p:void] [p:object].m()"));
		IFeature f4 = new DefinitionFeature(definedByReturnValue(newMethod("[p:int] [p:object].m()")));
		IFeature f5 = new UsageSiteFeature(call(newMethod("[p:int] [p:object].m()")));
		IFeature f6 = new UsageSiteFeature(callParameter(newMethod("[p:int] [p:object].m([p:bool] p)"), 1));
		IFeature f7 = new UsageSiteFeature(fieldAccess(newField("[p:int] [p:object]._f")));
		add(f1, f2, f3, f4, f5, f6, f7);
		assertDict(f1, f7);
	}
}