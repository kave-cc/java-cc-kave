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

import static cc.kave.commons.assertions.Asserts.fail;

public class ToStringAsserts {
	public static void assertToStringUtils(Object obj) {
		String openingBrace = obj instanceof Iterable ? "[" : "{";
		String typeName = obj.getClass().getSimpleName();
		int hashCode = obj.hashCode();
		String expectedStart = String.format("%s@%d %s\n", typeName, hashCode, openingBrace);
		String actual = obj.toString();
		if (!actual.startsWith(expectedStart)) {
			fail(String.format("unexpected toString output!\nexpected start:\n'%s'\nbut was\n'%s'\n", expectedStart,
					actual));
		}
	}
}