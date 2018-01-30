/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.rsse.calls;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kace.rsse.calls.LambdaContextUtils;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.testing.ParameterData;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class LambdaContextUtilsTest {

	public static Object[][] provideLambdas() {
		ParameterData pd = new ParameterData();
		// id, isLambda(id), addLambda(id)
		pd.add("[?] [?].xxx(?)", false, "[?] [?].xxx$Lambda(?)");
		pd.add("[?] [?].xxx$Lambda(?)", true, "[?] [?].xxx$Lambda$Lambda(?)");
		return pd.toArray();
	}

	@Test
	@Parameters(method = "provideLambdas")
	public void isLambda(String idA, boolean isLambda, String idB) {
		IMethodName mA = Names.newMethod(idA);
		IMethodName mB = Names.newMethod(idB);

		Assert.assertEquals(isLambda, LambdaContextUtils.isLambdaName(mA));
		Assert.assertTrue(LambdaContextUtils.isLambdaName(mB));
	}

	@Test
	@Parameters(method = "provideLambdas")
	public void add(String idA, boolean isLambda, String idB) {
		IMethodName mA = Names.newMethod(idA);
		IMethodName mB = Names.newMethod(idB);
		Assert.assertEquals(mB, LambdaContextUtils.addLambda(mA));
	}

	@Test
	@Parameters(method = "provideLambdas")
	public void remove(String idA, boolean isLambda, String idB) {
		IMethodName mA = Names.newMethod(idA);
		IMethodName mB = Names.newMethod(idB);
		Assert.assertEquals(mA, LambdaContextUtils.removeLambda(mB));
	}

	@Test(expected = IllegalArgumentException.class)
	public void cannotRemoveLambdaFromRegularMethod() {
		LambdaContextUtils.removeLambda(Names.newMethod("[?] [?].m()"));
	}
}