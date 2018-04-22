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
package cc.kave.commons.utils.io.json;

import java.io.Serializable;
import java.lang.reflect.Type;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;

public class JsonUtilsSerializableTest {

	@Test
	public void to_compact() {
		String json = JsonUtils.toJson(new TestSerializable<Integer>(123));
		Assert.assertEquals("{\"Value\":123}", json);
	}

	@Test
	public void to_formatted() {
		String json = JsonUtils.toJsonFormatted(new TestSerializable<Integer>(123));
		Assert.assertEquals("{\n  \"Value\": 123\n}", json);
	}

	@Test
	public void from() {
		Type t = new TypeToken<TestSerializable<Integer>>() {
		}.getType();

		JsonUtils.fromJson("{\"Value\":123}", t);
	}

	public static class TestSerializable<T> implements Serializable {
		private static final long serialVersionUID = 1L;
		public T value;

		public TestSerializable(T value) {
			this.value = value;
		}
	}
}