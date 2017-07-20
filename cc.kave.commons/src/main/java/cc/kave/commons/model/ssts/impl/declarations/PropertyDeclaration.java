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
package cc.kave.commons.model.ssts.impl.declarations;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.ToStringUtils;

public class PropertyDeclaration implements IPropertyDeclaration {

	private IPropertyName name;
	private List<IStatement> get;
	private List<IStatement> set;

	public PropertyDeclaration() {
		this.name = Names.getUnknownProperty();
		this.get = new ArrayList<>();
		this.set = new ArrayList<>();
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		return Lists.newArrayList();
	}

	@Override
	public IPropertyName getName() {
		return name;
	}

	@Override
	public List<IStatement> getGet() {
		return get;
	}

	@Override
	public List<IStatement> getSet() {
		return set;
	}

	public void setName(IPropertyName name) {
		this.name = name;
	}

	public void setGet(List<IStatement> get) {
		this.get = get;
	}

	public void setSet(List<IStatement> set) {
		this.set = set;
	}

	@Override
	public int hashCode() {
		int hashCode = this.get.hashCode();
		hashCode = (hashCode * 397) ^ this.set.hashCode();
		hashCode = (hashCode * 397) ^ this.name.hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyDeclaration other = (PropertyDeclaration) obj;
		if (get == null) {
			if (other.get != null)
				return false;
		} else if (!get.equals(other.get))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (set == null) {
			if (other.set != null)
				return false;
		} else if (!set.equals(other.set))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
}