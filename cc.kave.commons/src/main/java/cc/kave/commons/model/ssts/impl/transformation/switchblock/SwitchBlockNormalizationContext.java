/**
 * Copyright 2016 Carina Oberle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.transformation.switchblock;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;

public class SwitchBlockNormalizationContext {
	private IReferenceExpression fallthroughCondition;
	private List<IReferenceExpression> labelConditions;
	private boolean conditionalFallthrough;

	public SwitchBlockNormalizationContext() {
		this.fallthroughCondition = null;
		this.labelConditions = new ArrayList<IReferenceExpression>();
		this.conditionalFallthrough = false;
	}

	public SwitchBlockNormalizationContext(IReferenceExpression fallthroughCondition,
			List<IReferenceExpression> labelConditions, boolean conditionalFallthrough) {
		this.fallthroughCondition = fallthroughCondition;
		this.labelConditions = labelConditions;
		this.conditionalFallthrough = conditionalFallthrough;
	}

	public SwitchBlockNormalizationContext updated(IReferenceExpression fallthroughCondition) {
		return new SwitchBlockNormalizationContext(fallthroughCondition, labelConditions, conditionalFallthrough);
	}

	public IReferenceExpression getFallthroughCondition() {
		return fallthroughCondition;
	}

	public void setFallthroughCondition(IReferenceExpression fallthroughCondition) {
		this.fallthroughCondition = fallthroughCondition;
	}

	public List<IReferenceExpression> getLabelConditions() {
		return labelConditions;
	}

	public void addLabelCondition(IReferenceExpression cond) {
		labelConditions.add(cond);
	}

	public boolean getConditionalFallthrough() {
		return conditionalFallthrough;
	}

	public void updateConditionalFallthrough(boolean fallthrough, boolean conditionalFallthrough) {
		if (!fallthrough) {
			this.conditionalFallthrough = false;
		} else {
			this.conditionalFallthrough |= conditionalFallthrough;
		}
	}
}
