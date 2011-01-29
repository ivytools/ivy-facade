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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.procippus.ivy.IvyFacadeConstants;
import com.procippus.ivy.adapter.ModuleAdapter;
import com.procippus.ivy.model.Module;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class HTMLUtil {
	private static Logger logger = LoggerFactory.getLogger(HTMLUtil.class);
	private static ModuleAdapter moduleAdapter = new ModuleAdapter();
	
	private static String INDEX = null;
	private static final String FULL_PATH = "FULL_PATH";
	private static final String ORG = "ORG";
	private static final String MOD = "MOD";
	private static final String REV = "REV";
	
	public static File ivyRoot = null;
	
	public static void init(File ivyRoot) {
		HTMLUtil.ivyRoot = ivyRoot;
		INDEX = PropertiesUtil.getValue(IvyFacadeConstants.KEY_HTML_DEFAULT);
	}
	
	private static void writeIndexFile(String path, String content) {
		try {
			File file = new File(path);
			FileWriter writer = new FileWriter(file);
			writer.write(content);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void buildIvyHtml(File styleSheet, Module ivyFile) {
		Document ivyDoc;
		Document stylesheet;
		try {
			//Create the image
			int width = Integer.parseInt(PropertiesUtil.getValue(IvyFacadeConstants.KEY_GRAPHIC_WIDTH));
			int height = Integer.parseInt(PropertiesUtil.getValue(IvyFacadeConstants.KEY_GRAPHIC_HEIGHT));
			
			BufferedImage bi = GraphicUtil.drawDependencyGraph(width, height, ivyFile);
			ivyFile.setBase64Image(GraphicUtil.writeImageToBase64(bi));
			
			File f = new File(ivyFile.getFilePath());
			Builder builder = new Builder();
			stylesheet = builder.build(styleSheet);
			
			ivyDoc = new Document(moduleAdapter.toElement(ivyFile));

			Document result = XMLUtil.transform(stylesheet, ivyDoc, null);
			writeIndexFile(f.getParent() + File.separatorChar + INDEX, result.toXML());			
		} catch (ParsingException ex) {
			logger.error(PropertiesUtil.getValue(IvyFacadeConstants.KEY_ERR_FILE, ivyFile.getFilePath()));
		} catch (IOException ex) {
			logger.error(ivyFile.getFilePath());
			logger.error(PropertiesUtil.getValue(IvyFacadeConstants.KEY_ERR_IO), ex);
		}
	}
	
	public static void buildDirectoryHtml(File directory, List<File> directories) {
		Collections.sort(directories);
		String currentDirectoryPath = directory.getPath();
		int modifier = (currentDirectoryPath.length() > ivyRoot.getPath().length()) ? 1 : 0;
		currentDirectoryPath = currentDirectoryPath.substring(ivyRoot.getPath().length() + modifier);
		
		String[] remainder = null;
		if (File.separatorChar == '\\') {
			remainder = currentDirectoryPath.split("[\\\\]");
		} else {
			remainder = currentDirectoryPath.split(""+File.separatorChar);
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(FULL_PATH, currentDirectoryPath);
		if (remainder != null && remainder.length > 0) {
			if (remainder.length >= 1)
				params.put(ORG, remainder[0]);
			if (remainder.length >= 2)
				params.put(MOD, remainder[1]);
			if (remainder.length >= 3)
				params.put(REV, remainder[2]);
		}

		Document directoriesAsXml = null; 
		if (directory.getPath().equals(ivyRoot.getPath())) {
			directoriesAsXml = XMLUtil.buildHomeDirectoryXML(directories);
		} else {
			directoriesAsXml = XMLUtil.buildDirectoryXML(directories);
		}
		if (directoriesAsXml != null
				&& directoriesAsXml.getRootElement() != null) {
			try {
				Builder builder = new Builder();
				Document stylesheet = null;
				if (directory.getPath().equals(ivyRoot.getPath())) {
					stylesheet = builder.build(FileUtil.homeStylesheet);
				} else {
					stylesheet = builder.build(FileUtil.dirStylesheet);
				}
				Document result = XMLUtil.transform(stylesheet, directoriesAsXml, params);
				writeIndexFile(directory.getPath() + File.separatorChar + "index.html", result.toXML());
			} catch (Exception e) {
				logger.error("Error while transforming: " + directoriesAsXml);
			}
		}
	}
}
