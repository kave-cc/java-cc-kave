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
package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.ToStringUtils;

public class ForEachLoop implements IForEachLoop {

	private IVariableDeclaration declaration;
	private IVariableReference loopedReference;
	private List<IStatement> body;

	public ForEachLoop() {
		this.declaration = new VariableDeclaration();
		this.loopedReference = new VariableReference();
		this.body = new ArrayList<>();
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		List<ISSTNode> children = Lists.newArrayList(declaration, loopedReference);
		children.addAll(body);
		return children;
	}

	@Override
	public IVariableDeclaration getDeclaration() {
		return this.declaration;
	}

	@Override
	public IVariableReference getLoopedReference() {
		return this.loopedReference;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setDeclaration(IVariableDeclaration declaration) {
		this.declaration = declaration;
	}

	public void setLoopedReference(IVariableReference loopedReference) {
		this.loopedReference = loopedReference;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((declaration == null) ? 0 : declaration.hashCode());
		result = prime * result + ((loopedReference == null) ? 0 : loopedReference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ForEachLoop))
			return false;
		ForEachLoop other = (ForEachLoop) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
			return false;
		if (loopedReference == null) {
			if (other.loopedReference != null)
				return false;
		} else if (!loopedReference.equals(other.loopedReference))
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