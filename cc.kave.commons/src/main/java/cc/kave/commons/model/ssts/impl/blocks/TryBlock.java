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

import com.google.gson.annotations.SerializedName;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.ToStringUtils;

public class TryBlock implements ITryBlock {

	private List<IStatement> body;
	private List<ICatchBlock> catchBlocks;
	@SerializedName("Finally")
	private List<IStatement> _finally;

	public TryBlock() {
		this.body = new ArrayList<>();
		this.catchBlocks = new ArrayList<>();
		this._finally = new ArrayList<>();
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		List<ISSTNode> children = new ArrayList<>(body);
		children.addAll(_finally);
		return children;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	@Override
	public List<ICatchBlock> getCatchBlocks() {
		return this.catchBlocks;
	}

	@Override
	public List<IStatement> getFinally() {
		return this._finally;
	}

	public void setFinally(List<IStatement> _finally) {
		this._finally = _finally;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	public void setCatchBlocks(List<ICatchBlock> catchBlocks) {
		this.catchBlocks = catchBlocks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_finally == null) ? 0 : _finally.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((catchBlocks == null) ? 0 : catchBlocks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TryBlock))
			return false;
		TryBlock other = (TryBlock) obj;
		if (_finally == null) {
			if (other._finally != null)
				return false;
		} else if (!_finally.equals(other._finally))
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (catchBlocks == null) {
			if (other.catchBlocks != null)
				return false;
		} else if (!catchBlocks.equals(other.catchBlocks))
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