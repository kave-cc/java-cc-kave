/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.testing;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * to be used with JUnitParams (https://github.com/Pragmatists/JUnitParams)
 */
public class ParameterData {

	private Set<List<Object>> cases = Sets.newHashSet();

	public ParameterData add(Object... args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof String) {
				String s = (String) args[i];
				args[i] = s;// .replace(",", "\\,").replace("|", "\\|");
			}
		}
		cases.add(Lists.newArrayList(args));
		return this;
	}

	public Object[][] toArray() {

		int numCases = cases.size();
		int numParams = cases.iterator().next().size();

		Object[][] arr = new Object[numCases][numParams];

		int curCase = 0;
		for (List<Object> aCase : cases) {
			int curParam = 0;
			for (Object param : aCase) {
				arr[curCase][curParam++] = param;
			}
			curCase++;
		}

		return arr;
	}
}