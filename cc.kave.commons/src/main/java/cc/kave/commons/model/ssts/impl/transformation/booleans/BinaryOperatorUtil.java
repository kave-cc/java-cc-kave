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
package cc.kave.commons.model.ssts.impl.transformation.booleans;

import com.google.common.collect.ImmutableBiMap;

import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;

public class BinaryOperatorUtil {
	//@formatter:off
	private static final ImmutableBiMap<BinaryOperator, BinaryOperator> opNegation = ImmutableBiMap.of(
			BinaryOperator.And, BinaryOperator.Or, 
			BinaryOperator.Equal, BinaryOperator.NotEqual, 
			BinaryOperator.LessThan, BinaryOperator.GreaterThanOrEqual, 
			BinaryOperator.GreaterThan, BinaryOperator.LessThanOrEqual);
	//@formatter:on

	public static BinaryOperator getNegated(BinaryOperator op) {
		return opNegation.getOrDefault(op, opNegation.inverse().get(op));
	}

	public static boolean isLogical(BinaryOperator op) {
		return op == BinaryOperator.And || op == BinaryOperator.Or;
	}

	public static boolean isRelational(BinaryOperator op) {
		boolean isRelational = false;
		switch (op) {
		case Equal:
		case NotEqual:
		case LessThan:
		case GreaterThan:
		case LessThanOrEqual:
		case GreaterThanOrEqual:
			isRelational = true;
		default:
			break;
		}
		return isRelational;
	}
}
