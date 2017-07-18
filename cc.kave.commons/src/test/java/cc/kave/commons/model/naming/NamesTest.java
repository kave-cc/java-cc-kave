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
package cc.kave.commons.model.naming;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.naming.impl.v0.GeneralName;
import cc.kave.commons.model.naming.impl.v0.codeelements.AliasName;
import cc.kave.commons.model.naming.impl.v0.codeelements.EventName;
import cc.kave.commons.model.naming.impl.v0.codeelements.FieldName;
import cc.kave.commons.model.naming.impl.v0.codeelements.LambdaName;
import cc.kave.commons.model.naming.impl.v0.codeelements.LocalVariableName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.naming.impl.v0.codeelements.ParameterName;
import cc.kave.commons.model.naming.impl.v0.codeelements.PropertyName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.CommandBarControlName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.CommandName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.DocumentName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.ProjectItemName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.ProjectName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.SolutionName;
import cc.kave.commons.model.naming.impl.v0.idecomponents.WindowName;
import cc.kave.commons.model.naming.impl.v0.others.ReSharperLiveTemplateName;
import cc.kave.commons.model.naming.impl.v0.types.ArrayTypeName;
import cc.kave.commons.model.naming.impl.v0.types.DelegateTypeName;
import cc.kave.commons.model.naming.impl.v0.types.PredefinedTypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeParameterName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyVersion;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;

public class NamesTest {

	@Test
	public void CorrectInitializationAndNonNull() {
		AssertInit(Names.newGeneral("x"), GeneralName.class);

		AssertInit(Names.newAlias("x"), AliasName.class);
		AssertInit(Names.newEvent("x"), EventName.class);
		AssertInit(Names.newField("x"), FieldName.class);
		AssertInit(Names.newLambda("x"), LambdaName.class);
		AssertInit(Names.newLocalVariable("x"), LocalVariableName.class);
		AssertInit(Names.newMethod("x"), MethodName.class);
		AssertInit(Names.newParameter("[?] p"), ParameterName.class);
		AssertInit(Names.newProperty("get [?] [?].P()"), PropertyName.class);

		AssertInit(Names.newCommandBarControl("x"), CommandBarControlName.class);
		AssertInit(Names.newCommand("x"), CommandName.class);
		AssertInit(Names.newDocument("x y"), DocumentName.class);
		AssertInit(Names.newProjectItem("x y"), ProjectItemName.class);
		AssertInit(Names.newProject("x y"), ProjectName.class);
		AssertInit(Names.newSolution("x"), SolutionName.class);
		AssertInit(Names.newWindow("x y"), WindowName.class);

		AssertInit(Names.newReSharperLiveTemplate("x:y"), ReSharperLiveTemplateName.class);

		AssertInit(Names.newAssembly("x"), AssemblyName.class);
		AssertInit(Names.newAssemblyVersion("1.2.3.4"), AssemblyVersion.class);
		AssertInit(Names.newNamespace("x"), NamespaceName.class);

		AssertInit(Names.newType("T,P"), TypeName.class);
		AssertInit(Names.newType("T"), TypeParameterName.class);
		AssertInit(Names.newType("T[],P"), ArrayTypeName.class);
		AssertInit(Names.newType("d:[?] [?].()"), DelegateTypeName.class);
		AssertInit(Names.newType("p:int"), PredefinedTypeName.class);
	}

	@Test
	public void Unknowns() {
		AssertUnknown(Names.getUnknownGeneral(), new GeneralName());

		AssertUnknown(Names.getUnknownAlias(), new AliasName());
		AssertUnknown(Names.getUnknownEvent(), new EventName());
		AssertUnknown(Names.getUnknownField(), new FieldName());
		AssertUnknown(Names.getUnknownLambda(), new LambdaName());
		AssertUnknown(Names.getUnknownLocalVariable(), new LocalVariableName());
		AssertUnknown(Names.getUnknownMethod(), new MethodName());
		AssertUnknown(Names.getUnknownParameter(), new ParameterName());
		AssertUnknown(Names.getUnknownProperty(), new PropertyName());

		AssertUnknown(Names.getUnknownCommandBarControl(), new CommandBarControlName());
		AssertUnknown(Names.getUnknownCommand(), new CommandName());
		AssertUnknown(Names.getUnknownDocument(), new DocumentName());
		AssertUnknown(Names.getUnknownProjectItem(), new ProjectItemName());
		AssertUnknown(Names.getUnknownProject(), new ProjectName());
		AssertUnknown(Names.getUnknownSolution(), new SolutionName());
		AssertUnknown(Names.getUnknownWindow(), new WindowName());

		AssertUnknown(Names.getUnknownReSharperLiveTemplate(), new ReSharperLiveTemplateName());

		AssertUnknown(Names.getUnknownAssembly(), new AssemblyName());
		AssertUnknown(Names.getUnknownAssemblyVersion(), new AssemblyVersion());
		AssertUnknown(Names.getUnknownNamespace(), new NamespaceName());

		AssertUnknown(Names.getUnknownType(), new TypeName());
		AssertUnknown(Names.getUnknownDelegateType(), new DelegateTypeName());
	}

	@Test
	public void CanDeriveArrays() {
		IName actual = Names.newArrayType(2, new TypeName("T, P"));
		IName expected = new ArrayTypeName("T[,], P");
		assertEquals(expected, actual);
	}

	@Test
	public void CanCreateTypeParameter() {
		IName actual = Names.newTypeParameter("T", "U, P");
		IName expected = new TypeParameterName("T -> U, P");
		assertEquals(expected, actual);
	}

	@Test
	public void CanCreateTypeParameter_unbound() {
		IName actual = Names.newTypeParameter("T");
		IName expected = new TypeParameterName("T");
		assertEquals(expected, actual);
	}

	@Test
	public void DoesNotBreakForRegularStringsThatDoNotNeedToBeReplaced_General() {
		IName actual = Names.newGeneral("CombinedLookupItem:public override ToString() { ... }");
		IName expected = new GeneralName("CombinedLookupItem:public override ToString() { ... }");
		assertEquals(expected, actual);
	}

	@Test
	public void DoesNotBreakForRegularStringsThatDoNotNeedToBeReplaced_Command() {
		IName actual = Names.newCommand("{E272D1B...}:42:SomeId");
		IName expected = new CommandName("{E272D1B...}:42:SomeId");
		assertEquals(expected, actual);
	}

	private static void AssertUnknown(IName actual, IName expected) {
		assertTrue(actual.isUnknown());
		assertEquals(expected, actual);
	}

	private static void AssertInit(IName name, Class<?> expectedType) {
		assertNotNull(name);
		assertTrue(expectedType.isAssignableFrom(name.getClass()));
	}
}