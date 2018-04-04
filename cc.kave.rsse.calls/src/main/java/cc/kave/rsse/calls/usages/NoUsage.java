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
package cc.kave.rsse.calls.usages;

import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;

/**
 * This class represents the special case of "no usage existed" in a history of
 * usages. Should not be used in a context different to this question.<br>
 * <br>
 * It is typically not necessary to use this class, it was used for the ASE16
 * evaluation.
 */
@Deprecated
public class NoUsage implements IUsage {

	private RuntimeException ex() {
		return new RuntimeException("not implemented");
	}

	@Override
	public ITypeName getType() {
		throw ex();
	}

	@Override
	public ITypeName getClassContext() {
		throw ex();
	}

	@Override
	public IMethodName getMethodContext() {
		throw ex();
	}

	@Override
	public DefinitionSite getDefinitionSite() {
		throw ex();
	}

	@Override
	public Set<UsageAccess> getAllAccesses() {
		throw ex();
	}

	@Override
	public Set<UsageAccess> getReceiverCallsites() {
		throw ex();
	}

	@Override
	public Set<UsageAccess> getParameterCallsites() {
		throw ex();
	}

	@Override
	public int hashCode() {
		return 42;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof NoUsage;
	}

	@Override
	public String toString() {
		return "{NoUsage}";
	}
}