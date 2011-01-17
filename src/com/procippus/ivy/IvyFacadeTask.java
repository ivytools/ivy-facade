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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.procippus.ivy.util.FileUtil;
import com.procippus.ivy.util.HTMLUtil;
import com.procippus.ivy.util.PropertiesUtil;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class IvyFacadeTask extends Task {
	private String ivyRoot;
	private String propertiesFile;

	public void setIvyRoot(String ivyRoot) {
		this.ivyRoot = ivyRoot;
	}

	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}
	
	public void execute() throws BuildException {
		boolean validRoot = FileUtil.validateIvyRoot(ivyRoot);
		if (validRoot) {
			File ivyRootFile = new File(ivyRoot);
			HTMLUtil.init(ivyRootFile);
			PropertiesUtil.init();
			if (propertiesFile!=null) 
				PropertiesUtil.setUserProperties(propertiesFile);
			FileUtil.init();
			
			out.println(PropertiesUtil.getValue("msg.reading", ivyRoot));
			FileUtil.readDirectoryStructure(ivyRootFile);
			
			out.println(PropertiesUtil.getValue("msg.dependents"));
			FileUtil.createDependentGraph();
			
			out.println(PropertiesUtil.getValue("msg.create.ivy"));
			FileUtil.writeIvyHtmlFiles();
			
			out.println(PropertiesUtil.getValue("msg.create.total", FileUtil.ivyFiles.size()));
			
			out.println(PropertiesUtil.getValue("msg.finished"));
		}
	}
	
}
