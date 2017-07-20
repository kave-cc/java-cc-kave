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
package cc.kave.commons.utils.ssts.sstprintingvisitortestsuite;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.commons.utils.ssts.SSTPrintingContext;

public class DeclarationPrinterTest extends SSTPrintingVisitorBaseTest {
	@Test
	public void SSTDeclaration_EmptyClass() {
		SST sst = new SST();
		sst.setEnclosingType(Names.newType("TestClass,TestProject"));

		assertPrint(sst, "class TestClass", "{", "}");
	}

	@Test
	public void SSTDeclaration_WithSupertypes() {
		ITypeName thisType = Names.newType("TestClass,P");
		ITypeName superType = Names.newType("SuperClass,P");
		ITypeName interface1 = Names.newType("i:IDoesSomething,P");
		ITypeName interface2 = Names.newType("i:IDoesSomethingElse,P");

		SST sst = new SST();
		sst.setEnclosingType(thisType);
		TypeShape typeShape = new TypeShape();
		TypeHierarchy type = new TypeHierarchy();
		TypeHierarchy type2 = new TypeHierarchy();
		TypeHierarchy interface1Hierarchy = new TypeHierarchy();
		TypeHierarchy interface2Hierarchy = new TypeHierarchy();
		interface1Hierarchy.setElement(interface1);
		interface2Hierarchy.setElement(interface2);
		type2.setElement(superType);
		type.setElement(thisType);
		type.setExtends(type2);
		type.getImplements().add(interface1Hierarchy);
		type.getImplements().add(interface2Hierarchy);
		typeShape.setTypeHierarchy(type);

		SSTPrintingContext context = new SSTPrintingContext();
		context.setTypeShape(typeShape);
		assertPrintWithCustomContext(sst, context, "class TestClass : SuperClass, IDoesSomething, IDoesSomethingElse",
				"{", "}");
	}

	@Test
	public void SSTDeclaration_WithSupertypes_OnlyInterface() {
		ITypeName thisType = Names.newType("TestClass,P");
		ITypeName interface1 = Names.newType("i:IDoesSomething,P");

		SST sst = new SST();
		sst.setEnclosingType(thisType);
		TypeShape typeShape = new TypeShape();
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		TypeHierarchy type2 = new TypeHierarchy();
		type2.setElement(interface1);
		typeHierarchy.setElement(thisType);
		typeHierarchy.getImplements().add(type2);
		typeShape.setTypeHierarchy(typeHierarchy);

		SSTPrintingContext context = new SSTPrintingContext();
		context.setTypeShape(typeShape);
		assertPrintWithCustomContext(sst, context, "class TestClass : IDoesSomething", "{", "}");
	}

	@Test
	public void SSTDeclaration_WithSupertypes_OnlySuperclass() {
		ITypeName thisType = Names.newType("TestClass,P");
		ITypeName superType = Names.newType("SuperClass,P");

		SST sst = new SST();
		sst.setEnclosingType(thisType);
		TypeShape typeShape = new TypeShape();
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(thisType);

		TypeHierarchy type2 = new TypeHierarchy();
		type2.setElement(superType);
		typeHierarchy.setExtends(type2);
		typeShape.setTypeHierarchy(typeHierarchy);
		SSTPrintingContext context = new SSTPrintingContext();
		context.setTypeShape(typeShape);
		assertPrintWithCustomContext(sst, context, "class TestClass : SuperClass", "{", "}");
	}

	@Test
	public void SSTDeclaration_FullClass() {
		SST sst = new SST();
		sst.setEnclosingType(Names.newType("TestClass,P"));
		DelegateDeclaration delegate = new DelegateDeclaration();
		delegate.setName(Names.newType("d:[T,P][TestDelegate,P].()").asDelegateTypeName());
		sst.getDelegates().add(delegate);
		EventDeclaration event = new EventDeclaration();
		event.setName(Names.newEvent("[EventType,P] [TestClass,P].SomethingHappened"));
		sst.getEvents().add(event);
		FieldDeclaration field1 = new FieldDeclaration();
		FieldDeclaration field2 = new FieldDeclaration();
		field1.setName(Names.newField("[FieldType,P] [TestClass,P].SomeField"));
		field2.setName(Names.newField("[FieldType,P] [TestClass,P].AnotherField"));
		sst.getFields().add(field1);
		sst.getFields().add(field2);
		PropertyDeclaration property = new PropertyDeclaration();
		property.setName(Names.newProperty("get set [PropertyType,P] [TestClass,P].SomeProperty()"));
		sst.getProperties().add(property);
		MethodDeclaration method1 = new MethodDeclaration();
		MethodDeclaration method2 = new MethodDeclaration();
		method1.setName(Names.newMethod("[ReturnType,P] [TestClass,P].M([ParameterType,P] p)"));
		method2.setName(Names.newMethod("[ReturnType,P] [TestClass,P].M2()"));
		method2.getBody().add(new BreakStatement());
		sst.getMethods().add(method1);
		sst.getMethods().add(method2);

		assertPrint(sst, "class TestClass", "{", "    delegate TestDelegate();", "",
				"    event EventType SomethingHappened;", "", "    FieldType SomeField;", "    FieldType AnotherField;",
				"", "    PropertyType SomeProperty { get; set; }", "", "    ReturnType M(ParameterType p) { }", "",
				"    ReturnType M2()", "    {", "        break;", "    }", "}");
	}

	@Test
	public void SSTDeclaration_Interface() {
		SST sst = new SST();
		sst.setEnclosingType(Names.newType("i:SomeInterface,P"));

		assertPrint(sst, "interface SomeInterface", "{", "}");
	}

	@Test
	public void SSTDeclaration_Struct() {
		SST sst = new SST();
		sst.setEnclosingType(Names.newType("s:SomeStruct,P"));

		assertPrint(sst, "struct SomeStruct", "{", "}");
	}

	@Test
	public void SSTDeclaration_Enum() {
		SST sst = new SST();
		sst.setEnclosingType(Names.newType("e:SomeEnum,P"));

		assertPrint(sst, "enum SomeEnum", "{", "}");
	}

	@Test
	public void DelegateDeclaration_Parameterless() {
		DelegateDeclaration sst = new DelegateDeclaration();
		sst.setName(Names.newType("d:[R, P] [Some.DelegateType, P].()").asDelegateTypeName());

		assertPrint(sst, "delegate DelegateType();");
	}

	@Test
	public void DelegateDeclaration_WithParameters() {
		DelegateDeclaration sst = new DelegateDeclaration();
		sst.setName(Names.newType("d:[R, P] [Some.DelegateType, P].([C, P] p1, [D, P] p2)").asDelegateTypeName());

		assertPrint(sst, "delegate DelegateType(C p1, D p2);");
	}

	@Test
	public void DelegateDeclaration_Generic() {
		DelegateDeclaration sst = new DelegateDeclaration();
		sst.setName(Names.newType("d:[R, P] [Some.DelegateType`1[[T -> T]], P].([T] p1)").asDelegateTypeName());

		assertPrint(sst, "delegate DelegateType<T>(T p1);");
	}

	@Test
	public void EventDeclaration() {
		EventDeclaration sst = new EventDeclaration();
		sst.setName(Names.newEvent("[EventType,P] [DeclaringType,P].E"));

		assertPrint(sst, "event EventType E;");
	}

	@Test
	public void EventDeclaration_GenericEventArgsType() {
		EventDeclaration sst = new EventDeclaration();
		sst.setName(Names.newEvent("[EventType`1[[T -> EventArgsType,P]],P] [DeclaringType,P].E"));

		assertPrint(sst, "event EventType<EventArgsType> E;");
	}

	@Test
	public void FieldDeclaration() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(Names.newField("[FieldType,P] [DeclaringType,P].F"));

		assertPrint(sst, "FieldType F;");
	}

	@Test
	public void FieldDeclaration_Static() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(Names.newField("static [FieldType,P] [DeclaringType,P].F"));

		assertPrint(sst, "static FieldType F;");
	}

	@Test
	public void FieldDeclaration_Array() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(Names.newField("[d:[V, A] [N.TD, A].()[]] [DT, A]._delegatesField"));

		assertPrint(sst, "TD[] _delegatesField;");
	}

	@Test
	public void PropertyDeclaration_GetterOnly() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(Names.newProperty("get [PropertyType,P] [DeclaringType,P].P()"));

		assertPrint(sst, "PropertyType P { get; }");
	}

	@Test
	public void PropertyDeclaration_SetterOnly() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(Names.newProperty("set [PropertyType,P] [DeclaringType,P].P()"));

		assertPrint(sst, "PropertyType P { set; }");
	}

	@Test
	public void PropertyDeclaration() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(Names.newProperty("get set [PropertyType,P] [DeclaringType,P].P()"));

		assertPrint(sst, "PropertyType P { get; set; }");
	}

	@Test
	public void PropertyDeclaration_WithBodies() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(Names.newProperty("get set [PropertyType,P] [DeclaringType,P].P()"));
		sst.getGet().add(new ContinueStatement());
		sst.getGet().add(new BreakStatement());
		sst.getSet().add(new BreakStatement());
		sst.getSet().add(new ContinueStatement());

		assertPrint(sst, "PropertyType P", "{", "    get", "    {", "        continue;", "        break;", "    }",
				"    set", "    {", "        break;", "        continue;", "    }", "}");
	}

	@Test
	public void PropertyDeclaration_WithOnlyGetterBody() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(Names.newProperty("get set [PropertyType,P] [DeclaringType,P].P()"));
		sst.getGet().add(new BreakStatement());

		assertPrint(sst, "PropertyType P", "{", "    get", "    {", "        break;", "    }", "    set;", "}");
	}

	@Test
	public void PropertyDeclaration_WithOnlySetterBody() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(Names.newProperty("get set [PropertyType,P] [DeclaringType,P].P()"));
		sst.getSet().add(new BreakStatement());

		assertPrint(sst, "PropertyType P", "{", "    get;", "    set", "    {", "        break;", "    }", "}");
	}

	@Test
	public void MethodDeclaration_EmptyMethod() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(Names.newMethod("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));

		assertPrint(sst, "ReturnType M(ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_Static() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(Names.newMethod("static [ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));

		assertPrint(sst, "static ReturnType M(ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_PassedByReference() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(Names.newMethod("[ReturnType,P] [DeclaringType,P].M(ref [p:int] p)"));

		assertPrint(sst, "ReturnType M(ref int p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_Output() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(Names.newMethod("[ReturnType,P] [DeclaringType,P].M(out [ParameterType,P] p)"));

		assertPrint(sst, "ReturnType M(out ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_Params() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(Names.newMethod("[ReturnType,P] [DeclaringType,P].M(params [ParameterType[],P] p)"));

		assertPrint(sst, "ReturnType M(params ParameterType[] p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_Optional() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(Names.newMethod("[ReturnType,P] [DeclaringType,P].M(opt [ParameterType,P] p)"));

		assertPrint(sst, "ReturnType M(opt ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_WithBody() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(Names.newMethod("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "ReturnType M(ParameterType p)", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void MethodDeclaration_Generic() {

		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(Names.newMethod("[ReturnType, P] [DeclaringType, P].M`1[[T -> T]]([T] p)"));

		assertPrint(sst, "ReturnType M<T>(T p) { }");
	}

	@Test
	public void VariableDeclaration() {
		IVariableDeclaration sst = SSTUtil.declare("var", Names.newType("T,P"));

		assertPrint(sst, "T var;");
	}
}