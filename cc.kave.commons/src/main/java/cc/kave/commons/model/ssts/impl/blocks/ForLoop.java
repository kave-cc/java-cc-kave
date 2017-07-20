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

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.ToStringUtils;

public class ForLoop implements IForLoop {

	private List<IStatement> init;
	private ILoopHeaderExpression condition;
	private List<IStatement> step;
	private List<IStatement> body;

	public ForLoop() {
		this.init = new ArrayList<>();
		this.step = new ArrayList<>();
		this.body = new ArrayList<>();
		this.condition = new UnknownExpression();
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		List<ISSTNode> children = new ArrayList<>(init);
		children.add(condition);
		children.addAll(step);
		children.addAll(body);
		return children;
	}

	@Override
	public List<IStatement> getInit() {
		return this.init;
	}

	@Override
	public ILoopHeaderExpression getCondition() {
		return this.condition;
	}

	@Override
	public List<IStatement> getStep() {
		return this.step;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setInit(List<IStatement> init) {
		this.init = init;
	}

	public void setStep(List<IStatement> step) {
		this.step = step;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	public void setCondition(ILoopHeaderExpression condition) {
		this.condition = condition;
	}

	public int hashCode() {
		int hashCode = 34 + this.init.hashCode();
		hashCode = (hashCode * 397) ^ this.step.hashCode();
		hashCode = (hashCode * 397) ^ this.body.hashCode();
		hashCode = (hashCode * 397) ^ this.condition.hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ForLoop))
			return false;
		ForLoop other = (ForLoop) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (init == null) {
			if (other.init != null)
				return false;
		} else if (!init.equals(other.init))
			return false;
		if (step == null) {
			if (other.step != null)
				return false;
		} else if (!step.equals(other.step))
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