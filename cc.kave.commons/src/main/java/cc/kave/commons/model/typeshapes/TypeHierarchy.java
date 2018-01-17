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

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.gson.annotations.SerializedName;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.ToStringUtils;

public class TypeHierarchy implements ITypeHierarchy {

	private ITypeName element;
	@SerializedName("Extends")
	private ITypeHierarchy _extends;
	@SerializedName("Implements")
	private LinkedHashSet<ITypeHierarchy> _implements;

	public TypeHierarchy() {
		this.element = Names.getUnknownType();
		this._implements = new LinkedHashSet<>();
	}

	public TypeHierarchy(String elementQualifiedName) {
		this.element = Names.newType(elementQualifiedName);
	}

	public void setExtends(ITypeHierarchy _extends) {
		this._extends = _extends;
	}

	public void setImplements(Set<ITypeHierarchy> th) {
		this._implements.clear();
		this._implements.addAll(th);
	}

	public void setElement(ITypeName element) {
		this.element = element;
	}

	@Override
	public ITypeName getElement() {
		return this.element;
	}

	@Override
	public ITypeHierarchy getExtends() {
		return this._extends;
	}

	@Override
	public Set<ITypeHierarchy> getImplements() {
		return this._implements;
	}

	@Override
	public boolean hasSupertypes() {
		return this.hasSuperclass() || this.isImplementingInterfaces();
	}

	@Override
	public boolean hasSuperclass() {
		return this._extends != null;
	}

	@Override
	public boolean isImplementingInterfaces() {
		return this._implements.size() > 0;
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