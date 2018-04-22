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

import static cc.kave.commons.model.naming.impl.v0.NameUtils.toValueType;
import static cc.kave.commons.utils.ssts.TypeShapeUtils.findFirstOccurrenceInHierachy;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccess;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMethodParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByThis;
import static java.util.stream.Collectors.toList;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.caret.analyses.IPathInsensitivePointToAnalysis;
import cc.kave.caret.analyses.IPathInsensitivePointsToInfo;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.naming.TypeErasure;
import cc.kave.commons.utils.ssts.SSTNodeHierarchy;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.Usage;

public class UsageExtraction {

	private final IPathInsensitivePointToAnalysis p2a;

	private IPathInsensitivePointsToInfo p2info;
	private UsageExtractionAssignmentDefinitionVisitor defVisitor;
	private LinkedList<ILambdaExpression> lambdaQueue;
	private Context ctx;
	private Map<Object, List<IUsage>> globalMap;

	public UsageExtraction(IPathInsensitivePointToAnalysis p2a) {
		this.p2a = p2a;
	}

	public List<IUsage> extract(Context ctx) {
		Map<Object, List<IUsage>> map = extractMap(ctx);
		return map.values().stream().flatMap(e -> e.stream()).collect(toList());
	}

	public Map<Object, List<IUsage>> extractMap(Context ctx) {
		lambdaQueue = new LinkedList<>();
		globalMap = new IdentityHashMap<>();
		this.ctx = ctx;
		try {
			p2info = p2a.analyze(ctx);

			ITypeShape typeShape = ctx.getTypeShape();
			defVisitor = new UsageExtractionAssignmentDefinitionVisitor(typeShape);

			for (IPropertyDeclaration pd : ctx.getSST().getProperties()) {
				IPropertyName firstInHierarchy = findFirstOccurrenceInHierachy(pd.getName(), typeShape);
				// TODO test: using pd.getName() vs firstInHierarchy crashes
				// TODO test: why do I need to check both? (--> ClearCanvas/ClearCanvas/Samples/
				// Google/Calendar/Calendar.sln-contexts.zip)
				if (pd.getName().hasGetter() && firstInHierarchy.hasGetter()) {
					IMethodName elem = pd.getName().getExplicitGetterName();
					IMethodName mCtx = firstInHierarchy.getExplicitGetterName();
					analyzeMethodContext(elem, mCtx, pd.getGet());
				}
				if (pd.getName().hasSetter() && firstInHierarchy.hasSetter()) {
					IMethodName elem = pd.getName().getExplicitSetterName();
					IMethodName mCtx = firstInHierarchy.getExplicitSetterName();
					analyzeMethodContext(elem, mCtx, pd.getSet());
				}
			}

			for (IMethodDeclaration md : ctx.getSST().getMethods()) {
				IMethodName elem = md.getName();
				IMethodName mCtx = findFirstOccurrenceInHierachy(elem, typeShape);
				analyzeMethodContext(elem, mCtx, md.getBody());
			}

			SSTNodeHierarchy sstHier = new SSTNodeHierarchy(ctx.getSST());

			while (!lambdaQueue.isEmpty()) {
				ILambdaExpression expr = lambdaQueue.pop();

				analyzeLambda(ctx, typeShape, sstHier, expr);
			}

		} catch (Exception e) {
			// TODO test: add some case that crashes
			Logger.err("Failed to process context: %s", e);
			e.printStackTrace();
		}
		return globalMap;
	}

	private void analyzeLambda(Context ctx, ITypeShape typeShape, SSTNodeHierarchy sstHier, ILambdaExpression expr) {
		ITypeName cCtx = typeShape.getTypeHierarchy().getElement();
		IMethodName mCtx = expr.getName().getExplicitMethodName();
		AbstractObjectToUsageMapper aoToUsages = new AbstractObjectToUsageMapper(p2info, cCtx, TypeErasure.of(mCtx));

		initMembers(ctx.getSST(), aoToUsages);
		initLambda(expr, aoToUsages, sstHier);

		UsageExtractionVisitor usageVisitor = new UsageExtractionVisitor(aoToUsages, ctx.getTypeShape(), defVisitor,
				lambdaQueue);
		usageVisitor.visit(expr.getBody(), null);
		copyResults(aoToUsages.map, globalMap);
	}

	private void initLambda(ILambdaExpression expr, AbstractObjectToUsageMapper aoToUsages, SSTNodeHierarchy sstHier) {
		ILambdaExpression le = sstHier.findParent(expr, ILambdaExpression.class);
		if (le != null) {
			initLambda(le, aoToUsages, sstHier);
		}
		for (IParameterName p : expr.getName().getParameters()) {
			Usage u = aoToUsages.get(p);
			u.type = p.getValueType();
			u.definition = Definitions.definedByLambdaParameter();
		}
	}

	private void analyzeMethodContext(IMethodName mCtx, IMethodName firstCtx, List<IStatement> body) {
		// TODO test: in which the difference between mCtx/firstCtx creates a
		// problem (if initParams is set back to "firstCtx")
		ISST sst = ctx.getSST();
		ITypeName cCtx = sst.getEnclosingType();

		AbstractObjectToUsageMapper aoToUsages = new AbstractObjectToUsageMapper(p2info, cCtx, firstCtx);

		initMembers(sst, aoToUsages);
		initParams(mCtx, aoToUsages);

		UsageExtractionVisitor usageVisitor = new UsageExtractionVisitor(aoToUsages, ctx.getTypeShape(), defVisitor,
				lambdaQueue);
		usageVisitor.visit(body, null);
		copyResults(aoToUsages.map, globalMap);
	}

	private static void copyResults(Map<Object, Usage> localMap, Map<Object, List<IUsage>> globalMap) {
		for (Object ao : localMap.keySet()) {
			List<IUsage> usages = globalMap.getOrDefault(ao, new LinkedList<>());
			usages.add(localMap.get(ao));
			globalMap.put(ao, usages);
		}
	}

	public void initMembers(ISST sst, AbstractObjectToUsageMapper usages) {

		Usage u = usages.get(sst);
		u.type = sst.getEnclosingType();
		u.definition = definedByThis();

		for (IEventDeclaration ed : sst.getEvents()) {
			u = usages.get(ed);
			u.type = ed.getName().getValueType();
			u.definition = definedByMemberAccess(ed.getName());
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
			u.definition = definedByMemberAccess(md.getName());
		}
	}

	private void initParams(IMethodName m, AbstractObjectToUsageMapper usages) {
		Usage u;
		int paramNum = 0;
		for (IParameterName p : m.getParameters()) {
			u = usages.get(p);
			u.type = p.getValueType();
			u.definition = definedByMethodParameter(m, paramNum++);
		}
	}
}