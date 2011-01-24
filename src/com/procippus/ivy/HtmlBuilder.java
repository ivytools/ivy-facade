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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.procippus.ivy.util.PropertiesUtil;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class HtmlBuilder {
	private static Logger logger = LoggerFactory.getLogger(HtmlBuilder.class);

	/**
	 * HtmlBuilder is the main class of the Ivy Facade project. This class will
	 * parse an ivy repository, and generate HTML pages for organizations,
	 * modules, revisions, and the actual ivy.xml. Not all properties of the
	 * ivy.xml file are supported at this time.
	 * 
	 * Usage:
	 * 
	 * @param args ivyRootDirectory <propertiesFile>
	 * 
	 */
	public static void main(String[] args) {
		PropertiesUtil.init();
		
		if (args == null || args.length < 1) {
			logger.error(PropertiesUtil.getValue("err.usage"));
		} else {
			String ivyRoot = args[0];
			String propertiesFileName = null;
			if (args.length==2)
				propertiesFileName = args[1];
			
			//Initialize the properties files
			if (propertiesFileName!=null) 
				PropertiesUtil.setUserProperties(propertiesFileName);
			
			ExecutionStrategy ex = new ExecutionStrategy();
			ex.execute(ivyRoot);
		}
	}

}
