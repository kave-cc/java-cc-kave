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

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;

import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.commons.utils.io.json.JsonUtils.IAdditionalBuilderConfiguration;
import cc.kave.rsse.calls.model.features.CallParameterFeature;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MemberAccessFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IMemberAccess;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.CallParameter;
import cc.kave.rsse.calls.model.usages.impl.Definition;
import cc.kave.rsse.calls.model.usages.impl.MemberAccess;
import cc.kave.rsse.calls.model.usages.impl.NoUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;

@SuppressWarnings("deprecation")
public class JsonUtilsCcKaveRsseCalls {
	public static void registerJsonAdapters() {
		JsonUtils.registerBuilderConfig(new IAdditionalBuilderConfiguration() {
			@Override
			public void configure(GsonBuilder gb) {
				register(gb, new UsageTypeAdapter(), IUsage.class, Usage.class, NoUsage.class);
				register(gb, new DefinitionTypeAdapter(), IDefinition.class, Definition.class);
				register(gb, new CallParameterTypeAdapter(), ICallParameter.class, CallParameter.class);
				register(gb, new MemberAccessTypeAdapter(), IMemberAccess.class, MemberAccess.class);

				register(gb, new FeatureTypeAdapter(), TypeFeature.class, ClassContextFeature.class,
						MethodContextFeature.class, DefinitionFeature.class, CallParameterFeature.class,
						MemberAccessFeature.class, IFeature.class);

				gb.registerTypeAdapterFactory(new DictionaryTypeAdapterFactory());
			}
		});
	}

	private static void register(GsonBuilder gb, Object adapter, Type... types) {
		for (Type t : types) {
			gb.registerTypeAdapter(t, adapter);
		}
	}
}