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

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;

public class Usage extends AbstractUsage {

	// make sure the naming is consistent to the hardcoded names in
	// "UsageTypeAdapter"

	private ITypeName type;
	private ITypeName classCtx;
	private IMethodName methodCtx;
	private DefinitionSite definition;
	public final Set<UsageAccess> accesses = Sets.newLinkedHashSet();

	public static Usage createAsCopyFrom(IUsage usage) {
		Usage q = new Usage();
		q.setType(usage.getType());
		q.setClassContext(usage.getClassContext());
		q.setMethodContext(usage.getMethodContext());
		q.setDefinition(usage.getDefinitionSite());
		for (UsageAccess s : usage.getAllAccesses()) {
			q.addCallSite(s);
		}
		return q;
	}

	public void setType(ITypeName typeName) {
		this.type = typeName;
	}

	public ITypeName getType() {
		return type;
	}

	public ITypeName getClassContext() {
		return classCtx;
	}

	public IMethodName getMethodContext() {
		return methodCtx;
	}

	public DefinitionSite getDefinitionSite() {
		return definition;
	}

	public boolean addCallSite(UsageAccess site) {
		if (!accesses.contains(site)) {
			return accesses.add(site);
		} else {
			return false;
		}
	}

	public Set<UsageAccess> getAllAccesses() {
		return accesses;
	}

	public void setClassContext(ITypeName typeName) {
		this.classCtx = typeName;
	}

	public void setMethodContext(IMethodName methodName) {
		this.methodCtx = methodName;
	}

	public void setDefinition(DefinitionSite definition) {
		this.definition = definition;
	}
}