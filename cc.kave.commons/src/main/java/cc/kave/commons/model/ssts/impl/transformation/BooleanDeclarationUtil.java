/**
 * Copyright 2016 Carina Oberle
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
package cc.kave.commons.model.ssts.impl.transformation;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class BooleanDeclarationUtil {
	public static final IConstantValueExpression TRUE = constant("true");
	public static final IConstantValueExpression FALSE = constant("false");

	public static IVariableReference newVar(int i) {
		return variableReference("$var_" + i);
	}

	/**
	 * Extract main condition (i.e., the one declared last) from a list of
	 * definition statements.
	 */
	public static IReferenceExpression mainCondition(List<IStatement> conditionDefinition) {
		return refExpr(mainVar(conditionDefinition));
	}

	/**
	 * Extract main variable (the one declared last) from a list of definition
	 * statements.
	 */
	public static IVariableReference mainVar(List<IStatement> definition) {
		List<IVariableDeclaration> vars = definition.stream().filter(s -> s instanceof IVariableDeclaration)
				.map(v -> (IVariableDeclaration) v).collect(Collectors.toList());
		return vars.isEmpty() ? null : vars.get(vars.size() - 1).getReference();
	}

	/**
	 * Declare the given reference and assign the given expression.
	 */
	public static List<IStatement> define(IVariableReference ref, IAssignableExpression expr) {
		IVariableDeclaration varDec = booleanDeclaration(ref);
		IAssignment varAssign = assign(ref, expr);
		return Lists.newArrayList(varDec, varAssign);
	}

	/**
	 * Declare new variable and assign the given expression.
	 */
	public static List<IStatement> define(int count, IAssignableExpression expr) {
		return define(newVar(count), expr);
	}

	/**
	 * Declare a variable with the given identifier and assign the given
	 * expression to it.
	 * 
	 * @param identifier
	 *            Name of the variable to declare.
	 * @param expr
	 *            The expression to assign.
	 */
	public static List<IStatement> define(String identifier, IAssignableExpression expr) {
		IVariableDeclaration varDec = booleanDeclaration(identifier);
		IAssignment varAssign = assign(variableReference(identifier), expr);
		return Lists.newArrayList(varDec, varAssign);
	}

	public static IVariableDeclaration booleanDeclaration(String identifier) {
		return declare(identifier, Names.newType("p:bool"));
	}

	public static IVariableDeclaration booleanDeclaration(IVariableReference ref) {
		return booleanDeclaration(ref.getIdentifier());
	}
}