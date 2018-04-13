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
package cc.kave.rsse.calls.utils.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.kave.rsse.calls.model.Dictionary;

public class DictionaryTypeAdapterFactory implements TypeAdapterFactory {

	@Override
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (Dictionary.class.isAssignableFrom(type.getRawType())) {

			TypeToken<T> typeOfT = new TypeToken<T>() {
			};

			return (TypeAdapter<T>) new DictionaryTypeAdapter<T>(gson.getAdapter(typeOfT));
		}
		return null;
	}

	private class DictionaryTypeAdapter<T> extends TypeAdapter<Dictionary<T>> {

		private TypeAdapter<T> adapterOfT;

		public DictionaryTypeAdapter(TypeAdapter<T> adapterOfT) {
			this.adapterOfT = adapterOfT;
		}

		@Override
		public void write(JsonWriter out, Dictionary<T> src) throws IOException {
			out.beginArray();
			if (src.size() > 0) {

				for (T entry : src.getAllEntries()) {
					adapterOfT.write(out, entry);
				}
			}
			out.endArray();
		}

		@Override
		public Dictionary<T> read(JsonReader in) throws IOException {
			Dictionary<T> d = new Dictionary<T>();
			in.beginArray();
			while (in.hasNext()) {
				T entry = adapterOfT.read(in);
				d.add(entry);
			}
			in.endArray();
			return d;
		}
	}
}