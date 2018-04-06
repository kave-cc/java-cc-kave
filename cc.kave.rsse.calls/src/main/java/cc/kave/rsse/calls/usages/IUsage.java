/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.usages;

import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;

public interface IUsage {

	public ITypeName getType();

	public ITypeName getClassContext();

	public IMethodName getMethodContext();

	public DefinitionSite getDefinitionSite();

	public Set<UsageSite> getAllUsageSites();

	public Set<UsageSite> getCallSites();

	public Set<UsageSite> getParameterSites();
}