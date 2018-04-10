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
package cc.kave.commons.testing;

import static cc.kave.commons.assertions.Asserts.assertEquals;

import cc.kave.commons.exceptions.AssertionException;

public class DataStructureEqualityAsserts {

	public static void assertEqualDataStructures(Object a, Object b) {
		assertGoodHash(a);
		assertEquals(a, b, m("Unexpected, objects are not equal", a, b));
		assertEquals(a.hashCode(), b.hashCode(), m("Unexpected, objects have different hash", a, b));
	}

	private static void assertGoodHash(Object o) {
		int hash = o.hashCode();
		if (hash == 0 || hash == 1 || hash == -1) {
			throw new AssertionException("Provided object does have a poor hash value:\n---\n%s\n---", o);
		}
	}

	public static void assertNotEqualDataStructures(Object a, Object b) {
		assertGoodHash(a);
		assertGoodHash(b);
		boolean areEqual = a.equals(b);
		boolean haveSameHash = a.hashCode() == b.hashCode();
		if (areEqual && haveSameHash) {
			if (areEqual) {
				throw new AssertionException(m("Unexpected, objects are equal", a, b));
			} else {
				throw new AssertionException(m("Unexpected, objects have same hash", a, b));
			}
		}
	}

	private static String m(String msg, Object a, Object b) {
		return String.format("%s:\n---\na: %s\n---\nb: %s\n---", msg, a, b);
	}
}