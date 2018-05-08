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
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.commons.assertions.Asserts.assertGreaterThan;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCast;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCatchParameter;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.CallParameter;
import cc.kave.rsse.calls.utils.OptionsBuilder;
import cc.kave.rsse.calls.utils.json.JsonUtilsCcKaveRsseCalls;

public class PBNModelStoreTest {

	private static final ITypeName SOME_TYPE = Names.newType("T,P");
	private static final Options OPTS = OptionsBuilder.bmn().calls(0.3).get();
	private static final String OPTS_STR = OPTS.toString();

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	private String rootDir;
	private PBNModelStore sut;

	@Before
	public void setup() throws IOException {
		JsonUtilsCcKaveRsseCalls.registerJsonAdapters();
		rootDir = tmp.newFolder("some_dir").getAbsolutePath();
		assertEquals("APP[bmn]+MCTX+CALLS(0.30)", OPTS_STR);
		sut = new PBNModelStore(rootDir, OPTS);
	}

	@After
	public void teardown() {
		JsonUtils.resetAllConfiguration();
	}

	@Test
	public void afolderIsBeingCreated() throws IOException {
		Assert.assertTrue(new File(rootDir, OPTS_STR).isDirectory());
	}

	@Test
	public void fileNamingIsUsed() throws IOException {
		String path = OPTS_STR + "/local/P/T.json";
		Assert.assertFalse(new File(rootDir, path).exists());
		store("T,P", createModel(3));
		Assert.assertTrue(new File(rootDir, path).exists());
	}

	@Test
	public void hasModelWorks() throws IOException {
		Assert.assertFalse(sut.hasModel(SOME_TYPE));
		sut.store(SOME_TYPE, createModel(3));
		Assert.assertTrue(sut.hasModel(SOME_TYPE));
	}

	@Test
	public void clearingWorks() throws IOException {
		sut.store(SOME_TYPE, createModel(3));
		Assert.assertTrue(sut.hasModel(SOME_TYPE));
		sut.clear();
		Assert.assertFalse(sut.hasModel(SOME_TYPE));
	}

	@Test
	public void getModelWorks() throws IOException {
		PBNModel in = createModel(3);
		sut.store(SOME_TYPE, in);
		PBNModel out = sut.getModel(SOME_TYPE);
		assertEquals(in, out);
	}

	@Test(expected = AssertionException.class)
	public void getModel_unknownType() throws IOException {
		sut.getModel(SOME_TYPE);
	}

	@Test
	public void getModelWorksWithActualModel() throws IOException {
		PBNModel in = createModel(20);
		sut.store(SOME_TYPE, in);
		PBNModel out = sut.getModel(SOME_TYPE);
		assertEquals(in, out);
	}

	@Test
	@Ignore("long running test, run manually on PBNModelStore changes")
	public void hugeModelWorks() throws IOException {
		PBNModel in = createModel(500000);
		sut.store(SOME_TYPE, in);
		PBNModel out = sut.getModel(SOME_TYPE);
		assertEquals(in, out);
	}

	private PBNModel createModel(final int size) {
		assertGreaterThan(size, 1);
		Random rnd = new Random();

		PBNModel in = new PBNModel();
		in.type = Names.newType("T, P");
		in.numObservations = rnd.nextInt(size);

		in.classContexts = new ITypeName[] { t(1), t(2) };
		in.methodContexts = new IMethodName[] { m(1), m(2) };
		in.definitions = new IDefinition[] { definedByCast(), definedByCatchParameter() };
		in.callParameters = new ICallParameter[] { new CallParameter(m(3), 0), new CallParameter(m(4), 0) };
		in.members = new IMemberName[] { m(5), m(6) };

		in.patternProbabilities = getProbabilities(rnd, size);
		in.classContextProbabilities = getProbabilities(rnd, 2 * size);
		in.methodContextProbabilities = getProbabilities(rnd, 2 * size);
		in.definitionProbabilities = getProbabilities(rnd, 2 * size);
		in.callParameterProbabilityTrue = getProbabilities(rnd, 2 * size);
		in.memberProbabilityTrue = getProbabilities(rnd, 2 * size);

		return in;
	}

	private static IMethodName m(int i) {
		return newMethod("[p:void] [T, P].m%d([p:int] p)", i);
	}

	private static ITypeName t(int i) {
		return newType("T%d, P", i);
	}

	private double[] getProbabilities(Random rnd, int num) {
		return rnd.doubles(0, 1).limit(num).toArray();
	}

	private void store(String id, PBNModel m) {
		ITypeName t = Names.newType(id);
		sut.store(t, m);
	}
}