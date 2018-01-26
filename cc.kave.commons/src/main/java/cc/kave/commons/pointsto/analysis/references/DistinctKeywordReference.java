/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.analysis.references;

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import com.google.common.base.MoreObjects;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.references.IVariableReference;;

public class DistinctKeywordReference implements DistinctReference {

	private final String keyword;
	private final ITypeName type;

	public DistinctKeywordReference(String keyword, ITypeName type) {
		this.keyword = keyword;
		this.type = type;
	}

	@Override
	public IVariableReference getReference() {
		return variableReference(keyword);
	}

	@Override
	public ITypeName getType() {
		return type;
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
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
		DistinctKeywordReference other = (DistinctKeywordReference) obj;
		if (keyword == null) {
			if (other.keyword != null)
				return false;
		} else if (!keyword.equals(other.keyword))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(DistinctKeywordReference.class).add("keyword", keyword)
				.add("type", type.getName()).toString();
	}
}