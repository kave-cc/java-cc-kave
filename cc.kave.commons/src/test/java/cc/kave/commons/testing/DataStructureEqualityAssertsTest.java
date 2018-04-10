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

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.exceptions.AssertionException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class DataStructureEqualityAssertsTest {

	@Test
	public void equal() {
		assertEqualDataStructures(obj(111, 222), obj(111, 222));
	}

	@Test
	public void diffVal() {
		assertNotEqualDataStructures(obj(111, 222), obj(333, 222));
	}

	@Test
	public void diffHash() {
		assertNotEqualDataStructures(obj(111, 222), obj(111, 333));
	}

	@Test(expected = AssertionException.class)
	public void failEquals_val() {
		assertEqualDataStructures(obj(111, 222), obj(333, 222));
	}

	@Test(expected = AssertionException.class)
	public void failEquals_hash() {
		assertEqualDataStructures(obj(111, 222), obj(111, 333));
	}

	@Test(expected = AssertionException.class)
	public void failNotEquals() {
		assertNotEqualDataStructures(obj(111, 222), obj(111, 222));
	}

	@Test(expected = AssertionException.class)
	@Parameters(method = "providePoorHashes")
	public void poorHash_equals(int poorHash) {
		assertEqualDataStructures(obj(poorHash, 111), obj(poorHash, 111));
	}

	@Test(expected = AssertionException.class)
	@Parameters(method = "providePoorHashes")
	public void poorHash_notEquals_first(int poorHash) {
		assertNotEqualDataStructures(obj(poorHash, 111), obj(222, 111));
	}

	@Test(expected = AssertionException.class)
	@Parameters(method = "providePoorHashes")
	public void poorHash_notEqualsSecond(int poorHash) {
		assertNotEqualDataStructures(obj(222, 111), obj(poorHash, 111));
	}

	public static Object[][] providePoorHashes() {
		ParameterData pd = new ParameterData();
		pd.add(-1);
		pd.add(0);
		pd.add(1);
		return pd.toArray();

	}

	private static EqualityTestObject obj(int hash, int val) {
		return new EqualityTestObject(val, hash);
	}

	private static class EqualityTestObject {

		private int val;
		private int hash;

		public EqualityTestObject(int val, int hash) {
			this.val = val;
			this.hash = hash;

		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof EqualityTestObject) {
				EqualityTestObject other = (EqualityTestObject) obj;
				return val == other.val;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public String toString() {
			return String.format("%s@%d(val: %d)", getClass().getSimpleName(), hash, val);
		}
	}
}