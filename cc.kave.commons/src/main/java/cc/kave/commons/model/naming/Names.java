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

import static cc.kave.commons.utils.StringUtils.f;

import cc.kave.commons.model.naming.codeelements.IAliasName;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.ILocalVariableName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
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
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeParameterName;
import cc.kave.commons.model.naming.impl.v0.types.TypeUtils;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyVersion;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.model.naming.others.IReSharperLiveTemplateName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.IAssemblyVersion;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class Names {

	public static IParameterName getUnknownParameter() {
		return new ParameterName();
	}

	public static IDelegateTypeName getUnknownDelegateType() {
		return new DelegateTypeName();
	}

	public static IEventName getUnknownEvent() {
		return new EventName();
	}

	public static IFieldName getUnknownField() {
		return new FieldName();
	}

	public static IMethodName getUnknownMethod() {
		return new MethodName();
	}

	public static IPropertyName getUnknownProperty() {
		return new PropertyName();
	}

	public static ITypeName getUnknownType() {
		return new TypeName();
	}

	public static ILambdaName getUnknownLambda() {
		return new LambdaName();
	}

	public static ITypeName newType(String id, Object... args) {
		return TypeUtils.createTypeName(f(id, args));
	}

	public static IFieldName newField(String id) {
		return new FieldName(id);
	}

	public static IPropertyName newProperty(String id) {
		return new PropertyName(id);
	}

	public static IMethodName newMethod(String id) {
		return new MethodName(id);
	}

	public static IParameterName newParameter(String id) {
		return new ParameterName(id);
	}

	public static INamespaceName getUnknownNamespace() {
		return new NamespaceName();
	}

	public static IAliasName newAlias(String id) {
		return new AliasName(id);
	}

	public static IAssemblyName newAssembly(String id) {
		return new AssemblyName(id);
	}

	public static IEventName newEvent(String id) {
		return new EventName(id);
	}

	public static ILambdaName newLambda(String id) {
		return new LambdaName(id);
	}

	public static ILocalVariableName newLocalVariable(String id) {
		return new LocalVariableName(id);
	}

	public static IName newGeneral(String id) {
		return new GeneralName(id);
	}

	public static INamespaceName newNamespace(String id) {
		return new NamespaceName(id);
	}

	public static IReSharperLiveTemplateName newLiveTemplateName(String id) {
		return new ReSharperLiveTemplateName(id);
	}

	public static IAssemblyVersion newAssemblyVersion(String id) {
		return new AssemblyVersion(id);
	}

	public static ILocalVariableName getUnknownLocalVariable() {
		return new LocalVariableName();
	}

	public static IName newCommand(String id) {
		return new CommandName(id);
	}

	public static IName newCommandBarControl(String id) {
		return new CommandBarControlName(id);
	}

	public static IName newDocument(String id) {
		return new DocumentName(id);
	}

	public static IName newProjectItem(String id) {
		return new ProjectItemName(id);
	}

	public static IName newProject(String id) {
		return new ProjectName(id);
	}

	public static IName newSolution(String id) {
		return new SolutionName(id);
	}

	public static IName newWindow(String id) {
		return new WindowName(id);
	}

	public static IName newReSharperLiveTemplate(String id) {
		return new ReSharperLiveTemplateName(id);
	}

	public static IName getUnknownGeneral() {
		return new GeneralName();
	}

	public static IName getUnknownAlias() {
		return new AliasName();
	}

	public static IName getUnknownCommandBarControl() {
		return new CommandBarControlName();
	}

	public static IName getUnknownCommand() {
		return new CommandName();
	}

	public static IName getUnknownDocument() {
		return new DocumentName();
	}

	public static IName getUnknownProjectItem() {
		return new ProjectItemName();
	}

	public static IName getUnknownProject() {
		return new ProjectName();
	}

	public static IName getUnknownSolution() {
		return new SolutionName();
	}

	public static IName getUnknownWindow() {
		return new WindowName();
	}

	public static IName getUnknownReSharperLiveTemplate() {
		return new ReSharperLiveTemplateName();
	}

	public static IName getUnknownAssembly() {
		return new AssemblyName();
	}

	public static IName getUnknownAssemblyVersion() {
		return new AssemblyVersion();
	}

	public static IName newTypeParameter(String shortName) {
		return new TypeParameterName(shortName);
	}

	public static IName newTypeParameter(String shortName, String boundId) {
		return new TypeParameterName(f("%s -> %s", shortName, boundId));
	}

	public static IName newArrayType(int rank, TypeName baseType) {
		return ArrayTypeName.from(baseType, rank);
	}
}