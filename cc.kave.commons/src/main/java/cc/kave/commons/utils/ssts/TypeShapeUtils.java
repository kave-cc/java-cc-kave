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
package cc.kave.commons.utils.ssts;

import static cc.kave.commons.utils.io.Logger.debug;

import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;

public class TypeShapeUtils {

	public static boolean isDeclaredInSameType(IMemberName m, ITypeShape typeShape) {
		ITypeName cCtx = typeShape.getTypeHierarchy().getElement();
		return cCtx.equals(m.getDeclaringType());
	}

	@SuppressWarnings("unchecked")
	public static <T extends IMemberName> T findFirstOccurrenceInHierachy(T m, ITypeShape typeShape) {
		if (m == null) {
			throw new IllegalArgumentException("Member is null.");
		}

		if (m.isStatic()) {
			return m;
		}

		// TODO test: can it happen that the current handling crashes for rebased
		// members?
		if (!isDeclaredInSameType(m, typeShape)) {
			return m;
		}
		// if (!isDeclaredInSameType(m, typeShape)) {
		// String msg = "Member %s is defined in the wrong type. Expected: %s";
		// String fmsg = format(msg, m, typeShape.getTypeHierarchy().getElement());
		// throw new IllegalArgumentException(fmsg);
		// }

		if (m instanceof IEventName) {
			return (T) findFirst((IEventName) m, typeShape.getEventHierarchies());
		}
		if (m instanceof IMethodName) {
			IMethodName meth = (IMethodName) m;
			if (meth.isConstructor() || meth.isInit() || meth.isStatic()) {
				return m;
			}
			return (T) findFirst(meth, typeShape.getMethodHierarchies());
		}
		if (m instanceof IPropertyName) {
			return (T) findFirst((IPropertyName) m, typeShape.getPropertyHierarchies());
		}

		throw new IllegalArgumentException(
				String.format("Member type %s cannot be overridden in a hierarchy.", m.getClass().getSimpleName()));
	}

	private static <T extends IMemberName> T findFirst(T m, Set<IMemberHierarchy<T>> mhs) {
		for (IMemberHierarchy<T> mh : mhs) {
			if (m.equals(mh.getElement())) {
				if (mh.getFirst() != null) {
					return mh.getFirst();
				}
				if (mh.getSuper() != null) {
					return mh.getSuper();
				}
				return m;
			}
		}
		debug("Cannot find the first occurrence of %s in the type shape.", m);
		return m;
	}

	@SuppressWarnings("unchecked")
	public static <T extends IMemberName> T findFirstOccurrenceInHierachyFromBase(T m, ITypeShape typeShape) {
		if (m == null) {
			throw new IllegalArgumentException("Member is null.");
		}
		if (m instanceof IEventName) {
			return (T) findFirstFromBase((IEventName) m, typeShape.getEventHierarchies());
		}
		if (m instanceof IMethodName) {
			return (T) findFirstFromBase((IMethodName) m, typeShape.getMethodHierarchies());
		}
		if (m instanceof IPropertyName) {
			return (T) findFirstFromBase((IPropertyName) m, typeShape.getPropertyHierarchies());
		}

		throw new IllegalArgumentException(
				String.format("Member type %s cannot be overridden in a hierarchy.", m.getClass().getSimpleName()));
	}

	private static <T extends IMemberName> T findFirstFromBase(T m, Set<IMemberHierarchy<T>> mhs) {
		for (IMemberHierarchy<T> mh : mhs) {
			if (m.equals(mh.getSuper())) {
				if (mh.getFirst() != null) {
					return mh.getFirst();
				}
				return m;
			}
		}
		return m;
	}
}