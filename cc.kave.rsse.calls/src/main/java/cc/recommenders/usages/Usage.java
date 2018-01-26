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
package cc.recommenders.usages;

import java.util.Set;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;

public interface Usage {

	public ICoReTypeName getType();

	public ICoReTypeName getClassContext();

	public ICoReMethodName getMethodContext();

	public DefinitionSite getDefinitionSite();

	/**
	 * @return concatenation of paths of the underlying usage, which contains
	 *         each callsite exactly once
	 */
	public Set<CallSite> getAllCallsites();

	/**
	 * @return concatenation of paths of the underlying usage, which contains
	 *         each receiver callsite exactly once
	 */
	public Set<CallSite> getReceiverCallsites();

	/**
	 * @return concatenation of paths of the underlying usage, which contains
	 *         each parameter callsite exactly once
	 */
	public Set<CallSite> getParameterCallsites();
}