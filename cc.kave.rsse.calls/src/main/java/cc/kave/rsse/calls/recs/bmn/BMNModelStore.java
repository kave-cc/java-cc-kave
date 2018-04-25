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

import static cc.kave.commons.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.IModelStore;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.utils.FileNamingStrategy;

public class BMNModelStore implements IModelStore<BMNModel> {

	private static FileNamingStrategy naming = new FileNamingStrategy();

	private File baseDir;

	public BMNModelStore(String dir, Options opts) {
		File rootDir = new File(dir);
		assertTrue(!rootDir.exists() || rootDir.isDirectory());
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		baseDir = new File(rootDir, opts.toString());
		assertTrue(!baseDir.exists() || baseDir.isDirectory());
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}

	}

	private File file(ITypeName t) {
		String path = String.join(File.separator, baseDir.getAbsolutePath(), naming.getRelativePath(t) + ".json");
		return new File(path);
	}

	public void clear() {
		try {
			FileUtils.deleteDirectory(baseDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void store(ITypeName t, BMNModel m) {
		double size = m.table.getSize() / (double) (1024 * 1024);
		int numRows = m.table.getBMNTable().length;
		int numCols = m.dictionary.size();
		Logger.debug("writing BMN Model (size: %.2f MB, numRows: %d, numCols: %d)", size, numRows, numCols);

		try {
			File f = file(t);
			ensureParent(f);

			try (FileOutputStream fos = new FileOutputStream(f);
					OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
					JsonWriter jw = new JsonWriter(osw)) {

				jw.setIndent("  ");

				jw.beginObject();
				//
				jw.name("Dictionary");
				jw.beginArray();
				for (IFeature feature : m.dictionary.getAllEntries()) {
					jw.value(JsonUtils.toJson(feature));
				}
				jw.endArray();
				//
				jw.name("Table");
				jw.beginArray();

				int num = 0;
				for (boolean[] row : m.table.getBMNTable()) {
					if (num++ % 1000 == 0) {
						jw.flush();
					}
					String rowStr = rowToString(row);
					jw.value(rowStr);
				}

				jw.endArray();
				//
				jw.name("Frequencies");
				jw.beginArray();
				for (int freq : m.table.getFrequencies()) {
					jw.value(freq);
				}
				jw.endArray();
				//
				jw.endObject();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String rowToString(boolean[] row) {
		char[] cs = new char[row.length];
		for (int i = 0; i < row.length; i++) {
			cs[i] = row[i] ? '1' : '0';
		}
		return String.valueOf(cs);
	}

	private void ensureParent(File f) {
		File parent = f.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
	}

	@Override
	public boolean hasModel(ITypeName t) {
		return file(t).exists();
	}

	@Override
	public BMNModel getModel(ITypeName t) {
		Asserts.assertTrue(hasModel(t),
				"no model available, better use 'hasModel(t)' to check existance before accessing it!");

		Dictionary<IFeature> dict = new Dictionary<>();
		List<boolean[]> rows = Lists.newLinkedList();
		List<Integer> freqs = Lists.newLinkedList();

		File f = file(t);
		try (FileInputStream fis = new FileInputStream(f);
				InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
				JsonReader jr = new JsonReader(isr)) {

			jr.beginObject();
			//
			Asserts.assertEquals("Dictionary", jr.nextName(), "wrong name");
			jr.beginArray();
			while (jr.hasNext()) {
				String json = jr.nextString();
				IFeature uf = JsonUtils.fromJson(json, IFeature.class);
				dict.add(uf);
			}
			jr.endArray();
			//
			Asserts.assertEquals("Table", jr.nextName(), "wrong name");
			jr.beginArray();
			while (jr.hasNext()) {
				String json = jr.nextString();
				boolean[] row = stringToRow(json);
				rows.add(row);
			}
			jr.endArray();
			//
			Asserts.assertEquals("Frequencies", jr.nextName(), "wrong name");
			jr.beginArray();
			while (jr.hasNext()) {
				int freq = jr.nextInt();
				freqs.add(freq);
			}
			jr.endArray();
			//
			jr.endObject();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		boolean[][] bmnTable = new boolean[rows.size()][];
		int rowId = 0;
		for (boolean[] row : rows) {
			bmnTable[rowId++] = row;
		}

		int[] bmnFreqs = new int[freqs.size()];
		int freqId = 0;
		for (int freq : freqs) {
			bmnFreqs[freqId++] = freq;
		}

		BMNModel model = new BMNModel();
		model.dictionary = dict;
		model.table = new Table(bmnTable, bmnFreqs);
		return model;
	}

	private boolean[] stringToRow(String json) {
		boolean[] row = new boolean[json.length()];
		for (int i = 0; i < row.length; i++) {
			row[i] = json.charAt(i) == '1' ? true : false;
		}
		return row;
	}
}