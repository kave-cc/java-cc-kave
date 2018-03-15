/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.rsse.calls.extraction.usages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedSSTNodeException;
import cc.kave.commons.pointsto.analysis.utils.EnclosingNodeHelper;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.SSTBuilder;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.ssts.SSTNodeHierarchy;
import cc.kave.rsse.calls.extraction.usages.stats.NopUsageStatisticsCollector;
import cc.kave.rsse.calls.extraction.usages.stats.UsageStatisticsCollector;
import cc.kave.rsse.calls.usages.DefinitionSiteKind;
import cc.kave.rsse.calls.usages.Query;
import cc.kave.rsse.calls.usages.Usage;
import cc.kave.rsse.calls.utils.LambdaContextUtils;

public class PointsToUsageExtractor {

	private final DescentStrategy descentStrategy;
	private CallsitePruning callsitePruningBehavior;
	private MethodContextReplacement methodContextRewritingStrategy;

	private UsageStatisticsCollector collector;

	public PointsToUsageExtractor() {
		this.descentStrategy = new SimpleDescentStrategy();
		this.callsitePruningBehavior = CallsitePruning.EMPTY_RECV_CALLSITES;
		this.methodContextRewritingStrategy = MethodContextReplacement.FIRST_OR_SUPER_OR_ELEMENT;
		this.collector = new NopUsageStatisticsCollector();
	}

	@Inject
	public PointsToUsageExtractor(DescentStrategy descentStrategy, CallsitePruning callsitePruningBehavior,
			MethodContextReplacement methodContextRewritingStrategy) {
		this.descentStrategy = descentStrategy;
		this.callsitePruningBehavior = callsitePruningBehavior;
		this.methodContextRewritingStrategy = methodContextRewritingStrategy;
		this.collector = new NopUsageStatisticsCollector();
	}

	public CallsitePruning getCallsitePruningBehavior() {
		return callsitePruningBehavior;
	}

	public void setCallsitePruningBehavior(CallsitePruning callsitePruningBehavior) {
		this.callsitePruningBehavior = callsitePruningBehavior;
	}

	public MethodContextReplacement getMethodContextRewritingStrategy() {
		return methodContextRewritingStrategy;
	}

	public void setMethodContextRewritingStrategy(MethodContextReplacement methodContextRewritingStrategy) {
		this.methodContextRewritingStrategy = methodContextRewritingStrategy;
	}

	public void setStatisticsCollector(UsageStatisticsCollector collector) {
		this.collector = collector;
	}

	public UsageStatisticsCollector getStatisticsCollector() {
		return collector;
	}

	public List<Usage> extract(PointsToContext context) {
		collector.onProcessContext(context);

		List<Usage> contextUsages = new ArrayList<>();
		UsageExtractionVisitor visitor = new UsageExtractionVisitor();
		UsageExtractionVisitorContext visitorContext = new UsageExtractionVisitorContext(context, descentStrategy);
		String className = context.getSST().getEnclosingType().getName();
		for (IMethodDeclaration methodDecl : context.getSST().getEntryPoints()) {
			try {
				visitor.visitEntryPoint(methodDecl, visitorContext);
			} catch (AssertionException ex) {
				throw ex;
			} catch (RuntimeException ex) {
				Logger.err("Failed to extract usages from " + className + ":" + methodDecl.getName().getName(), ex);
				ex.printStackTrace();
				continue;
			}

			List<Query> rawUsages = visitorContext.getUsages();
			List<? extends Usage> processedUsages = processUsages(rawUsages, context.getTypeShape());
			contextUsages.addAll(processedUsages);
			collector.onEntryPointUsagesExtracted(methodDecl, processedUsages);
		}

		return contextUsages;
	}

	public List<Usage> extractQueries(ICompletionExpression completionExpression, PointsToContext context,
			PointsToQueryBuilder queryBuilder, SSTNodeHierarchy nodeHierarchy) {
		if (completionExpression.getTypeReference() != null) {
			Logger.err("Queries for type references are not supported");
			return Collections.emptyList();
		}

		IVariableReference receiverVarRef = completionExpression.getVariableReference();
		if (receiverVarRef == null) {
			Logger.debug("Treating missing variable reference as 'this'");
			receiverVarRef = SSTBuilder.variableReference("this");
		}

		// find enclosing statement and method
		EnclosingNodeHelper nodeHelper = new EnclosingNodeHelper(nodeHierarchy);
		IStatement enclosingStatement = nodeHelper.getEnclosingStatement(completionExpression);
		IMemberDeclaration enclosingDecl = nodeHelper.getEnclosingDeclaration(enclosingStatement);
		if (!(enclosingDecl instanceof IMethodDeclaration)) {
			Logger.err("Completion expression must be within a method declaration");
			return Collections.emptyList();
		}
		IMethodDeclaration enclosingMethod = (IMethodDeclaration) enclosingDecl;

		UsageExtractionVisitor visitor = new UsageExtractionVisitor();
		UsageExtractionVisitorContext visitorContext = new UsageExtractionVisitorContext(context, descentStrategy);

		try {
			// treat the enclosing method as entry point although it might not
			// be one
			visitor.visitEntryPoint(enclosingMethod, visitorContext);

			PointsToQuery ptQuery;
			if (completionExpression.getVariableReference() == null) {
				// the query builder cannot infer the type of references that
				// are not part of the original SST
				cc.kave.commons.model.naming.types.ITypeName type = context.getTypeShape().getTypeHierarchy()
						.getElement();
				ptQuery = new PointsToQuery(receiverVarRef, type, enclosingStatement, enclosingMethod.getName());
			} else {
				ptQuery = queryBuilder.newQuery(receiverVarRef, enclosingStatement, enclosingMethod.getName());
			}
			Set<AbstractLocation> locations = context.getPointerAnalysis().query(ptQuery);
			List<Query> usages = new ArrayList<>();
			for (AbstractLocation location : locations) {
				Query usage = visitorContext.getUsage(location);
				if (usage != null) {
					usages.add(usage);
				}
			}

			// just rewrite, do not prune
			rewriteUsages(usages, context.getTypeShape());
			collector.onEntryPointUsagesExtracted(enclosingMethod, usages);

			return new ArrayList<Usage>(usages);
		} catch (AssertionException | UnexpectedSSTNodeException ex) {
			throw ex;
		} catch (RuntimeException ex) {
			Logger.err("Failed to extract queries", ex);
			return Collections.emptyList();
		}

	}

	private List<Query> processUsages(List<Query> rawUsages, ITypeShape typeShape) {
		List<Query> processedUsages = pruneUsages(rawUsages);
		rewriteUsages(processedUsages, typeShape);
		return processedUsages;
	}

	private List<Query> pruneUsages(List<Query> usages) {
		List<Query> retainedUsages = new ArrayList<>(usages.size());
		int numPruned = 0;

		for (Query usage : usages) {
			// prune usages that have no call sites or have an unknown type
			if (pruneUsage(usage)) {
				++numPruned;
			} else {
				retainedUsages.add(usage);
			}
		}

		collector.onUsagesPruned(numPruned);
		return retainedUsages;
	}

	private boolean pruneUsage(Usage usage) {
		switch (callsitePruningBehavior) {
		case EMPTY_CALLSITES:
			return usage.getAllCallsites().isEmpty() || usage.getType().isUnknown();
		case EMPTY_RECV_CALLSITES:
			return usage.getReceiverCallsites().isEmpty() || usage.getType().isUnknown();
		default:
			throw new IllegalStateException("Unknown call site pruning behavior");
		}
	}

	private void rewriteUsages(List<Query> usages, ITypeShape typeShape) {
		rewriteThisType(usages, typeShape);
		rewriteContexts(usages, typeShape);
	}

	private void rewriteThisType(List<Query> usages, ITypeShape typeShape) {
		// TODO what about the methods of the call sites?

		ITypeHierarchy typeHierarchy = typeShape.getTypeHierarchy();
		if (typeHierarchy.hasSuperclass()) {
			ITypeName superType = typeHierarchy.getExtends().getElement();

			for (Query usage : usages) {
				// change type of usages referring to the enclosing class to the
				// super class
				if (usage.getDefinitionSite().getKind() == DefinitionSiteKind.THIS) {
					// TODO maybe add check whether this is safe (call sites do
					// not refer to 'this')
					usage.setType(superType);
				}
			}

		}
	}

	private void rewriteContexts(List<Query> usages, ITypeShape typeShape) {
		for (Query usage : usages) {
			usage.setClassContext(getClassContext(usage.getClassContext(), typeShape.getTypeHierarchy()));
			usage.setMethodContext(getMethodContext(usage.getMethodContext(), typeShape.getMethodHierarchies()));
		}
	}

	private ITypeName getClassContext(ITypeName currentContext, ITypeHierarchy typeHierarchy) {
		if (typeHierarchy.hasSuperclass()) {
			ITypeName superType = typeHierarchy.getExtends().getElement();
			return superType;
		} else {
			return currentContext;
		}
	}

	private IMethodName getMethodContext(IMethodName currentContext,
			Collection<IMemberHierarchy<IMethodName>> hierarchies) {
		boolean wasLambdaContext = LambdaContextUtils.isLambdaName(currentContext);
		IMethodName restoredMethod = currentContext;
		if (wasLambdaContext) {
			// remove lambda qualifiers to find method in hierarchy...
			restoredMethod = LambdaContextUtils.removeLambda(currentContext);
		}

		for (IMemberHierarchy<IMethodName> methodHierarchy : hierarchies) {
			IMethodName method = methodHierarchy.getElement();

			if (restoredMethod.equals(method)) {
				IMethodName firstMethod = methodHierarchy.getFirst();
				IMethodName superMethod = methodHierarchy.getSuper();

				IMethodName newMethodContext = currentContext;
				if (firstMethod != null) {
					newMethodContext = firstMethod;
				} else if (methodContextRewritingStrategy == MethodContextReplacement.FIRST_OR_UNKNOWN) {
					return Names.getUnknownMethod();
				} else if (superMethod != null) {
					newMethodContext = superMethod;
				} else if (methodContextRewritingStrategy == MethodContextReplacement.FIRST_OR_SUPER_OR_UNKNOWN) {
					return Names.getUnknownMethod();
				} else {
					// FIRST_OR_SUPER_OR_ELEMENT
					return currentContext;
				}

				// ... but add it again later
				if (wasLambdaContext) {
					newMethodContext = LambdaContextUtils.addLambda(newMethodContext);
				}

				return newMethodContext;
			}
		}

		return currentContext;
	}
}