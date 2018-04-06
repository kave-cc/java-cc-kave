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

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.ToStringUtils;

public abstract class AbstractUsage implements IUsage {

	public abstract ITypeName getType();

	public abstract ITypeName getClassContext();

	public abstract IMethodName getMethodContext();

	public abstract DefinitionSite getDefinitionSite();

	public abstract Set<UsageSite> getAllUsageSites();

	public Set<UsageSite> getCallSites() {
		Set<UsageSite> filtered = Sets.newLinkedHashSet();
		for (UsageSite site : getAllUsageSites()) {
			boolean isReceiverCall = site.getKind().equals(UsageAccessType.CALL_RECEIVER);
			if (isReceiverCall) {
				filtered.add(site);
			}
		}
		return filtered;
	}

	public Set<UsageSite> getParameterSites() {
		Set<UsageSite> filtered = Sets.newLinkedHashSet();
		for (UsageSite site : getAllUsageSites()) {
			boolean isReceiverCall = site.getKind().equals(UsageAccessType.CALL_PARAMETER);
			if (isReceiverCall) {
				filtered.add(site);
			}
		}
		return filtered;
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}

	@Override
	public boolean equals(Object obj) {
		return reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return reflectionHashCode(this);
	}
}