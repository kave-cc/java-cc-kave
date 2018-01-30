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
package cc.kave.commons.pointsto.extraction.stats;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.recommenders.usages.Usage;

public class TypeHistogramUsageStatisticsCollector implements UsageStatisticsCollector {

	private Map<ITypeName, Integer> histrogram = new HashMap<>();

	@Override
	public UsageStatisticsCollector create() {
		return new TypeHistogramUsageStatisticsCollector();
	}

	@Override
	public void merge(UsageStatisticsCollector other) {
		TypeHistogramUsageStatisticsCollector otherHistoCollector = (TypeHistogramUsageStatisticsCollector) other;

		synchronized (histrogram) {
			for (Map.Entry<ITypeName, Integer> entry : otherHistoCollector.histrogram.entrySet()) {
				Integer oldCount = histrogram.getOrDefault(entry.getKey(), 0);
				histrogram.put(entry.getKey(), oldCount + entry.getValue());
			}
		}
	}

	@Override
	public void onProcessContext(Context context) {

	}

	@Override
	public void onEntryPointUsagesExtracted(IMethodDeclaration entryPoint, List<? extends Usage> usages) {
		process(usages);
	}

	@Override
	public void process(List<? extends Usage> usages) {
		for (Usage usage : usages) {
			ITypeName type = usage.getType();

			Integer oldCount = histrogram.getOrDefault(type, 0);
			histrogram.put(type, oldCount + 1);
		}
	}

	@Override
	public void onUsagesPruned(int numPrunedUsages) {
		// TODO Auto-generated method stub

	}

	@Override
	public void output(Path file) throws IOException {
		List<Map.Entry<ITypeName, Integer>> entries = new ArrayList<>(histrogram.entrySet());
		// List<Map.Entry<ITypeName, Integer>> entries = new
		// ArrayList<>(histrogram.size());
		//
		// // skip entries that occur only once
		// for (Map.Entry<ITypeName, Integer> entry : histrogram.entrySet()) {
		// if (entry.getValue() > 1) {
		// entries.add(entry);
		// }
		// }

		entries.sort(new Comparator<Map.Entry<ITypeName, Integer>>() {

			@Override
			public int compare(Entry<ITypeName, Integer> o1, Entry<ITypeName, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		});

		try (BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"))) {
			for (Map.Entry<ITypeName, Integer> entry : entries) {
				ITypeName type = entry.getKey();
				writer.write(type.getIdentifier());
				writer.write(' ');
				writer.write(Integer.toString(entry.getValue()));
				writer.newLine();
			}
		}

	}
}