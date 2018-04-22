/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.caret.analyses;

import static cc.kave.commons.model.naming.Names.newArrayType;
import static cc.kave.commons.model.naming.impl.v0.NameUtils.toValueType;

import java.util.HashMap;
import java.util.Map;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.utils.ssts.completioninfo.VariableScope;
import cc.kave.commons.utils.ssts.completioninfo.VariableScope.ErrorHandling;

public class TypeBasedPointsToAnalysis implements IPathInsensitivePointToAnalysis {

	private final ErrorHandling errorHandling;

	public TypeBasedPointsToAnalysis(ErrorHandling errorHandling) {
		this.errorHandling = errorHandling;
	}

	private class AnalysisContext {
		public final VariableScope<ITypeName> scope = new VariableScope<ITypeName>(errorHandling);
		public final Map<ITypeName, Object> aos = new HashMap<>();
		public final PathInsensitivePointsToInfo p2info = new PathInsensitivePointsToInfo();

		public void register(Object key, ITypeName t) {
			Object ao;
			if (aos.containsKey(t)) {
				ao = aos.get(t);
			} else {
				ao = t;
				aos.put(t, t);
			}
			p2info.set(key, ao);
		}

		public void register(String id, Object key, ITypeName t) {
			scope.declare(id, t);
			register((Object) key, t);
		}

		public void register(String id, Object key) {
			ITypeName t = scope.get(id);
			register((Object) key, t);
		}
	}

	@Override
	public IPathInsensitivePointsToInfo analyze(Context ctx) {
		TypeBasedPointsToAnalysisVisitor visitor = new TypeBasedPointsToAnalysisVisitor();
		AnalysisContext context = new AnalysisContext();
		ctx.getSST().accept(visitor, context);
		return context.p2info;
	}

	private int count = 0;

	private ITypeName rnd() {
		return Names.newType("Rnd%d, P", count++);
	}

	private class TypeBasedPointsToAnalysisVisitor extends AbstractTraversingNodeVisitor<AnalysisContext, Void> {

		@Override
		public Void visit(ISST sst, AnalysisContext context) {
			ITypeName t = sst.getEnclosingType();
			context.register("this", sst, t);
			return super.visit(sst, context);
		}

		@Override
		public Void visit(IEventDeclaration ed, AnalysisContext context) {
			context.register(ed, ed.getName().getValueType());
			return null;
		}

		@Override
		public Void visit(IFieldDeclaration fd, AnalysisContext context) {
			context.register(fd, fd.getName().getValueType());
			return null;
		}

		@Override
		public Void visit(IMethodDeclaration md, AnalysisContext context) {
			context.register(md, toValueType(md.getName(), true));

			context.scope.open();
			for (IParameterName p : md.getName().getParameters()) {
				context.register(p, p.getValueType());
			}
			super.visit(md, context);
			context.scope.close();

			return null;
		}

		@Override
		public Void visit(IPropertyDeclaration pd, AnalysisContext context) {
			context.register(pd, pd.getName().getValueType());

			context.scope.open();
			if (pd.getName().hasSetter()) {
				IParameterName valueParam = pd.getName().getSetterValueParam();
				context.register(valueParam, valueParam.getValueType());
			}
			for (IParameterName n : pd.getName().getParameters()) {
				context.register(n.getName(), n, n.getValueType());
			}

			context.scope.open();
			visit(pd.getGet(), context);
			context.scope.close();

			context.scope.open();
			visit(pd.getSet(), context);
			context.scope.close();

			context.scope.close();
			return null;
		}

		// ########################################################################

		@Override
		public Void visit(IDoLoop block, AnalysisContext context) {
			context.scope.open();
			visit(block.getBody(), context);
			context.scope.close();
			block.getCondition().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IForEachLoop block, AnalysisContext context) {
			context.scope.open();
			super.visit(block, context);
			context.scope.close();
			return null;
		}

		@Override
		public Void visit(IForLoop block, AnalysisContext context) {
			context.scope.open();
			super.visit(block, context);
			context.scope.close();
			return null;
		}

		@Override
		public Void visit(IIfElseBlock block, AnalysisContext context) {
			block.getCondition().accept(this, context);
			context.scope.open();
			visit(block.getThen(), context);
			context.scope.close();
			context.scope.open();
			visit(block.getElse(), context);
			context.scope.close();
			return null;
		}

		@Override
		public Void visit(ILockBlock block, AnalysisContext context) {
			context.scope.open();
			super.visit(block, context);
			context.scope.close();
			return null;
		}

		@Override
		public Void visit(ISwitchBlock block, AnalysisContext context) {
			context.scope.open();
			super.visit(block, context);
			context.scope.close();
			return null;
		}

		// TODO replace with SSTNodeHierarchy in visit(Throw) as soon as ICatchBlock is
		// an ISSTNode
		private ITypeName currentCatchType;

		@Override
		public Void visit(ITryBlock block, AnalysisContext context) {
			context.scope.open();
			visit(block.getBody(), context);
			context.scope.close();

			for (ICatchBlock cb : block.getCatchBlocks()) {
				IParameterName p = cb.getParameter();

				context.scope.open();
				ITypeName tmp = currentCatchType; // does not solve the problem that nested structures could override
													// the type, but reduces the probability of a problem by one level
				currentCatchType = p.getValueType();

				if (CatchBlockKind.Default == cb.getKind()) {
					context.register(p.getName(), p, currentCatchType);
				} else {
					context.register(p, currentCatchType == null ? Names.getUnknownType() : currentCatchType);
				}
				visit(cb.getBody(), context);

				currentCatchType = tmp;
				context.scope.close();
			}

			context.scope.open();
			visit(block.getFinally(), context);
			context.scope.close();

			return null;
		}

		@Override
		public Void visit(IUncheckedBlock block, AnalysisContext context) {
			context.scope.open();
			super.visit(block, context);
			context.scope.close();
			return null;
		}

		@Override
		public Void visit(IUsingBlock block, AnalysisContext context) {
			context.scope.open();
			super.visit(block, context);
			context.scope.close();
			return null;
		}

		@Override
		public Void visit(IWhileLoop block, AnalysisContext context) {
			block.getCondition().accept(this, context);
			context.scope.open();
			visit(block.getBody(), context);
			context.scope.close();
			return null;
		}

		// ########################################################################

		@Override
		public Void visit(IVariableDeclaration stmt, AnalysisContext context) {
			ITypeName t = stmt.getType();
			String id = stmt.getReference().getIdentifier();
			context.register(id, stmt.getReference(), t);
			return null;
		}

		@Override
		public Void visit(ILambdaExpression e, AnalysisContext context) {
			ILambdaName n = e.getName();
			context.register(e, toValueType(n.getExplicitMethodName(), false));
			context.scope.open();
			for (IParameterName p : n.getParameters()) {
				context.register(p.getName(), p, p.getValueType());
			}
			super.visit(e, context);
			context.scope.close();
			return null;
		}

		@Override
		public Void visit(IThrowStatement stmt, AnalysisContext context) {
			if (!stmt.isReThrow()) {
				stmt.getReference().accept(this, context);
			} else {
				context.register(stmt.getReference(), currentCatchType);
			}

			return null;
		}

		// ########################################################################

		@Override
		public Void visit(IEventReference ref, AnalysisContext context) {
			context.register(ref, ref.getEventName().getValueType());
			return super.visit(ref, context);
		}

		@Override
		public Void visit(IFieldReference ref, AnalysisContext context) {
			context.register(ref, ref.getFieldName().getValueType());
			return super.visit(ref, context);
		}

		@Override
		public Void visit(IIndexAccessReference ref, AnalysisContext context) {
			IVariableReference varRef = ref.getExpression().getReference();
			ITypeName t = context.scope.get(varRef.getIdentifier());

			if (t.isArray()) {
				IArrayTypeName at = t.asArrayTypeName();
				ITypeName baseType = at.getArrayBaseType();
				int newRank = at.getRank() - ref.getExpression().getIndices().size();
				if (newRank > 0) {
					context.register(ref, newArrayType(newRank, baseType));
				} else {
					context.register(ref, baseType);
				}
			} else {
				// TODO improve over this!
				context.register(ref, Names.getUnknownType());
			}
			return super.visit(ref, context);
		}

		@Override
		public Void visit(IMethodReference ref, AnalysisContext context) {
			context.register(ref, toValueType(ref.getMethodName(), true));
			return super.visit(ref, context);
		}

		@Override
		public Void visit(IPropertyReference ref, AnalysisContext context) {
			context.register(ref, ref.getPropertyName().getValueType());
			return super.visit(ref, context);
		}

		@Override
		public Void visit(IVariableReference r, AnalysisContext context) {
			context.register(r.getIdentifier(), r);
			return null;
		}
	}
}