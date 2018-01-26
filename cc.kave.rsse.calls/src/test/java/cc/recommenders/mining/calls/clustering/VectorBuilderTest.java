/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.calls.clustering;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.junit.Before;
import org.junit.Test;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.clustering.FeatureWeighter;
import cc.recommenders.mining.calls.clustering.VectorBuilder;

public class VectorBuilderTest {

    private FeatureWeighter<String> weighter;
    private Dictionary<String> dictionary;

    private List<List<String>> features;
    private VectorBuilder<String> sut;
    private List<Vector> vectors;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        weighter = mock(FeatureWeighter.class);
        when(weighter.getWeight("1")).thenReturn(0.5);
        when(weighter.getWeight("2")).thenReturn(0.5);
        when(weighter.getWeight("a")).thenReturn(1.0);
        when(weighter.getWeight("b")).thenReturn(1.0);

        dictionary = mock(Dictionary.class);
        when(dictionary.size()).thenReturn(4);
        when(dictionary.getId(anyString())).thenReturn(-1);
        when(dictionary.getId("a")).thenReturn(0);
        when(dictionary.getId("1")).thenReturn(1);
        when(dictionary.getId("b")).thenReturn(2);
        when(dictionary.getId("2")).thenReturn(3);

        sut = new VectorBuilder<String>(weighter);
    }

    @Test
    public void allFeaturesAreWeighted() {
        features = createFeatures("a:1");
        sut.build(features, dictionary);

        verify(weighter).getWeight("a");
        verify(weighter).getWeight("1");
    }

    @Test
    public void featuresThatDoNotExistInTheDictionaryAreNotWeighted() {
        features = createFeatures("a:1","c:2");
        sut.build(features, dictionary);

        verify(weighter).getWeight("a");
        verify(weighter).getWeight("1");
        verify(weighter).getWeight("2");
        verifyNoMoreInteractions(weighter);
    }

    @Test
    public void allExpectedVectorsAreCreated() {
        features = createFeatures("a:1", "b:2");
        vectors = sut.build(features, dictionary);

        assertEquals(2, vectors.size());
    }

    @Test
    public void resultingVectorsAreCorrect() {
        features = createFeatures("a:1", "b:2", "a:2");
        vectors = sut.build(features, dictionary);

        List<Vector> expected = newArrayList();
        expected.add(createVector(1.0, 0.5, 0.0, 0.0));
        expected.add(createVector(0.0, 0.0, 1.0, 0.5));
        expected.add(createVector(1.0, 0.0, 0.0, 0.5));

        assertEquals(expected, vectors);
    }

    private static List<List<String>> createFeatures(String... usages) {
        List<List<String>> allFeatures = newArrayList();

        for (String usage : usages) {
            List<String> features = newArrayList(usage.split(":"));
            allFeatures.add(features);
        }

        return allFeatures;
    }

    private Vector createVector(double... values) {
        Vector v = new RandomAccessSparseVector(4);
        for (int i = 0; i < values.length; i++) {
            v.set(i, values[i]);
        }
        return v;
    }
}