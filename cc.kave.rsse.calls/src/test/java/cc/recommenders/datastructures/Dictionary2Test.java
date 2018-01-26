/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.datastructures;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cc.recommenders.datastructures.Dictionary;

public class Dictionary2Test {

	private Dictionary<String> sut;

	@Before
	public void setup() {
		sut = new Dictionary<String>();
	}

	@Test
	public void ensureStableOrder() {
		Set<String> expected = Sets.newLinkedHashSet();
		for (int i = 0; i < 1000; i++) {
			sut.add("num" + i);
			expected.add("num" + i);
		}

		Set<String> actual = sut.getAllEntries();
		assertSetEquals(expected, actual);
	}

	@Test
	public void ensureStableOrderWhenSerialized() {
		Set<String> expected = Sets.newLinkedHashSet();
		for (int i = 0; i < 1000; i++) {
			sut.add("num" + i);
			expected.add("num" + i);
		}

		String json = new Gson().toJson(sut);
		Type fooType = new TypeToken<Dictionary<String>>() {
		}.getType();

		Dictionary<String> deserializedSut = new Gson().fromJson(json, fooType);

		Set<String> actual = deserializedSut.getAllEntries();

		assertSetEquals(expected, actual);
	}

	private void assertSetEquals(Set<String> expected, Set<String> actual) {
		assertEquals(expected.size(), actual.size());
		Iterator<String> ait = actual.iterator();
		Iterator<String> eit = expected.iterator();
		while (ait.hasNext()) {
			String a = ait.next();
			String e = eit.next();
			assertEquals(e, a);
		}
	}
}