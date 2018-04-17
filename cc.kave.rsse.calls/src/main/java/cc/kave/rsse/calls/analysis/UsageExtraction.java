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

import static cc.kave.commons.model.naming.Names.getUnknownType;
import static cc.kave.commons.model.naming.impl.v0.NameUtils.toValueType;
import static cc.kave.rsse.calls.analysis.LambdaContextUtils.addLambda;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCatchParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccess;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMethodParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByThis;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.fieldAccess;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.propertyAccess;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import cc.kave.caret.analyses.IPathInsensitivePointToAnalysis;
import cc.kave.caret.analyses.IPathInsensitivePointsToInfo;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Definition;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSite;

public class UsageExtraction {

	private final IPathInsensitivePointToAnalysis p2a;
	private final List<Pair<IMethodName, ILambdaExpression>> lambdaQueue;

	public UsageExtraction(IPathInsensitivePointToAnalysis p2a) {
		this.p2a = p2a;
		lambdaQueue = new LinkedList<>();
	}

	public Map<Object, List<IUsage>> extractMap(Context ctx) {
		Map<Object, List<IUsage>> globalMap = new IdentityHashMap<>();
		IPathInsensitivePointsToInfo p2info = p2a.analyze(ctx);

		ISST sst = ctx.getSST();
		ITypeName cCtx = sst.getEnclosingType();
		for (IMethodDeclaration md : sst.getMethods()) {
			IMethodName mCtx = md.getName();
			AbstractObjectToUsageMapper aoToUsages = new AbstractObjectToUsageMapper(p2info, cCtx, mCtx);
			UsageExtractionVisitor visitor = new UsageExtractionVisitor(aoToUsages,
					ctx.getTypeShape().getMethodHierarchies(), cCtx, md.getName());

			visitor.initMembers(sst);
			md.accept(visitor, null);

			Map<Object, List<IUsage>> localMap = aoToUsages.getMap();
			for (Object ao : localMap.keySet()) {
				List<IUsage> usages = globalMap.getOrDefault(ao, new LinkedList<>());
				usages.addAll(localMap.get(ao));
				globalMap.put(ao, usages);
			}
		}
		// TODO process "lambda queue"
		return globalMap;
	}

	public List<IUsage> extract(Context ctx) {
		Map<Object, List<IUsage>> map = extractMap(ctx);
		return map.values().stream().flatMap(e -> e.stream()).collect(toList());
	}

	public class AbstractObjectToUsageMapper {

		private final IPathInsensitivePointsToInfo p2info;
		private final ITypeName cCtx;
		private final IMethodName mCtx;
		private final Map<Object, Usage> usages = new HashMap<>();

		public AbstractObjectToUsageMapper(IPathInsensitivePointsToInfo p2info, ITypeName cCtx, IMethodName mCtx) {
			this.p2info = p2info;
			this.cCtx = cCtx;
			this.mCtx = mCtx;
		}

		private Usage getUsageForAbstractObject(Object ao) {
			if (usages.containsKey(ao)) {
				return usages.get(ao);
			}
			Usage q = new Usage();
			q.type = getUnknownType();
			q.classCtx = cCtx;
			q.methodCtx = mCtx;
			q.definition = definedByUnknown();
			usages.put(ao, q);
			return q;
		}

		public Map<Object, List<IUsage>> getMap() {
			Map<Object, List<IUsage>> map = new IdentityHashMap<>();
			for (Object ao : usages.keySet()) {
				if (!map.containsKey(ao)) {
					map.put(ao, new LinkedList<>());
				}
				map.get(ao).add(usages.get(ao));
			}
			return map;
		}

		public Usage usage(ISST sst) {
			Object ao = p2info.getAbstractObject(sst);
			Usage u = getUsageForAbstractObject(ao);
			return u;
		}

		public Usage get(IMemberDeclaration d) {
			Object ao = p2info.getAbstractObject(d);
			Usage u = getUsageForAbstractObject(ao);
			return u;
		}

		public Usage get(IParameterName k) {
			Object ao = p2info.getAbstractObject(k);
			Usage u = getUsageForAbstractObject(ao);
			return u;
		}

		public Usage get(IReference r) {
			Object ao = p2info.getAbstractObject(r);
			Usage u = getUsageForAbstractObject(ao);
			return u;
		}
	}

	public class UsageExtractionVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

		private final AbstractObjectToUsageMapper usages;
		private final Set<IMemberHierarchy<IMethodName>> methodHierarchies;
		private final ITypeName cCtx;
		private final IMethodName mCtx;

		public UsageExtractionVisitor(AbstractObjectToUsageMapper usages,
				Set<IMemberHierarchy<IMethodName>> methodHierarchies, ITypeName cCtx, IMethodName mCtx) {
			this.usages = usages;
			this.methodHierarchies = methodHierarchies;
			this.cCtx = cCtx;
			this.mCtx = mCtx;
		}

		// declaration

		public void initMembers(ISST sst) {

			Usage u = usages.usage(sst);
			u.type = sst.getEnclosingType();
			u.definition = definedByThis();

			for (IEventDeclaration ed : sst.getEvents()) {
				u = usages.get(ed);
				u.type = ed.getName().getValueType();
				u.definition = definedByUnknown();
			}

			for (IFieldDeclaration fd : sst.getFields()) {
				u = usages.get(fd);
				u.type = fd.getName().getValueType();
				u.definition = definedByMemberAccess(fd.getName());
			}

			for (IPropertyDeclaration pd : sst.getProperties()) {
				u = usages.get(pd);
				u.type = pd.getName().getValueType();
				u.definition = definedByMemberAccess(pd.getName());
			}

			for (IMethodDeclaration md : sst.getMethods()) {
				u = usages.get(md);
				u.type = toValueType(md.getName(), false);
				u.definition = definedByUnknown();
			}
		}

		@Override
		public Void visit(IMethodDeclaration md, Void context) {
			int paramNum = 0;
			IMethodName m = md.getName();
			for (IParameterName p : m.getParameters()) {
				paramNum++;
				Usage u = usages.get(p);
				u.type = p.getValueType();
				u.definition = definedByMethodParameter(m, paramNum);
			}
			return super.visit(md, context);
		}

		@Override
		public Void visit(ITryBlock block, Void context) {
			for (ICatchBlock cb : block.getCatchBlocks()) {
				if (cb.getKind() == CatchBlockKind.General) {
					Usage u = usages.get(cb.getParameter());
					ITypeName t = cb.getParameter().getValueType();
					u.type = t;
					u.definition = definedByCatchParameter(t);
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
			q.definition = getDefSiteByRightHandSide(stmt.getExpression());
			return super.visit(stmt, null);
		}

		private Definition getDefSiteByRightHandSide(IAssignableExpression e) {

			if (e instanceof IConstantValueExpression) {
				return Definitions.definedByConstant();
			}

			if (e instanceof IInvocationExpression) {
				IMethodName m = ((IInvocationExpression) e).getMethodName();
				if (m.isConstructor()) {
					return Definitions.definedByConstructor(m);
				} else {
					return Definitions.definedByReturnValue(m);
				}
			}

			if (e instanceof IReferenceExpression) {
				IReference r = ((IReferenceExpression) e).getReference();

				if (r instanceof IFieldReference) {
					IFieldName f = ((IFieldReference) r).getFieldName();
					return definedByMemberAccess(f);
				}

				if (r instanceof IPropertyReference) {
					IPropertyName p = ((IPropertyReference) r).getPropertyName();
					return definedByMemberAccess(p);
				}
			}

			return Definitions.definedByUnknown();
		}

		// usage

		@Override
		public Void visit(IInvocationExpression expr, Void context) {
			IMethodName m = expr.getMethodName();

			if (isLocalMethod(m)) {
				m = findOverriddenMethod(m);
			}

			if (!m.isStatic()) {
				IReference r = expr.getReference();
				usages.get(r).getUsageSites().add(call(m));
			}

			int argNum = 0;
			for (ISimpleExpression e : expr.getParameters()) {
				argNum++;
				if (e instanceof IReferenceExpression) {
					IReference r = ((IReferenceExpression) e).getReference();
					UsageSite cs = callParameter(m, argNum);
					usages.get(r).getUsageSites().add(cs);
				}
			}

			return null;
		}

		private boolean isLocalMethod(IMethodName m) {
			boolean isLocalMethod = cCtx.equals(m.getDeclaringType());
			return isLocalMethod;
		}

		private IMethodName findOverriddenMethod(IMethodName m) {
			for (IMemberHierarchy<IMethodName> mh : methodHierarchies) {
				if (m.equals(mh.getElement())) {
					if (mh.getFirst() != null) {
						return mh.getFirst();
					}
					if (mh.getSuper() != null) {
						return mh.getSuper();
					}
					break;
				}
			}
			return m;
		}

		@Override
		public Void visit(IReferenceExpression expr, Void context) {
			Usage u = usages.get(expr.getReference());
			IReference ref = expr.getReference();

			if (ref instanceof IFieldReference) {
				IFieldReference fr = (IFieldReference) ref;
				u.usageSites.add(fieldAccess(fr.getFieldName()));
			} else if (ref instanceof IPropertyReference) {
				IPropertyReference pr = (IPropertyReference) ref;
				u.usageSites.add(propertyAccess(pr.getPropertyName()));
			}

			return null;
		}

		@Override
		public Void visit(ILambdaExpression expr, Void context) {
			lambdaQueue.add(Pair.of(addLambda(mCtx), expr));
			return null;
		}
	}
}