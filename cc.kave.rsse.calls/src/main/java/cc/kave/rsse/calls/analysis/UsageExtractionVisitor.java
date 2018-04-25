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

import static cc.kave.commons.utils.ssts.TypeShapeUtils.findFirstOccurrenceInHierachy;
import static cc.kave.commons.utils.ssts.TypeShapeUtils.findFirstOccurrenceInHierachyFromBase;
import static cc.kave.commons.utils.ssts.TypeShapeUtils.isDeclaredInSameType;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCatchParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.memberAccess;

import java.util.List;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.naming.TypeErasure;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSite;

public class UsageExtractionVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	private final ITypeShape typeShape;
	private final AbstractObjectToUsageMapper usages;
	private final UsageExtractionAssignmentDefinitionVisitor defVisitor;

	private final List<ILambdaExpression> lambdaQueue;

	public UsageExtractionVisitor(AbstractObjectToUsageMapper usages, ITypeShape typeShape,
			UsageExtractionAssignmentDefinitionVisitor defVisitor, List<ILambdaExpression> lambdaQueue) {
		this.usages = usages;
		this.typeShape = typeShape;
		this.defVisitor = defVisitor;
		this.lambdaQueue = lambdaQueue;
	}

	@Override
	public List<Void> visit(List<IStatement> body, Void context) {
		for (IStatement stmt : body) {
			// TODO test: failing case
			try {
				stmt.accept(this, context);
			} catch (Exception e) {
				Logger.err("UsageExtractionVisitor has caught exception: %s", e);
				e.printStackTrace();
			}
		}
		return null;
	}

	// declaration

	@Override
	public Void visit(ITryBlock block, Void context) {
		for (ICatchBlock cb : block.getCatchBlocks()) {
			if (cb.getKind() == CatchBlockKind.General) {
				IParameterName parameter = cb.getParameter();
				Usage u = usages.get(parameter);
				ITypeName t = cb.getParameter().getValueType();
				u.type = t;
				u.definition = definedByCatchParameter();
			}
		}
		return super.visit(block, context);
	}

	@Override
	public Void visit(IVariableDeclaration stmt, Void context) {
		Usage q = usages.get(stmt.getReference());
		q.type = stmt.getType();
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, Void context) {
		Usage q = usages.get(stmt.getReference());
		q.definition = stmt.getExpression().accept(defVisitor, null);
		return super.visit(stmt, null);
	}

	// usage

	@Override
	public Void visit(IInvocationExpression expr, Void context) {
		IMethodName m = expr.getMethodName();

		if (m.isUnknown()) {
			// TODO test
			return null;
		}

		String varRef = expr.getReference().getIdentifier();
		if ("this".equals(varRef) && isDeclaredInSameType(m, typeShape)) {
			m = findFirstOccurrenceInHierachy(m, typeShape);
		} else if ("base".equals(varRef)) {
			m = findFirstOccurrenceInHierachyFromBase(m, typeShape);
		}

		List<IParameterName> formalParams = m.getParameters();
		List<ISimpleExpression> actualParams = expr.getParameters();

		int minNumParams = formalParams.size();
		int maxNumParams = formalParams.size();
		// TODO test: varargs
		boolean hasVarArgs = minNumParams > 0 && m.getParameters().get(minNumParams - 1).isParameterArray();
		if (hasVarArgs) {
			minNumParams -= 1;
			maxNumParams = 100;
		}
		// TODO test: optionals
		while (minNumParams > 0 && m.getParameters().get(minNumParams - 1).isOptional()) {
			minNumParams -= 1;
		}
		// TODO test: error handling (can opts/params be refined in subtypes?)
		int numActualParam = actualParams.size();
		if (numActualParam < minNumParams || numActualParam > maxNumParams) {
			// if (m.getDeclaringType().getAssembly().isLocalProject()) {
			// return;
			// }
			// TODO test: base call of .ctor
			if (m.isConstructor() && SSTUtil.varRef("base").equals(expr.getReference())) {
				return null;
			}
			// TODO test: seems to be a bug in the transformation
			if (minNumParams > 0 && formalParams.get(0).isExtensionMethodParameter()
					&& new VariableReference().equals(expr.getReference())) {
				return null;
			}
			Logger.err("Invocation has %d actual params, but %d formal params, ignoring. (%s)\n", actualParams.size(),
					formalParams.size(), m.getIdentifier());
			return null;
		}

		// TypeErasure must not happen before requesting the parameters!
		m = TypeErasure.of(m);

		if (!m.isStatic()) {
			IReference r = expr.getReference();
			usages.get(r).getUsageSites().add(call(m));
		}

		int argNum = 0;
		for (ISimpleExpression ap : actualParams) {
			IParameterName fp = formalParams.size() > argNum ? formalParams.get(argNum) : null;

			if (ap instanceof IReferenceExpression) {
				IReference r = ((IReferenceExpression) ap).getReference();
				Usage u = usages.get(r);

				if (fp != null) {
					if (fp.isOutput()) {
						u.definition = Definitions.definedByOutParameter(m);
					} else {
						UsageSite cs = callParameter(m, argNum);
						u.getUsageSites().add(cs);
					}
				}
				// TODO test: stay at last varargs param
				argNum = Math.min(formalParams.size() - 1, ++argNum);
			}
		}

		return null;
	}

	@Override
	public Void visit(ICompletionExpression expr, Void context) {
		IVariableReference ref = expr.getVariableReference();
		if (ref != null) {
			usages.get(ref).isQuery = true;
		}
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, Void context) {
		IReference ref = expr.getReference();
		if (ref instanceof IMemberReference) {
			IMemberReference mref = (IMemberReference) ref;
			String varRef = mref.getReference().getIdentifier();
			Usage u = usages.get(mref.getReference());
			if (ref instanceof IEventReference) {
				IEventReference er = (IEventReference) ref;
				u.usageSites.add(memberAccess(er.getEventName()));
			} else if (ref instanceof IFieldReference) {
				IFieldReference fr = (IFieldReference) ref;
				u.usageSites.add(memberAccess(fr.getFieldName()));
			} else if (ref instanceof IPropertyReference) {
				IPropertyReference pr = (IPropertyReference) ref;
				IPropertyName pn = pr.getPropertyName();
				if ("this".equals(varRef)) {
					pn = findFirstOccurrenceInHierachy(pn, typeShape);
				} else if ("base".equals(varRef)) {
					pn = findFirstOccurrenceInHierachyFromBase(pn, typeShape);
				}
				u.usageSites.add(memberAccess(pn));
			} else if (ref instanceof IMethodReference) {
				IMethodReference mr = (IMethodReference) ref;
				IMethodName mn = mr.getMethodName();
				if ("this".equals(varRef)) {
					mn = findFirstOccurrenceInHierachy(mn, typeShape);
				} else if ("base".equals(varRef)) {
					mn = findFirstOccurrenceInHierachyFromBase(mn, typeShape);
				}
				u.usageSites.add(memberAccess(mn));
			}
		}
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, Void context) {
		Usage u = usages.get(expr);
		u.type = expr.getName().getExplicitMethodName().getDeclaringType();
		u.definition = Definitions.definedByLambdaDecl();

		lambdaQueue.add(expr);
		return null;
	}
}