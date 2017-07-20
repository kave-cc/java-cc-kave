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
package cc.kave.commons.utils.io.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EnumDeSerializer<T extends Enum<T>> implements JsonSerializer<T>, JsonDeserializer<T> {

	private T[] values;

	private EnumDeSerializer(T[] values) {
		this.values = values;
	}

	public static <U extends Enum<U>> EnumDeSerializer<U> create(U[] values) {
		return new EnumDeSerializer<>(values);
	}

	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		int index = -1;
		for (int i = 0; i < values.length; i++) {
			if (values[i] == src) {
				index = i;
			}
		}

		return new JsonPrimitive(index);
	}

	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		int index = json.getAsInt();
		if (index >= 0 && index < values.length) {
			return values[index];
		}
		return values[0];
	}
}