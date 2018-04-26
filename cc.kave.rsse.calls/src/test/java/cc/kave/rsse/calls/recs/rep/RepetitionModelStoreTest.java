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
package cc.kave.rsse.calls.recs.rep;

import static cc.kave.commons.model.naming.Names.newType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.utils.json.JsonUtilsCcKaveRsseCalls;

public class RepetitionModelStoreTest {

	private static final ITypeName SOME_TYPE = Names.newType("T,P");

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	private File rootDir;
	private RepetitionModelStore sut;

	@Before
	public void setup() throws IOException {
		JsonUtilsCcKaveRsseCalls.registerJsonAdapters();
		rootDir = new File(tmp.getRoot().getAbsolutePath(), "some_dir");
		assertFalse(rootDir.exists());
		sut = new RepetitionModelStore(rootDir);
	}

	@After
	public void teardown() {
		JsonUtils.resetAllConfiguration();
	}

	@Test
	public void afolderIsBeingCreated() throws IOException {
		Assert.assertTrue(rootDir.isDirectory());
	}

	@Test
	public void fileNamingIsUsed() throws IOException {
		String path = "/local/P/T.json";
		Assert.assertFalse(new File(rootDir, path).exists());
		sut.store(newType("T,P"), createModel());
		Assert.assertTrue(new File(rootDir, path).exists());
	}

	@Test
	public void hasModelWorks() throws IOException {
		Assert.assertFalse(sut.hasModel(SOME_TYPE));
		sut.store(SOME_TYPE, createModel());
		Assert.assertTrue(sut.hasModel(SOME_TYPE));
	}

	@Test
	public void clearingWorks() throws IOException {
		sut.store(SOME_TYPE, createModel());
		Assert.assertTrue(sut.hasModel(SOME_TYPE));
		sut.clear();
		Assert.assertFalse(sut.hasModel(SOME_TYPE));
	}

	@Test
	public void getModelWorks() throws IOException {
		RepetitionModel in = createModel();
		sut.store(SOME_TYPE, in);
		RepetitionModel out = sut.getModel(SOME_TYPE);
		assertEquals(in, out);
	}

	@Test(expected = AssertionException.class)
	public void getModel_unknownType() throws IOException {
		sut.getModel(SOME_TYPE);
	}

	@Test
	public void getModelWorksWithActualModel() throws IOException {
		RepetitionModel in = createModel(100);
		sut.store(SOME_TYPE, in);
		RepetitionModel out = sut.getModel(SOME_TYPE);
		assertEquals(in, out);
	}

	private RepetitionModel createModel() {
		return createModel(3);
	}

	private RepetitionModel createModel(int numProbabilities) {
		RepetitionModel model = new RepetitionModel();
		model.type = newType("T%d, P", numProbabilities);
		for (int i = 0; i < numProbabilities; i++) {
			IMemberName m = Names.newField("[p:int] [T,P]._f%d", i);
			double prob = ThreadLocalRandom.current().nextDouble(0, 1);
			model.setRepetitionProbability(m, prob);
		}
		return model;
	}
}