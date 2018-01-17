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
package cc.kave.commons.generated;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.json.JsonUtils;

public class GeneratedContextTest {

	// Please do not manually edit this file! Generate the JSON in C# and just copy
	// it to this class.

	@Test
	public void DeSerializationRountrip() {
		Context obj = JsonUtils.fromJson(json, Context.class);
		String actual = JsonUtils.toJsonFormatted(obj);
		Assert.assertEquals(json, actual);
	}

	@Test
	public void RegularAndFormattedAreEqual() {
		Context obj = JsonUtils.fromJson(json, Context.class);

		String json1 = JsonUtils.toJson(obj);
		Context ctx1 = JsonUtils.fromJson(json1, Context.class);

		String json2 = JsonUtils.toJsonFormatted(obj);
		Context ctx2 = JsonUtils.fromJson(json2, Context.class);

		Assert.assertEquals(ctx1, ctx2);
	}

	private String json = "{\n" + //
			"    \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\",\n" + //
			"    \"TypeShape\": {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeShape, KaVE.Commons\",\n" + //
			"        \"TypeHierarchy\": {\n" + //
			"            \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeHierarchy, KaVE.Commons\",\n" + //
			"            \"Element\": \"0T:T1, P\",\n" + //
			"            \"Extends\": {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeHierarchy, KaVE.Commons\",\n" + //
			"                \"Element\": \"0T:T2, P\",\n" + //
			"                \"Implements\": []\n" + //
			"            },\n" + //
			"            \"Implements\": [\n" + //
			"                {\n" + //
			"                    \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeHierarchy, KaVE.Commons\",\n" + //
			"                    \"Element\": \"0T:T3, P\",\n" + //
			"                    \"Implements\": []\n" + //
			"                }\n" + //
			"            ]\n" + //
			"        },\n" + //
			"        \"NestedTypes\": [\n" + //
			"            \"0T:A.B.T+N, P\"\n" + //
			"        ],\n" + //
			"        \"Delegates\": [\n" + //
			"            \"0T:d:[p:void] [T,P].()\"\n" + //
			"        ],\n" + //
			"        \"EventHierarchies\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.TypeShapes.EventHierarchy, KaVE.Commons\",\n" + //
			"                \"Element\": \"0E:[p:int] [T,P].E\",\n" + //
			"                \"Super\": \"0E:[p:int] [S,P].E\",\n" + //
			"                \"First\": \"0E:[p:int] [F,P].E\"\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"Fields\": [\n" + //
			"            \"0F:[p:int] [T,P]._f\"\n" + //
			"        ],\n" + //
			"        \"MethodHierarchies\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.TypeShapes.MethodHierarchy, KaVE.Commons\",\n" + //
			"                \"Element\": \"0M:[?] [?].M1()\",\n" + //
			"                \"Super\": \"0M:[?] [?].M2()\",\n" + //
			"                \"First\": \"0M:[?] [?].M3()\"\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"PropertyHierarchies\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.TypeShapes.PropertyHierarchy, KaVE.Commons\",\n" + //
			"                \"Element\": \"0P:get [p:int] [T,P].P()\",\n" + //
			"                \"Super\": \"0P:get [p:int] [S,P].P()\",\n" + //
			"                \"First\": \"0P:get [p:int] [F,P].P()\"\n" + //
			"            }\n" + //
			"        ]\n" + //
			"    },\n" + //
			"    \"SST\": {\n" + //
			"        \"$type\": \"[SST:SST]\",\n" + //
			"        \"EnclosingType\": \"0T:T,P\",\n" + //
			"        \"Fields\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"[SST:Declarations.FieldDeclaration]\",\n" + //
			"                \"Name\": \"0F:[T4,P] [T5,P].F\"\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"Properties\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"[SST:Declarations.PropertyDeclaration]\",\n" + //
			"                \"Name\": \"0P:get [T10,P] [T11,P].P()\",\n" + //
			"                \"Get\": [\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.ReturnStatement]\",\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.UnknownExpression]\"\n" + //
			"                        },\n" + //
			"                        \"IsVoid\": false\n" + //
			"                    }\n" + //
			"                ],\n" + //
			"                \"Set\": [\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.UnknownExpression]\"\n" + //
			"                        }\n" + //
			"                    }\n" + //
			"                ]\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"Methods\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"[SST:Declarations.MethodDeclaration]\",\n" + //
			"                \"Name\": \"0M:[T6,P] [T7,P].M1()\",\n" + //
			"                \"IsEntryPoint\": false,\n" + //
			"                \"Body\": [\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.DoLoop]\",\n" + //
			"                        \"Condition\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                        },\n" + //
			"                        \"Body\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.ForEachLoop]\",\n" + //
			"                        \"Declaration\": {\n" + //
			"                            \"$type\": \"[SST:Statements.VariableDeclaration]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                \"Identifier\": \"\"\n" + //
			"                            },\n" + //
			"                            \"Type\": \"0T:T1,P\"\n" + //
			"                        },\n" + //
			"                        \"LoopedReference\": {\n" + //
			"                            \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                            \"Identifier\": \"\"\n" + //
			"                        },\n" + //
			"                        \"Body\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.ForLoop]\",\n" + //
			"                        \"Init\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ],\n" + //
			"                        \"Condition\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                        },\n" + //
			"                        \"Step\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ],\n" + //
			"                        \"Body\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.IfElseBlock]\",\n" + //
			"                        \"Condition\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                        },\n" + //
			"                        \"Then\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ],\n" + //
			"                        \"Else\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.LockBlock]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                            \"Identifier\": \"\"\n" + //
			"                        },\n" + //
			"                        \"Body\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.SwitchBlock]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                            \"Identifier\": \"\"\n" + //
			"                        },\n" + //
			"                        \"Sections\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Blocks.CaseBlock]\",\n" + //
			"                                \"Label\": {\n" + //
			"                                    \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                                },\n" + //
			"                                \"Body\": [\n" + //
			"                                    {\n" + //
			"                                        \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                                    }\n" + //
			"                                ]\n" + //
			"                            }\n" + //
			"                        ],\n" + //
			"                        \"DefaultSection\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.TryBlock]\",\n" + //
			"                        \"Body\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ],\n" + //
			"                        \"CatchBlocks\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Blocks.CatchBlock]\",\n" + //
			"                                \"Kind\": 2,\n" + //
			"                                \"Parameter\": \"0Param:[?] p\",\n" + //
			"                                \"Body\": [\n" + //
			"                                    {\n" + //
			"                                        \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                                    }\n" + //
			"                                ]\n" + //
			"                            }\n" + //
			"                        ],\n" + //
			"                        \"Finally\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.UncheckedBlock]\",\n" + //
			"                        \"Body\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.UnsafeBlock]\"\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.UsingBlock]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                            \"Identifier\": \"\"\n" + //
			"                        },\n" + //
			"                        \"Body\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.WhileLoop]\",\n" + //
			"                        \"Condition\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                        },\n" + //
			"                        \"Body\": [\n" + //
			"                            {\n" + //
			"                                \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                            }\n" + //
			"                        ]\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                            \"Identifier\": \"\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.ContinueStatement]\"\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.EventSubscriptionStatement]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                            \"Identifier\": \"\"\n" + //
			"                        },\n" + //
			"                        \"Operation\": 0,\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.ExpressionStatement]\",\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.GotoStatement]\",\n" + //
			"                        \"Label\": \"l\"\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.LabelledStatement]\",\n" + //
			"                        \"Label\": \"l\",\n" + //
			"                        \"Statement\": {\n" + //
			"                            \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.ReturnStatement]\",\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                        },\n" + //
			"                        \"IsVoid\": true\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.ThrowStatement]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                            \"Identifier\": \"\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.UnknownStatement]\"\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.VariableDeclaration]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                            \"Identifier\": \"\"\n" + //
			"                        },\n" + //
			"                        \"Type\": \"0T:T2, P\"\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.BinaryExpression]\",\n" + //
			"                            \"LeftOperand\": {\n" + //
			"                                \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                            },\n" + //
			"                            \"Operator\": 14,\n" + //
			"                            \"RightOperand\": {\n" + //
			"                                \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.CastExpression]\",\n" + //
			"                            \"TargetType\": \"0T:T3, P\",\n" + //
			"                            \"Operator\": 2,\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                \"Identifier\": \"\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.CompletionExpression]\",\n" + //
			"                            \"TypeReference\": \"0T:T4, P\",\n" + //
			"                            \"VariableReference\": {\n" + //
			"                                \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                \"Identifier\": \"\"\n" + //
			"                            },\n" + //
			"                            \"Token\": \"t\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.ComposedExpression]\",\n" + //
			"                            \"References\": [\n" + //
			"                                {\n" + //
			"                                    \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                    \"Identifier\": \"\"\n" + //
			"                                }\n" + //
			"                            ]\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.IfElseExpression]\",\n" + //
			"                            \"Condition\": {\n" + //
			"                                \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                            },\n" + //
			"                            \"ThenExpression\": {\n" + //
			"                                \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                            },\n" + //
			"                            \"ElseExpression\": {\n" + //
			"                                \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.IndexAccessExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                \"Identifier\": \"\"\n" + //
			"                            },\n" + //
			"                            \"Indices\": [\n" + //
			"                                {\n" + //
			"                                    \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                                }\n" + //
			"                            ]\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.InvocationExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                \"Identifier\": \"\"\n" + //
			"                            },\n" + //
			"                            \"MethodName\": \"0M:[?] [?].M()\",\n" + //
			"                            \"Parameters\": [\n" + //
			"                                {\n" + //
			"                                    \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                                }\n" + //
			"                            ]\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.LambdaExpression]\",\n" + //
			"                            \"Name\": \"0L:[?] ()\",\n" + //
			"                            \"Body\": [\n" + //
			"                                {\n" + //
			"                                    \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                                }\n" + //
			"                            ]\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.TypeCheckExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                \"Identifier\": \"\"\n" + //
			"                            },\n" + //
			"                            \"Type\": \"0T:T4, P\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Assignable.UnaryExpression]\",\n" + //
			"                            \"Operator\": 7,\n" + //
			"                            \"Operand\": {\n" + //
			"                                \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Blocks.WhileLoop]\",\n" + //
			"                        \"Condition\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.LoopHeader.LoopHeaderBlockExpression]\",\n" + //
			"                            \"Body\": [\n" + //
			"                                {\n" + //
			"                                    \"$type\": \"[SST:Statements.BreakStatement]\"\n" + //
			"                                }\n" + //
			"                            ]\n" + //
			"                        },\n" + //
			"                        \"Body\": []\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\",\n" + //
			"                            \"Value\": \"v\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.NullExpression]\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ReferenceExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                \"Identifier\": \"\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.UnknownExpression]\"\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ReferenceExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.EventReference]\",\n" + //
			"                                \"Reference\": {\n" + //
			"                                    \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                    \"Identifier\": \"\"\n" + //
			"                                },\n" + //
			"                                \"EventName\": \"0E:[?] [?].e\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ReferenceExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.FieldReference]\",\n" + //
			"                                \"Reference\": {\n" + //
			"                                    \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                    \"Identifier\": \"\"\n" + //
			"                                },\n" + //
			"                                \"FieldName\": \"0F:[?] [?]._f\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ReferenceExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.IndexAccessReference]\",\n" + //
			"                                \"Expression\": {\n" + //
			"                                    \"$type\": \"[SST:Expressions.Assignable.IndexAccessExpression]\",\n" + //
			"                                    \"Reference\": {\n" + //
			"                                        \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                        \"Identifier\": \"\"\n" + //
			"                                    },\n" + //
			"                                    \"Indices\": [\n" + //
			"                                        {\n" + //
			"                                            \"$type\": \"[SST:Expressions.Simple.ConstantValueExpression]\"\n"
			+ //
			"                                        }\n" + //
			"                                    ]\n" + //
			"                                }\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ReferenceExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.MethodReference]\",\n" + //
			"                                \"Reference\": {\n" + //
			"                                    \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                    \"Identifier\": \"\"\n" + //
			"                                },\n" + //
			"                                \"MethodName\": \"0M:[?] [?].M()\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ReferenceExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.PropertyReference]\",\n" + //
			"                                \"Reference\": {\n" + //
			"                                    \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                    \"Identifier\": \"\"\n" + //
			"                                },\n" + //
			"                                \"PropertyName\": \"0P:get [?] [?].P()\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ReferenceExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    },\n" + //
			"                    {\n" + //
			"                        \"$type\": \"[SST:Statements.Assignment]\",\n" + //
			"                        \"Reference\": {\n" + //
			"                            \"$type\": \"[SST:References.UnknownReference]\"\n" + //
			"                        },\n" + //
			"                        \"Expression\": {\n" + //
			"                            \"$type\": \"[SST:Expressions.Simple.ReferenceExpression]\",\n" + //
			"                            \"Reference\": {\n" + //
			"                                \"$type\": \"[SST:References.VariableReference]\",\n" + //
			"                                \"Identifier\": \"id\"\n" + //
			"                            }\n" + //
			"                        }\n" + //
			"                    }\n" + //
			"                ]\n" + //
			"            },\n" + //
			"            {\n" + //
			"                \"$type\": \"[SST:Declarations.MethodDeclaration]\",\n" + //
			"                \"Name\": \"0M:[T8,P] [T9,P].M2()\",\n" + //
			"                \"IsEntryPoint\": true,\n" + //
			"                \"Body\": []\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"Events\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"[SST:Declarations.EventDeclaration]\",\n" + //
			"                \"Name\": \"0E:[T2,P] [T3,P].E\"\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"Delegates\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"[SST:Declarations.DelegateDeclaration]\",\n" + //
			"                \"Name\": \"0T:d:[R,P] [T2,P].()\"\n" + //
			"            }\n" + //
			"        ]\n" + //
			"    }\n" + //
			"}";
}