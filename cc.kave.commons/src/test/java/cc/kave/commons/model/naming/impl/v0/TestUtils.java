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
package cc.kave.commons.model.naming.impl.v0;

import cc.kave.testcommons.ParameterData;

public class TestUtils {

	public static Object[][] provideTypes() {
		ParameterData pd = new ParameterData();
		// unknown
		pd.add("?");
		// regular
		pd.add("T");
		pd.add("T -> T,P");
		pd.add("T,P");
		pd.add("T[],P");
		pd.add("d:[?] [n.C+D, P].()");
		pd.add("T`1[[P -> T2,P]],P");
		// arrays
		pd.add("T[]");
		pd.add("T[] -> T,P");
		pd.add("T[],P");
		pd.add("d:[?] [?].()[]");
		// nested
		pd.add("n.C+D`1[[T]], P");
		pd.add("n.C`1[[T]]+D, P");
		// predefined
		pd.add("p:int");
		return pd.toArray();
	}
}