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
package cc.recommenders.mining.calls;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.DictionaryBuilder;
import cc.recommenders.mining.features.FeatureExtractor;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;


public class DictionaryBuilderTest {

    private DictionaryBuilder<String, String> sut;
    private FeatureExtractor<String, String> extractor;
    private List<String> usages;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        extractor = mock(FeatureExtractor.class);
        when(extractor.extract(eq("1:2"))).thenReturn(newArrayList("1", "2"));
        when(extractor.extract(eq("a:b"))).thenReturn(newArrayList("a", "b"));
        when(extractor.extract(eq("a:b:c"))).thenReturn(newArrayList("a", "b", "c"));

        sut = new DictionaryBuilder<String, String>(extractor);
    }

    @Test
    public void usagesAreDelegatedToExtractor() {
        usages = newArrayList("1:2", "a:b");
        Dictionary<String> actual = sut.newDictionary(usages);
        Dictionary<String> expected = createDictionary("1", "2", "a", "b");
        
        assertEquals(expected, actual);
        verify(extractor).extract("1:2");
        verify(extractor).extract("a:b");
    }

    @Test
    public void predicateFiltersResult() {
        usages = newArrayList("1:2", "a:b");
        Dictionary<String> actual = sut.newDictionary(usages, noA());
        Dictionary<String> expected = createDictionary("1", "2", "b");
        
        assertEquals(expected, actual);

        verify(extractor).extract("1:2");
        verify(extractor).extract("a:b");
    }

    public void asd() {
        usages = newArrayList("1:2", "a:b", "1");
        Dictionary<String> actual = sut.newDictionary(usages);
        Dictionary<String> expected = createDictionary("1", "2", "a", "b");
        assertEquals(expected, actual);
    }

    private Dictionary<String> createDictionary(String... values) {
        Dictionary<String> dictionary = new Dictionary<String>();
        for (String value : values) {
            dictionary.add(value);
        }
        return dictionary;
    }
    
    private static Predicate<String> noA() {
    	return new Predicate<String>() {
			@Override
			public boolean apply(String s) {
				return !"a".equals(s);
			}
		};
    }
}
