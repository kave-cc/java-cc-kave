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
package cc.kave.commons.model.typeshapes;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.ToStringUtils;

public class TypeShape implements ITypeShape {

	private ITypeHierarchy typeHierarchy;
	private Set<ITypeName> nestedTypes;
	private Set<IDelegateTypeName> delegates;
	private Set<IMemberHierarchy<IEventName>> eventHierarchies;
	private Set<IFieldName> fields;
	private Set<IMemberHierarchy<IMethodName>> methodHierarchies;
	private Set<IMemberHierarchy<IPropertyName>> propertyHierarchies;

	public TypeShape() {
		this.typeHierarchy = new TypeHierarchy();
		this.nestedTypes = new HashSet<>();
		this.delegates = new HashSet<>();
		this.fields = new HashSet<>();
		this.eventHierarchies = new HashSet<>();
		this.methodHierarchies = new HashSet<>();
		this.propertyHierarchies = new HashSet<>();
	}

	@Override
	public ITypeHierarchy getTypeHierarchy() {
		return this.typeHierarchy;
	}

	@Override
	public void setTypeHierarchy(ITypeHierarchy typeHierarchy) {
		this.typeHierarchy = typeHierarchy;
	}

	@Override
	public Set<IMemberHierarchy<IEventName>> getEventHierarchies() {
		return this.eventHierarchies;
	}

	@Override
	public void setEventHierarchies(Set<IMemberHierarchy<IEventName>> eventHierarchies) {
		this.eventHierarchies = eventHierarchies;
	}

	@Override
	public Set<IMemberHierarchy<IMethodName>> getMethodHierarchies() {
		return this.methodHierarchies;
	}

	@Override
	public void setMethodHierarchies(Set<IMemberHierarchy<IMethodName>> methodHierarchies) {
		this.methodHierarchies = methodHierarchies;
	}

	@Override
	public Set<IMemberHierarchy<IPropertyName>> getPropertyHierarchies() {
		return this.propertyHierarchies;
	}

	@Override
	public void setPropertyHierarchies(Set<IMemberHierarchy<IPropertyName>> propertyHierarchies) {
		this.propertyHierarchies = propertyHierarchies;
	}

	@Override
	public Set<ITypeName> getNestedTypes() {
		return nestedTypes;
	}

	@Override
	public void setNestedTypes(Set<ITypeName> nestedTypes) {
		this.nestedTypes = nestedTypes;
	}

	@Override
	public Set<IDelegateTypeName> getDelegates() {
		return delegates;
	}

	@Override
	public void setDelegates(Set<IDelegateTypeName> delegates) {
		this.delegates = delegates;
	}

	@Override
	public Set<IFieldName> getFields() {
		return fields;
	}

	@Override
	public void setFields(Set<IFieldName> fields) {
		this.fields = fields;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
}