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
package cc.kave.commons.utils.sstprinter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;

public class SSTPrintingVisitor extends AbstractThrowingNodeVisitor<SSTPrintingContext, Void> {

	@Override
	public Void visit(ISST sst, SSTPrintingContext context) {
		context.indentation();

		if (sst.getEnclosingType().isInterfaceType()) {
			context.keyword("interface");
		} else if (sst.getEnclosingType().isEnumType()) {
			context.keyword("enum");
		} else if (sst.getEnclosingType().isStructType()) {
			context.keyword("struct");
		} else {
			context.keyword("class");
		}

		context.space().type(sst.getEnclosingType());
		if (context.typeShape != null && context.typeShape.getTypeHierarchy().hasSupertypes()) {

			context.text(" : ");

			ITypeHierarchy extends1 = context.typeShape.getTypeHierarchy().getExtends();
			if (context.typeShape.getTypeHierarchy().hasSuperclass() && extends1 != null) {
				context.type(extends1.getElement());

				if (context.typeShape.getTypeHierarchy().isImplementingInterfaces()) {
					context.text(", ");
				}
			}
			int index = 0;
			for (ITypeHierarchy i : context.typeShape.getTypeHierarchy().getImplements()) {
				context.type(i.getElement());
				index++;
				if (index != context.typeShape.getTypeHierarchy().getImplements().size()) {
					context.text(", ");
				}
			}
		}

		context.newLine().indentation().text("{").newLine();

		context.indentationLevel++;

		appendMemberDeclarationGroup(context, sst.getDelegates(), 1, 2);
		appendMemberDeclarationGroup(context, sst.getEvents(), 1, 2);
		appendMemberDeclarationGroup(context, sst.getFields(), 1, 2);
		appendMemberDeclarationGroup(context, sst.getProperties(), 1, 2);
		appendMemberDeclarationGroup(context, sst.getMethods(), 2, 1);

		context.indentationLevel--;

		context.indentation().text("}");
		return null;
	}

	private <T extends IMemberDeclaration> Void appendMemberDeclarationGroup(SSTPrintingContext context,
			Set<T> nodeGroup, int inBetweenNewLineCount, int trailingNewLineCount) {

		List<T> nodeList = nodeGroup.stream().collect(Collectors.toList());
		for (int i = 0; i < nodeList.size(); i++) {
			T node = nodeList.get(i);
			node.accept(this, context);

			int newLinesNeeded = (i < (nodeList.size() - 1) ? inBetweenNewLineCount : trailingNewLineCount);

			for (int j = 0; j < newLinesNeeded; j++) {
				context.newLine();
			}
		}
		return null;
	}

	@Override
	public Void visit(IDelegateDeclaration stmt, SSTPrintingContext context) {
		context.indentation().keyword("delegate").space().type(stmt.getName())
				.parameterList(((IDelegateTypeName) stmt.getName()).getParameters()).text(";");
		return null;
	}

	@Override
	public Void visit(IEventDeclaration stmt, SSTPrintingContext context) {
		context.indentation().keyword("event").space().type(stmt.getName().getHandlerType()).space()
				.text(stmt.getName().getName()).text(";");
		return null;
	}

	@Override
	public Void visit(IFieldDeclaration stmt, SSTPrintingContext context) {
		context.indentation();

		if (stmt.getName().isStatic()) {
			context.keyword("static").space();
		}

		context.type(stmt.getName().getValueType()).space().text(stmt.getName().getName()).text(";");
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, SSTPrintingContext context) {
		context.indentation();

		if (stmt.getName().isStatic()) {
			context.keyword("static").space();
		}

		context.type(stmt.getName().getReturnType()).space().text(stmt.getName().getName());
		if (stmt.getName().hasTypeParameters()) {
			context.typeParameters(stmt.getName().getTypeParameters());
		}

		context.parameterList(stmt.getName().getParameters());

		context.statementBlock(stmt.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, SSTPrintingContext context) {
		context.indentation().type(stmt.getName().getValueType()).space().text(stmt.getName().getName());

		boolean hasBody = !stmt.getGet().isEmpty() || !stmt.getSet().isEmpty();

		if (hasBody) // Long version: At least one body exists --> line breaks +
						// indentation
		{
			context.newLine().indentation();

			context.indentationLevel++;

			context.text("{").newLine();

			if (stmt.getName().hasGetter()) {
				appendPropertyAccessor(context, stmt.getGet(), "get");
			}

			if (stmt.getName().hasSetter()) {
				appendPropertyAccessor(context, stmt.getSet(), "set");
			}

			context.indentationLevel--;

			context.indentation().text("}");
		} else // Short Version: No bodies --> getter/setter declaration : same
				// line
		{
			context.text(" { ");
			if (stmt.getName().hasGetter()) {
				context.keyword("get").text(";").space();
			}
			if (stmt.getName().hasSetter()) {
				context.keyword("set").text(";").space();
			}
			context.text("}");
		}
		return null;
	}

	private Void appendPropertyAccessor(SSTPrintingContext context, List<IStatement> body, String keyword) {
		if (!body.isEmpty()) {
			context.indentation().text(keyword);
			context.statementBlock(body, this, true);
		} else {
			context.indentation().text(keyword).text(";");
		}

		context.newLine();
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, SSTPrintingContext context) {
		context.indentation().type(stmt.getType()).space();
		stmt.getReference().accept(this, context);
		context.text(";");
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, SSTPrintingContext context) {
		context.indentation();
		stmt.getReference().accept(this, context);
		context.text(" = ");
		stmt.getExpression().accept(this, context);
		context.text(";");
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, SSTPrintingContext context) {
		context.indentation().keyword("break").text(";");
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, SSTPrintingContext context) {
		context.indentation().keyword("continue").text(";");
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, SSTPrintingContext context) {
		context.indentation();
		stmt.getExpression().accept(this, context);
		context.text(";");
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, SSTPrintingContext context) {
		context.indentation().keyword("goto").space().text(stmt.getLabel()).text(";");
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, SSTPrintingContext context) {
		context.indentation().keyword(stmt.getLabel()).text(":").newLine();
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, SSTPrintingContext context) {
		context.indentation().keyword("return");

		if (!stmt.isVoid()) {
			context.space();
			stmt.getExpression().accept(this, context);
		}

		context.text(";");
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, SSTPrintingContext context) {
		context.indentation().keyword("throw").space().keyword("new").space().text(stmt.getReference().getIdentifier())
				.text("();");
		return null;
	}

	@Override
	public Void visit(IDoLoop block, SSTPrintingContext context) {
		context.indentation().keyword("do");

		context.statementBlock(block.getBody(), this, true);

		context.newLine().indentation().keyword("while").space().text("(");
		context.indentationLevel++;
		block.getCondition().accept(this, context);
		context.indentationLevel--;
		context.newLine().indentation().text(")");
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, SSTPrintingContext context) {
		context.indentation().keyword("foreach").space().text("(").type(block.getDeclaration().getType()).space();
		block.getDeclaration().getReference().accept(this, context);
		context.space().keyword("in").space();
		block.getLoopedReference().accept(this, context);
		context.text(")");

		context.statementBlock(block.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(IForLoop block, SSTPrintingContext context) {
		context.indentation().keyword("for").space().text("(");

		context.indentationLevel++;

		context.statementBlock(block.getInit(), this, true);
		context.text(";");
		block.getCondition().accept(this, context);
		context.text(";");
		context.statementBlock(block.getStep(), this, true);

		context.indentationLevel--;

		context.newLine().indentation().text(")");

		context.statementBlock(block.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, SSTPrintingContext context) {
		context.indentation().keyword("if").space().text("(");
		block.getCondition().accept(this, context);
		context.text(")");

		context.statementBlock(block.getThen(), this, true);

		if (!block.getElse().isEmpty()) {
			context.newLine().indentation().keyword("else");

			context.statementBlock(block.getElse(), this, true);
		}
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, SSTPrintingContext context) {
		context.indentation().keyword("lock").space().text("(");
		stmt.getReference().accept(this, context);
		context.text(")");

		context.statementBlock(stmt.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, SSTPrintingContext context) {
		context.indentation().keyword("switch").space().text("(");
		block.getReference().accept(this, context);
		context.text(")").newLine().indentation();
		context.indentationLevel++;
		context.text("{");

		for (ICaseBlock section : block.getSections()) {
			context.newLine().indentation().keyword("case").space();
			section.getLabel().accept(this, context);
			context.text(":").statementBlock(section.getBody(), this, false);
		}

		if (!block.getDefaultSection().isEmpty()) {
			context.newLine().indentation().keyword("default").text(":").statementBlock(block.getDefaultSection(), this,
					false);
		}

		context.newLine();
		context.indentationLevel--;
		context.indentation().text("}");
		return null;
	}

	@Override
	public Void visit(ITryBlock block, SSTPrintingContext context) {
		context.indentation().keyword("try").statementBlock(block.getBody(), this, true);

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			context.newLine().indentation().keyword("catch");

			if (catchBlock.getKind() != CatchBlockKind.General) {
				context.space().text("(").type(catchBlock.getParameter().getValueType());

				if (catchBlock.getKind() != CatchBlockKind.Unnamed) {
					context.space().text(catchBlock.getParameter().getName());
				}

				context.text(")");
			}

			context.statementBlock(catchBlock.getBody(), this, true);
		}

		if (!block.getFinally().isEmpty()) {
			context.newLine().indentation().keyword("finally").statementBlock(block.getFinally(), this, true);
		}
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, SSTPrintingContext context) {
		context.indentation().keyword("unchecked").statementBlock(block.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, SSTPrintingContext context) {
		context.indentation().keyword("unsafe").text(" { ").comment("/* content ignored */").text(" }");
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, SSTPrintingContext context) {
		context.indentation().keyword("using").space().text("(");
		block.getReference().accept(this, context);
		context.text(")").statementBlock(block.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, SSTPrintingContext context) {
		context.indentation().keyword("while").space().text("(");
		context.indentationLevel++;
		block.getCondition().accept(this, context);
		context.indentationLevel--;
		context.newLine().indentation().text(")");

		context.statementBlock(block.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, SSTPrintingContext context) {
		IVariableReference objectReference = entity.getVariableReference();
		if (objectReference != null) {
			context.text(objectReference.getIdentifier()).text(".");
		} else if (entity.getTypeReference() != null) {
			context.type(entity.getTypeReference()).text(".");
		}

		context.text(entity.getToken()).cursorPosition();
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, SSTPrintingContext context) {
		context.keyword("composed").text("(");

		for (IReference reference : expr.getReferences()) {
			reference.accept(this, context);

			if (!reference.equals(expr.getReferences().get(expr.getReferences().size() - 1))) {
				context.text(", ");
			}
		}

		context.text(")");
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, SSTPrintingContext context) {
		context.text("(");
		expr.getCondition().accept(this, context);
		context.text(")").space().text("?").space();
		expr.getThenExpression().accept(this, context);
		context.space().text(":").space();
		expr.getElseExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IInvocationExpression expr, SSTPrintingContext context) {
		IMethodName methodName = expr.getMethodName();

		if (methodName.isConstructor()) {
			context.keyword("new");
			context.space();
			context.text(methodName.getDeclaringType().getName());
		} else {
			if (methodName.isStatic()) {
				context.text(methodName.getDeclaringType().getName());
			} else {
				expr.getReference().accept(this, context);
			}
			context.text(".").text(methodName.getName());
		}

		context.text("(");
		boolean isFirst = true;
		for (ISimpleExpression parameter : expr.getParameters()) {
			if (!isFirst) {
				context.text(", ");
				isFirst = false;
			}
			parameter.accept(this, context);
		}
		context.text(")");

		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, SSTPrintingContext context) {
		context.parameterList(expr.getName().getParameters()).space().text("=>");
		context.statementBlock(expr.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, SSTPrintingContext context) {
		context.statementBlock(expr.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, SSTPrintingContext context) {
		String value2 = expr.getValue();
		if (value2 != null) {
			String value = !value2.isEmpty() ? value2 : "...";

			// Double.TryParse(expr.Value, out parsed
			if (value.equals("false") || value.equals("true") || value.matches("[0-9]+")
					|| value.matches("[0-9]+\\.[0-9]+")) {
				context.keyword(value);
			} else {
				context.stringLiteral(value);
			}
		}
		return null;
	}

	@Override
	public Void visit(INullExpression expr, SSTPrintingContext context) {
		context.keyword("null");
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, SSTPrintingContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, SSTPrintingContext context) {
		context.text(eventRef.getReference().getIdentifier());
		context.text(".");
		context.text(eventRef.getEventName().getName());
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, SSTPrintingContext context) {
		context.text(fieldRef.getReference().getIdentifier());
		context.text(".");
		context.text(fieldRef.getFieldName().getName());
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, SSTPrintingContext context) {
		context.text(methodRef.getReference().getIdentifier());
		context.text(".");
		context.text(methodRef.getMethodName().getName());
		return null;
	}

	@Override
	public Void visit(IPropertyReference propertyRef, SSTPrintingContext context) {
		context.text(propertyRef.getReference().getIdentifier());
		context.text(".");
		context.text(propertyRef.getPropertyName().getName());
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, SSTPrintingContext context) {
		context.text(varRef.getIdentifier());
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, SSTPrintingContext context) {
		context.unknownMarker();
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, SSTPrintingContext context) {
		context.unknownMarker();
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, SSTPrintingContext context) {
		context.indentation().unknownMarker().text(";");
		return null;
	}

	@Override
	public Void visit(IIndexAccessReference indexAccessRef, SSTPrintingContext context) {
		// TODO: How to display these references ?
		indexAccessRef.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ICastExpression expr, SSTPrintingContext context) {
		if (expr.getOperator() == CastOperator.SafeCast) {
			context.text(expr.getReference().getIdentifier());
			context.text(" as ");
			context.text(expr.getTargetType().getName());
		} else {
			context.text("(" + expr.getTargetType().getName() + ") ");
			context.text(expr.getReference().getIdentifier());
		}
		return null;
	}

	@Override
	public Void visit(ITypeCheckExpression expr, SSTPrintingContext context) {
		context.text(expr.getReference().getIdentifier());
		context.text(" instanceof ");
		context.text(expr.getType().getName());
		return null;
	}

	@Override
	public Void visit(IIndexAccessExpression expr, SSTPrintingContext context) {
		context.text(expr.getReference().getIdentifier());
		context.text("[");
		for (int i = 0; i < expr.getIndices().size(); i++) {
			expr.getIndices().get(i).accept(this, context);
			if (i < expr.getIndices().size() - 1)
				context.text(",");
		}
		context.text("]");
		return null;
	}

	@Override
	public Void visit(IUnaryExpression expr, SSTPrintingContext context) {
		switch (expr.getOperator()) {
		case Not:
			context.text("!");
			expr.getOperand().accept(this, context);
			break;
		case PreIncrement:
			context.text("++");
			expr.getOperand().accept(this, context);
			break;
		case PostIncrement:
			expr.getOperand().accept(this, context);
			context.text("++");
			break;
		case PreDecrement:
			context.text("--");
			expr.getOperand().accept(this, context);
			break;
		case PostDecrement:
			expr.getOperand().accept(this, context);
			context.text("--");
			break;
		case Plus:
			context.text("+");
			expr.getOperand().accept(this, context);
			break;
		case Minus:
			context.text("-");
			expr.getOperand().accept(this, context);
			break;
		case Complement:
			context.text("~");
			expr.getOperand().accept(this, context);
			break;
		default:
			context.text("?");
			expr.getOperand().accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IBinaryExpression expr, SSTPrintingContext context) {
		expr.getLeftOperand().accept(this, context);
		switch (expr.getOperator()) {
		case And:
			context.text(" && ");
			break;
		case BitwiseAnd:
			context.text(" & ");
			break;
		case BitwiseOr:
			context.text(" | ");
			break;
		case BitwiseXor:
			context.text(" ^ ");
			break;
		case Divide:
			context.text(" / ");
			break;
		case Equal:
			context.text(" == ");
			break;
		case GreaterThan:
			context.text(" > ");
			break;
		case GreaterThanOrEqual:
			context.text(" >= ");
			break;
		case LessThan:
			context.text(" < ");
			break;
		case LessThanOrEqual:
			context.text(" <= ");
			break;
		case Minus:
			context.text(" - ");
			break;
		case Modulo:
			context.text(" % ");
			break;
		case Multiply:
			context.text(" * ");
			break;
		case NotEqual:
			context.text(" != ");
			break;
		case Or:
			context.text(" || ");
			break;
		case Plus:
			context.text(" + ");
			break;
		case ShiftLeft:
			context.text(" << ");
			break;
		case ShiftRight:
			context.text(" >> ");
			break;
		default:
			context.text(" ?? ");
			break;
		}
		expr.getRightOperand().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, SSTPrintingContext context) {
		context.indentation();
		stmt.getReference().accept(this, context);
		switch (stmt.getOperation()) {
		case Add:
			context.text(" += ");
			break;
		case Remove:
			context.text(" -= ");
			break;
		default:
			context.text(" ?? ");
		}
		stmt.getExpression().accept(this, context);
		return null;
	}
}
