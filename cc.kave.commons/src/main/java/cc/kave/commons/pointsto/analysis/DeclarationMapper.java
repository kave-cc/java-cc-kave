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
package cc.kave.commons.pointsto.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedNameException;

public class DeclarationMapper {

	private Map<IMethodName, IMethodDeclaration> methods;
	private Map<IFieldName, IFieldDeclaration> fields;
	private Map<IPropertyName, IPropertyDeclaration> properties;
	private Map<IEventName, IEventDeclaration> events;

	public DeclarationMapper(Context context) {
		this(context.getSST());
	}

	public DeclarationMapper(ISST sst) {
		methods = new HashMap<>(sst.getMethods().size());
		for (IMethodDeclaration methodDecl : sst.getMethods()) {
			methods.put(methodDecl.getName(), methodDecl);
		}

		fields = new HashMap<>(sst.getFields().size());
		for (IFieldDeclaration fieldDecl : sst.getFields()) {
			fields.put(fieldDecl.getName(), fieldDecl);
		}

		properties = new HashMap<>(sst.getProperties().size());
		for (IPropertyDeclaration propertyDecl : sst.getProperties()) {
			properties.put(propertyDecl.getName(), propertyDecl);
		}

		events = new HashMap<>(sst.getEvents().size());
		for (IEventDeclaration eventDecl : sst.getEvents()) {
			events.put(eventDecl.getName(), eventDecl);
		}
	}

	public IMethodDeclaration get(IMethodName method) {
		return methods.get(method);
	}

	public IFieldDeclaration get(IFieldName field) {
		return fields.get(field);
	}

	public IPropertyDeclaration get(IPropertyName property) {
		return properties.get(property);
	}

	public IEventDeclaration get(IEventName event) {
		return events.get(event);
	}

	public IMemberDeclaration get(IMemberName member) {
		if (member instanceof IFieldName) {
			return get((IFieldName) member);
		} else if (member instanceof IPropertyName) {
			return get((IPropertyName) member);
		} else if (member instanceof IEventName) {
			return get((IEventName) member);
		}
		throw new UnexpectedNameException(member);
	}

	public Set<IMemberName> getAssignableMembers() {
		Set<IMemberName> members = new HashSet<>(fields.size() + properties.size() + events.size());
		members.addAll(fields.keySet());
		members.addAll(properties.keySet());
		members.addAll(events.keySet());
		return members;
	}
}