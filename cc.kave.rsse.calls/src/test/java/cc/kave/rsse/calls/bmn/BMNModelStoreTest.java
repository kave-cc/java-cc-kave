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
package cc.kave.rsse.calls.bmn;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.ICallsRecommender;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.usages.Query;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.utils.RsseCallsJsonUtils;

public class BMNModelStoreTest {

	private static final ITypeName SOME_TYPE = Names.newType("T,P");

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	private String rootDir;
	private BMNModelStore sut;

	@Before
	public void setup() throws IOException {
		RsseCallsJsonUtils.registerJsonAdapters();
		rootDir = tmp.newFolder("some_dir").getAbsolutePath();
		sut = new BMNModelStore(rootDir, "XYZ");
	}

	@After
	public void teardown() {
		JsonUtils.resetAllConfiguration();
	}

	@Test
	public void afolderIsBeingCreated() throws IOException {
		tmp.newFile("a");
		Assert.assertTrue(new File(rootDir, "XYZ").isDirectory());
	}

	@Test
	public void fileNamingIsUsed() throws IOException {
		String path = "XYZ/P/local/T.json";
		Assert.assertFalse(new File(rootDir, path).exists());
		store("T,P", new BMNModel());
		Assert.assertTrue(new File(rootDir, path).exists());
	}

	@Test
	public void hasModelWorks() throws IOException {
		Assert.assertFalse(sut.hasModel(SOME_TYPE));
		sut.store(SOME_TYPE, new BMNModel());
		Assert.assertTrue(sut.hasModel(SOME_TYPE));
	}

	@Test
	public void clearingWorks() throws IOException {
		sut.store(SOME_TYPE, new BMNModel());
		Assert.assertTrue(sut.hasModel(SOME_TYPE));
		sut.clear();
		Assert.assertFalse(sut.hasModel(SOME_TYPE));
	}

	@Test
	public void getModelWorks() throws IOException {
		BMNModel in = new BMNModel();
		sut.store(SOME_TYPE, in);
		BMNModel out = sut.getModel(SOME_TYPE);
		assertEquals(in, out);
	}

	@Test(expected = AssertionException.class)
	public void getModel_unknownType() throws IOException {
		sut.getModel(SOME_TYPE);
	}

	@Test
	public void getModelWorksWithActualModel() throws IOException {
		BMNModel in = new BMNModel();
		in.dictionary = new Dictionary<>();
		// col names
		for (int i = 0; i < 20; i++) {
			in.dictionary.add(new CallFeature(Names.newMethod("[p:void] [T,P].m%d()", i)));
		}
		// table
		boolean[][] bmnTable = new boolean[40][20];
		int[] frequencies = new int[40];
		Random rnd = new Random();
		for (int rowId = 0; rowId < 40; rowId++) {
			frequencies[rowId] = rowId + 13;
			bmnTable[rowId] = new boolean[20];
			for (int colId = 0; colId < 20; colId++) {
				bmnTable[rowId][colId] = rnd.nextBoolean();
			}
		}
		in.table = new Table(bmnTable, frequencies);

		sut.store(SOME_TYPE, in);
		BMNModel out = sut.getModel(SOME_TYPE);
		assertEquals(in, out);
	}

	private void store(String id, BMNModel m) {
		ITypeName t = Names.newType(id);
		sut.store(t, m);
	}
}