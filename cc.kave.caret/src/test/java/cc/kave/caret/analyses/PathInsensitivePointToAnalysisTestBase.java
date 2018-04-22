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
package cc.kave.caret.analyses;

import static cc.kave.commons.model.naming.Names.newMethod;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class PathInsensitivePointToAnalysisTestBase {

	protected IPathInsensitivePointToAnalysis sut;
	protected PathInsensitivePointsToInfo expected;

	protected SST sst;
	protected MethodDeclaration md1;

	@Before
	public void setupBase() {
		md1 = new MethodDeclaration();
		md1.setName(newMethod("[p:void] [%s].m()", t(1).getIdentifier()));

		sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		resetAOs();
		addUniqueAOs(sst, md1);
	}

	protected void resetAOs() {
		expected = new PathInsensitivePointsToInfo();
	}

	protected void addAO(Object... keys) {
		Object ao = new Object();
		for (Object key : keys) {
			expected.set(key, ao);
		}
	}

	protected void addUniqueAOs(Object... keys) {
		for (Object key : keys) {
			expected.set(key, new Object());
		}
	}

	protected PathInsensitivePointsToInfo assertAOs() {
		if (sut == null) {
			throw new IllegalStateException("Cannot run test, sut has not been initialized yet!");
		}
		Context ctx = new Context();
		ctx.setSST(sst);
		PathInsensitivePointsToInfo actual = (PathInsensitivePointsToInfo) sut.analyze(ctx);

		Set<Object> eks = expected.getKeys();
		Set<Object> aks = actual.getKeys();
		if (!eks.equals(aks)) {

			StringBuilder sb = new StringBuilder("Registered keys do not match. Expected:\n###############\n");
			append(sb, eks, actual);
			sb.append("\n##### Instead, we found: #####\n");
			append(sb, aks, actual);

			throw new AssertionError(sb.toString());
		}
		assertEquals(eks, aks);

		Map<Object, Set<Object>> eaos = new IdentityHashMap<>();
		Map<Object, Set<Object>> aaos = new IdentityHashMap<>();

		for (Object key : expected.getKeys()) {
			Set<Object> keys;

			Object eao = expected.getAbstractObject(key);
			assertNotNull(eao);
			keys = eaos.getOrDefault(eao, Sets.newIdentityHashSet());
			keys.add(key);
			eaos.put(eao, keys);

			Object aao = actual.getAbstractObject(key);
			assertNotNull(aao);
			keys = aaos.getOrDefault(aao, Sets.newIdentityHashSet());
			keys.add(key);
			aaos.put(aao, keys);
		}

		Collection<Set<Object>> es = eaos.values();
		Collection<Set<Object>> as = aaos.values();
		for (Set<Object> keySet : es) {
			if (!contains(as, keySet)) {
				StringBuilder sb = new StringBuilder(
						"Expected keyset not found. Expected existance of (all entries point to actual AOs):\n###############\n");
				append(sb, keySet, actual);
				sb.append("\n##### Instead, we only found: #####\n");

				boolean isFirst = true;
				for (Set<Object> s : as) {
					if (!isFirst) {
						sb.append("\n----------------------------------\n");
					}
					isFirst = false;
					append(sb, s, actual);
				}

				sb.append("\n###############");
				throw new AssertionError(sb.toString());
			}
		}
		for (Set<Object> keySet : as) {
			if (!contains(es, keySet)) {
				StringBuilder sb = new StringBuilder("Superficial keyset found:\n###############\n");
				append(sb, keySet, actual);
				sb.append("\n##### Instead, we have expected: #####\n");

				boolean isFirst = true;
				for (Set<Object> s : es) {
					if (!isFirst) {
						sb.append("\n----------------------------------\n");
					}
					isFirst = false;
					append(sb, s, actual);
				}

				sb.append("\n###############");
				throw new AssertionError(sb.toString());
			}
		}

		return actual;
	}

	private void append(StringBuilder sb, Set<Object> set, PathInsensitivePointsToInfo p2info) {
		sb.append("[\n");
		for (Object o : set) {
			sb.append("  ").append(o.getClass().getSimpleName()).append('@').append(System.identityHashCode(o));
			if (o instanceof IParameterName || o instanceof IVariableReference) {
				sb.append(" (").append(o).append(")");
			}
			if (o instanceof IMemberDeclaration) {
				sb.append(" (");

				if (o instanceof IEventDeclaration) {
					sb.append(((IEventDeclaration) o).getName().getIdentifier());
				} else if (o instanceof IFieldDeclaration) {
					sb.append(((IFieldDeclaration) o).getName().getIdentifier());
				} else if (o instanceof IMethodDeclaration) {
					sb.append(((IMethodDeclaration) o).getName().getIdentifier());
				} else if (o instanceof IPropertyDeclaration) {
					sb.append(((IPropertyDeclaration) o).getName().getIdentifier());
				}

				sb.append(")");
			}
			sb.append(" ---> ");
			sb.append(p2info.getAbstractObject(o));
			sb.append(",\n");
		}
		sb.append("]");
	}

	private boolean contains(Collection<Set<Object>> as, Set<Object> e) {
		// for some reason, "as.contains(e)" always returns "false"
		for (Set<Object> a : as) {
			if (e.equals(a)) {
				return true;
			}
		}
		return false;
	}

	protected static ITypeName t(int i) {
		return Names.newType("T%d, P", i);
	}
}