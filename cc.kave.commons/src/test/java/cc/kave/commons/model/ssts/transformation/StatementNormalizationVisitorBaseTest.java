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
package cc.kave.commons.model.ssts.transformation;

import java.util.List;

import org.junit.Before;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTUtil;

public abstract class StatementNormalizationVisitorBaseTest<TContext>
		extends NormalizationVisitorBaseTest<TContext, List<IStatement>> {
	protected IStatement stmt0, stmt1, stmt2, stmt3;

	@Before
	public void setup() {
		super.setup();

		stmt0 = dummyStatement(0);
		stmt1 = dummyStatement(1);
		stmt2 = dummyStatement(2);
		stmt3 = dummyStatement(3);
	}

	// ---------------------------- helpers -----------------------------------

	protected IStatement dummyStatement(int i) {
		return SSTUtil.declareVar("dummy" + i);
	}

}
