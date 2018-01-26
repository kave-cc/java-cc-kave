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
package cc.kave.commons.pointsto.analysis.unification;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedSSTNodeException;
import cc.kave.commons.utils.io.Logger;

public abstract class AssignmentHandler<T> {

	protected abstract IReference getReference(T entry);

	public final void process(T dest, T src) {
		IReference destRef = getReference(dest);
		IReference srcRef = getReference(src);

		if (destRef instanceof IVariableReference) {
			if (srcRef instanceof IVariableReference) {
				assignVarToVar(dest, src);
			} else if (srcRef instanceof IFieldReference) {
				assignFieldToVar(dest, src);
			} else if (srcRef instanceof IPropertyReference) {
				assignPropToVar(dest, src);
			} else if (srcRef instanceof IIndexAccessReference) {
				assignArrayToVar(dest, src);
			} else if (srcRef instanceof IMethodReference) {
				assignMethodToVar(dest, src);
			} else if (srcRef instanceof IEventReference) {
				Logger.log("Ignoring event reference");
			} else {
				throw new UnexpectedSSTNodeException(srcRef);
			}
		} else if (destRef instanceof IFieldReference) {
			if (srcRef instanceof IVariableReference) {
				assignVarToField(dest, src);
			} else if (srcRef instanceof IFieldReference) {
				assignFieldToField(dest, src);
			} else if (srcRef instanceof IPropertyReference) {
				assignPropToField(dest, src);
			} else if (srcRef instanceof IIndexAccessReference) {
				assignArrayToField(dest, src);
			} else if (srcRef instanceof IMethodReference) {
				assignMethodToField(dest, src);
			} else if (srcRef instanceof IEventReference) {
				Logger.log("Ignoring event reference");
			} else {
				throw new UnexpectedSSTNodeException(srcRef);
			}
		} else if (destRef instanceof IPropertyReference) {
			if (srcRef instanceof IVariableReference) {
				assignVarToProp(dest, src);
			} else if (srcRef instanceof IFieldReference) {
				assignFieldToProp(dest, src);
			} else if (srcRef instanceof IPropertyReference) {
				assignPropToProp(dest, src);
			} else if (srcRef instanceof IIndexAccessReference) {
				assignArrayToProp(dest, src);
			} else if (srcRef instanceof IMethodReference) {
				assignMethodToProp(dest, src);
			} else if (srcRef instanceof IEventReference) {
				Logger.log("Ignoring event reference");
			} else {
				throw new UnexpectedSSTNodeException(srcRef);
			}
		} else if (destRef instanceof IIndexAccessReference) {
			if (srcRef instanceof IVariableReference) {
				assignVarToArray(dest, src);
			} else if (srcRef instanceof IFieldReference) {
				assignFieldToArray(dest, src);
			} else if (srcRef instanceof IPropertyReference) {
				assignPropToArray(dest, src);
			} else if (srcRef instanceof IIndexAccessReference) {
				assignArrayToArray(dest, src);
			} else if (srcRef instanceof IMethodReference) {
				assignMethodToArray(dest, src);
			} else if (srcRef instanceof IEventReference) {
				Logger.log("Ignoring event reference");
			} else {
				throw new UnexpectedSSTNodeException(srcRef);
			}
		} else if (destRef instanceof IEventReference) {
			if (srcRef instanceof IMethodReference) {
				assignMethodToEvent(dest, src);
			} else if (srcRef instanceof IVariableReference) {
				assignVarToEvent(dest, src);
			} else if (srcRef instanceof IFieldReference) {
				assignFieldToEvent(dest, src);
			} else if (srcRef instanceof IPropertyReference) {
				assignPropToEvent(dest, src);
			} else if (srcRef instanceof IEventReference) {
				assignEventToEvent(dest, src);
			} else {
				throw new UnexpectedSSTNodeException(srcRef);
			}
		} else {
			throw new UnexpectedSSTNodeException(destRef);
		}
	}

	protected abstract void assignVarToVar(T dest, T src);

	protected abstract void assignFieldToVar(T dest, T src);

	protected abstract void assignPropToVar(T dest, T src);

	protected abstract void assignArrayToVar(T dest, T src);

	protected abstract void assignMethodToVar(T dest, T src);

	protected abstract void assignVarToField(T dest, T src);

	protected abstract void assignFieldToField(T dest, T src);

	protected abstract void assignPropToField(T dest, T src);

	protected abstract void assignArrayToField(T dest, T src);

	protected abstract void assignMethodToField(T dest, T src);

	protected abstract void assignVarToProp(T dest, T src);

	protected abstract void assignFieldToProp(T dest, T src);

	protected abstract void assignPropToProp(T dest, T src);

	protected abstract void assignArrayToProp(T dest, T src);

	protected abstract void assignMethodToProp(T dest, T src);

	protected abstract void assignVarToArray(T dest, T src);

	protected abstract void assignFieldToArray(T dest, T src);

	protected abstract void assignPropToArray(T dest, T src);

	protected abstract void assignArrayToArray(T dest, T src);

	protected abstract void assignMethodToArray(T dest, T src);

	protected abstract void assignMethodToEvent(T dest, T src);

	protected abstract void assignVarToEvent(T dest, T src);

	protected abstract void assignFieldToEvent(T dest, T src);

	protected abstract void assignPropToEvent(T dest, T src);

	protected abstract void assignEventToEvent(T dest, T src);

}
