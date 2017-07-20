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
package cc.kave.commons.model.ssts.impl.transformation.booleans;

import java.util.HashMap;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class RefLookup extends HashMap<IVariableReference, IAssignableExpression> {
	public static final IAssignableExpression UNKNOWN = new UnknownExpression();

	private static final long serialVersionUID = 1L;

	public RefLookup(IVariableReference ref, IAssignableExpression expr) {
		this.put(ref, expr);
	}

	public RefLookup() {
	}

	public IAssignableExpression tryLookup(ISimpleExpression simple) {
		IAssignableExpression result = null;
		if (simple instanceof IReferenceExpression) {
			result = get(((IReferenceExpression) simple).getReference());
		}
		return result;
	}

	public boolean isKnown(IReference ref) {
		return this.containsKey(ref) && !this.get(ref).equals(UNKNOWN);
	}
}
