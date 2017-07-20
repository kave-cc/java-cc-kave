/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.utils.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.utils.io.json.JsonUtils;

public class NestedZipFolders<T> {

	private final Directory root;
	private Class<T> classOfMetaData;

	public NestedZipFolders(Directory root, Class<T> classOfMetaData) {
		this.root = root;
		this.classOfMetaData = classOfMetaData;
	}

	public URL getUrl() throws MalformedURLException {
		return root.getUrl();
	}

	public Set<T> findKeys() {
		Set<T> keys = Sets.newHashSet();
		Set<String> markerFiles = root.findFiles(relFileName -> relFileName.endsWith(".zipfolder"));
		for (String markerFile : markerFiles) {
			keys.add(readMetaData(markerFile));
		}
		return keys;
	}

	private T readMetaData(String file) {
		try {
			return root.read(file, classOfMetaData);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean hasZips(T key) {
		if (isUnknown(key)) {
			return false;
		}

		Directory zipFolder = getZipFolder(key);
		Set<String> zips = findZipsIn(zipFolder);
		return !zips.isEmpty();
	}

	private boolean isUnknown(T key) {
		String markerName = getMarkerName(key);
		return !root.exists(markerName);
	}

	private Set<String> findZipsIn(Directory zipFolder) {
		return zipFolder.list(f -> f.endsWith(".zip"));
	}

	private Directory getZipFolder(T key) {
		String file = getMarkerName(key);
		Directory typeDir = root.getParentDirectory(file);
		return typeDir;
	}

	private String getMarkerName(T key) {
		String file = JsonUtils.toJson(key);
		file = file.replaceAll("\\\"", "\""); // quotes inside json
		file = file.replaceAll("\"", ""); // surrounding quotes
		file = file.replaceAll("\\.", "/"); // surrounding quotes
		file = file.replaceAll("[^a-zA-Z0-9\\[\\](){}\\\\/,-_+$]", "_");
		file = file + "/.zipfolder";
		return file;
	}

	public <V> List<V> readAllZips(T key, Class<V> classOfV) {
		if (isUnknown(key)) {
			return Lists.newLinkedList();
		}

		List<V> values = Lists.newLinkedList();
		Directory zipFolder = getZipFolder(key);
		Set<String> zips = findZipsIn(zipFolder);
		for (String zip : zips) {
			try {
				IReadingArchive ra = zipFolder.getReadingArchive(zip);
				while (ra.hasNext()) {
					values.add(ra.getNext(classOfV));
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return values;
	}
}