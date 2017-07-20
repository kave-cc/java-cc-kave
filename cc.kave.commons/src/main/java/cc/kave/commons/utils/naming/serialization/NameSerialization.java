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
package cc.kave.commons.utils.naming.serialization;

import static cc.kave.commons.utils.StringUtils.f;

import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.IName;

public class NameSerialization {

	private static final Set<INameSerializer> serializers = Sets.newHashSet(//
			new NameSerializerV0()
	// new NameSerializerV1()
	);

	public static <T extends IName> T deserialize(String input) {
		int delimIdx = input.indexOf(':');
		String prefix = input.substring(0, delimIdx);
		delimIdx++; // skip delim
		String id = input.substring(delimIdx, input.length());

		for (INameSerializer s : serializers) {
			if (s.canDeserialize(prefix)) {
				return s.deserialize(prefix, id);
			}
		}

		throw new IllegalArgumentException(
				f("no matching deserializer found for prefix '{0}' (id: '{1}')", prefix, id));
	}

	public static String serialize(IName n) {
		for (INameSerializer s : serializers) {
			if (s.canSerialize(n)) {
				return s.serialize(n);
			}
		}
		throw new IllegalArgumentException(
				f("no matching serializer found for type '{0}'", n.getClass().getSimpleName()));
	}
}