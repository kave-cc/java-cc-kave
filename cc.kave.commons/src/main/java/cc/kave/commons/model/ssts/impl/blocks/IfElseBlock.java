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
import com.google.gson.annotations.SerializedName;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.ToStringUtils;

public class IfElseBlock implements IIfElseBlock {

	private ISimpleExpression condition;
	private List<IStatement> then;
	@SerializedName("Else")
	private List<IStatement> _else;

	public IfElseBlock() {
		this.condition = new UnknownExpression();
		this._else = new ArrayList<>();
		this.then = new ArrayList<>();
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		List<ISSTNode> children = Lists.newArrayList(condition);
		children.addAll(then);
		children.addAll(_else);
		return children;
	}

	@Override
	public ISimpleExpression getCondition() {
		return this.condition;
	}

	@Override
	public List<IStatement> getThen() {
		return this.then;
	}

	@Override
	public List<IStatement> getElse() {
		return this._else;
	}

	public void setElse(List<IStatement> _else) {
		this._else = _else;
	}

	public void setThen(List<IStatement> then) {
		this.then = then;
	}

	public void setCondition(ISimpleExpression condition) {
		this.condition = condition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_else == null) ? 0 : _else.hashCode());
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((then == null) ? 0 : then.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IfElseBlock))
			return false;
		IfElseBlock other = (IfElseBlock) obj;
		if (_else == null) {
			if (other._else != null)
				return false;
		} else if (!_else.equals(other._else))
			return false;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (then == null) {
			if (other.then != null)
				return false;
		} else if (!then.equals(other.then))
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