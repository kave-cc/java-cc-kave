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
package cc.kave.rsse.calls.analysis;

import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.Definitions;

public class UsageExtractionAssignmentDefinitionVisitor extends AbstractThrowingNodeVisitor<Void, IDefinition> {

	private ITypeShape typeShape;

	public UsageExtractionAssignmentDefinitionVisitor(ITypeShape typeShape) {
		this.typeShape = typeShape;
	}

	@Override
	public IDefinition visit(IConstantValueExpression expr, Void context) {
		return Definitions.definedByConstant();
	}

	@Override
	public IDefinition visit(IUnknownExpression expr, Void context) {
		return Definitions.definedByUnknown();
	}

	@Override
	public IDefinition visit(INullExpression expr, Void context) {
		return Definitions.definedByConstant();
	}

	@Override
	public IDefinition visit(IReferenceExpression expr, Void context) {
		return expr.getReference().accept(this, context);
	}

	// ### assignable ############################################################

	@Override
	public IDefinition visit(IBinaryExpression expr, Void context) {
		return Definitions.definedByUnknown();
	}

	@Override
	public IDefinition visit(ICastExpression expr, Void context) {
		return Definitions.definedByCast();
	}

	@Override
	public IDefinition visit(ICompletionExpression entity, Void context) {
		return Definitions.definedByUnknown();
	}

	@Override
	public IDefinition visit(IComposedExpression expr, Void context) {
		return Definitions.definedByUnknown();
	}

	@Override
	public IDefinition visit(IIfElseExpression expr, Void context) {
		return Definitions.definedByUnknown();
	}

	@Override
	public IDefinition visit(IIndexAccessExpression expr, Void context) {
		return Definitions.definedByUnknown();
	}

	@Override
	public IDefinition visit(IInvocationExpression expr, Void context) {
		IMethodName m = expr.getMethodName();
		if (m.isConstructor()) {
			return Definitions.definedByConstructor(m);
		}
		if ("this".equals(expr.getReference().getIdentifier())) {
			for (IMemberHierarchy<IMethodName> eh : typeShape.getMethodHierarchies()) {
				if (m.equals(eh.getElement())) {
					if (eh.getFirst() != null) {
						return Definitions.definedByReturnValue(eh.getFirst());
					} else if (eh.getSuper() != null) {
						return Definitions.definedByReturnValue(eh.getSuper());
					}
					break;
				}
			}
		}
		return Definitions.definedByReturnValue(m);
	}

	@Override
	public IDefinition visit(ILambdaExpression expr, Void context) {
		return Definitions.definedByLambdaDecl();
	}

	@Override
	public IDefinition visit(ITypeCheckExpression expr, Void context) {
		return Definitions.definedByConstant();
	}

	@Override
	public IDefinition visit(IUnaryExpression expr, Void context) {
		return Definitions.definedByUnknown();
	}

	// ### references ############################################################

	@Override
	public IDefinition visit(IEventReference ref, Void context) {
		IEventName n = ref.getEventName();
		if ("this".equals(ref.getReference().getIdentifier())) {
			for (IMemberHierarchy<IEventName> eh : typeShape.getEventHierarchies()) {
				if (n.equals(eh.getElement())) {
					if (eh.getFirst() != null) {
						return Definitions.definedByMemberAccess(eh.getFirst());
					} else if (eh.getSuper() != null) {
						return Definitions.definedByMemberAccess(eh.getSuper());
					}
					break;
				}
			}
		}
		return Definitions.definedByMemberAccess(n);
	}

	@Override
	public IDefinition visit(IFieldReference ref, Void context) {
		return Definitions.definedByMemberAccess(ref.getFieldName());
	}

	@Override
	public IDefinition visit(IIndexAccessReference indexAccessRef, Void context) {
		return Definitions.definedByUnknown();
	}

	@Override
	public IDefinition visit(IMethodReference eventRef, Void context) {
		IMethodName n = eventRef.getMethodName();
		if ("this".equals(eventRef.getReference().getIdentifier())) {
			for (IMemberHierarchy<IMethodName> eh : typeShape.getMethodHierarchies()) {
				if (n.equals(eh.getElement())) {
					if (eh.getFirst() != null) {
						return Definitions.definedByMemberAccess(eh.getFirst());
					} else if (eh.getSuper() != null) {
						return Definitions.definedByMemberAccess(eh.getSuper());
					}
					break;
				}
			}
		}
		return Definitions.definedByMemberAccess(n);
	}

	@Override
	public IDefinition visit(IPropertyReference ref, Void context) {
		IPropertyName n = ref.getPropertyName();
		if ("this".equals(ref.getReference().getIdentifier())) {
			for (IMemberHierarchy<IPropertyName> eh : typeShape.getPropertyHierarchies()) {
				if (n.equals(eh.getElement())) {
					if (eh.getFirst() != null) {
						return Definitions.definedByMemberAccess(eh.getFirst());
					} else if (eh.getSuper() != null) {
						return Definitions.definedByMemberAccess(eh.getSuper());
					}
					break;
				}
			}
		}
		return Definitions.definedByMemberAccess(n);
	}

	@Override
	public IDefinition visit(IUnknownReference ref, Void context) {
		return Definitions.definedByUnknown();
	}

	@Override
	public IDefinition visit(IVariableReference ref, Void context) {
		String id = ref.getIdentifier();
		if ("this".equals(id) || "base".equals(id)) {
			return Definitions.definedByThis();
		}
		return Definitions.definedByUnknown();
	}
}