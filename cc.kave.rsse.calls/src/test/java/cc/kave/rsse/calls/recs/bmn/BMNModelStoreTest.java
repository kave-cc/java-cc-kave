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
package cc.kave.rsse.calls.recs.bmn;

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
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.recs.bmn.BMNModel;
import cc.kave.rsse.calls.recs.bmn.BMNModelStore;
import cc.kave.rsse.calls.recs.bmn.Table;
import cc.kave.rsse.calls.utils.json.JsonUtilsCcKaveRsseCalls;

public class BMNModelStoreTest {

	private static final ITypeName SOME_TYPE = Names.newType("T,P");

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	private String rootDir;
	private BMNModelStore sut;

	@Before
	public void setup() throws IOException {
		JsonUtilsCcKaveRsseCalls.registerJsonAdapters();
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
		store("T,P", createModel(3, 3));
		Assert.assertTrue(new File(rootDir, path).exists());
	}

	@Test
	public void hasModelWorks() throws IOException {
		Assert.assertFalse(sut.hasModel(SOME_TYPE));
		sut.store(SOME_TYPE, createModel(3, 3));
		Assert.assertTrue(sut.hasModel(SOME_TYPE));
	}

	@Test
	public void clearingWorks() throws IOException {
		sut.store(SOME_TYPE, createModel(3, 3));
		Assert.assertTrue(sut.hasModel(SOME_TYPE));
		sut.clear();
		Assert.assertFalse(sut.hasModel(SOME_TYPE));
	}

	@Test
	public void getModelWorks() throws IOException {
		BMNModel in = createModel(3, 3);
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
		BMNModel in = createModel(20, 40);
		sut.store(SOME_TYPE, in);
		BMNModel out = sut.getModel(SOME_TYPE);
		assertEquals(in, out);
	}

	@Test
	@Ignore("long running test, run manually on BMNModelStore changes")
	public void hugeModelWorks() throws IOException {
		BMNModel in = createModel(40000, 40000);
		sut.store(SOME_TYPE, in);
		BMNModel out = sut.getModel(SOME_TYPE);
		assertEquals(in, out);
	}

	private BMNModel createModel(final int numCols, final int numRows) {
		BMNModel in = new BMNModel();
		in.dictionary = new Dictionary<>();
		// col names
		for (int i = 0; i < numCols; i++) {
			in.dictionary.add(new UsageSiteFeature(Names.newMethod("[p:void] [T,P].m%d()", i)));
		}
		// table
		boolean[][] bmnTable = new boolean[numRows][numCols];
		int[] frequencies = new int[numRows];
		Random rnd = new Random();
		for (int rowId = 0; rowId < numRows; rowId++) {
			frequencies[rowId] = rowId + 13;
			bmnTable[rowId] = new boolean[numCols];
			for (int colId = 0; colId < numCols; colId++) {
				bmnTable[rowId][colId] = rnd.nextBoolean();
			}
		}
		in.table = new Table(bmnTable, frequencies);
		return in;
	}

	private void store(String id, BMNModel m) {
		ITypeName t = Names.newType(id);
		sut.store(t, m);
	}
}