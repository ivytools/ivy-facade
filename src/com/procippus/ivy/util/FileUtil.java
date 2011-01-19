package com.procippus.ivy.util;

/*
 * 
 * Copyright 2011 Procippus, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static java.lang.System.out;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.procippus.ivy.model.IvyDependency;
import com.procippus.ivy.model.IvyFile;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class FileUtil {
	public static List<IvyFile> ivyFiles = new ArrayList<IvyFile>();
	
	private static String[] ignoreFiles = null;
	private static String ivyMatchPattern = null;
	private static String ignoreDirectory = null;
	
	public static File ivyStylesheet;
	public static File dirStylesheet;
	public static File homeStylesheet;
	public static File commonStylesheet;
	
	private static final String COMMA = ",";
	public static void init() {
		ignoreDirectory = PropertiesUtil.getValue("files.ignore.directory");
		ignoreFiles = PropertiesUtil.getValue(PropertiesUtil.KEY_FIL_IGNORE).split(COMMA);
		ivyMatchPattern = PropertiesUtil.getValue(PropertiesUtil.KEY_IVY_PATTERNS);
		
		ivyStylesheet = XMLUtil.prepareXSL(PropertiesUtil.getValue(PropertiesUtil.KEY_XSL_IVY));
		dirStylesheet = XMLUtil.prepareXSL(PropertiesUtil.getValue(PropertiesUtil.KEY_XSL_DIR));
		homeStylesheet = XMLUtil.prepareXSL(PropertiesUtil.getValue(PropertiesUtil.KEY_XSL_HOME));
		commonStylesheet = XMLUtil.prepareXSL(PropertiesUtil.getValue(PropertiesUtil.KEY_XSL_COMMON));
	}
	
	public static boolean validateIvyRoot(String ivyRoot) {
		File ivyRootDirectory = new File(ivyRoot);
		if (!ivyRootDirectory.exists() || !ivyRootDirectory.isDirectory() || !ivyRootDirectory.canRead()) {
			out.println("Unable to read ivy root: " + ivyRoot);
			return false;
		}
		return true;
	}
	
	public static void readDirectoryStructure(File directory) {
		if (ignoreFiles == null || ivyMatchPattern == null) {
			throw(new RuntimeException("Please initialize the FileUtil class before using."));
		} else {
			List<File> directories = new ArrayList<File>();
			Boolean hasFiles = Boolean.FALSE;
			
			if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				for (File f : files) {
					if (f.isDirectory()) {
						if (ignoreDirectory.indexOf(f.getName()) < 0)
							directories.add(f);
					} else {
						Boolean ignore = Boolean.FALSE;
						for (String i : ignoreFiles) {
							if (f.getName().contains(i)) {
								ignore = Boolean.TRUE;
							}
						}
						if (!ignore) hasFiles = Boolean.TRUE; 
						if (f.getName().matches(ivyMatchPattern)) {
							IvyFile ivyf = XMLUtil.parseIvyFile(f);
							if (!ivyFiles.contains(ivyf)) ivyFiles.add(ivyf);
						}
					}
				}
			}
			if (!hasFiles) {
				HTMLUtil.buildDirectoryHtml(directory, directories);
			}
			for (File d : directories) {
				readDirectoryStructure(d);
			}
		}
	}
	
	public static void createDependentGraph() {
		for (IvyFile ivyF : FileUtil.ivyFiles) {
			for (IvyDependency ivyDep : ivyF.getDependencies()) {
				IvyFile f = new IvyFile();
				f.setOrganization(ivyDep.getOrganization());
				f.setModule(ivyDep.getName());
				f.setRevision(ivyDep.getRevision());
				for (IvyFile innerFile : FileUtil.ivyFiles) {
					if (innerFile.equals(f)) {
						innerFile.addDependent(ivyF);
					}
				}
			}
		}
	}
	
	public static void writeIvyHtmlFiles() {
		for (IvyFile ivyFile : ivyFiles) {
			HTMLUtil.buildIvyHtml(ivyStylesheet, ivyFile);
		}
	}
}
