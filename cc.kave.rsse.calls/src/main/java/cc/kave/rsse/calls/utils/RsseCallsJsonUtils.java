/**
 * Copyright 2018 University of Zurich
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
package cc.kave.rsse.calls.utils;

import com.google.gson.GsonBuilder;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.commons.utils.io.json.JsonUtils.IAdditionalBuilderConfiguration;
import cc.kave.commons.utils.io.json.RuntimeTypeAdapterFactory;
import cc.kave.rsse.calls.usages.NoUsage;
import cc.kave.rsse.calls.usages.Usage;
import cc.kave.rsse.calls.usages.IUsage;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.SuperMethodFeature;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;

public class RsseCallsJsonUtils {
	public static void registerJsonAdapters() {
		JsonUtils.registerBuilderConfig(new IAdditionalBuilderConfiguration() {
			@Override
			public void configure(GsonBuilder gb) {
				gb.registerTypeAdapter(IUsage.class, new UsageTypeAdapter());
				gb.registerTypeAdapter(Usage.class, new UsageTypeAdapter());
				gb.registerTypeAdapter(NoUsage.class, new UsageTypeAdapter());

				registerUsageFeatureHierarchy(gb);
			}
		});
	}

	private static void registerUsageFeatureHierarchy(GsonBuilder gb) {
		registerHierarchy(gb, UsageFeature.class, CallFeature.class, ClassFeature.class, DefinitionFeature.class,
				FirstMethodFeature.class, ParameterFeature.class, SuperMethodFeature.class, TypeFeature.class);
		registerHierarchyLeave(gb, CallFeature.class);
		registerHierarchyLeave(gb, ClassFeature.class);
		registerHierarchyLeave(gb, DefinitionFeature.class);
		registerHierarchyLeave(gb, FirstMethodFeature.class);
		registerHierarchyLeave(gb, ParameterFeature.class);
		registerHierarchyLeave(gb, SuperMethodFeature.class);
		registerHierarchyLeave(gb, TypeFeature.class);
	}

	@SafeVarargs
	private static <T> void registerHierarchy(GsonBuilder gsonBuilder, Class<T> type, Class<? extends T>... subtypes) {
		Asserts.assertTrue(subtypes.length > 0);
		RuntimeTypeAdapterFactory<T> factory = RuntimeTypeAdapterFactory.of(type, "$type");
		for (int i = 0; i < subtypes.length; i++) {
			factory = factory.registerSubtype(subtypes[i]);
		}
		gsonBuilder.registerTypeAdapterFactory(factory);
	}

	private static <T> void registerHierarchyLeave(GsonBuilder gsonBuilder, Class<T> type) {
		RuntimeTypeAdapterFactory<T> factory = RuntimeTypeAdapterFactory.of(type, "$type");
		factory = factory.registerSubtype(type);
		gsonBuilder.registerTypeAdapterFactory(factory);
	}
}