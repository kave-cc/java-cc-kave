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
package cc.kave.commons.utils.naming.serialization;

import cc.kave.commons.model.naming.Names;
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

public class NameSerializerV0 extends NameSerializerBase {

	@Override
	protected void RegisterTypes() {
		Register(GeneralName.class, Names::newGeneral, "0General", "CSharp.Name");

		// code elements
		Register(AliasName.class, Names::newAlias, "0Alias", "CSharp.AliasName");
		Register(EventName.class, Names::newEvent, "0E", "CSharp.EventName");
		Register(FieldName.class, Names::newField, "0F", "CSharp.FieldName");
		Register(LambdaName.class, Names::newLambda, "0L", "CSharp.LambdaName");
		Register(LocalVariableName.class, Names::newLocalVariable, "0LocalVar", "CSharp.LocalVariableName");
		Register(MethodName.class, Names::newMethod, "0M", "CSharp.MethodName");
		Register(ParameterName.class, Names::newParameter, "0Param", "CSharp.ParameterName");
		Register(PropertyName.class, Names::newProperty, "0P", "CSharp.PropertyName");

		// ide components
		Register(CommandBarControlName.class, Names::newCommandBarControl, "0Ctrl",
				"VisualStudio.CommandBarControlName");
		Register(CommandName.class, Names::newCommand, "0Cmd", "VisualStudio.CommandName");
		Register(DocumentName.class, Names::newDocument, "0Doc", "VisualStudio.DocumentName");
		Register(ProjectItemName.class, Names::newProjectItem, "0Itm", "VisualStudio.ProjectItemName");
		Register(ProjectName.class, Names::newProject, "0Prj", "VisualStudio.ProjectName");
		Register(SolutionName.class, Names::newSolution, "0Sln", "VisualStudio.SolutionName");
		Register(WindowName.class, Names::newWindow, "0Win", "VisualStudio.WindowName");

		// others
		Register(ReSharperLiveTemplateName.class, Names::newReSharperLiveTemplate, "0RSTpl",
				"ReSharper.LiveTemplateName");

		// types/organization
		Register(AssemblyName.class, Names::newAssembly, "0A", "CSharp.AssemblyName");
		Register(AssemblyVersion.class, Names::newAssemblyVersion, "0V", "CSharp.AssemblyVersion");
		Register(NamespaceName.class, Names::newNamespace, "0N", "CSharp.NamespaceName");

		// types
		RegisterTypeMapping(TypeName.class, ArrayTypeName.class, DelegateTypeName.class, TypeParameterName.class,
				PredefinedTypeName.class);
		Register(TypeName.class, Names::newType, "0T", "CSharp.TypeName", "CSharp.UnknownTypeName",
				"CSharp.ArrayTypeName", "CSharp.DelegateTypeName", "CSharp.EnumTypeName", "CSharp.InterfaceTypeName",
				"CSharp.StructTypeName", "CSharp.PredefinedTypeName", "CSharp.TypeParameterName");
	}
}