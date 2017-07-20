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
package cc.kave.commons.model.events.completionevents;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.gson.annotations.SerializedName;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.commons.utils.ToStringUtils;

public class Context {
	private ITypeShape typeShape;
	@SerializedName("SST")
	private ISST sst;

	public Context() {
		this.typeShape = new TypeShape();
		this.sst = new SST();
	}

	public ITypeShape getTypeShape() {
		return typeShape;
	}

	public void setTypeShape(ITypeShape typeShape) {
		this.typeShape = typeShape;
	}

	public ISST getSST() {
		return sst;
	}

	public void setSST(ISST sst) {
		this.sst = sst;
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