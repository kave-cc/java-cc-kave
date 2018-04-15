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
package cc.kave.rsse.calls.mining;

import static cc.kave.commons.model.naming.Names.getUnknownMethod;
import static cc.kave.commons.model.naming.Names.getUnknownType;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.rsse.calls.model.usages.DefinitionType.CATCH_PARAMETER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.LOOP_HEADER;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByReturnValue;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.DefinitionType;

public class DictionaryBuilder {

	private static final ITypeName DUMMY_TYPE = newType("D,P");
	private static final IMethodName DUMMY_METHOD = newMethod("[R,P] [D,P].m()");

	public static final ClassContextFeature DUMMY_CCF = new ClassContextFeature(DUMMY_TYPE);
	public static final ClassContextFeature UNKNOWN_CCF = new ClassContextFeature(getUnknownType());
	public static final MethodContextFeature DUMMY_MCF = new MethodContextFeature(DUMMY_METHOD);
	public static final MethodContextFeature UNKNOWN_MCF = new MethodContextFeature(getUnknownMethod());
	public static final DefinitionFeature DUMMY_DF = new DefinitionFeature(definedByReturnValue(DUMMY_METHOD));
	public static final DefinitionFeature UNKNOWN_DF = new DefinitionFeature(definedByUnknown());

	private Options opts;

	public DictionaryBuilder(Options opts) {
		this.opts = opts;
	}

	public Dictionary<IFeature> build(List<List<IFeature>> llf) {
		Dictionary<IFeature> dictionary = new Dictionary<IFeature>();
		addDummies(dictionary);

		Map<IFeature, Integer> counts = new LinkedHashMap<>();
		for (List<IFeature> lf : llf) {
			for (IFeature f : new LinkedHashSet<>(lf)) {
				if (counts.containsKey(f)) {
					counts.put(f, counts.get(f) + 1);
				} else if (shouldUse(f)) {
					counts.put(f, 1);
				}
			}
		}
		for (IFeature f : counts.keySet()) {
			int count = counts.get(f);
			if (f instanceof TypeFeature || count >= opts.keepOnlyFeaturesWithAtLeastOccurrences) {
				dictionary.add(f);
			}
		}

		return dictionary;
	}

	private boolean shouldUse(IFeature f) {
		return isEnabled(f) && !isLocal(f);
	}

	private void addDummies(Dictionary<IFeature> d) {
		d.add(DUMMY_CCF);
		d.add(DUMMY_MCF);
		d.add(DUMMY_DF);
		d.add(UNKNOWN_CCF);
		d.add(UNKNOWN_MCF);
		d.add(UNKNOWN_DF);
	}

	public boolean isEnabled(IFeature f) {
		if (f instanceof ClassContextFeature) {
			return opts.useClassCtx();
		}
		if (f instanceof MethodContextFeature) {
			return opts.useMethodCtx();
		}
		if (f instanceof DefinitionFeature) {
			return opts.useDef();
		}
		if (f instanceof UsageSiteFeature) {
			UsageSiteFeature usf = (UsageSiteFeature) f;
			switch (usf.site.getType()) {
			case CALL_RECEIVER:
				return opts.useCalls();
			case CALL_PARAMETER:
				return opts.useParams();
			case FIELD_ACCESS:
			case PROPERTY_ACCESS:
				return opts.useMembers();
			}
		}
		return true;
	}

	private static boolean isLocal(IFeature f) {
		if (f instanceof ClassContextFeature) {
			ClassContextFeature ccf = (ClassContextFeature) f;
			return ccf.type.getAssembly().isLocalProject();
		} else if (f instanceof MethodContextFeature) {
			MethodContextFeature mcf = (MethodContextFeature) f;
			return mcf.method.getDeclaringType().getAssembly().isLocalProject();
		} else if (f instanceof DefinitionFeature) {
			DefinitionFeature df = (DefinitionFeature) f;
			IMemberName m = df.definition.getMember(IMemberName.class);
			if (m != null) {
				DefinitionType dt = df.definition.getType();
				if (dt == LOOP_HEADER || dt == CATCH_PARAMETER) {
					IMethodName m2 = (IMethodName) m;
					return m2.getParameters().get(0).getValueType().getAssembly().isLocalProject();
				} else if (m.getDeclaringType().getAssembly().isLocalProject()) {
					return true;
				}
			}
		} else if (f instanceof UsageSiteFeature) {
			UsageSiteFeature usf = (UsageSiteFeature) f;
			return usf.site.getMember(IMemberName.class).getDeclaringType().getAssembly().isLocalProject();
		}
		return false;
	}
}