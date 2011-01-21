package com.procippus.ivy;

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

import com.procippus.ivy.util.AssetsUtil;
import com.procippus.ivy.util.FileUtil;
import com.procippus.ivy.util.HTMLUtil;
import com.procippus.ivy.util.PropertiesUtil;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class HtmlBuilder {
	private static File ivyRootDirectory = null;
	
	private static void validateIvyRoot(String ivyRoot) {
		ivyRootDirectory = new File(ivyRoot);
		if (!ivyRootDirectory.exists() || !ivyRootDirectory.isDirectory() || !ivyRootDirectory.canRead()) {
			out.println("Unable to read ivy root: " + ivyRoot);
			ivyRootDirectory = null;
		}
	}

	/**
	 * HtmlBuilder is the main class of the Ivy Facade project. This class will
	 * parse an ivy repository, and generate HTML pages for organizations,
	 * modules, revisions, and the actual ivy.xml. Not all properties of the
	 * ivy.xml file are supported at this time.
	 * 
	 * Usage:
	 * 
	 * @param ivyRootDirectory
	 *            the ivy root directory
	 * 
	 */
	public static void main(String[] args) {
		PropertiesUtil.init();
		
		if (args == null || args.length < 1) {
			out.println(PropertiesUtil.getValue("err.usage"));
		} else {
			
			String ivyRoot = args[0];
			String propertiesFileName = null;
			if (args.length==2)
				propertiesFileName = args[1];
			
			validateIvyRoot(ivyRoot);
			if (ivyRootDirectory!=null) {
				
				//Initialize the HTMLUtil with the root ivyDirectory
				HTMLUtil.init(ivyRootDirectory);
				
				//Initialize the properties files
				if (propertiesFileName!=null) 
					PropertiesUtil.setUserProperties(propertiesFileName);
				
				//Initialize the File Util.
				FileUtil.init();
				
				//Write out the assets directory structure first
				out.println("Writing assets");
				AssetsUtil.setPath(PropertiesUtil.getValue("assets.root.write"));
				AssetsUtil.writeAssetsFromClasspath();
				
				//Read the IvyRepository
				out.println(PropertiesUtil.getValue("msg.reading", ivyRoot));
				FileUtil.readDirectoryStructure(ivyRootDirectory);
				
				//Generate dependency graph
				out.println(PropertiesUtil.getValue("msg.dependents"));
				FileUtil.createDependentGraph();
				
				//Generate directory HTML
				out.println("Generating Directory HTML");
				FileUtil.writeDirectoryFiles();
				
				//Generate Ivy HTML
				out.println(PropertiesUtil.getValue("msg.create.ivy"));
				FileUtil.writeIvyHtmlFiles();
				
				out.println(PropertiesUtil.getValue("msg.create.total", FileUtil.modules.size()));
				
			} else {
				out.println(PropertiesUtil.getValue("err.process", ivyRootDirectory.getPath()));
			}
			out.println(PropertiesUtil.getValue("msg.finished"));
		}
	}

}
