/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.analysis.utils;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedSSTNodeException;
import cc.kave.commons.utils.ssts.SSTNodeHierarchy;

public class EnclosingNodeHelper {

	private final SSTNodeHierarchy nodeHierarchy;

	public EnclosingNodeHelper(SSTNodeHierarchy nodeHierarchy) {
		this.nodeHierarchy = nodeHierarchy;
	}

	public IStatement getEnclosingStatement(IExpression expr) {
		ISSTNode currentNode = expr;
		while (!(currentNode instanceof IStatement)) {
			currentNode = nodeHierarchy.getParent(currentNode);
		}

		return (IStatement) currentNode;
	}

	public IMemberDeclaration getEnclosingDeclaration(IStatement stmt) {
		ISSTNode currentNode = stmt;
		while (!(currentNode instanceof IMemberDeclaration)) {
			currentNode = nodeHierarchy.getParent(currentNode);
		}

		return (IMemberDeclaration) currentNode;
	}

	public IMemberName getEnclosingMember(IStatement stmt) {
		IMemberDeclaration memberDecl = getEnclosingDeclaration(stmt);
		if (memberDecl instanceof IMethodDeclaration) {
			return ((IMethodDeclaration) memberDecl).getName();
		} else if (memberDecl instanceof IPropertyDeclaration) {
			return ((IPropertyDeclaration) memberDecl).getName();
		} else {
			throw new UnexpectedSSTNodeException(memberDecl);
		}
	}
}