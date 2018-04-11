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
package cc.kave.rsse.calls.utils.json;

import com.google.gson.GsonBuilder;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.commons.utils.io.json.JsonUtils.IAdditionalBuilderConfiguration;
import cc.kave.commons.utils.io.json.RuntimeTypeAdapterFactory;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.NoUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;

@SuppressWarnings("deprecation")
public class JsonUtilsCcKaveRsseCalls {
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
		registerHierarchy(gb, IFeature.class, UsageSiteFeature.class, ClassContextFeature.class,
				DefinitionFeature.class, MethodContextFeature.class, TypeFeature.class);
		registerHierarchyLeaf(gb, UsageSiteFeature.class);
		// registerHierarchyLeaf(gb, ClassContextFeature.class);
		// registerHierarchyLeaf(gb, DefinitionFeature.class);
		// registerHierarchyLeaf(gb, MethodContextFeature.class);
		// registerHierarchyLeaf(gb, ParameterFeature.class);
		// registerHierarchyLeaf(gb, SuperMethodFeature.class);
		// registerHierarchyLeaf(gb, TypeFeature.class);
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

	private static <T> void registerHierarchyLeaf(GsonBuilder gsonBuilder, Class<T> type) {
		RuntimeTypeAdapterFactory<T> factory = RuntimeTypeAdapterFactory.of(type, "$type");
		factory = factory.registerSubtype(type);
		gsonBuilder.registerTypeAdapterFactory(factory);
	}
}