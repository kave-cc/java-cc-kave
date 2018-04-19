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
package cc.kave.rsse.calls.analysis;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class UsageExtractionContextsTest extends UsageExtractionTestBase {

	@Test
	public void cCtx() {
		// context, not invocation!!
		Assert.fail();
	}

	@Test
	public void mCtx_Method() {
		// context, not invocation!!
		Assert.fail();
	}

	@Test
	public void mCtx_PropertyGet() {
		// context, not invocation!!
		Assert.fail();
	}

	@Test
	public void mCtx_PropertySet() {
		// context, not invocation!!
		Assert.fail();
	}

	@Ignore
	@Test
	public void mCtx_Lambda() {
		// context, not invocation!!
		Assert.fail();
	}

	@Ignore
	@Test
	public void mCtx_LambdaInMethod() {
		// context, not invocation!!
		Assert.fail();
	}

	@Ignore
	@Test
	public void mCtx_LambdaInProperty() {
		// context, not invocation!!
		Assert.fail();
	}

	@Ignore
	@Test
	public void mCtx_LambdaInLambda() {
		// context, not invocation!!
		Assert.fail();
	}

	@Test
	public void sameAoInDiffContextsIsDiffUsage() {
		Assert.fail();
		// mbody, get, set, lambda1, lambda2
	}

	@Test
	public void rebaseOverriddenMCtx_method() {
		// context, not invocation!!
		Assert.fail();
	}

	@Test
	public void rebaseOverriddenMCtx_propertyGet() {
		// context, not invocation!!
		Assert.fail();
	}

	@Test
	public void rebaseOverriddenMCtx_propertySet() {
		// context, not invocation!!
		Assert.fail();
	}

	@Ignore
	@Test
	public void rebaseOverriddenMCtx_lambdaInMethod() {
		// context, not invocation!!
		Assert.fail();
	}

	@Ignore
	@Test
	public void rebaseOverriddenMCtx_lambdaInProperty() {
		// context, not invocation!!
		Assert.fail();
	}

	@Ignore
	@Test
	public void rebaseOverriddenMCtx_lambdaInLambda() {
		// context, not invocation!!
		Assert.fail();
	}
}