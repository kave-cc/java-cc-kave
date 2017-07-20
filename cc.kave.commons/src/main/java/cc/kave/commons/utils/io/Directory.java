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
package cc.kave.commons.utils.io;

import static com.google.common.base.Predicates.alwaysTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.utils.io.json.JsonUtils;

public class Directory {

	private final String rootDir;

	public Directory(String rootDir) {
		String pathSep = Character.toString(File.separatorChar);
		if (rootDir.endsWith(pathSep)) {
			this.rootDir = rootDir;
		} else {
			this.rootDir = rootDir + pathSep;
		}

		File rootFile = new File(rootDir);
		Asserts.assertFalse(rootFile.isFile(), "unable to create directory (is file)");
		if (!rootFile.isDirectory()) {
			rootFile.mkdirs();
			if (!rootFile.isDirectory()) {
				Asserts.fail("unable to create directory");
			}
		}
	}

	public <T> T read(String relativePath, Type classOfT) throws IOException {

		File file = new File(rootDir, relativePath);
		T obj = JsonUtils.fromJson(file, classOfT);

		return obj;
	}

	public <T> void write(T obj, String relativePath) throws IOException {

		File file = new File(rootDir, relativePath);
		JsonUtils.toJson(obj, file);
	}

	public String readContent(String relativePath) throws IOException {
		File file = new File(rootDir, relativePath);
		return FileUtils.readFileToString(file);
	}

	public void writeContent(String content, String relativePath) throws IOException {
		File file = new File(rootDir, relativePath);
		FileUtils.writeStringToFile(file, content);
	}

	public boolean exists(String relativePath) {
		File file = new File(rootDir, relativePath);
		return file.exists();
	}

	public Directory createDirectory(String relativePath) {
		String folderName = rootDir + relativePath;
		File folder = new File(folderName);
		folder.mkdirs();
		return new Directory(folderName);
	}

	public void clear() {

		File root = new File(rootDir);

		if (root.exists() && root.isDirectory()) {
			for (File file : root.listFiles()) {
				delete(file);
			}
		}

	}

	private void delete(File file) {
		if (file.isDirectory()) {
			for (File sub : file.listFiles()) {
				delete(sub);
			}
		}
		file.delete();
	}

	public int count() {
		return new File(rootDir).list().length;
	}

	public WritingArchive getWritingArchive(String relativePath) throws IOException {
		File file = new File(rootDir, relativePath);
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		WritingArchive archive = new WritingArchive(file);

		return archive;
	}

	public WritingArchive reopenWritingArchive(String fileName, Type classOfT) throws IOException {
		String tmpFileName = createTempFile(fileName);
		tmpFileName = tmpFileName.replace('-', '_');
		if (!exists(fileName) || exists(tmpFileName)) {
			throw new IllegalArgumentException("file does not exists or name collision of tmpfile");
		}

		File old = new File(rootDir, fileName);
		File tmp = new File(rootDir, tmpFileName);
		old.renameTo(tmp);

		IReadingArchive oldArchive = getReadingArchive(tmpFileName);
		WritingArchive newArchive = getWritingArchive(fileName);

		while (oldArchive.hasNext()) {
			Object o = oldArchive.getNext(classOfT);
			newArchive.add(o);
		}
		oldArchive.close();
		tmp.delete();

		return newArchive;
	}

	private String createTempFile(String fileName) {
		return fileName + fileName.hashCode();
	}

	public ReadingArchive getReadingArchive(String relativePath) throws IOException {

		File file = new File(rootDir, relativePath);
		ReadingArchive archive = new ReadingArchive(file);
		return archive;
	}

	public void delete(String relativePath) {
		File file = new File(rootDir, relativePath);
		file.delete();
	}

	public Set<String> list() {
		Predicate<String> allFiles = alwaysTrue();
		return list(allFiles);
	}

	public Set<String> list(Predicate<String> predicate) {
		// TODO create test case for ordering
		Set<String> files = new LinkedHashSet<String>();
		for (String file : new File(rootDir).list()) {
			if (predicate.apply(file)) {
				files.add(file);
			}
		}
		return files;
	}

	public URL getUrl() throws MalformedURLException {
		return new File(rootDir).toURI().toURL();
	}

	public URL getUrl(String fileName) throws MalformedURLException {
		return Paths.get(rootDir, fileName).toUri().toURL();
	}

	public static String createFileName(String s) {
		return s.trim().replaceAll("[^a-zA-Z0-9_.-]+", "_");
	}

	public Set<String> findFiles(Predicate<String> predicate) {
		IOFileFilter fileFilter = new AbstractFileFilter() {
			@Override
			public boolean accept(File file) {
				return predicate.apply(file.getAbsolutePath());
			}
		};
		IOFileFilter allDirs = FileFilterUtils.trueFileFilter();
		Iterator<File> it = FileUtils.iterateFiles(new File(rootDir), fileFilter, allDirs);

		int rootPrefixLength = new File(rootDir).getAbsolutePath().length(); // catch relative paths
		
		Set<String> files = Sets.newLinkedHashSet();
		while (it.hasNext()) {
			String absPath = it.next().getAbsolutePath();
			String relPath = absPath.substring(rootPrefixLength); // TODO -1?
			if (relPath.startsWith(File.separator)) {
				relPath = relPath.substring(1);
			}
			files.add(relPath);
		}

		return files;
	}

	public Directory getParentDirectory(String relativeFileName) {
		File f = new File(rootDir, relativeFileName);
		Asserts.assertTrue(f.exists());
		Asserts.assertFalse(f.getAbsolutePath().contains(".."));
		return new Directory(f.getParent());
	}
}