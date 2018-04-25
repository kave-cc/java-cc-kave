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
package cc.kave.rsse.calls.model.usages;

import java.util.List;
import java.util.function.Predicate;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;

public interface IUsage {

	public ITypeName getType();

	public ITypeName getClassContext();

	public IMethodName getMethodContext();

	public IDefinition getDefinition();

	public List<IUsageSite> getUsageSites();

	public List<IUsageSite> getUsageSites(Predicate<IUsageSite> p);

	public boolean isQuery();

	public IUsage clone();
}