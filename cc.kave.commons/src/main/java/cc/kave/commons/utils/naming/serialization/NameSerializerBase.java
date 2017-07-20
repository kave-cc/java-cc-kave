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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.IName;

public abstract class NameSerializerBase implements INameSerializer {

	private final Map<Class<?>, Class<?>> _sourceToTarget;
	private final Map<String, Function<String, IName>> _idToFactory;
	private final Map<Class<?>, String> _typeToId;

	protected NameSerializerBase() {
		_sourceToTarget = new HashMap<Class<?>, Class<?>>();
		_idToFactory = new HashMap<String, Function<String, IName>>();
		_typeToId = new HashMap<Class<?>, String>();

		// ReSharper disable once VirtualMemberCallInConstructor
		RegisterTypes();
	}

	protected abstract void RegisterTypes();

	protected void RegisterTypeMapping(Class<?> targetType, Class<?>... types) {
		Asserts.assertTrue(types.length > 0);
		for (Class<?> type : types) {
			_sourceToTarget.put(type, targetType);
		}
	}

	// first prefix is primary and will be used for serialization
	protected void Register(Class<?> type, Function<String, IName> cbCreate, String... prefixes) {
		Asserts.assertTrue(prefixes.length > 0);
		for (String prefix : prefixes) {
			_idToFactory.put(prefix, cbCreate);
		}
		_typeToId.put(type, prefixes[0]);
	}

	@Override
	public boolean canDeserialize(String prefix) {
		return _idToFactory.containsKey(prefix);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IName> T deserialize(String prefix, String id) {
		Asserts.assertTrue(_idToFactory.containsKey(prefix));
		IName name = _idToFactory.get(prefix).apply(fixLegacyIdentifiers(id));
		return (T) name;
	}

	// this method can be overridden in serializers to fix broken ids
	protected String fixLegacyIdentifiers(String id) {
		return id;
	}

	@Override
	public boolean canSerialize(IName name) {
		Class<?> effectiveType = getEffectiveType(name);
		return _typeToId.containsKey(effectiveType);
	}

	@Override
	public String serialize(IName n) {
		Class<?> effectiveType = getEffectiveType(n);
		Asserts.assertTrue(_typeToId.containsKey(effectiveType));
		return f("%s:%s", _typeToId.get(effectiveType), n.getIdentifier());
	}

	private Class<?> getEffectiveType(IName n) {
		Class<?> type = n.getClass();
		if (_sourceToTarget.containsKey(type)) {
			type = _sourceToTarget.get(type);
		}
		return type;
	}
}