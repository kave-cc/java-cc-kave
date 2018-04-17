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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.Mockito;

import cc.kave.caret.analyses.IPathInsensitivePointToAnalysis;
import cc.kave.caret.analyses.PathInsensitivePointsToInfo;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;

public class UsageExtractionTestBase {

	protected UsageExtraction sut;
	protected PathInsensitivePointsToInfo p2info;

	@Before
	public void setupBase() {
		IPathInsensitivePointToAnalysis p2a = Mockito.mock(IPathInsensitivePointToAnalysis.class);
		p2info = new PathInsensitivePointsToInfo();
		Mockito.when(p2a.analyze(Matchers.any(Context.class))).thenReturn(p2info);
		sut = new UsageExtraction(p2a);
	}

	protected void setToSameAO(Object o1, Object... os) {
		Object ao = o1;
		p2info.set(o1, ao);
		for (Object o : os) {
			Assert.assertFalse(p2info.hasKey(o));
			p2info.set(o, ao);
		}
	}

	protected void setToDifferentAOs(Object... os) {
		for (Object o : os) {
			Assert.assertFalse(p2info.hasKey(o));
			p2info.set(o, o);
		}
	}

	protected static ITypeName t(int num) {
		return Names.newType("T%d, A, 1.2.3.4", num);
	}

	protected static ITypeName tLoc(int num) {
		return Names.newType("T%d, P", num);
	}

	protected static IMethodName m(int tNum, int num) {
		return Names.newMethod("[p:void] [%s].m%d()", t(tNum).getIdentifier(), num);
	}

	protected static IMethodName mLoc(int tNum, int num) {
		return Names.newMethod("[p:void] [%s].m%d()", tLoc(tNum).getIdentifier(), num);
	}

	protected static Context ctx(ISST sst) {
		Context ctx = new Context();
		ctx.setSST(sst);
		ctx.getTypeShape().setTypeHierarchy(new TypeHierarchy(sst.getEnclosingType().getIdentifier()));
		return ctx;
	}

	protected void assertInit(Context ctx, Object ao, ITypeName expectedType, IDefinition expectedDefSite) {
		Map<Object, List<IUsage>> map = sut.extractMap(ctx);
		Assert.assertTrue(map.containsKey(ao));
		List<IUsage> usages = map.get(ao);
		Assert.assertEquals(1, usages.size());
		IUsage actual = usages.get(0);
		assertEquals(expectedType, actual.getType());
		assertEquals(expectedDefSite, actual.getDefinition());
	}

	protected void assertUsages(Context ctx, IUsage... expecteds) {
		List<IUsage> actuals = sut.extract(ctx).stream()
				.filter(e -> e.getUsageSites().size() > 0 && !e.getType().isUnknown()).collect(Collectors.toList());
		Assert.assertEquals(asList(expecteds), actuals);
	}

	protected void assertUsage(Context ctx, IMethodName mCtx, Object ao, Usage expected) {
		Map<Object, List<IUsage>> map = sut.extractMap(ctx);
		Assert.assertTrue(map.containsKey(ao));
		List<IUsage> actuals = map.get(ao);
		for (IUsage actual : actuals) {
			if (mCtx.equals(actual.getMethodContext())) {
				Assert.assertEquals(expected, actual);
			}
		}
	}
}