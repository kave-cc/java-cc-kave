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
package cc.kave.commons.model.ssts.impl.visitor;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.commons.utils.naming.TypeErasure;

public class TypeErasureVisitor extends AbstractThrowingNodeVisitor<Object, Object> {

	public static Context erase(Context in) {
		Context out = new Context();
		out.setTypeShape(erase(in.getTypeShape()));
		out.setSST(erase(in.getSST()));
		return out;
	}

	public static ITypeShape erase(ITypeShape in) {
		TypeShape out = new TypeShape();
		out.setTypeHierarchy(erase(in.getTypeHierarchy()));
		for (IMemberHierarchy<IMethodName> mh : in.getMethodHierarchies()) {
			out.getMethodHierarchies().add(erase(mh));
		}
		return out;
	}

	public static IMemberHierarchy<IMethodName> erase(IMemberHierarchy<IMethodName> in) {
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

	public static ITypeHierarchy erase(ITypeHierarchy in) {
		TypeHierarchy out = new TypeHierarchy();
		out.setElement(TypeErasure.of(in.getElement()));

		if (in.getExtends() != null) {
			out.setExtends(erase(in.getExtends()));
		}

		for (ITypeHierarchy t : in.getImplements()) {
			out.getImplements().add(erase(t));
		}

		return out;
	}

	public static ISST erase(ISST in) {
		TypeErasureVisitor visitor = new TypeErasureVisitor();
		return (ISST) in.accept(visitor, null);
	}

	@Override
	public Object visit(ISST in, Object ctx) {
		SST out = new SST();
		out.setEnclosingType(TypeErasure.of(in.getEnclosingType()));
		out.setDelegates(in.getDelegates());
		out.setEvents(in.getEvents());
		out.setFields(in.getFields());
		for (IMethodDeclaration md : in.getMethods()) {
			out.getMethods().add((IMethodDeclaration) visit(md, null));
		}
		out.setProperties(in.getProperties());
		return out;
	}

	@Override
	public Object visit(IMethodDeclaration in, Object context) {
		MethodDeclaration out = new MethodDeclaration();
		out.setName(TypeErasure.of(in.getName()));
		out.setEntryPoint(in.isEntryPoint());
		out.setBody(in.getBody());
		return out;
	}
}