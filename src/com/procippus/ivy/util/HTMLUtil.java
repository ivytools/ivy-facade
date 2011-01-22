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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

import com.procippus.ivy.model.Module;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class HTMLUtil {
	private static final String INDEX = "index.html";
	private static final String FULL_PATH = "FULL_PATH";
	private static final String ORG = "ORG";
	private static final String MOD = "MOD";
	private static final String REV = "REV";
	
	public static File ivyRoot = null;
	
	public static void init(File ivyRoot) {
		HTMLUtil.ivyRoot = ivyRoot;
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
			int width = Integer.parseInt(PropertiesUtil.getValue("graphics.width"));
			int height = Integer.parseInt(PropertiesUtil.getValue("graphics.height"));
			
			BufferedImage bi = GraphicUtil.draw(width, height, ivyFile);
			ivyFile.setBase64Image(GraphicUtil.writeImageToBase64(bi));
			
			File f = new File(ivyFile.getFilePath());
			Builder builder = new Builder();
			stylesheet = builder.build(styleSheet);
			
			ivyDoc = new Document(ivyFile.toElement());

			Document result = XMLUtil.transform(stylesheet, ivyDoc, null);
			writeIndexFile(f.getParent() + File.separatorChar + INDEX, result.toXML());			
		} catch (ParsingException ex) {
			System.err.println(PropertiesUtil.getValue(PropertiesUtil.KEY_ERR_FILE, ivyFile.getFilePath()));
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println(ivyFile.getFilePath());
			System.err.println(PropertiesUtil.getValue(PropertiesUtil.KEY_ERR_IO));
		}
	}
	
	public static void buildDirectoryHtml(File directory, List<File> directories) {
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
				out.println("Error while transforming: " + directoriesAsXml);
			}
		}
	}
}
