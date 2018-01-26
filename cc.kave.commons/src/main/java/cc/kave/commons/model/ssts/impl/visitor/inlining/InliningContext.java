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
package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.CountReturnContext;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.CountReturnsVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.InvocationMethodNameVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.NameScopeVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class InliningContext {

	public static final ITypeName GOT_RESULT_TYPE = Names.newType("p:bool");
	public static final String RESULT_NAME = "$result_";
	public static final String RESULT_FLAG = "$gotNoResult_";
	public static final String CONDITION_VAR = "$returnCondition_";

	private int counter;
	private SST sst;
	private NameScopeVisitor visitor;
	private Scope scope;
	private Set<IMethodDeclaration> nonEntryPoints;
	private boolean inline = false;
	private boolean guardNeeded = false;
	private boolean globalGuardNeed = false;
	private boolean hasPreparedGuards = false;
	private final InliningVisitor inliningVisitor;
	private IAssignableExpression returnExpression = null;

	private Set<IMethodDeclaration> inlinedMethods;
	private Set<IMethodDeclaration> methods;
	private boolean isVoid;

	public InliningContext() {
		this.nonEntryPoints = new HashSet<>();
		this.sst = new SST();
		this.inline = false;
		this.scope = null;
		this.visitor = new NameScopeVisitor();
		this.counter = 0;
		this.inliningVisitor = new InliningVisitor();
		this.inlinedMethods = new LinkedHashSet<>();
		this.methods = new LinkedHashSet<>();
	}

	public void addInlinedMethod(IMethodDeclaration method) {
		inlinedMethods.add(method);
	}

	public void addMethod(IMethodDeclaration method) {
		methods.add(method);
	}

	public void addStatement(IStatement stmt) {
		scope.body.add(stmt);
	}

	private boolean checkCallTree(Set<IMethodName> invocations, Map<IMethodName, Set<IMethodName>> calls,
			HashSet<IMethodName> met, IMethodDeclaration method) {
		if (met.size() == calls.size() || invocations == null) {
			return false;
		} else {
			for (IMethodName call : invocations) {
				if (met.contains(call)) {
					continue;
				}
				met.add(call);
				if (call.equals(method.getName()) || checkCallTree(calls.get(call), calls, met, method)) {
					return true;
				}
			}
		}
		return false;

	}

	public void createSST(ISST oldSST) {
		sst.getDelegates().addAll(oldSST.getDelegates());
		sst.setEnclosingType(oldSST.getEnclosingType());
		sst.getEvents().addAll(oldSST.getEvents());
		sst.getFields().addAll(oldSST.getFields());
		sst.getProperties().addAll(oldSST.getProperties());
	}

	public void enterBlock() {
		Scope newScope = new Scope();
		newScope.parent = scope;
		if (scope != null) {
			newScope.existingIds.addAll(scope.existingIds);
			newScope.changedNames = scope.changedNames;
			newScope.resultName = scope.resultName;
			newScope.gotResultName = scope.gotResultName;
			newScope.isInCondition = scope.isInCondition;
			newScope.hasReturnInLoop = scope.hasReturnInLoop;
		}
		scope = newScope;
	}

	public void leaveBlock(List<IStatement> body) {
		body.clear();
		body.addAll(scope.body);
		scope.parent.resultName = scope.resultName;
		scope.parent.gotResultName = scope.gotResultName;
		setGuardNeeded(isGlobalGuardNeeded());
		scope = scope.parent;
	}

	public void enterScope(List<IStatement> body, Map<IVariableReference, IVariableReference> preChangedNames) {
		Set<IVariableReference> newNames = collectNames(body);
		Scope newScope = new Scope();
		if (scope != null) {
			newScope.existingIds.addAll(scope.existingIds);
			newScope.parent = scope;
			for (IVariableReference ref : newNames) {
				if (newScope.existingIds.contains(ref)) {
					IVariableReference newRef = generateNewRef(ref);
					newScope.changedNames.put(ref, newRef);
					newScope.existingIds.add(newRef);
				} else {
					newScope.existingIds.add(ref);
				}
			}
		} else {
			newScope.existingIds.addAll(newNames);
		}
		if (preChangedNames != null) {
			newScope.changedNames.putAll(preChangedNames);
		}
		scope = newScope;
	}

	public void leaveScope() {
		if (scope.parent != null) {
			scope.parent.existingIds.addAll(scope.existingIds);
			scope.parent.body.addAll(scope.body);
			scope = scope.parent;
		}
		setGlobalGuardNeeded(false);
		setGuardNeeded(false);
	}

	public void resetScope() {
		scope = null;
		counter = 0;
	}

	private Set<IVariableReference> collectNames(List<IStatement> body) {
		Set<IVariableReference> newNames = new LinkedHashSet<>();
		for (IStatement statement : body) {
			statement.accept(visitor, newNames);
		}
		return newNames;
	}

	private IVariableReference generateNewRef(IVariableReference reference) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier("$" + counter + "_" + reference.getIdentifier());
		counter++;
		return ref;
	}

	private Set<IMethodDeclaration> testForRecursiveCalls(Set<IMethodDeclaration> nonEntryPoints) {
		Set<IMethodDeclaration> outputNonEntryPoints = new HashSet<>();
		Map<IMethodName, Set<IMethodName>> calls = getMethodNamesOfInvocations(nonEntryPoints);
		for (IMethodDeclaration method : nonEntryPoints) {
			Set<IMethodName> invocations = calls.get(method.getName());
			if (invocations == null || invocations.contains(method.getName())
					|| (!invocations.isEmpty() && checkCallTree(invocations, calls, Sets.newHashSet(), method))) {
				addMethod(method);
			} else {
				outputNonEntryPoints.add(method);
			}
		}
		return outputNonEntryPoints;
	}

	private Map<IMethodName, Set<IMethodName>> getMethodNamesOfInvocations(Set<IMethodDeclaration> nonEntryPoints) {
		Map<IMethodName, Set<IMethodName>> calls = new HashMap<>();
		Set<IMethodName> removeList = new HashSet<>();
		for (IMethodDeclaration method : nonEntryPoints) {
			Set<IMethodName> context = new HashSet<>();
			method.accept(new InvocationMethodNameVisitor(), context);
			if (calls.containsKey(method.getName())) {
				// Remove MethodDeclarations with duplicated names
				removeList.add(method.getName());
			}
			calls.put(method.getName(), context);
		}
		for (IMethodName name : removeList) {
			calls.remove(name);
		}
		return calls;
	}

	public IStatement visit(IStatement statement, InliningContext context) {
		ArrayList<IStatement> body = Lists.newArrayList(statement);
		visitBlock(body);
		return body.get(0);
	}

	public void visitBlock(List<IStatement> body) {
		enterBlock();
		int index = 0;
		for (IStatement statement : body) {
			statement.accept(getVisitor(), this);
			if ((isGuardNeeded()) && index < body.size() - 1 && body.subList(index + 1, body.size()).size() > 0) {
				addGuardStatement(body, index, false);
				break;
			}
			index++;

		}
		leaveBlock(body);
	}

	public void visitScope(List<IStatement> body) {
		enterScope(body, null);
		int index = 0;
		for (IStatement statement : body) {
			statement.accept(getVisitor(), this);
			if ((isGuardNeeded() || isGlobalGuardNeeded()) && body.subList(index + 1, body.size()).size() > 0) {
				addGuardStatement(body, index, true);
				break;
			}
			index++;
		}
		leaveScope();
	}

	private void addGuardStatement(List<IStatement> body, int index, boolean globalGuard) {
		IfElseBlock block = new IfElseBlock();
		if (this.scope.hasReturnInLoop) {
			block.getElse().add(new BreakStatement());
		}
		block.setCondition(SSTUtil.referenceExprToVariable(getGotResultName()));
		setGuardNeeded(false);
		if (globalGuard)
			setGlobalGuardNeeded(false);
		block.getThen().addAll(body.subList(index + 1, body.size()));
		visitBlock(block.getThen());
		addStatement(block);
	}

	public void setGlobalGuardNeeded(boolean guardNeeded) {
		this.globalGuardNeed = guardNeeded;
	}

	public void setGotResultName(String name) {
		scope.gotResultName = name;
	}

	public void setGuardNeeded(boolean guardNeeded) {
		this.guardNeeded = guardNeeded;
	}

	public void setGuardVariableNames(IMethodName methodName) {
		scope.resultName = RESULT_NAME + methodName.getName();
		scope.gotResultName = RESULT_FLAG + methodName.getName();
	}

	public void setInline(boolean inline) {
		this.inline = inline;
	}

	public void setNonEntryPoints(Set<IMethodDeclaration> nonEntryPoints) {
		this.nonEntryPoints = testForRecursiveCalls(nonEntryPoints);
	}

	public void setResultName(String name) {
		scope.resultName = name;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public List<IStatement> getBody() {
		return scope.body;
	}

	public String getGotResultName() {
		return scope.gotResultName;
	}

	public Set<IMethodDeclaration> getInlinedMethods() {
		return inlinedMethods;
	}

	public IMethodDeclaration getNonEntryPoint(IMethodName methodName) {
		for (IMethodDeclaration method : nonEntryPoints)
			if (method.getName().equals(methodName))
				return method;
		return null;
	}

	public Set<IMethodDeclaration> getNonEntryPoints() {
		return nonEntryPoints;
	}

	public String getResultName() {
		return scope.resultName;
	}

	public ISST getSST() {
		return sst;
	}

	public InliningVisitor getVisitor() {
		return inliningVisitor;
	}

	public boolean isGlobalGuardNeeded() {
		return globalGuardNeed;
	}

	public boolean isGuardNeeded() {
		return guardNeeded;
	}

	public boolean isInline() {
		return inline;
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void resolve(IVariableReference ref) {
		scope.resolve(ref);
	}

	public IAssignableExpression getReturnExpression() {
		return returnExpression;
	}

	public void setReturnExpression(IAssignableExpression returnExpression) {
		this.returnExpression = returnExpression;
	}

	public void setMethods() {
		sst.setMethods(methods);
	}

	public boolean hasPreparedGuards() {
		return hasPreparedGuards;
	}

	public void setPreparedGuards(boolean hasPreparedGuards) {
		this.hasPreparedGuards = hasPreparedGuards;
	}

	public void enterCondition() {
		this.enterBlock();
		this.scope.isInCondition = true;
	}

	public void leaveCondition() {
		this.scope.isInCondition = false;
		this.leaveScope();
	}

	public boolean isInCondition() {
		return this.scope.isInCondition;
	}

	public boolean checkForReturn(IStatement statement) {
		CountReturnContext countContext = new CountReturnContext();
		CountReturnsVisitor countVisitor = new CountReturnsVisitor();
		statement.accept(countVisitor, countContext);
		return countContext.returnCount > 0;
	}

	public void sethasReturnInLoop(boolean b) {
		this.scope.hasReturnInLoop = b;
	}

	public boolean hasReturnInLoop() {
		return this.scope.hasReturnInLoop;
	}

}
