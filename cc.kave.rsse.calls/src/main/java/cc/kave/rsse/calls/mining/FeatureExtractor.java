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

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.Constants;
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

public class FeatureExtractor {

	private Options opts;

	public FeatureExtractor(Options opts) {
		this.opts = opts;
	}

	public List<List<IFeature>> extract(List<IUsage> usages) {
		List<List<IFeature>> features = newArrayList();
		for (IUsage usage : usages) {
			if (usage != null) {
				features.add(extract(usage));
			}
		}
		return features;
	}

	public List<IFeature> extract(IUsage usage) {
		if (usage == null) {
			throw new IllegalArgumentException("Unexpected, usage is null.");
		}
		List<IFeature> features = Lists.newArrayList();

		ITypeName type = usage.getType();
		if (type == null || isLocal(type)) {
			features.add(Constants.UNKNOWN_TF);
		} else {
			features.add(new TypeFeature(type));
		}
		ITypeName cctx = usage.getClassContext();
		if (cctx == null || isLocal(cctx) || !opts.useClassCtx()) {
			features.add(Constants.UNKNOWN_CCF);
		} else {
			features.add(new ClassContextFeature(cctx));
		}
		IMethodName mctx = usage.getMethodContext();
		if (mctx == null || isLocal(mctx) || !opts.useMethodCtx()) {
			features.add(Constants.UNKNOWN_MCF);
		} else {
			features.add(new MethodContextFeature(mctx));
		}
		IDefinition def = usage.getDefinition();
		if (def == null || isLocal(def) || !opts.useDef()) {
			features.add(Constants.UNKNOWN_DF);
		} else {
			features.add(new DefinitionFeature(def));
		}
		for (ICallParameter cp : usage.getCallParameters()) {
			if (cp != null && useCallParam(cp)) {
				features.add(new CallParameterFeature(cp));
			}
		}

		for (IMemberAccess site : usage.getMemberAccesses()) {
			if (site != null && useSite(site)) {
				features.add(new MemberAccessFeature(site));
			}
		}

		return features;
	}

	private boolean useCallParam(ICallParameter cp) {
		if (isLocal(cp.getMethod())) {
			return false;
		}
		return opts.useParams();
	}

	private boolean useSite(IMemberAccess site) {
		if (isLocal(site.getMember())) {
			return false;
		}
		switch (site.getType()) {
		case METHOD_CALL:
			return opts.useCalls();
		default: // FIELD_ACCESS, PROPERTY_ACCESS:
			return opts.useMembers();
		}
	}

	private static boolean isLocal(ITypeName type) {
		return type.getAssembly().isLocalProject();
	}

	private static boolean isLocal(IMemberName member) {
		if (member.getDeclaringType().getAssembly().isLocalProject()) {
			return true;
		}
		if (member instanceof IMethodName) {
			IMethodName method = (IMethodName) member;
			for (IParameterName n : method.getParameters()) {
				if (isLocal(n.getValueType())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isLocal(IDefinition d) {
		IMemberName m = d.getMember(IMemberName.class);
		if (m != null) {
			return isLocal(m);
		}
		return false;
	}
}