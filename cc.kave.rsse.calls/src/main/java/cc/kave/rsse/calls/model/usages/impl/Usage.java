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
package cc.kave.rsse.calls.model.usages.impl;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.ToStringUtils;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.IUsageSite;
import cc.kave.rsse.calls.model.usages.UsageSiteType;

public class Usage implements IUsage {

	public ITypeName type;
	public ITypeName classCtx;
	public IMethodName methodCtx;
	public IDefinition definition;
	public final List<IUsageSite> usageSites = new LinkedList<>();
	public boolean isQuery;

	@Override
	public ITypeName getType() {
		return type;
	}

	@Override
	public ITypeName getClassContext() {
		return classCtx;
	}

	@Override
	public IMethodName getMethodContext() {
		return methodCtx;
	}

	@Override
	public IDefinition getDefinition() {
		return definition;
	}

	@Override
	public List<IUsageSite> getUsageSites() {
		return usageSites;
	}

	@Override
	public List<IUsageSite> getUsageSites(UsageSiteType t) {
		Asserts.assertNotNull(t);
		List<IUsageSite> filtered = new LinkedList<>();
		for (IUsageSite site : getUsageSites()) {
			if (t.equals(site.getType())) {
				filtered.add(site);
			}
		}
		return filtered;
	}

	@Override
	public boolean isQuery() {
		return isQuery;
	}

	@Override
	public IUsage clone() {
		Usage clone = new Usage();
		clone.type = getType();
		clone.classCtx = getClassContext();
		clone.methodCtx = getMethodContext();
		clone.definition = getDefinition();
		clone.usageSites.addAll(getUsageSites());
		return clone;
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classCtx == null) ? 0 : classCtx.hashCode());
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + (isQuery ? 1231 : 1237);
		result = prime * result + ((methodCtx == null) ? 0 : methodCtx.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + usageSites.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usage other = (Usage) obj;
		if (classCtx == null) {
			if (other.classCtx != null)
				return false;
		} else if (!classCtx.equals(other.classCtx))
			return false;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (isQuery != other.isQuery)
			return false;
		if (methodCtx == null) {
			if (other.methodCtx != null)
				return false;
		} else if (!methodCtx.equals(other.methodCtx))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (!usageSites.equals(other.usageSites))
			return false;
		return true;
	}
}