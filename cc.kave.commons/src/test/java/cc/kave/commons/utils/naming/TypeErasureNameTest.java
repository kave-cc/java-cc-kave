/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.utils.naming;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.testing.ParameterData;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class TypeErasureNameTest {

	@Test
	public void noGenerics() {
		String inp = "T,P";
		String out = "T,P";
		assertRepl(inp, out);
	}

	@Test
	public void generic_unbound() {
		String inp = "T`1[[G]],P";
		String out = "T`1[[G]],P";
		assertRepl(inp, out);
	}

	@Test
	public void generic_bound() {
		String inp = "T`1[[G -> T2,P]],P";
		String out = "T`1[[G]],P";
		assertRepl(inp, out);
	}

	@Test
	public void generic_multiple_unbound() {
		String inp = "T`2[[G],[G2]],P";
		String out = "T`2[[G],[G2]],P";
		assertRepl(inp, out);
	}

	@Test
	public void generic_multiple_bound() {
		String inp = "T`2[[G -> T2,P],[G2 -> T3,P]],P";
		String out = "T`2[[G],[G2]],P";
		assertRepl(inp, out);
	}

	@Test
	public void nestedGenercis() {
		String inp = "T`1[[G1 -> T`1[[G2-> T2]],P]],P";
		String out = "T`1[[G1]],P";
		assertRepl(inp, out);
	}

	@Test
	public void strangeNestingSyntax_unbound() {
		String inp = "T`1+U`1[[G1],[G2]],P";
		String out = "T`1+U`1[[G1],[G2]],P";
		assertRepl(inp, out);
	}

	@Test
	@Ignore
	public void strangeNestingSyntax_bound() {
		String inp = "T`1+U`1[[G1 -> T2,P],[G2 -> T3,P]],P";
		String out = "T`1+U`1[[G1],[G2]],P";
		assertRepl(inp, out);
	}

	@Test
	public void method_happyPjgfath() {
		String inp = "[T,P] [T,P].M`2[[G1],[G2 -> T,P]]()";
		String out = "[T,P] [T,P].M`2[[G1],[G2]]()";
		assertRepl(inp, out);
	}

	@Test
	public void repeatingTheSameReplacement() {
		String inp = "[T`1[[G->T,P]],P] [T`1[[G->T,P]],P].M()";
		String out = "[T`1[[G]],P] [T`1[[G]],P].M()";
		assertRepl(inp, out);
	}

	@Test
	public void type_happyPath() {
		ITypeName inp = Names.newType("T`1[[G->T2,P]],P");
		ITypeName exp = Names.newType("T`1[[G]],P");
		ITypeName act = TypeErasure.of(inp);
		assertEquals(exp, act);
	}

	@Test
	public void method_happyPath() {
		IMethodName inp = Names.newMethod("[T,P] [T,P].M`1[[G1 -> T,P]]()");
		IMethodName exp = Names.newMethod("[T,P] [T,P].M`1[[G1]]()");
		IMethodName act = TypeErasure.of(inp);
		assertEquals(exp, act);
	}

	public static Object[][] createTypesWithGenericArrays() {
		ParameterData pd = new ParameterData();

		pd.add("Demo.G1`1[][[T -> System.Tuple`2[[T1 -> p:int],[T2 -> p:bool]], mscorlib, 4.0.0.0]], Demo",
				"Demo.G1`1[][[T]], Demo");

		pd.add("Demo.G1`2[][[S],[T -> System.Tuple`2[[T1 -> p:int],[T2 -> p:bool]], mscorlib, 4.0.0.0]], Demo",
				"Demo.G1`2[][[S],[T]], Demo");

		pd.add("Demo.G1`2[][][[S],[T -> System.Tuple`2[[T1 -> p:int],[T2 -> p:bool]], mscorlib, 4.0.0.0]], Demo",
				"Demo.G1`2[][][[S],[T]], Demo");

		pd.add("Demo.G1`2[,][[S],[T -> System.Tuple`2[[T1 -> p:int],[T2 -> p:bool]], mscorlib, 4.0.0.0]], Demo",
				"Demo.G1`2[,][[S],[T]], Demo");

		pd.add("Demo.G1`2[][[S],[T -> System.Tuple`2[[T1],[T2 -> p:bool]], mscorlib, 4.0.0.0]], Demo",
				"Demo.G1`2[][[S],[T]], Demo");

		return pd.toArray();
	}

	@Test
	@Parameters(method = "createTypesWithGenericArrays")
	public void typeRemoveGenericsFromArray1(String boundId, String expected) {
		ITypeName inp = Names.newType(boundId);
		ITypeName exp = Names.newType(expected);
		ITypeName act = TypeErasure.of(inp);
		assertEquals(exp, act);
	}

	public static Object[][] createMethods() {
		ParameterData pd = new ParameterData();

		pd.add("[p:double] [i:Accord.Math.Distances.IDistance`2[[T -> System.Collections.BitArray, mscorlib, 4.0.0.0],[U -> System.Collections.BitArray, mscorlib, 4.0.0.0]], Accord.Math].Distance([T] x, [U] y)",
				"[p:double] [i:Accord.Math.Distances.IDistance`2[[T],[U]], Accord.Math].Distance([T] x, [U] y)");
		pd.add("[p:double] [i:Accord.Math.Distances.IDistance`2[[T -> System.Tuple`2[[T1 -> p:double],[T2 -> p:double]], mscorlib, 4.0.0.0],[U -> System.Tuple`2[[T1 -> p:double],[T2 -> p:double]], mscorlib, 4.0.0.0]], Accord.Math].Distance([T] x, [U] y)",
				"[p:double] [i:Accord.Math.Distances.IDistance`2[[T],[U]], Accord.Math].Distance([T] x, [U] y)");
		pd.add("[p:double] [i:Accord.Math.Distances.IDistance`2[[T],[U -> System.Tuple`2[[T1],[U1]], mscorlib, 4.0.0.0]], Accord.Math].Distance([T] x, [U] y)",
				"[p:double] [i:Accord.Math.Distances.IDistance`2[[T],[U]], Accord.Math].Distance([T] x, [U] y)");
		pd.add("[p:double] [Accord.Math.ADistance`2[[A -> T],[B -> System.Tuple`2[[T1 -> p:int],[T2 -> p:bool]], mscorlib, 4.0.0.0]], Demo].Distance([A] x, [B] y)",
				"[p:double] [Accord.Math.ADistance`2[[A],[B]], Demo].Distance([A] x, [B] y)");

		return pd.toArray();
	}

	@Test
	@Parameters(method = "createMethods")
	public void methodRemoveGenerics(String boundId, String expected) {
		IMethodName inp = Names.newMethod(boundId);
		IMethodName exp = Names.newMethod(expected);
		IMethodName act = TypeErasure.of(inp);
		assertEquals(exp, act);
	}

	public static Object[][] createFields() {
		ParameterData pd = new ParameterData();

		pd.add("[p:void] [T,P]._f", "[p:void] [T,P]._f");
		pd.add("[p:void] [T`1[[U]],P]._f", "[p:void] [T`1[[U]],P]._f");
		pd.add("[p:void] [T`1[[U -> p:double]],P]._f", "[p:void] [T`1[[U]],P]._f");

		return pd.toArray();
	}

	@Test
	@Parameters(method = "createFields")
	public void fieldRemoveGenerics(String boundId, String expected) {
		IFieldName inp = Names.newField(boundId);
		IFieldName exp = Names.newField(expected);
		IFieldName act = TypeErasure.of(inp);
		assertEquals(exp, act);
	}

	public static Object[][] createEvents() {
		ParameterData pd = new ParameterData();

		pd.add("[p:void] [T,P].E()", "[p:void] [T,P].E()");
		pd.add("[p:void] [T`1[[U]],P].E()", "[p:void] [T`1[[U]],P].E()");
		pd.add("[p:void] [T`1[[U -> p:double]],P].E()", "[p:void] [T`1[[U]],P].E()");

		return pd.toArray();
	}

	@Test
	@Parameters(method = "createEvents")
	public void eventRemoveGenerics(String boundId, String expected) {
		IEventName inp = Names.newEvent(boundId);
		IEventName exp = Names.newEvent(expected);
		IEventName act = TypeErasure.of(inp);
		assertEquals(exp, act);
	}

	public static Object[][] createProperties() {
		ParameterData pd = new ParameterData();

		pd.add("get set [p:void] [T,P].P()", "get set [p:void] [T,P].P()");
		pd.add("get set [p:void] [T`1[[U]],P].P()", "get set [p:void] [T`1[[U]],P].P()");
		pd.add("get set [p:void] [T`1[[U -> p:double]],P].P()", "get set [p:void] [T`1[[U]],P].P()");

		return pd.toArray();
	}

	@Test
	@Parameters(method = "createProperties")
	public void propertyRemoveGenerics(String boundId, String expected) {
		IPropertyName inp = Names.newProperty(boundId);
		IPropertyName exp = Names.newProperty(expected);
		IPropertyName act = TypeErasure.of(inp);
		assertEquals(exp, act);
	}

	public static Object[][] createParameters() {
		ParameterData pd = new ParameterData();

		pd.add("[T,P] p", "[T,P] p");
		pd.add("[T`1[[U]],P] p", "[T`1[[U]],P] p");
		pd.add("[T`1[[U -> p:double]],P] p", "[T`1[[U]],P] p");

		return pd.toArray();
	}

	@Test
	@Parameters(method = "createParameters")
	public void parameterRemoveGenerics(String boundId, String expected) {
		IParameterName inp = Names.newParameter(boundId);
		IParameterName exp = Names.newParameter(expected);
		IParameterName act = TypeErasure.of(inp);
		assertEquals(exp, act);
	}

	public static Object[][] createLambdas() {
		ParameterData pd = new ParameterData();

		pd.add("[p:void] [T,P].()", "[p:void] [T,P].()");
		pd.add("[p:void] [T`1[[U]],P].()", "[p:void] [T`1[[U]],P].()");
		pd.add("[p:void] [T`1[[U -> p:double]],P].()", "[p:void] [T`1[[U]],P].()");

		return pd.toArray();
	}

	@Test
	@Parameters(method = "createLambdas")
	public void lambdaRemoveGenerics(String boundId, String expected) {
		ILambdaName inp = Names.newLambda(boundId);
		ILambdaName exp = Names.newLambda(expected);
		ILambdaName act = TypeErasure.of(inp);
		assertEquals(exp, act);
	}

	private void assertRepl(String in, String expected) {
		String actual = TypeErasure.of(in);
		assertEquals(expected, actual);
	}
}