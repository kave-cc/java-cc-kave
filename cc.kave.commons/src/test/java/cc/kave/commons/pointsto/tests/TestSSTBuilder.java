/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.tests;

import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.assignmentToLocal;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareFields;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.fieldRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invStmt;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationExpression;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;
import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.referenceExprToVariable;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnVariable;
import static cc.kave.commons.model.ssts.impl.SSTUtil.unknownStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.whileLoop;
import static cc.kave.commons.utils.ssts.SSTUtils.BYTE_ARR1D;
import static cc.kave.commons.utils.ssts.SSTUtils.FILESTREAM;
import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static cc.kave.commons.utils.ssts.SSTUtils.VOID;
import static cc.kave.commons.utils.ssts.SSTUtils.sst;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.commons.utils.io.json.JsonUtils;

/**
 * DUPLICATE of {@link cc.kave.commons.pointsto.extraction.TestSSTBuilder}
 */
public class TestSSTBuilder {

	public Context createContext(ISST sst) {
		Context context = new Context();
		context.setSST(sst);

		ITypeShape typeShape = new TypeShape();
		context.setTypeShape(typeShape);

		ITypeHierarchy typeHierarchy = new TypeHierarchy(sst.getEnclosingType().getIdentifier());
		typeShape.setTypeHierarchy(typeHierarchy);

		Set<IMemberHierarchy<IMethodName>> methodHierarchies = typeShape.getMethodHierarchies();
		for (IMethodDeclaration methodDecl : sst.getEntryPoints()) {
			methodHierarchies.add(new MethodHierarchy(methodDecl.getName()));
		}

		return context;
	}

	/**
	 * Creates a SST which realizes a class that copies one source file to a
	 * specific destination via FileStream instances. The source file is specified
	 * as constructor argument and saved in a field. The source stream is
	 * constructed in a separate helper method, whereas the destination stream is
	 * created in the single entry point method <i>CopyTo</i>.
	 */
	public Context createStreamTest() {
		ITypeName streamTestType = Names.newType("Test.StreamTest, Test");
		SST sst = sst(streamTestType);

		IFieldName sourceFieldName = Names.newField("[p:string] [Test.StreamTest, Test].source");
		sst.setFields(declareFields(sourceFieldName.getIdentifier()));

		IMethodName constructorName = newMethod("[p:void] [Test.StreamTest, Test]..ctor([p:string] source)");
		IMethodDeclaration constructorDecl = declareMethod(constructorName, true,
				assign(fieldRef("this", sourceFieldName), referenceExprToVariable("source")));

		IMethodName fileStreamCtorName = newMethod("[p:void] [" + FILESTREAM.getIdentifier()
				+ "]..ctor([p:string] path, [" + Names.getUnknownType().getIdentifier() + "] mode, ["
				+ Names.getUnknownType().getIdentifier() + "] access)");
		IMethodName openSourceName = Names
				.newMethod("[" + FILESTREAM.getIdentifier() + "] [Test.StreamTest, Test].OpenSource()");
		IMethodDeclaration openSourceDecl = declareMethod(openSourceName, false, declareVar("tmp", FILESTREAM),
				assign(varRef("tmp"),
						invocationExpression(fileStreamCtorName, Iterators.forArray(
								refExpr(fieldRef("this", sourceFieldName)), constant("Open"), constant("Read")))),
				returnVariable("tmp"));

		IMethodName copyToName = newMethod("[p:void] [Test.StreamTest, Test].CopyTo([p:string] dest)");
		IMethodName byteArrayCtorName = Names
				.newMethod("[p:void] [" + BYTE_ARR1D.getIdentifier() + "]..ctor([p:int] length)");
		IMethodName fsReadName = newMethod("[p:int] [%s].Read([p:byte[]] array, [p:int] offset, [p:int] size)",
				FILESTREAM.getIdentifier());
		IPropertyName intArrLengthName = Names.newProperty("get [p:int] [%s].Length()", BYTE_ARR1D.getIdentifier());
		IMethodName fsWriteName = newMethod(fsReadName.getIdentifier().replace(".Read(", ".Write("));
		IMethodName fsCloseName = newMethod("[p:void] [" + FILESTREAM.getIdentifier() + "].Close()");
		IMethodDeclaration copyToDecl = declareMethod(copyToName, true, declareVar("input", FILESTREAM),
				assignmentToLocal("input", invocationExpression("this", openSourceName)),
				declareVar("output", FILESTREAM),
				assignmentToLocal("output",
						invocationExpr(fileStreamCtorName, refExpr(varRef("dest")), constant("Create"),
								constant("Write"))),
				declareVar("buffer", BYTE_ARR1D),
				assignmentToLocal("buffer", invocationExpr(byteArrayCtorName, constant("1024"))),
				declareVar("read", INT),
				whileLoop(
						loopHeader(
								assignmentToLocal("read",
										invocationExpression("input", fsReadName,
												Iterators.forArray(refExpr("buffer"), constant("0"),
														refExpr(propertyRef("buffer", intArrLengthName))))),
								unknownStatement()),
						invStmt("output", fsWriteName,
								Iterators.forArray(refExpr("buffer"), constant("0"), refExpr("read")))),
				invStmt("input", fsCloseName), invStmt("output", fsCloseName));

		Set<IMethodDeclaration> methods = new HashSet<>(Arrays.asList(constructorDecl, copyToDecl, openSourceDecl));
		sst.setMethods(methods);

		return createContext(sst);
	}

	public List<Context> createPaperTest() {
		ITypeName sType = Names.newType("Test.PaperTest.S, Test");
		ITypeName aType = Names.newType("Test.PaperTest.A, Test");
		ITypeName bType = Names.newType("Test.PaperTest.B, Test");
		ITypeName cType = Names.newType("Test.PaperTest.C, Test");
		ITypeName dType = Names.newType("Test.PaperTest.D, Test");
		SST sst = sst(aType);

		sst.setFields(
				declareFields(String.format(Locale.US, "[%s] [%s].b", bType.getIdentifier(), aType.getIdentifier())));
		IFieldDeclaration bFieldDecl = sst.getFields().iterator().next();
		IMethodName helperName = Names
				.newMethod(String.format(Locale.US, "[p:void] [%s].helper()", aType.getIdentifier()));
		IMethodName fromSName = Names
				.newMethod(String.format(Locale.US, "[%s] [%s].fromS()", cType.getIdentifier(), sType.getIdentifier()));
		IMethodName entry2Name = newMethod(String.format(Locale.US, "[%s] [%s].entry2([%s] b)", VOID.getIdentifier(),
				cType.getIdentifier(), bType.getIdentifier()));
		IMethodDeclaration entry1Decl = declareMethod(
				newMethod(String.format(Locale.US, "[p:void] [%s].entry1()", aType.getIdentifier())), true,
				declareVar("tmpB", bType), assignmentToLocal("tmpB", refExpr(fieldRef("this", bFieldDecl.getName()))),
				invStmt("tmpB", newMethod(String.format(Locale.US, "[p:void] [%s].m1()", bType.getIdentifier()))),
				invStmt("this", helperName), declareVar("c", cType),
				assignmentToLocal("c", invocationExpression("this", fromSName)),
				invStmt("c", entry2Name, Iterators.forArray(refExpr(fieldRef("this", bFieldDecl.getName())))));

		IMethodDeclaration helperDecl = declareMethod(newMethod("[p:void] [%s].helper()", aType.getIdentifier()), false,
				declareVar("tmpB", bType), assignmentToLocal("tmpB", refExpr(fieldRef("this", bFieldDecl.getName()))),
				invStmt("tmpB", newMethod("[p:void] [%s].m2()", bType.getIdentifier())));

		sst.setMethods(Sets.newHashSet(entry1Decl, helperDecl));

		Context aContext = createContext(sst);
		TypeHierarchy typeHierarchy = (TypeHierarchy) aContext.getTypeShape().getTypeHierarchy();
		typeHierarchy.setExtends(new TypeHierarchy(sType.getIdentifier()));
		MethodHierarchy methodHierarchy = (MethodHierarchy) aContext.getTypeShape().getMethodHierarchies().iterator()
				.next();
		methodHierarchy.setSuper(
				newMethod(String.format(Locale.US, "[%s] [%s].entry1()", VOID.getIdentifier(), sType.getIdentifier())));

		// C
		sst = sst(cType);
		IMethodName entry3Name = newMethod("[p:void] [%s].entry3()", cType.getIdentifier());
		IMethodName dConstructor = newMethod("[p:void] [%s]..ctor()", dType.getIdentifier());
		IMethodDeclaration entry2Decl = declareMethod(entry2Name, true,
				invStmt("b", newMethod("[p:void] [%s].m3()", bType.getIdentifier())), invStmt("this", entry3Name));
		IMethodDeclaration entry3Decl = declareMethod(entry3Name, true, declareVar("d", dType),
				assignmentToLocal("d", invocationExpr(dConstructor)),
				tryBlock(invStmt("d", newMethod("[%s] [%s].m4()", VOID.getIdentifier(), dType.getIdentifier())),
						buildCatchBlock(invStmt("d",
								newMethod("[%s] [%s].m5()", VOID.getIdentifier(), dType.getIdentifier())))));
		sst.setMethods(Sets.newHashSet(entry2Decl, entry3Decl));
		Context cContext = createContext(sst);

		return Arrays.asList(aContext, cContext);
	}

	public Context createDelegateTest() {
		InputStream resource = getClass().getResourceAsStream("./DelegateTest.json");
		return JsonUtils.fromJson(resource, Context.class);
	}

	public Context createParameterArrayTest() {
		InputStream resource = getClass().getResourceAsStream("./ParameterArrayTest.json");
		return JsonUtils.fromJson(resource, Context.class);
	}

	public Context createRecursionTest() {
		InputStream resource = getClass().getResourceAsStream("./RecursionTest.json");
		return JsonUtils.fromJson(resource, Context.class);
	}

	public ITryBlock tryBlock(IStatement body, ICatchBlock catchBlock) {
		TryBlock tryBlock = new TryBlock();
		tryBlock.body.add(body);
		tryBlock.catchBlocks.add(catchBlock);
		return tryBlock;
	}

	public ICatchBlock buildCatchBlock(IStatement... body) {
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setKind(CatchBlockKind.Default);
		catchBlock.setBody(Arrays.asList(body));
		catchBlock.setParameter(Names.newParameter("[System.Exception, mscorlib] ex"));
		return catchBlock;
	}
}