/**
 * Copyright 2015 Carina Oberle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.transformation.constants;

import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class InlineConstantVisitor extends AbstractTraversingNodeVisitor<Void, Void> {
	private Set<IFieldDeclaration> constants;
	private AbstractConstantCollectorVisitor collector;

	public InlineConstantVisitor() {
		this.constants = new HashSet<IFieldDeclaration>();
		this.collector = new ConstantCollectorVisitor();
	}

	public InlineConstantVisitor(AbstractConstantCollectorVisitor collector) {
		this.constants = new HashSet<IFieldDeclaration>();
		this.collector = collector;
	}

	private void collectConstants(ISSTNode node) {
		constants.clear();
		constants.addAll(node.accept(collector, new HashSet<IFieldDeclaration>()));
	}

	@Override
	public Void visit(ISST sst, Void context) {
		collectConstants(sst);
		super.visit(sst, context);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, Void context) {
		ISimpleExpression expr = stmt.getExpression();
		if (isConstantExpression(expr)) {
			if (stmt instanceof ReturnStatement) {
				((ReturnStatement) stmt).setExpression(constant(null));
			}
		}
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, Void context) {
		List<ISimpleExpression> parameters = new ArrayList<ISimpleExpression>();
		for (ISimpleExpression expr : entity.getParameters()) {
			if (isConstantExpression(expr)) {
				parameters.add(constant(null));
			} else {
				parameters.add(expr);
			}
		}
		if (entity instanceof InvocationExpression) {
			((InvocationExpression) entity).setParameters(parameters);
		}
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, Void context) {
		super.visit(expr, context);
		if (expr instanceof IfElseExpression) {
			if (isConstantExpression(expr.getCondition())) {
				((IfElseExpression) expr).setCondition(constant(null));
			}
			if (isConstantExpression(expr.getThenExpression())) {
				((IfElseExpression) expr).setThenExpression(constant(null));
			}
			if (isConstantExpression(expr.getElseExpression())) {
				((IfElseExpression) expr).setElseExpression(constant(null));
			}
		}
		return null;
	}

	@Override
	public Void visit(IIndexAccessExpression expr, Void context) {
		super.visit(expr, context);
		if (expr instanceof IndexAccessExpression) {
			List<ISimpleExpression> indices = new ArrayList<ISimpleExpression>();
			for (ISimpleExpression idx : expr.getIndices()) {
				if (isConstantExpression(idx))
					indices.add(constant(null));
				else
					indices.add(idx);
			}
			((IndexAccessExpression) expr).setIndices(indices);
		}
		return null;
	}

	@Override
	public Void visit(IBinaryExpression expr, Void context) {
		super.visit(expr, context);
		if (expr instanceof BinaryExpression) {
			if (isConstantExpression(expr.getLeftOperand())) {
				((BinaryExpression) expr).setLeftOperand(constant(null));
			}
			if (isConstantExpression(expr.getRightOperand())) {
				((BinaryExpression) expr).setRightOperand(constant(null));
			}
		}
		return null;
	}

	@Override
	public Void visit(IUnaryExpression expr, Void context) {
		super.visit(expr, context);
		if (expr instanceof UnaryExpression) {
			if (isConstantExpression(expr.getOperand())) {
				((UnaryExpression) expr).setOperand(constant(null));
			}
		}
		expr.getOperand().accept(this, context);
		return null;
	}

	private boolean isConstantExpression(ISimpleExpression expr) {
		if (expr instanceof IReferenceExpression) {
			IReference reference = ((IReferenceExpression) expr).getReference();
			if (reference instanceof IFieldReference) {
				return isConstant((IFieldReference) reference);
			}
		}
		return false;
	}

	private boolean isConstant(IFieldReference field) {
		for (IFieldDeclaration constant : constants) {
			if (constant.getName().equals(field.getFieldName()))
				return true;
		}
		return false;
	}

}