/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.declarations;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.ToStringUtils;

public class MethodDeclaration implements IMethodDeclaration {

	private IMethodName name;
	private boolean isEntryPoint;
	public final List<IStatement> body;

	public MethodDeclaration() {
		name = Names.getUnknownMethod();
		body = new ArrayList<>();
	}

	public MethodDeclaration(IMethodName m) {
		this();
		name = m;
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		return Lists.newArrayList();
	}

	@Override
	public IMethodName getName() {
		return this.name;
	}

	@Override
	public boolean isEntryPoint() {
		return this.isEntryPoint;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setName(IMethodName name) {
		this.name = name;
	}

	public void setEntryPoint(boolean isEntryPoint) {
		this.isEntryPoint = isEntryPoint;
	}

	@Override
	public int hashCode() {
		int hashCode = this.body.hashCode();
		hashCode = (hashCode * 397) ^ this.name.hashCode();
		hashCode = (hashCode * 397) ^ (this.isEntryPoint ? 1231 : 1237);
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodDeclaration other = (MethodDeclaration) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (isEntryPoint != other.isEntryPoint)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
}