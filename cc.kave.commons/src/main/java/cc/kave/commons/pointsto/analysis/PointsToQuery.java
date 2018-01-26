/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis;

import static cc.kave.commons.pointsto.analysis.utils.GenericNameUtils.eraseGenericInstantiations;

import com.google.common.base.MoreObjects;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.pointsto.analysis.visitors.ReferenceNormalizationVisitor;

public class PointsToQuery {

	private static final ReferenceNormalizationVisitor REFERENCE_NORMALIZATION_VISITOR = new ReferenceNormalizationVisitor();

	private final IReference reference;
	private final ITypeName type;
	private final IStatement stmt;
	private final IMemberName member;

	public PointsToQuery(IReference reference, ITypeName type, IStatement stmt, IMemberName member) {
		this.reference = normalizeReference(reference);
		this.type = normalizeType(type);
		this.stmt = stmt;
		this.member = normalizeMember(member);
	}

	public IReference getReference() {
		return reference;
	}

	public IStatement getStmt() {
		return stmt;
	}

	public ITypeName getType() {
		return type;
	}

	public IMemberName getMember() {
		return member;
	}

	protected IReference normalizeReference(IReference reference) {
		return (reference != null) ? reference.accept(REFERENCE_NORMALIZATION_VISITOR, null) : null;
	}

	protected ITypeName normalizeType(ITypeName type) {
		if (type == null || type.isUnknown() || type.isTypeParameter()) {
			return null;
		} else {
			return type;
		}
	}

	protected IMemberName normalizeMember(IMemberName member) {
		if (member == null || member.isUnknown()) {
			return null;
		} else if (member.equals(Names.getUnknownMethod())) {
			// TODO remove once isUnknown is overridden in MethodName
			return null;
		} else {
			return eraseGenericInstantiations(member);
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(PointsToQuery.class).add("reference", reference).add("type", type)
				.add("stmt", stmt).add("member", member).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((member == null) ? 0 : member.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		PointsToQuery other = (PointsToQuery) obj;
		if (member == null) {
			if (other.member != null)
				return false;
		} else if (!member.equals(other.member))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (stmt == null) {
			if (other.stmt != null)
				return false;
		} else if (stmt != other.stmt) // instance equality
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}