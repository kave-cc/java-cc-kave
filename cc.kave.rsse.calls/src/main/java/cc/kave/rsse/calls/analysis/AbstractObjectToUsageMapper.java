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
package cc.kave.rsse.calls.analysis;

import static cc.kave.commons.model.naming.Names.getUnknownType;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.caret.analyses.IPathInsensitivePointsToInfo;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;

public class AbstractObjectToUsageMapper {

	private final IPathInsensitivePointsToInfo p2info;
	private final ITypeName cCtx;
	private final IMethodName mCtx;
	private final Map<Object, Usage> usages = new IdentityHashMap<>();

	public AbstractObjectToUsageMapper(IPathInsensitivePointsToInfo p2info, ITypeName cCtx, IMethodName mCtx) {
		this.p2info = p2info;
		this.cCtx = cCtx;
		this.mCtx = mCtx;
	}

	private Usage getUsageForAbstractObject(Object ao) {
		Usage u;
		if (usages.containsKey(ao)) {
			u = usages.get(ao);
			return u;
		}
		u = new Usage();
		u.type = getUnknownType();
		u.classCtx = cCtx;
		u.methodCtx = mCtx;
		u.definition = definedByUnknown();
		usages.put(ao, u);
		return u;
	}

	public Map<Object, List<IUsage>> getMap() {
		Map<Object, List<IUsage>> map = new IdentityHashMap<>();
		for (Object ao : usages.keySet()) {
			if (!map.containsKey(ao)) {
				map.put(ao, new LinkedList<>());
			}
			map.get(ao).add(usages.get(ao));
		}
		return map;
	}

	public Usage usage(ISST sst) {
		Object ao = p2info.getAbstractObject(sst);
		return getUsageForAbstractObject(ao);
	}

	public Usage get(IMemberDeclaration d) {
		Object ao = p2info.getAbstractObject(d);
		return getUsageForAbstractObject(ao);
	}

	public Usage get(IParameterName k) {
		Object ao = p2info.getAbstractObject(k);
		return getUsageForAbstractObject(ao);
	}

	public Usage get(IReference r) {
		Object ao = p2info.getAbstractObject(r);
		return getUsageForAbstractObject(ao);
	}
}