/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.utils.naming;

import static cc.kave.commons.utils.StringUtils.FindCorrespondingCloseBracket;
import static cc.kave.commons.utils.StringUtils.FindNext;

import java.util.Map;

import com.google.common.collect.Maps;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.visitor.TypeErasureVisitor;
import cc.kave.commons.model.typeshapes.EventHierarchy;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.PropertyHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.commons.utils.io.Logger;

public class TypeErasure {
	public static ITypeName of(ITypeName type) {
		return Names.newType(of(type.getIdentifier()));
	}

	public static IMethodName of(IMethodName method) {
		return Names.newMethod(of(method.getIdentifier()));
	}

	public static IFieldName of(IFieldName field) {
		return Names.newField(of(field.getIdentifier()));
	}

	public static IEventName of(IEventName event) {
		return Names.newEvent(of(event.getIdentifier()));
	}

	public static IPropertyName of(IPropertyName property) {
		return Names.newProperty(of(property.getIdentifier()));
	}

	public static IParameterName of(IParameterName parameter) {
		return Names.newParameter(of(parameter.getIdentifier()));
	}

	public static ILambdaName of(ILambdaName name) {
		return Names.newLambda(of(name.getIdentifier()));
	}

	public static String of(String id) {
		int startIdx = id.indexOf('`');
		if (startIdx == -1) {
			return id;
		}

		Map<String, String> replacements = Maps.newLinkedHashMap();
		int tick = FindNext(id, 0, '`');

		while (tick != -1) {
			int open = FindNext(id, tick, '[');
			int length = open - tick - 1;
			String numStr = length > 0 ? id.substring(tick + 1, open).trim() : "0";

			if (length < 1) {
				// TODO fix name creation, this should not happen!
				Logger.err("cannot remove generic (no tick number): %s", id);
			}

			int numGenerics = 0;
			try {
				numGenerics = Integer.parseInt(numStr);
			} catch (NumberFormatException e) {
				// TODO fix name creation, this should not happen!
				Logger.err("cannot remove generic (invalid tick number between %d and %d): %s", tick, open, id);
			}

			while (IsArray(id, open)) {
				open = FindNext(id, open + 1, '[');
			}

			for (int i = 0; i < numGenerics; i++) {
				open = FindNext(id, open + 1, '[');
				int close = FindCorrespondingCloseBracket(id, open);

				int arrowStart = FindNext(id, open, '-');
				if (arrowStart != -1 && arrowStart < close) {
					String param = id.substring(open, arrowStart).trim();
					String complete = id.substring(open, close);
					replacements.put(complete, param);
				}

				open = close + 1;
			}
			tick = FindNext(id, tick + 1, '`');
		}
		String res = id;
		for (String k : replacements.keySet()) {
			String with = replacements.get(k);
			res = res.replace(k, with);
		}
		boolean isStillBound = res.contains("->"); // can happen if nested binds repeat
		return isStillBound ? of(res) : res;
	}

	private static boolean IsArray(String id, int open) {
		Asserts.assertTrue(id.charAt(open) == '[', "not pointed to opening brace");
		boolean is1DArr = id.charAt(open + 1) == ']';
		boolean isNdArr = id.charAt(open + 1) == ',';
		return is1DArr || isNdArr;
	}

	public static Context of(Context in) {
		Context out = new Context();
		out.setTypeShape(of(in.getTypeShape()));
		out.setSST(of(in.getSST()));
		return out;
	}

	public static ITypeShape of(ITypeShape in) {
		TypeShape out = new TypeShape();
		out.setTypeHierarchy(of(in.getTypeHierarchy()));
		for (IMemberHierarchy<IEventName> mh : in.getEventHierarchies()) {
			out.getEventHierarchies().add(ofEvents(mh));
		}
		for (IMemberHierarchy<IMethodName> mh : in.getMethodHierarchies()) {
			out.getMethodHierarchies().add(ofMethods(mh));
		}
		for (IMemberHierarchy<IPropertyName> mh : in.getPropertyHierarchies()) {
			out.getPropertyHierarchies().add(ofProperties(mh));
		}
		for (ITypeName t : in.getNestedTypes()) {
			out.getNestedTypes().add(TypeErasure.of(t));
		}
		for (IFieldName f : in.getFields()) {
			out.getFields().add(TypeErasure.of(f));
		}
		for (IDelegateTypeName t : in.getDelegates()) {
			out.getDelegates().add(TypeErasure.of(t).asDelegateTypeName());
		}

		return out;
	}

	private static IMemberHierarchy<IEventName> ofEvents(IMemberHierarchy<IEventName> in) {
		EventHierarchy out = new EventHierarchy();
		if (in.getElement() != null) {
			out.setElement(TypeErasure.of(in.getElement()));
		}
		if (in.getSuper() != null) {
			out.setSuper(TypeErasure.of(in.getSuper()));
		}
		if (in.getFirst() != null) {
			out.setFirst(TypeErasure.of(in.getFirst()));
		}
		return out;
	}

	private static IMemberHierarchy<IMethodName> ofMethods(IMemberHierarchy<IMethodName> in) {
		MethodHierarchy out = new MethodHierarchy();
		if (in.getElement() != null) {
			out.setElement(TypeErasure.of(in.getElement()));
		}
		if (in.getSuper() != null) {
			out.setSuper(TypeErasure.of(in.getSuper()));
		}
		if (in.getFirst() != null) {
			out.setFirst(TypeErasure.of(in.getFirst()));
		}
		return out;
	}

	private static IMemberHierarchy<IPropertyName> ofProperties(IMemberHierarchy<IPropertyName> in) {
		PropertyHierarchy out = new PropertyHierarchy();
		if (in.getElement() != null) {
			out.setElement(TypeErasure.of(in.getElement()));
		}
		if (in.getSuper() != null) {
			out.setSuper(TypeErasure.of(in.getSuper()));
		}
		if (in.getFirst() != null) {
			out.setFirst(TypeErasure.of(in.getFirst()));
		}
		return out;
	}

	private static ITypeHierarchy of(ITypeHierarchy in) {
		TypeHierarchy out = new TypeHierarchy();
		out.setElement(TypeErasure.of(in.getElement()));

		if (in.getExtends() != null) {
			out.setExtends(of(in.getExtends()));
		}

		for (ITypeHierarchy t : in.getImplements()) {
			out.getImplements().add(of(t));
		}

		return out;
	}

	public static ISST of(ISST in) {
		return (ISST) in.accept(new TypeErasureVisitor(), null);
	}
}