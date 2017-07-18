// ##############################################################################
// Attention: This file was auto-generated, do not modify its contents manually!!
// (generated at: 8/15/2016 4:25:41 AM)
// ##############################################################################
/**
* Copyright 2016 Technische Universit„t Darmstadt
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
package cc.kave.commons.model.naming.impl.v0.types;
import static org.junit.Assert.*;
import org.junit.Test;
import cc.kave.commons.model.naming.impl.v0.codeelements.*;
import cc.kave.commons.model.naming.impl.v0.types.*;
import cc.kave.commons.model.naming.impl.v0.types.organization.*;
import cc.kave.commons.model.naming.codeelements.*;
import cc.kave.commons.model.naming.types.*;
import cc.kave.commons.model.naming.types.organization.*;
import cc.kave.commons.exceptions.*;
import cc.kave.commons.model.naming.*;

import com.google.common.collect.*;
public class GeneratedTest {
/*
 * default value tests
 */
@Test
public void DefaultValues_TypeName() {
String id = "?";
assertEquals(true, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertTrue(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?", sut.getFullName());
assertEquals("?", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void DefaultValues_DelegateTypeName() {
String id = "d:[?] [?].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertTrue(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?", sut.getFullName());
assertEquals("?", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("?"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertFalse(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new TypeName("?"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void DeriveArrayTest_0() {
String baseId = "?";
String arr1Id = "?[]";
String arr2Id = "?[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_1() {
String baseId = "T,P";
String arr1Id = "T[],P";
String arr2Id = "T[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_2() {
String baseId = "n.T`1[[G]],P";
String arr1Id = "n.T`1[][[G]],P";
String arr2Id = "n.T`1[,][[G]],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_3() {
String baseId = "n.T`1[[G -> p:byte]],P";
String arr1Id = "n.T`1[][[G -> p:byte]],P";
String arr2Id = "n.T`1[,][[G -> p:byte]],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_4() {
String baseId = "T";
String arr1Id = "T[]";
String arr2Id = "T[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_5() {
String baseId = "p:int";
String arr1Id = "p:int[]";
String arr2Id = "p:int[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof PredefinedTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof PredefinedTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_6() {
String baseId = "A, B, 1.2.3.4";
String arr1Id = "A[], B, 1.2.3.4";
String arr2Id = "A[,], B, 1.2.3.4";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_7() {
String baseId = "System.Nullable`1[[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4";
String arr1Id = "System.Nullable`1[][[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4";
String arr2Id = "System.Nullable`1[,][[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_8() {
String baseId = "s:S, P";
String arr1Id = "s:S[], P";
String arr2Id = "s:S[,], P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_9() {
String baseId = "T -> ?";
String arr1Id = "T[] -> ?";
String arr2Id = "T[,] -> ?";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_10() {
String baseId = "GT`1[[T -> PT, A]], A";
String arr1Id = "GT`1[][[T -> PT, A]], A";
String arr2Id = "GT`1[,][[T -> PT, A]], A";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_11() {
String baseId = "d:[RT, A] [DT, A].()";
String arr1Id = "d:[RT, A] [DT, A].()[]";
String arr2Id = "d:[RT, A] [DT, A].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_12() {
String baseId = "d:[RT[], A] [DT, A].([PT[], A] p)";
String arr1Id = "d:[RT[], A] [DT, A].([PT[], A] p)[]";
String arr2Id = "d:[RT[], A] [DT, A].([PT[], A] p)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_13() {
String baseId = "T`1[[T -> d:[TR] [T2, P2].([T] arg)]], P";
String arr1Id = "T`1[][[T -> d:[TR] [T2, P2].([T] arg)]], P";
String arr2Id = "T`1[,][[T -> d:[TR] [T2, P2].([T] arg)]], P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_14() {
String baseId = "n.C1+C2,P";
String arr1Id = "n.C1+C2[],P";
String arr2Id = "n.C1+C2[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_15() {
String baseId = "C1`1[[T1]],P";
String arr1Id = "C1`1[][[T1]],P";
String arr2Id = "C1`1[,][[T1]],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_16() {
String baseId = "C1+C2`1[[T2]],P";
String arr1Id = "C1+C2`1[][[T2]],P";
String arr2Id = "C1+C2`1[,][[T2]],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_17() {
String baseId = "C1`1[[T2]]+C2,P";
String arr1Id = "C1`1[[T2]]+C2[],P";
String arr2Id = "C1`1[[T2]]+C2[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_18() {
String baseId = "C1`1[[T1]]+C2`1[[T2]],P";
String arr1Id = "C1`1[[T1]]+C2`1[][[T2]],P";
String arr2Id = "C1`1[[T1]]+C2`1[,][[T2]],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_19() {
String baseId = "T -> T[],P";
String arr1Id = "T[] -> T[],P";
String arr2Id = "T[,] -> T[],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_20() {
String baseId = "T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[[T8 -> T9[],P]], P";
String arr1Id = "T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[][[T8 -> T9[],P]], P";
String arr2Id = "T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[,][[T8 -> T9[],P]], P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_21() {
String baseId = "d:[?] [?].()";
String arr1Id = "d:[?] [?].()[]";
String arr2Id = "d:[?] [?].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_22() {
String baseId = "d:[T,P] [T,P].()";
String arr1Id = "d:[T,P] [T,P].()[]";
String arr2Id = "d:[T,P] [T,P].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_23() {
String baseId = "d:[R, P] [O+D, P].()";
String arr1Id = "d:[R, P] [O+D, P].()[]";
String arr2Id = "d:[R, P] [O+D, P].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_24() {
String baseId = "d:[T`1[[T -> n.C+D, P]], P] [n.C+D, P].()";
String arr1Id = "d:[T`1[[T -> n.C+D, P]], P] [n.C+D, P].()[]";
String arr2Id = "d:[T`1[[T -> n.C+D, P]], P] [n.C+D, P].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_25() {
String baseId = "d:[?] [n.C+D, P].([T`1[[T -> n.C+D, P]], P] p)";
String arr1Id = "d:[?] [n.C+D, P].([T`1[[T -> n.C+D, P]], P] p)[]";
String arr2Id = "d:[?] [n.C+D, P].([T`1[[T -> n.C+D, P]], P] p)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_26() {
String baseId = "i:n.T1`1[[T2 -> p:int]], P";
String arr1Id = "i:n.T1`1[][[T2 -> p:int]], P";
String arr2Id = "i:n.T1`1[,][[T2 -> p:int]], P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_27() {
String baseId = "n.T,P";
String arr1Id = "n.T[],P";
String arr2Id = "n.T[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_28() {
String baseId = "s:T,P";
String arr1Id = "s:T[],P";
String arr2Id = "s:T[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_29() {
String baseId = "s:n.T,P";
String arr1Id = "s:n.T[],P";
String arr2Id = "s:n.T[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_30() {
String baseId = "e:T,P";
String arr1Id = "e:T[],P";
String arr2Id = "e:T[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_31() {
String baseId = "e:n.T,P";
String arr1Id = "e:n.T[],P";
String arr2Id = "e:n.T[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_32() {
String baseId = "i:T,P";
String arr1Id = "i:T[],P";
String arr2Id = "i:T[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_33() {
String baseId = "i:n.T,P";
String arr1Id = "i:n.T[],P";
String arr2Id = "i:n.T[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_34() {
String baseId = "n.T1`1[[T2]], P";
String arr1Id = "n.T1`1[][[T2]], P";
String arr2Id = "n.T1`1[,][[T2]], P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_35() {
String baseId = "n.T1+T2, P";
String arr1Id = "n.T1+T2[], P";
String arr2Id = "n.T1+T2[,], P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_36() {
String baseId = "n.T1`1[[T2]]+T3`1[[T4]], P";
String arr1Id = "n.T1`1[[T2]]+T3`1[][[T4]], P";
String arr2Id = "n.T1`1[[T2]]+T3`1[,][[T4]], P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_37() {
String baseId = "n.C+N`1[[T]],P";
String arr1Id = "n.C+N`1[][[T]],P";
String arr2Id = "n.C+N`1[,][[T]],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_38() {
String baseId = "n.C`1[[T]]+N,P";
String arr1Id = "n.C`1[[T]]+N[],P";
String arr2Id = "n.C`1[[T]]+N[,],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_39() {
String baseId = "n.C`1[[T]]+N`1[[T]],P";
String arr1Id = "n.C`1[[T]]+N`1[][[T]],P";
String arr2Id = "n.C`1[[T]]+N`1[,][[T]],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_40() {
String baseId = "s:System.Nullable`1[[T -> p:sbyte]], mscorlib, 4.0.0.0";
String arr1Id = "s:System.Nullable`1[][[T -> p:sbyte]], mscorlib, 4.0.0.0";
String arr2Id = "s:System.Nullable`1[,][[T -> p:sbyte]], mscorlib, 4.0.0.0";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_41() {
String baseId = "System.X, mscorlib, 4.0.0.0";
String arr1Id = "System.X[], mscorlib, 4.0.0.0";
String arr2Id = "System.X[,], mscorlib, 4.0.0.0";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_42() {
String baseId = "e:Full.Enum.Type, E, 1.2.3.4";
String arr1Id = "e:Full.Enum.Type[], E, 1.2.3.4";
String arr2Id = "e:Full.Enum.Type[,], E, 1.2.3.4";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_43() {
String baseId = "i:Full.Interface.Type, E, 1.2.3.4";
String arr1Id = "i:Full.Interface.Type[], E, 1.2.3.4";
String arr2Id = "i:Full.Interface.Type[,], E, 1.2.3.4";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_44() {
String baseId = "s:Full.Struct.Type, E, 1.2.3.4";
String arr1Id = "s:Full.Struct.Type[], E, 1.2.3.4";
String arr2Id = "s:Full.Struct.Type[,], E, 1.2.3.4";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_45() {
String baseId = "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
String arr1Id = "System.Nullable`1[][[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
String arr2Id = "System.Nullable`1[,][[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_46() {
String baseId = "Outer.Type+InnerType, As, 1.2.3.4";
String arr1Id = "Outer.Type+InnerType[], As, 1.2.3.4";
String arr2Id = "Outer.Type+InnerType[,], As, 1.2.3.4";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_47() {
String baseId = "Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
String arr1Id = "Task`1[][[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
String arr2Id = "Task`1[,][[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_48() {
String baseId = "d:[?] [?].([?] p1)";
String arr1Id = "d:[?] [?].([?] p1)[]";
String arr2Id = "d:[?] [?].([?] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_49() {
String baseId = "d:[?] [?].([?] p1, [?] p2)";
String arr1Id = "d:[?] [?].([?] p1, [?] p2)[]";
String arr2Id = "d:[?] [?].([?] p1, [?] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_50() {
String baseId = "d:[T] [T].()";
String arr1Id = "d:[T] [T].()[]";
String arr2Id = "d:[T] [T].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_51() {
String baseId = "d:[T] [T].([T] p1)";
String arr1Id = "d:[T] [T].([T] p1)[]";
String arr2Id = "d:[T] [T].([T] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_52() {
String baseId = "d:[T] [T].([T] p1, [T] p2)";
String arr1Id = "d:[T] [T].([T] p1, [T] p2)[]";
String arr2Id = "d:[T] [T].([T] p1, [T] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_53() {
String baseId = "d:[T -> T,P] [T -> T,P].()";
String arr1Id = "d:[T -> T,P] [T -> T,P].()[]";
String arr2Id = "d:[T -> T,P] [T -> T,P].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_54() {
String baseId = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1)";
String arr1Id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1)[]";
String arr2Id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_55() {
String baseId = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)";
String arr1Id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)[]";
String arr2Id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_56() {
String baseId = "d:[T,P] [T,P].([T,P] p1)";
String arr1Id = "d:[T,P] [T,P].([T,P] p1)[]";
String arr2Id = "d:[T,P] [T,P].([T,P] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_57() {
String baseId = "d:[T,P] [T,P].([T,P] p1, [T,P] p2)";
String arr1Id = "d:[T,P] [T,P].([T,P] p1, [T,P] p2)[]";
String arr2Id = "d:[T,P] [T,P].([T,P] p1, [T,P] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_58() {
String baseId = "d:[T[],P] [T[],P].()";
String arr1Id = "d:[T[],P] [T[],P].()[]";
String arr2Id = "d:[T[],P] [T[],P].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_59() {
String baseId = "d:[T[],P] [T[],P].([T[],P] p1)";
String arr1Id = "d:[T[],P] [T[],P].([T[],P] p1)[]";
String arr2Id = "d:[T[],P] [T[],P].([T[],P] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_60() {
String baseId = "d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)";
String arr1Id = "d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)[]";
String arr2Id = "d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_61() {
String baseId = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].()";
String arr1Id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].()[]";
String arr2Id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_62() {
String baseId = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)";
String arr1Id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)[]";
String arr2Id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_63() {
String baseId = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)";
String arr1Id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)[]";
String arr2Id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_64() {
String baseId = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].()";
String arr1Id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].()[]";
String arr2Id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_65() {
String baseId = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)";
String arr1Id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)[]";
String arr2Id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_66() {
String baseId = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)";
String arr1Id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)[]";
String arr2Id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_67() {
String baseId = "d:[T[]] [T[]].()";
String arr1Id = "d:[T[]] [T[]].()[]";
String arr2Id = "d:[T[]] [T[]].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_68() {
String baseId = "d:[T[]] [T[]].([T[]] p1)";
String arr1Id = "d:[T[]] [T[]].([T[]] p1)[]";
String arr2Id = "d:[T[]] [T[]].([T[]] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_69() {
String baseId = "d:[T[]] [T[]].([T[]] p1, [T[]] p2)";
String arr1Id = "d:[T[]] [T[]].([T[]] p1, [T[]] p2)[]";
String arr2Id = "d:[T[]] [T[]].([T[]] p1, [T[]] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_70() {
String baseId = "d:[T[] -> T,P] [T[] -> T,P].()";
String arr1Id = "d:[T[] -> T,P] [T[] -> T,P].()[]";
String arr2Id = "d:[T[] -> T,P] [T[] -> T,P].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_71() {
String baseId = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)";
String arr1Id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)[]";
String arr2Id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_72() {
String baseId = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)";
String arr1Id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)[]";
String arr2Id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_73() {
String baseId = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].()";
String arr1Id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].()[]";
String arr2Id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_74() {
String baseId = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)";
String arr1Id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)[]";
String arr2Id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_75() {
String baseId = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)";
String arr1Id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)[]";
String arr2Id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_76() {
String baseId = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].()";
String arr1Id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].()[]";
String arr2Id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_77() {
String baseId = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)";
String arr1Id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)[]";
String arr2Id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_78() {
String baseId = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)";
String arr1Id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)[]";
String arr2Id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_79() {
String baseId = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].()";
String arr1Id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].()[]";
String arr2Id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_80() {
String baseId = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)";
String arr1Id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)[]";
String arr2Id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_81() {
String baseId = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)";
String arr1Id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)[]";
String arr2Id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_82() {
String baseId = "d:[p:int] [p:int].()";
String arr1Id = "d:[p:int] [p:int].()[]";
String arr2Id = "d:[p:int] [p:int].()[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_83() {
String baseId = "d:[p:int] [p:int].([p:int] p1)";
String arr1Id = "d:[p:int] [p:int].([p:int] p1)[]";
String arr2Id = "d:[p:int] [p:int].([p:int] p1)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_84() {
String baseId = "d:[p:int] [p:int].([p:int] p1, [p:int] p2)";
String arr1Id = "d:[p:int] [p:int].([p:int] p1, [p:int] p2)[]";
String arr2Id = "d:[p:int] [p:int].([p:int] p1, [p:int] p2)[,]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof ArrayTypeName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof ArrayTypeName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_85() {
String baseId = "T -> T,P";
String arr1Id = "T[] -> T,P";
String arr2Id = "T[,] -> T,P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_86() {
String baseId = "T -> d:[?] [n.C+D, P].()";
String arr1Id = "T[] -> d:[?] [n.C+D, P].()";
String arr2Id = "T[,] -> d:[?] [n.C+D, P].()";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_87() {
String baseId = "T -> T`1[[P -> T2,P]],P";
String arr1Id = "T[] -> T`1[[P -> T2,P]],P";
String arr2Id = "T[,] -> T`1[[P -> T2,P]],P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_88() {
String baseId = "T -> d:[?] [?].()[]";
String arr1Id = "T[] -> d:[?] [?].()[]";
String arr2Id = "T[,] -> d:[?] [?].()[]";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_89() {
String baseId = "T -> n.C+D`1[[T]], P";
String arr1Id = "T[] -> n.C+D`1[[T]], P";
String arr2Id = "T[,] -> n.C+D`1[[T]], P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_90() {
String baseId = "T -> n.C`1[[T]]+D, P";
String arr1Id = "T[] -> n.C`1[[T]]+D, P";
String arr2Id = "T[,] -> n.C`1[[T]]+D, P";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void DeriveArrayTest_91() {
String baseId = "T -> p:int";
String arr1Id = "T[] -> p:int";
String arr2Id = "T[,] -> p:int";
ITypeName base = TypeUtils.createTypeName(baseId);
ITypeName arr1 = ArrayTypeName.from(base, 1);
assertTrue(arr1 instanceof TypeParameterName);
assertEquals(arr1Id, arr1.getIdentifier());
ITypeName arr2 = ArrayTypeName.from(base, 2);
assertTrue(arr2 instanceof TypeParameterName);
assertEquals(arr2Id, arr2.getIdentifier());
}
@Test
public void TypeTest_0() {
String id = "?";
assertEquals(true, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertTrue(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?", sut.getFullName());
assertEquals("?", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_1() {
String id = "?[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[]", sut.getFullName());
assertEquals("?[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("?"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_2() {
String id = "?[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[,]", sut.getFullName());
assertEquals("?[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("?"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_3() {
String id = "T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_4() {
String id = "T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_5() {
String id = "T[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_6() {
String id = "n.T`1[[G]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T`1[[G]]", sut.getFullName());
assertEquals("T", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("G")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_7() {
String id = "n.T`1[][[G]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T`1[[G]][]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.T`1[[G]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_8() {
String id = "n.T`1[,][[G]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T`1[[G]][,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.T`1[[G]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_9() {
String id = "n.T`1[[G -> p:byte]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T`1[[G -> p:byte]]", sut.getFullName());
assertEquals("T", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("G -> p:byte")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_10() {
String id = "n.T`1[][[G -> p:byte]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T`1[[G -> p:byte]][]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_11() {
String id = "n.T`1[,][[G -> p:byte]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T`1[[G -> p:byte]][,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_12() {
String id = "T";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertFalse(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new TypeName("?"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_13() {
String id = "T[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_14() {
String id = "T[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_15() {
String id = "p:int";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(true, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof PredefinedTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32", sut.getFullName());
assertEquals("int", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertTrue(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertTrue(sut.isSimpleType());
assertTrue(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertTrue(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertTrue(sut.isPredefined());
IPredefinedTypeName sutP = sut.asPredefinedTypeName();
assertEquals(new TypeName("s:System.Int32, mscorlib, ???"), sutP.getFullType());
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_16() {
String id = "p:int[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(true, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof PredefinedTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32[]", sut.getFullName());
assertEquals("int[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new PredefinedTypeName("p:int"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_17() {
String id = "p:int[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(true, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof PredefinedTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32[,]", sut.getFullName());
assertEquals("int[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new PredefinedTypeName("p:int"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_18() {
String id = "A, B, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("B, 1.2.3.4"), sut.getAssembly());
assertEquals("A", sut.getFullName());
assertEquals("A", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_19() {
String id = "A[], B, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("B, 1.2.3.4"), sut.getAssembly());
assertEquals("A[]", sut.getFullName());
assertEquals("A[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("A, B, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_20() {
String id = "A[,], B, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("B, 1.2.3.4"), sut.getAssembly());
assertEquals("A[,]", sut.getFullName());
assertEquals("A[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("A, B, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_21() {
String id = "System.Nullable`1[[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 1.2.3.4"), sut.getAssembly());
assertEquals("System.Nullable`1[[System.Int32, mscorlib, 1.2.3.4]]", sut.getFullName());
assertEquals("Nullable", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("System.Int32, mscorlib, 1.2.3.4")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_22() {
String id = "System.Nullable`1[][[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 1.2.3.4"), sut.getAssembly());
assertEquals("System.Nullable`1[[System.Int32, mscorlib, 1.2.3.4]][]", sut.getFullName());
assertEquals("Nullable[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("System.Nullable`1[[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_23() {
String id = "System.Nullable`1[,][[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 1.2.3.4"), sut.getAssembly());
assertEquals("System.Nullable`1[[System.Int32, mscorlib, 1.2.3.4]][,]", sut.getFullName());
assertEquals("Nullable[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("System.Nullable`1[[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_24() {
String id = "s:S, P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("S", sut.getFullName());
assertEquals("S", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertTrue(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertTrue(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_25() {
String id = "s:S[], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("S[]", sut.getFullName());
assertEquals("S[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("s:S, P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_26() {
String id = "s:S[,], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("S[,]", sut.getFullName());
assertEquals("S[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("s:S, P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_27() {
String id = "T -> ?";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertTrue(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new TypeName("?"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_28() {
String id = "T[] -> ?";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T -> ?"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_29() {
String id = "T[,] -> ?";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T -> ?"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_30() {
String id = "GT`1[[T -> PT, A]], A";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("A"), sut.getAssembly());
assertEquals("GT`1[[T -> PT, A]]", sut.getFullName());
assertEquals("GT", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T -> PT, A")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_31() {
String id = "GT`1[][[T -> PT, A]], A";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("A"), sut.getAssembly());
assertEquals("GT`1[[T -> PT, A]][]", sut.getFullName());
assertEquals("GT[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("GT`1[[T -> PT, A]], A"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_32() {
String id = "GT`1[,][[T -> PT, A]], A";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("A"), sut.getAssembly());
assertEquals("GT`1[[T -> PT, A]][,]", sut.getFullName());
assertEquals("GT[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("GT`1[[T -> PT, A]], A"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_33() {
String id = "d:[RT, A] [DT, A].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("A"), sut.getAssembly());
assertEquals("DT", sut.getFullName());
assertEquals("DT", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("DT, A"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertFalse(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new TypeName("RT, A"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_34() {
String id = "d:[RT, A] [DT, A].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("A"), sut.getAssembly());
assertEquals("DT[]", sut.getFullName());
assertEquals("DT[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[RT, A] [DT, A].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_35() {
String id = "d:[RT, A] [DT, A].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("A"), sut.getAssembly());
assertEquals("DT[,]", sut.getFullName());
assertEquals("DT[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[RT, A] [DT, A].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_36() {
String id = "d:[RT[], A] [DT, A].([PT[], A] p)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("A"), sut.getAssembly());
assertEquals("DT", sut.getFullName());
assertEquals("DT", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("DT, A"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertFalse(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[PT[], A] p")), sutD.getParameters());
assertEquals(new ArrayTypeName("RT[], A"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_37() {
String id = "d:[RT[], A] [DT, A].([PT[], A] p)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("A"), sut.getAssembly());
assertEquals("DT[]", sut.getFullName());
assertEquals("DT[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[RT[], A] [DT, A].([PT[], A] p)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_38() {
String id = "d:[RT[], A] [DT, A].([PT[], A] p)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("A"), sut.getAssembly());
assertEquals("DT[,]", sut.getFullName());
assertEquals("DT[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[RT[], A] [DT, A].([PT[], A] p)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_39() {
String id = "T`1[[T -> d:[TR] [T2, P2].([T] arg)]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[T -> d:[TR] [T2, P2].([T] arg)]]", sut.getFullName());
assertEquals("T", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T -> d:[TR] [T2, P2].([T] arg)")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_40() {
String id = "T`1[][[T -> d:[TR] [T2, P2].([T] arg)]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[T -> d:[TR] [T2, P2].([T] arg)]][]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("T`1[[T -> d:[TR] [T2, P2].([T] arg)]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_41() {
String id = "T`1[,][[T -> d:[TR] [T2, P2].([T] arg)]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[T -> d:[TR] [T2, P2].([T] arg)]][,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("T`1[[T -> d:[TR] [T2, P2].([T] arg)]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_42() {
String id = "n.C1+C2,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C1+C2", sut.getFullName());
assertEquals("C2", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C1, P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_43() {
String id = "n.C1+C2[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C1+C2[]", sut.getFullName());
assertEquals("C2[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.C1+C2,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_44() {
String id = "n.C1+C2[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C1+C2[,]", sut.getFullName());
assertEquals("C2[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.C1+C2,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_45() {
String id = "C1`1[[T1]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1`1[[T1]]", sut.getFullName());
assertEquals("C1", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T1")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_46() {
String id = "C1`1[][[T1]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1`1[[T1]][]", sut.getFullName());
assertEquals("C1[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("C1`1[[T1]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_47() {
String id = "C1`1[,][[T1]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1`1[[T1]][,]", sut.getFullName());
assertEquals("C1[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("C1`1[[T1]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_48() {
String id = "C1+C2`1[[T2]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1+C2`1[[T2]]", sut.getFullName());
assertEquals("C2", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("C1, P"), sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T2")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_49() {
String id = "C1+C2`1[][[T2]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1+C2`1[[T2]][]", sut.getFullName());
assertEquals("C2[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("C1+C2`1[[T2]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_50() {
String id = "C1+C2`1[,][[T2]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1+C2`1[[T2]][,]", sut.getFullName());
assertEquals("C2[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("C1+C2`1[[T2]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_51() {
String id = "C1`1[[T2]]+C2,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1`1[[T2]]+C2", sut.getFullName());
assertEquals("C2", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("C1`1[[T2]], P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_52() {
String id = "C1`1[[T2]]+C2[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1`1[[T2]]+C2[]", sut.getFullName());
assertEquals("C2[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("C1`1[[T2]]+C2,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_53() {
String id = "C1`1[[T2]]+C2[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1`1[[T2]]+C2[,]", sut.getFullName());
assertEquals("C2[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("C1`1[[T2]]+C2,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_54() {
String id = "C1`1[[T1]]+C2`1[[T2]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1`1[[T1]]+C2`1[[T2]]", sut.getFullName());
assertEquals("C2", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("C1`1[[T1]], P"), sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T2")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_55() {
String id = "C1`1[[T1]]+C2`1[][[T2]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1`1[[T1]]+C2`1[[T2]][]", sut.getFullName());
assertEquals("C2[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("C1`1[[T1]]+C2`1[[T2]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_56() {
String id = "C1`1[[T1]]+C2`1[,][[T2]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("C1`1[[T1]]+C2`1[[T2]][,]", sut.getFullName());
assertEquals("C2[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("C1`1[[T1]]+C2`1[[T2]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_57() {
String id = "T -> T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertTrue(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new ArrayTypeName("T[],P"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_58() {
String id = "T[] -> T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T -> T[],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_59() {
String id = "T[,] -> T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T -> T[],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_60() {
String id = "T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[[T8 -> T9[],P]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[[T8 -> T9[],P]]", sut.getFullName());
assertEquals("T7", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]], P"), sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T8 -> T9[],P")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_61() {
String id = "T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[][[T8 -> T9[],P]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[[T8 -> T9[],P]][]", sut.getFullName());
assertEquals("T7[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[[T8 -> T9[],P]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_62() {
String id = "T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[,][[T8 -> T9[],P]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[[T8 -> T9[],P]][,]", sut.getFullName());
assertEquals("T7[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("T1`1[][[T2 -> T3[],P]]+T4`1[][[T5 -> T6[],P]]+T7`1[[T8 -> T9[],P]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_63() {
String id = "d:[?] [?].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertTrue(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?", sut.getFullName());
assertEquals("?", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("?"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertFalse(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new TypeName("?"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_64() {
String id = "d:[?] [?].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[]", sut.getFullName());
assertEquals("?[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[?] [?].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_65() {
String id = "d:[?] [?].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[,]", sut.getFullName());
assertEquals("?[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[?] [?].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_66() {
String id = "d:[T,P] [T,P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("T,P"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T,P] [T,P].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_67() {
String id = "d:[T,P] [T,P].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T,P] [T,P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_68() {
String id = "d:[T,P] [T,P].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T,P] [T,P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_69() {
String id = "d:[R, P] [O+D, P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("O+D", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("O, P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("O+D, P"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertFalse(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new TypeName("R, P"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_70() {
String id = "d:[R, P] [O+D, P].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("O+D[]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[R, P] [O+D, P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_71() {
String id = "d:[R, P] [O+D, P].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("O+D[,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[R, P] [O+D, P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_72() {
String id = "d:[T`1[[T -> n.C+D, P]], P] [n.C+D, P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C, P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("n.C+D, P"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new TypeName("T`1[[T -> d:[T`1[[T -> n.C+D, P]], P] [n.C+D, P].()]], P"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_73() {
String id = "d:[T`1[[T -> n.C+D, P]], P] [n.C+D, P].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T`1[[T -> n.C+D, P]], P] [n.C+D, P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_74() {
String id = "d:[T`1[[T -> n.C+D, P]], P] [n.C+D, P].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T`1[[T -> n.C+D, P]], P] [n.C+D, P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_75() {
String id = "d:[?] [n.C+D, P].([T`1[[T -> n.C+D, P]], P] p)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C, P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("n.C+D, P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[T`1[[T -> d:[?] [n.C+D, P].([T`1[[T -> n.C+D, P]], P] p)]], P] p")), sutD.getParameters());
assertEquals(new TypeName("?"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_76() {
String id = "d:[?] [n.C+D, P].([T`1[[T -> n.C+D, P]], P] p)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[?] [n.C+D, P].([T`1[[T -> n.C+D, P]], P] p)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_77() {
String id = "d:[?] [n.C+D, P].([T`1[[T -> n.C+D, P]], P] p)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[?] [n.C+D, P].([T`1[[T -> n.C+D, P]], P] p)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_78() {
String id = "i:n.T1`1[[T2 -> p:int]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1`1[[T2 -> p:int]]", sut.getFullName());
assertEquals("T1", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertTrue(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T2 -> p:int")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_79() {
String id = "i:n.T1`1[][[T2 -> p:int]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1`1[[T2 -> p:int]][]", sut.getFullName());
assertEquals("T1[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("i:n.T1`1[[T2 -> p:int]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_80() {
String id = "i:n.T1`1[,][[T2 -> p:int]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1`1[[T2 -> p:int]][,]", sut.getFullName());
assertEquals("T1[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("i:n.T1`1[[T2 -> p:int]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_81() {
String id = "n.T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T", sut.getFullName());
assertEquals("T", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_82() {
String id = "n.T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_83() {
String id = "n.T[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_84() {
String id = "s:T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertTrue(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertTrue(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_85() {
String id = "s:T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("s:T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_86() {
String id = "s:T[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("s:T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_87() {
String id = "s:n.T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertTrue(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertTrue(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_88() {
String id = "s:n.T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("s:n.T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_89() {
String id = "s:n.T[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("s:n.T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_90() {
String id = "e:T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertTrue(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertTrue(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_91() {
String id = "e:T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("e:T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_92() {
String id = "e:T[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("e:T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_93() {
String id = "e:n.T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertTrue(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertTrue(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_94() {
String id = "e:n.T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("e:n.T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_95() {
String id = "e:n.T[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("e:n.T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_96() {
String id = "i:T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertTrue(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_97() {
String id = "i:T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("i:T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_98() {
String id = "i:T[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("i:T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_99() {
String id = "i:n.T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertTrue(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_100() {
String id = "i:n.T[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("i:n.T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_101() {
String id = "i:n.T[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("i:n.T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_102() {
String id = "n.T1`1[[T2]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1`1[[T2]]", sut.getFullName());
assertEquals("T1", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T2")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_103() {
String id = "n.T1`1[][[T2]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1`1[[T2]][]", sut.getFullName());
assertEquals("T1[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.T1`1[[T2]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_104() {
String id = "n.T1`1[,][[T2]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1`1[[T2]][,]", sut.getFullName());
assertEquals("T1[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.T1`1[[T2]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_105() {
String id = "n.T1+T2, P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1+T2", sut.getFullName());
assertEquals("T2", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.T1, P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_106() {
String id = "n.T1+T2[], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1+T2[]", sut.getFullName());
assertEquals("T2[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.T1+T2, P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_107() {
String id = "n.T1+T2[,], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1+T2[,]", sut.getFullName());
assertEquals("T2[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.T1+T2, P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_108() {
String id = "n.T1`1[[T2]]+T3`1[[T4]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1`1[[T2]]+T3`1[[T4]]", sut.getFullName());
assertEquals("T3", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.T1`1[[T2]], P"), sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T4")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_109() {
String id = "n.T1`1[[T2]]+T3`1[][[T4]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1`1[[T2]]+T3`1[[T4]][]", sut.getFullName());
assertEquals("T3[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.T1`1[[T2]]+T3`1[[T4]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_110() {
String id = "n.T1`1[[T2]]+T3`1[,][[T4]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.T1`1[[T2]]+T3`1[[T4]][,]", sut.getFullName());
assertEquals("T3[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.T1`1[[T2]]+T3`1[[T4]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_111() {
String id = "n.C+N`1[[T]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+N`1[[T]]", sut.getFullName());
assertEquals("N", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C, P"), sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_112() {
String id = "n.C+N`1[][[T]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+N`1[[T]][]", sut.getFullName());
assertEquals("N[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.C+N`1[[T]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_113() {
String id = "n.C+N`1[,][[T]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+N`1[[T]][,]", sut.getFullName());
assertEquals("N[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.C+N`1[[T]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_114() {
String id = "n.C`1[[T]]+N,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+N", sut.getFullName());
assertEquals("N", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C`1[[T]], P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_115() {
String id = "n.C`1[[T]]+N[],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+N[]", sut.getFullName());
assertEquals("N[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.C`1[[T]]+N,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_116() {
String id = "n.C`1[[T]]+N[,],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+N[,]", sut.getFullName());
assertEquals("N[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.C`1[[T]]+N,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_117() {
String id = "n.C`1[[T]]+N`1[[T]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+N`1[[T]]", sut.getFullName());
assertEquals("N", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C`1[[T]], P"), sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_118() {
String id = "n.C`1[[T]]+N`1[][[T]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+N`1[[T]][]", sut.getFullName());
assertEquals("N[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("n.C`1[[T]]+N`1[[T]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_119() {
String id = "n.C`1[[T]]+N`1[,][[T]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+N`1[[T]][,]", sut.getFullName());
assertEquals("N[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("n.C`1[[T]]+N`1[[T]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_120() {
String id = "s:System.Nullable`1[[T -> p:sbyte]], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("System.Nullable`1[[T -> p:sbyte]]", sut.getFullName());
assertEquals("Nullable", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertTrue(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertTrue(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertTrue(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T -> p:sbyte")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_121() {
String id = "s:System.Nullable`1[][[T -> p:sbyte]], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("System.Nullable`1[[T -> p:sbyte]][]", sut.getFullName());
assertEquals("Nullable[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("s:System.Nullable`1[[T -> p:sbyte]], mscorlib, 4.0.0.0"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_122() {
String id = "s:System.Nullable`1[,][[T -> p:sbyte]], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("System.Nullable`1[[T -> p:sbyte]][,]", sut.getFullName());
assertEquals("Nullable[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("s:System.Nullable`1[[T -> p:sbyte]], mscorlib, 4.0.0.0"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_123() {
String id = "System.X, mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("System.X", sut.getFullName());
assertEquals("X", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_124() {
String id = "System.X[], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("System.X[]", sut.getFullName());
assertEquals("X[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("System.X, mscorlib, 4.0.0.0"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_125() {
String id = "System.X[,], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("System.X[,]", sut.getFullName());
assertEquals("X[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("System.X, mscorlib, 4.0.0.0"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_126() {
String id = "e:Full.Enum.Type, E, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Full.Enum"), sut.getNamespace());
assertEquals(new AssemblyName("E, 1.2.3.4"), sut.getAssembly());
assertEquals("Full.Enum.Type", sut.getFullName());
assertEquals("Type", sut.getName());
assertFalse(sut.isClassType());
assertTrue(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertTrue(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_127() {
String id = "e:Full.Enum.Type[], E, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Full.Enum"), sut.getNamespace());
assertEquals(new AssemblyName("E, 1.2.3.4"), sut.getAssembly());
assertEquals("Full.Enum.Type[]", sut.getFullName());
assertEquals("Type[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("e:Full.Enum.Type, E, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_128() {
String id = "e:Full.Enum.Type[,], E, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Full.Enum"), sut.getNamespace());
assertEquals(new AssemblyName("E, 1.2.3.4"), sut.getAssembly());
assertEquals("Full.Enum.Type[,]", sut.getFullName());
assertEquals("Type[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("e:Full.Enum.Type, E, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_129() {
String id = "i:Full.Interface.Type, E, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Full.Interface"), sut.getNamespace());
assertEquals(new AssemblyName("E, 1.2.3.4"), sut.getAssembly());
assertEquals("Full.Interface.Type", sut.getFullName());
assertEquals("Type", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertTrue(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_130() {
String id = "i:Full.Interface.Type[], E, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Full.Interface"), sut.getNamespace());
assertEquals(new AssemblyName("E, 1.2.3.4"), sut.getAssembly());
assertEquals("Full.Interface.Type[]", sut.getFullName());
assertEquals("Type[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("i:Full.Interface.Type, E, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_131() {
String id = "i:Full.Interface.Type[,], E, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Full.Interface"), sut.getNamespace());
assertEquals(new AssemblyName("E, 1.2.3.4"), sut.getAssembly());
assertEquals("Full.Interface.Type[,]", sut.getFullName());
assertEquals("Type[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("i:Full.Interface.Type, E, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_132() {
String id = "s:Full.Struct.Type, E, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Full.Struct"), sut.getNamespace());
assertEquals(new AssemblyName("E, 1.2.3.4"), sut.getAssembly());
assertEquals("Full.Struct.Type", sut.getFullName());
assertEquals("Type", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertTrue(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertTrue(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_133() {
String id = "s:Full.Struct.Type[], E, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Full.Struct"), sut.getNamespace());
assertEquals(new AssemblyName("E, 1.2.3.4"), sut.getAssembly());
assertEquals("Full.Struct.Type[]", sut.getFullName());
assertEquals("Type[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("s:Full.Struct.Type, E, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_134() {
String id = "s:Full.Struct.Type[,], E, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Full.Struct"), sut.getNamespace());
assertEquals(new AssemblyName("E, 1.2.3.4"), sut.getAssembly());
assertEquals("Full.Struct.Type[,]", sut.getFullName());
assertEquals("Type[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("s:Full.Struct.Type, E, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_135() {
String id = "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]]", sut.getFullName());
assertEquals("Nullable", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("System.Int32, mscorlib, 4.0.0.0")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_136() {
String id = "System.Nullable`1[][[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]][]", sut.getFullName());
assertEquals("Nullable[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_137() {
String id = "System.Nullable`1[,][[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]][,]", sut.getFullName());
assertEquals("Nullable[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_138() {
String id = "Outer.Type+InnerType, As, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Outer"), sut.getNamespace());
assertEquals(new AssemblyName("As, 1.2.3.4"), sut.getAssembly());
assertEquals("Outer.Type+InnerType", sut.getFullName());
assertEquals("InnerType", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("Outer.Type, As, 1.2.3.4"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_139() {
String id = "Outer.Type+InnerType[], As, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Outer"), sut.getNamespace());
assertEquals(new AssemblyName("As, 1.2.3.4"), sut.getAssembly());
assertEquals("Outer.Type+InnerType[]", sut.getFullName());
assertEquals("InnerType[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("Outer.Type+InnerType, As, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_140() {
String id = "Outer.Type+InnerType[,], As, 1.2.3.4";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("Outer"), sut.getNamespace());
assertEquals(new AssemblyName("As, 1.2.3.4"), sut.getAssembly());
assertEquals("Outer.Type+InnerType[,]", sut.getFullName());
assertEquals("InnerType[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("Outer.Type+InnerType, As, 1.2.3.4"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_141() {
String id = "Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(true, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]]", sut.getFullName());
assertEquals("Task", sut.getName());
assertTrue(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_142() {
String id = "Task`1[][[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]][]", sut.getFullName());
assertEquals("Task[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeName("Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_143() {
String id = "Task`1[,][[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
assertEquals("Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]][,]", sut.getFullName());
assertEquals("Task[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeName("Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_144() {
String id = "d:[?] [?].([?] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?", sut.getFullName());
assertEquals("?", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("?"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertFalse(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[?] p1")), sutD.getParameters());
assertEquals(new TypeName("?"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_145() {
String id = "d:[?] [?].([?] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[]", sut.getFullName());
assertEquals("?[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[?] [?].([?] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_146() {
String id = "d:[?] [?].([?] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[,]", sut.getFullName());
assertEquals("?[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[?] [?].([?] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_147() {
String id = "d:[?] [?].([?] p1, [?] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?", sut.getFullName());
assertEquals("?", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("?"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertFalse(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[?] p1"),new ParameterName("[?] p2")), sutD.getParameters());
assertEquals(new TypeName("?"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_148() {
String id = "d:[?] [?].([?] p1, [?] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[]", sut.getFullName());
assertEquals("?[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[?] [?].([?] p1, [?] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_149() {
String id = "d:[?] [?].([?] p1, [?] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[,]", sut.getFullName());
assertEquals("?[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[?] [?].([?] p1, [?] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_150() {
String id = "d:[T] [T].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T] [T].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_151() {
String id = "d:[T] [T].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T] [T].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_152() {
String id = "d:[T] [T].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T] [T].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_153() {
String id = "d:[T] [T].([T] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T] [T].([T] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T] [T].([T] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_154() {
String id = "d:[T] [T].([T] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T] [T].([T] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_155() {
String id = "d:[T] [T].([T] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T] [T].([T] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_156() {
String id = "d:[T] [T].([T] p1, [T] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T] [T].([T] p1, [T] p2)] p1"),new ParameterName("[d:[T] [T].([T] p1, [T] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T] [T].([T] p1, [T] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_157() {
String id = "d:[T] [T].([T] p1, [T] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T] [T].([T] p1, [T] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_158() {
String id = "d:[T] [T].([T] p1, [T] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T] [T].([T] p1, [T] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_159() {
String id = "d:[T -> T,P] [T -> T,P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T -> T,P"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T -> T,P] [T -> T,P].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_160() {
String id = "d:[T -> T,P] [T -> T,P].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T -> T,P] [T -> T,P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_161() {
String id = "d:[T -> T,P] [T -> T,P].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T -> T,P] [T -> T,P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_162() {
String id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T -> T,P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T -> T,P] [T -> T,P].([T -> T,P] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T -> T,P] [T -> T,P].([T -> T,P] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_163() {
String id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T -> T,P] [T -> T,P].([T -> T,P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_164() {
String id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T -> T,P] [T -> T,P].([T -> T,P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_165() {
String id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T -> T,P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)] p1"),new ParameterName("[d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_166() {
String id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_167() {
String id = "d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T -> T,P] [T -> T,P].([T -> T,P] p1, [T -> T,P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_168() {
String id = "d:[T,P] [T,P].([T,P] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("T,P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T,P] [T,P].([T,P] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T,P] [T,P].([T,P] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_169() {
String id = "d:[T,P] [T,P].([T,P] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T,P] [T,P].([T,P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_170() {
String id = "d:[T,P] [T,P].([T,P] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T,P] [T,P].([T,P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_171() {
String id = "d:[T,P] [T,P].([T,P] p1, [T,P] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("T,P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T,P] [T,P].([T,P] p1, [T,P] p2)] p1"),new ParameterName("[d:[T,P] [T,P].([T,P] p1, [T,P] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T,P] [T,P].([T,P] p1, [T,P] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_172() {
String id = "d:[T,P] [T,P].([T,P] p1, [T,P] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T,P] [T,P].([T,P] p1, [T,P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_173() {
String id = "d:[T,P] [T,P].([T,P] p1, [T,P] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T,P] [T,P].([T,P] p1, [T,P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_174() {
String id = "d:[T[],P] [T[],P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new ArrayTypeName("T[],P"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T[],P] [T[],P].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_175() {
String id = "d:[T[],P] [T[],P].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[][]", sut.getFullName());
assertEquals("T[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[],P] [T[],P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_176() {
String id = "d:[T[],P] [T[],P].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[][,]", sut.getFullName());
assertEquals("T[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[],P] [T[],P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_177() {
String id = "d:[T[],P] [T[],P].([T[],P] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new ArrayTypeName("T[],P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T[],P] [T[],P].([T[],P] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T[],P] [T[],P].([T[],P] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_178() {
String id = "d:[T[],P] [T[],P].([T[],P] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[][]", sut.getFullName());
assertEquals("T[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[],P] [T[],P].([T[],P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_179() {
String id = "d:[T[],P] [T[],P].([T[],P] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[][,]", sut.getFullName());
assertEquals("T[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[],P] [T[],P].([T[],P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_180() {
String id = "d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new ArrayTypeName("T[],P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)] p1"),new ParameterName("[d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_181() {
String id = "d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[][]", sut.getFullName());
assertEquals("T[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_182() {
String id = "d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T[][,]", sut.getFullName());
assertEquals("T[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[],P] [T[],P].([T[],P] p1, [T[],P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_183() {
String id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C, P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new DelegateTypeName("d:[?] [n.C+D, P].()"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_184() {
String id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_185() {
String id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_186() {
String id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C, P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new DelegateTypeName("d:[?] [n.C+D, P].()"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_187() {
String id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_188() {
String id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_189() {
String id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C, P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new DelegateTypeName("d:[?] [n.C+D, P].()"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)] p1"),new ParameterName("[d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_190() {
String id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_191() {
String id = "d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D[,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [n.C+D, P].()] [d:[?] [n.C+D, P].()].([d:[?] [n.C+D, P].()] p1, [d:[?] [n.C+D, P].()] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_192() {
String id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[P -> T2,P]]", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("P -> T2,P")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("T`1[[P -> T2,P]],P"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_193() {
String id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[P -> T2,P]][]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_194() {
String id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[P -> T2,P]][,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_195() {
String id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[P -> T2,P]]", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("P -> T2,P")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("T`1[[P -> T2,P]],P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_196() {
String id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[P -> T2,P]][]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_197() {
String id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[P -> T2,P]][,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_198() {
String id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[P -> T2,P]]", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("P -> T2,P")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("T`1[[P -> T2,P]],P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)] p1"),new ParameterName("[d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_199() {
String id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[P -> T2,P]][]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_200() {
String id = "d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName(""), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("T`1[[P -> T2,P]][,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T`1[[P -> T2,P]],P] [T`1[[P -> T2,P]],P].([T`1[[P -> T2,P]],P] p1, [T`1[[P -> T2,P]],P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_201() {
String id = "d:[T[]] [T[]].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T[]"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T[]] [T[]].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_202() {
String id = "d:[T[]] [T[]].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][]", sut.getFullName());
assertEquals("T[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[]] [T[]].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_203() {
String id = "d:[T[]] [T[]].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][,]", sut.getFullName());
assertEquals("T[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[]] [T[]].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_204() {
String id = "d:[T[]] [T[]].([T[]] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T[]"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T[]] [T[]].([T[]] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T[]] [T[]].([T[]] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_205() {
String id = "d:[T[]] [T[]].([T[]] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][]", sut.getFullName());
assertEquals("T[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[]] [T[]].([T[]] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_206() {
String id = "d:[T[]] [T[]].([T[]] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][,]", sut.getFullName());
assertEquals("T[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[]] [T[]].([T[]] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_207() {
String id = "d:[T[]] [T[]].([T[]] p1, [T[]] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T[]"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T[]] [T[]].([T[]] p1, [T[]] p2)] p1"),new ParameterName("[d:[T[]] [T[]].([T[]] p1, [T[]] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T[]] [T[]].([T[]] p1, [T[]] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_208() {
String id = "d:[T[]] [T[]].([T[]] p1, [T[]] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][]", sut.getFullName());
assertEquals("T[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[]] [T[]].([T[]] p1, [T[]] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_209() {
String id = "d:[T[]] [T[]].([T[]] p1, [T[]] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][,]", sut.getFullName());
assertEquals("T[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[]] [T[]].([T[]] p1, [T[]] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_210() {
String id = "d:[T[] -> T,P] [T[] -> T,P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T[] -> T,P"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T[] -> T,P] [T[] -> T,P].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_211() {
String id = "d:[T[] -> T,P] [T[] -> T,P].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][]", sut.getFullName());
assertEquals("T[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[] -> T,P] [T[] -> T,P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_212() {
String id = "d:[T[] -> T,P] [T[] -> T,P].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][,]", sut.getFullName());
assertEquals("T[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[] -> T,P] [T[] -> T,P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_213() {
String id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T[] -> T,P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_214() {
String id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][]", sut.getFullName());
assertEquals("T[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_215() {
String id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][,]", sut.getFullName());
assertEquals("T[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_216() {
String id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeParameterName("T[] -> T,P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)] p1"),new ParameterName("[d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_217() {
String id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][]", sut.getFullName());
assertEquals("T[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_218() {
String id = "d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[][,]", sut.getFullName());
assertEquals("T[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[T[] -> T,P] [T[] -> T,P].([T[] -> T,P] p1, [T[] -> T,P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_219() {
String id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[]", sut.getFullName());
assertEquals("?[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new ArrayTypeName("d:[?] [?].()[]"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[d:[?] [?].()[]] [d:[?] [?].()[]].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_220() {
String id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[][]", sut.getFullName());
assertEquals("?[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [?].()[]] [d:[?] [?].()[]].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_221() {
String id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[][,]", sut.getFullName());
assertEquals("?[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [?].()[]] [d:[?] [?].()[]].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_222() {
String id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[]", sut.getFullName());
assertEquals("?[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new ArrayTypeName("d:[?] [?].()[]"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_223() {
String id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[][]", sut.getFullName());
assertEquals("?[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_224() {
String id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[][,]", sut.getFullName());
assertEquals("?[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_225() {
String id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[]", sut.getFullName());
assertEquals("?[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new ArrayTypeName("d:[?] [?].()[]"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)] p1"),new ParameterName("[d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_226() {
String id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[][]", sut.getFullName());
assertEquals("?[][]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_227() {
String id = "d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("?[][,]", sut.getFullName());
assertEquals("?[][,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[d:[?] [?].()[]] [d:[?] [?].()[]].([d:[?] [?].()[]] p1, [d:[?] [?].()[]] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_228() {
String id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D`1[[T]]", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C, P"), sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("n.C+D`1[[T]], P"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_229() {
String id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D`1[[T]][]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_230() {
String id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D`1[[T]][,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_231() {
String id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D`1[[T]]", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C, P"), sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("n.C+D`1[[T]], P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_232() {
String id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D`1[[T]][]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_233() {
String id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D`1[[T]][,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_234() {
String id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D`1[[T]]", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C, P"), sut.getDeclaringType());
assertTrue(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(new TypeParameterName("T")), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("n.C+D`1[[T]], P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)] p1"),new ParameterName("[d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_235() {
String id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D`1[[T]][]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_236() {
String id = "d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C+D`1[[T]][,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C+D`1[[T]], P] [n.C+D`1[[T]], P].([n.C+D`1[[T]], P] p1, [n.C+D`1[[T]], P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_237() {
String id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+D", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C`1[[T]], P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("n.C`1[[T]]+D, P"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_238() {
String id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+D[]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_239() {
String id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+D[,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_240() {
String id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+D", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C`1[[T]], P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("n.C`1[[T]]+D, P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_241() {
String id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+D[]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_242() {
String id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+D[,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_243() {
String id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+D", sut.getFullName());
assertEquals("D", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertTrue(sut.isNestedType());
assertEquals(new TypeName("n.C`1[[T]], P"), sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new TypeName("n.C`1[[T]]+D, P"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)] p1"),new ParameterName("[d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_244() {
String id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+D[]", sut.getFullName());
assertEquals("D[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_245() {
String id = "d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("n"), sut.getNamespace());
assertEquals(new AssemblyName("P"), sut.getAssembly());
assertEquals("n.C`1[[T]]+D[,]", sut.getFullName());
assertEquals("D[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[n.C`1[[T]]+D, P] [n.C`1[[T]]+D, P].([n.C`1[[T]]+D, P] p1, [n.C`1[[T]]+D, P] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_246() {
String id = "d:[p:int] [p:int].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32", sut.getFullName());
assertEquals("int", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new PredefinedTypeName("p:int"), sutD.getDelegateType());
assertFalse(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[p:int] [p:int].()"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_247() {
String id = "d:[p:int] [p:int].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32[]", sut.getFullName());
assertEquals("int[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[p:int] [p:int].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_248() {
String id = "d:[p:int] [p:int].()[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32[,]", sut.getFullName());
assertEquals("int[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[p:int] [p:int].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_249() {
String id = "d:[p:int] [p:int].([p:int] p1)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32", sut.getFullName());
assertEquals("int", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new PredefinedTypeName("p:int"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[p:int] [p:int].([p:int] p1)] p1")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[p:int] [p:int].([p:int] p1)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_250() {
String id = "d:[p:int] [p:int].([p:int] p1)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32[]", sut.getFullName());
assertEquals("int[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[p:int] [p:int].([p:int] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_251() {
String id = "d:[p:int] [p:int].([p:int] p1)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32[,]", sut.getFullName());
assertEquals("int[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[p:int] [p:int].([p:int] p1)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_252() {
String id = "d:[p:int] [p:int].([p:int] p1, [p:int] p2)";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof DelegateTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32", sut.getFullName());
assertEquals("int", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertTrue(sut.isDelegateType());
IDelegateTypeName sutD = sut.asDelegateTypeName();
assertEquals(new PredefinedTypeName("p:int"), sutD.getDelegateType());
assertTrue(sutD.hasParameters());
assertTrue(sutD.isRecursive());
assertEquals(Lists.newArrayList(new ParameterName("[d:[p:int] [p:int].([p:int] p1, [p:int] p2)] p1"),new ParameterName("[d:[p:int] [p:int].([p:int] p1, [p:int] p2)] p2")), sutD.getParameters());
assertEquals(new DelegateTypeName("d:[p:int] [p:int].([p:int] p1, [p:int] p2)"), sutD.getReturnType());
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_253() {
String id = "d:[p:int] [p:int].([p:int] p1, [p:int] p2)[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32[]", sut.getFullName());
assertEquals("int[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[p:int] [p:int].([p:int] p1, [p:int] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_254() {
String id = "d:[p:int] [p:int].([p:int] p1, [p:int] p2)[,]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof ArrayTypeName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("System"), sut.getNamespace());
assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
assertEquals("System.Int32[,]", sut.getFullName());
assertEquals("int[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new DelegateTypeName("d:[p:int] [p:int].([p:int] p1, [p:int] p2)"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_255() {
String id = "T -> T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertTrue(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new TypeName("T,P"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_256() {
String id = "T[] -> T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T -> T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_257() {
String id = "T[,] -> T,P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T -> T,P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_258() {
String id = "T -> d:[?] [n.C+D, P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertTrue(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new DelegateTypeName("d:[?] [n.C+D, P].()"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_259() {
String id = "T[] -> d:[?] [n.C+D, P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T -> d:[?] [n.C+D, P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_260() {
String id = "T[,] -> d:[?] [n.C+D, P].()";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T -> d:[?] [n.C+D, P].()"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_261() {
String id = "T -> T`1[[P -> T2,P]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertTrue(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new TypeName("T`1[[P -> T2,P]],P"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_262() {
String id = "T[] -> T`1[[P -> T2,P]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T -> T`1[[P -> T2,P]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_263() {
String id = "T[,] -> T`1[[P -> T2,P]],P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T -> T`1[[P -> T2,P]],P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_264() {
String id = "T -> d:[?] [?].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertTrue(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new ArrayTypeName("d:[?] [?].()[]"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_265() {
String id = "T[] -> d:[?] [?].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T -> d:[?] [?].()[]"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_266() {
String id = "T[,] -> d:[?] [?].()[]";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T -> d:[?] [?].()[]"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_267() {
String id = "T -> n.C+D`1[[T]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertTrue(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new TypeName("n.C+D`1[[T]], P"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_268() {
String id = "T[] -> n.C+D`1[[T]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T -> n.C+D`1[[T]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_269() {
String id = "T[,] -> n.C+D`1[[T]], P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T -> n.C+D`1[[T]], P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_270() {
String id = "T -> n.C`1[[T]]+D, P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertTrue(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new TypeName("n.C`1[[T]]+D, P"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_271() {
String id = "T[] -> n.C`1[[T]]+D, P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T -> n.C`1[[T]]+D, P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_272() {
String id = "T[,] -> n.C`1[[T]]+D, P";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T -> n.C`1[[T]]+D, P"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_273() {
String id = "T -> p:int";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T", sut.getFullName());
assertEquals("T", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertFalse(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertTrue(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertFalse(sut.isArray());
hasThrown = false;
try { sut.asArrayTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertTrue(sut.isTypeParameter());
ITypeParameterName sutT = sut.asTypeParameterName();
assertTrue(sutT.isBound());
assertEquals("T", sutT.getTypeParameterShortName());
assertEquals(new PredefinedTypeName("p:int"), sutT.getTypeParameterType());
}
@Test
public void TypeTest_274() {
String id = "T[] -> p:int";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[]", sut.getFullName());
assertEquals("T[]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(1, sutArr.getRank());
assertEquals(new TypeParameterName("T -> p:int"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
@Test
public void TypeTest_275() {
String id = "T[,] -> p:int";
assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
assertEquals(false, TypeName.isTypeNameIdentifier(id));
assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
ITypeName sut = TypeUtils.createTypeName(id);
assertTrue(sut instanceof TypeParameterName);
assertFalse(sut.isHashed());
assertFalse(sut.isUnknown());
assertEquals(new NamespaceName("???"), sut.getNamespace());
assertEquals(new AssemblyName("???"), sut.getAssembly());
assertEquals("T[,]", sut.getFullName());
assertEquals("T[,]", sut.getName());
assertFalse(sut.isClassType());
assertFalse(sut.isEnumType());
assertFalse(sut.isInterfaceType());
assertFalse(sut.isNullableType());
assertFalse(sut.isPredefined());
assertTrue(sut.isReferenceType());
assertFalse(sut.isSimpleType());
assertFalse(sut.isStructType());
assertFalse(sut.isTypeParameter());
assertFalse(sut.isValueType());
assertFalse(sut.isVoidType());
assertFalse(sut.isNestedType());
assertNull(sut.getDeclaringType());
assertFalse(sut.hasTypeParameters());
assertEquals(Lists.newArrayList(), sut.getTypeParameters());
boolean hasThrown;
// array
assertTrue(sut.isArray());
IArrayTypeName sutArr = sut.asArrayTypeName();
assertEquals(2, sutArr.getRank());
assertEquals(new TypeParameterName("T -> p:int"), sutArr.getArrayBaseType());
// delegates
assertFalse(sut.isDelegateType());
hasThrown = false;
try { sut.asDelegateTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// predefined
assertFalse(sut.isPredefined());
hasThrown = false;
try { sut.asPredefinedTypeName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
// type parameters
assertFalse(sut.isTypeParameter());
hasThrown = false;
try { sut.asTypeParameterName(); } catch(AssertionException e) {
hasThrown = true;
}
assertTrue(hasThrown);
}
}

