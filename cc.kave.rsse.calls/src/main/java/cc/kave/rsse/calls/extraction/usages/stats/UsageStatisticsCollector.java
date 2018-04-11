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
package cc.kave.rsse.calls.extraction.usages.stats;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.rsse.calls.usages.model.IUsage;

public interface UsageStatisticsCollector {

	void onProcessContext(Context context);

	void onEntryPointUsagesExtracted(IMethodDeclaration entryPoint, List<? extends IUsage> usages);

	void process(List<? extends IUsage> usages);

	void onUsagesPruned(int numPrunedUsages);

	void output(Path file) throws IOException;
}