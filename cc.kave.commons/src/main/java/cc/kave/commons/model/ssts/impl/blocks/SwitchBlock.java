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
package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.ToStringUtils;

public class SwitchBlock implements ISwitchBlock {

	private IVariableReference reference;
	private List<ICaseBlock> sections;
	private List<IStatement> defaultSection;

	public SwitchBlock() {
		this.reference = new VariableReference();
		this.sections = new ArrayList<>();
		this.defaultSection = new ArrayList<>();
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		List<ISSTNode> children = Lists.newArrayList(reference);
		children.addAll(defaultSection);
		return children;
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public List<ICaseBlock> getSections() {
		return this.sections;
	}

	@Override
	public List<IStatement> getDefaultSection() {
		return this.defaultSection;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setSections(List<ICaseBlock> sections) {
		this.sections = sections;
	}

	public void setDefaultSection(List<IStatement> defaultSection) {
		this.defaultSection = defaultSection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defaultSection == null) ? 0 : defaultSection.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((sections == null) ? 0 : sections.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SwitchBlock))
			return false;
		SwitchBlock other = (SwitchBlock) obj;
		if (defaultSection == null) {
			if (other.defaultSection != null)
				return false;
		} else if (!defaultSection.equals(other.defaultSection))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (sections == null) {
			if (other.sections != null)
				return false;
		} else if (!sections.equals(other.sections))
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