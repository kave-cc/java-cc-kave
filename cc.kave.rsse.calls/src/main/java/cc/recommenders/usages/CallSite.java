/**
 * Copyright (c) 2011-2014 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.usages;

import cc.kave.commons.model.naming.codeelements.IMethodName;

/**
 * @see cc.cc.recommenders.model.usages.CallSites
 */
public class CallSite {

	// ensure consistent naming with hard-coded names in "UsageTypeAdapter"

	private CallSiteKind kind;
	private IMethodName method;
	private int argIndex = 0;

	CallSite() {
		// not meant to be instantiated manually
	}

	public CallSiteKind getKind() {
		return kind;
	}

	public void setKind(CallSiteKind kind) {
		this.kind = kind;
	}

	public IMethodName getMethod() {
		return method;
	}

	public void setMethod(IMethodName method) {
		this.method = method;
	}

	public int getArgIndex() {
		return argIndex;
	}

	public void setArgIndex(int argIndex) {
		this.argIndex = argIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + argIndex;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
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
		CallSite other = (CallSite) obj;
		if (argIndex != other.argIndex)
			return false;
		if (kind != other.kind)
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (kind == null || method == null) {
			return "INVALID";
		}

		StringBuilder sb = new StringBuilder();

		switch (kind) {
		case PARAMETER:
			sb.append("PARAM(");
			sb.append(argIndex);
			sb.append("):");
			sb.append(method.getDeclaringType().getName());
			sb.append('.');
			sb.append(method.getName());
			break;
		case RECEIVER:
			sb.append("CALL:");
			sb.append(method.getDeclaringType().getName());
			sb.append('.');
			sb.append(method.getName());
			break;
		}
		return sb.toString();
	}
}