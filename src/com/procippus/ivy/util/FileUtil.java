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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.procippus.ivy.model.Dependency;
import com.procippus.ivy.model.Info;
import com.procippus.ivy.model.Module;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class FileUtil {
	public static List<Module> modules = new ArrayList<Module>();
	public static List<Module> missingDependencies = new ArrayList<Module>();
	
	public static Map<File, List<File>> allDirectories = new HashMap<File, List<File>>();
	
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
			
			//Assume the directory is empty until proven otherwise
			Boolean hasFiles = Boolean.FALSE;
			
			if (directory.isDirectory()) {
				//Get a list of files in the directory
				File[] files = directory.listFiles();
				
				//Iterate over the files
				for (File f : files) {
					//If the file is a directory, and not ignored recurse into directory
					if (f.isDirectory()) {
						if (ignoreDirectory.indexOf(f.getName()) < 0)
							directories.add(f);
						
					//Otherwise determine if the file should be ignored
					} else {
						Boolean ignore = Boolean.FALSE;
						for (String i : ignoreFiles) {
							if (f.getName().contains(i)) {
								ignore = Boolean.TRUE;
							}
						}
						//If the file should not be ignored, then tell the 
						//parser the directory has files
						if (!ignore) hasFiles = Boolean.TRUE;
						
						//If the file is an Ivy File, parse the file and add it
						//to the ivyFile array
						if (f.getName().matches(ivyMatchPattern)) {
							Module module = XMLUtil.parseIvyFile(f);
							if (!modules.contains(module)) modules.add(module);
						}
					}
				}
			}
			//If the directory does not have files, create index.html based on the directory.xsl
			if (!hasFiles) {
				allDirectories.put(directory, directories);
			}
			
			//Recurse into directory structure
			for (File d : directories) {
				readDirectoryStructure(d);
			}
		}
	}
	
	public static void createDependentGraph() {
		//Validate that the FileUtil has been initialized and read
		if (FileUtil.modules != null && FileUtil.modules.size()>0) {
			//Iterate over the ivyFiles
			for (Module module : FileUtil.modules) {
				List<Dependency> dependencies = module.getDependencyList().getDependencies();
				Boolean isMissingDeps = Boolean.FALSE;
				for (Dependency dep : dependencies) {
					String filePath = (HTMLUtil.ivyRoot + File.separator + dep.getPath());
					File f = new File(filePath);
					if (!f.exists()) {
						dep.setMissing(Boolean.TRUE);
						isMissingDeps = Boolean.TRUE;
					}
					
					for (Module innerModule : FileUtil.modules) {
						Info info = innerModule.getInfo();
						if (dep.isEqualToInfo(info)) {
							innerModule.getDependentList().addDependency(new Dependency(module));
						}
					}
				}
				if (isMissingDeps) {
					missingDependencies.add(module);
				}
			}
		}
	}
	
	public static void writeIvyHtmlFiles() {
		for (Module module : modules) {
			HTMLUtil.buildIvyHtml(ivyStylesheet, module);
		}
	}
	
	public static void writeDirectoryFiles() {
		for (File key : allDirectories.keySet()) {
			HTMLUtil.buildDirectoryHtml(key, allDirectories.get(key));
		}
	}
}
