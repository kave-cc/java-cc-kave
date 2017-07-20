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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.utils.naming.serialization.NameSerialization;

public class GsonNameDeserializer implements JsonDeserializer<IName>, JsonSerializer<IName> {

	private static Pattern sstDeserializationPattern = Pattern.compile("\\[SST:([.a-zA-Z0-9_]+)\\]");

	@Override
	public IName deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		String str = json.getAsString();
		StringBuffer sb = new StringBuffer();

		Matcher m = sstDeserializationPattern.matcher(str);
		while (m.find()) {
			@SuppressWarnings("unused")
			String srch = m.group(0);
			String repl = "KaVE.Commons.Model.SSTs.Impl." + m.group(1) + ", KaVE.Commons";
			m.appendReplacement(sb, repl);
		}
		m.appendTail(sb);
		return NameSerialization.deserialize(sb.toString());
	}

	private static Pattern sstSerializationPattern = Pattern
			.compile("KaVE\\.Commons\\.Model\\.SSTs\\.Impl\\.([.a-zA-Z0-9_]+), KaVE.Commons");

	@Override
	public JsonElement serialize(IName src, Type typeOfSrc, JsonSerializationContext context) {

		String json = NameSerialization.serialize(src);
		StringBuffer sb = new StringBuffer();

		Matcher m = sstSerializationPattern.matcher(json);
		while (m.find()) {
			@SuppressWarnings("unused")
			String srch = m.group(0);
			String repl = "[SST:" + m.group(1) + "]";
			m.appendReplacement(sb, repl);
		}
		m.appendTail(sb);
		return new JsonPrimitive(sb.toString());
	}
}