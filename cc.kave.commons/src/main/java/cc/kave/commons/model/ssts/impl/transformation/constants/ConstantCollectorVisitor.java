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

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.references.IFieldReference;

public class ConstantCollectorVisitor extends AbstractConstantCollectorVisitor {
	@Override
	public Set<IFieldDeclaration> visit(ISST sst, Set<IFieldDeclaration> constants) {

		for (IFieldDeclaration field : sst.getFields()) {
			if (field.getName().getDeclaringType().isSimpleType()) {
				constants.add(field);
			}
		}
		for (IPropertyDeclaration property : sst.getProperties()) {
			constants.addAll(property.accept(this, constants));
		}
		for (IMethodDeclaration method : sst.getMethods()) {
			constants.addAll(method.accept(this, constants));
		}
		return constants;
	}

	/**
	 * Remove field from set of possible constants in case it appears on lhs of
	 * assignment. (We only visit field references for which this is the case.)
	 */
	@Override
	public Set<IFieldDeclaration> visit(IFieldReference fieldRef, Set<IFieldDeclaration> constants) {
		constants.removeIf(field -> field.getName().equals(fieldRef.getFieldName()));
		return new HashSet<IFieldDeclaration>();
	}

}
