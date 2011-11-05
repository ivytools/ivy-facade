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
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.xslt.XSLTransform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.procippus.ivy.IvyFacadeConstants;
import com.procippus.ivy.adapter.ModuleAdapter;
import com.procippus.ivy.graphics.PieValue;
import com.procippus.ivy.model.Module;

import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class XMLUtil {
	static Logger logger = LoggerFactory.getLogger(XMLUtil.class);
	private static ModuleAdapter moduleAdapter = new ModuleAdapter();
	
	static final SimpleDateFormat sdf = new SimpleDateFormat(PropertiesUtil.getValue("default.date.format"));
	static final Date processTime = new Date();
	
	static final String PRM_TS = "timestamp";
	static final String PRM_AR = "assetRoot";
	static final String PRM_IE = "imageExtension";
	
	public static Document transform(Document stylesheet, Document xml, Map<String, String> params) {
		Document result = null;
		try {
			if (xml != null && xml.getRootElement() != null
					&& stylesheet != null) {
				XSLTransform transform = new XSLTransform(stylesheet);
				if (params != null && !params.isEmpty()) {
					for (String key : params.keySet()) {
						transform.setParameter(key, params.get(key));
					}
				}
				transform.setParameter(PRM_TS, sdf.format(processTime));
				transform.setParameter(PRM_AR, PropertiesUtil.getValue("assets.root.absolute.url"));
				transform.setParameter(PRM_IE, PropertiesUtil.getValue("graphics.extension"));
				Nodes output = transform.transform(xml);
				result = XSLTransform.toDocument(output);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	static final String ERR_XSL_FILE = "Unable to find or read file: ";
	
	public static File prepareXSL(String xslFile) {
		File f = new File(xslFile);
		if (f.exists() && f.canRead()) {
			return f;
		} else {
			InputStream is = XMLUtil.class.getClassLoader().getResourceAsStream(xslFile);
			try {
				if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
				FileOutputStream fos = new FileOutputStream(f);
				
				byte buf[]=new byte[1024];
			    int len;
			    while((len=is.read(buf))>0) {
			    	fos.write(buf,0,len);
			    }
			    fos.close();
			    is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}
	
	private static final String EL_HOME = "home";
	private static final String EL_HEALTH = "health";
	private static final String EL_DIRECTORIES = "directories";
	private static final String EL_DIRECTORY = "directory";
	private static final String ATTR_NAME = "name";
	
	private static Element buildDirectoryElement(List<File> directories) {
		Element root = new Element(EL_DIRECTORIES);
		for (File d : directories) {
			Element dir = new Element(EL_DIRECTORY);
			dir.addAttribute(new Attribute(ATTR_NAME, d.getName()));
			root.appendChild(dir);
		}
		return root;
	}
	
	static final String EL_PIECHART = "piechart";
	private static Element createPieChart() {
		Element pieChart = new Element(EL_PIECHART);
		pieChart.addAttribute(new Attribute("mimeType", PropertiesUtil.getValue("graphics.mime.type")));
		
		double missing = FileUtil.allMissingDependencies.size();
		double total = FileUtil.modules.size();
		double pct = (missing / total)*100;

		PieValue err = new PieValue(pct, GraphicUtil.RED);
		PieValue all = new PieValue(100d-pct, GraphicUtil.GREEN);
		
		int pieChartWidth = Integer.parseInt(PropertiesUtil.getValue("graphics.piechart.width"));
		int pieChartHeight = Integer.parseInt(PropertiesUtil.getValue("graphics.piechart.height"));
		
		BufferedImage bi = GraphicUtil.drawPie(new PieValue[] {err, all}, pieChartWidth, pieChartHeight);
		
		String imgText = NumberFormat.getInstance().format(100.00d-pct) + "%";
		
		GraphicUtil.addCenterTextToPieChart(bi, imgText, Color.WHITE, pieChartWidth, pieChartHeight); 
		String hex = GraphicUtil.writeImageToBase64(bi);
		
		pieChart.appendChild(hex);
		
		return pieChart;
	}
	
	
	public static Document buildHomeDirectoryXML(List<File> directories) {
		Element root = new Element(EL_HOME);
		root.appendChild(buildDirectoryElement(directories));

		Element health = new Element(EL_HEALTH);
		
		Element moduleStats = new Element("moduleStats");
		moduleStats.addAttribute(new Attribute("totalModules", ""+FileUtil.modules.size()));
		moduleStats.addAttribute(new Attribute("totalMissingDependencies", ""+FileUtil.allMissingDependencies.size()));
		health.appendChild(moduleStats);
		
		Element missing = new Element("missing");
		for (Module m : FileUtil.missingDependencies) {
			missing.appendChild(moduleAdapter.toDependencyElement(m));
		}
		health.appendChild(missing);
		health.appendChild(createPieChart());
		root.appendChild(health);
		
		Element index = new Element("index");
		for (Module m : FileUtil.modules) {
			index.appendChild(moduleAdapter.toDependencyElement(m));
		}
		root.appendChild(index);
		
		Document d = new Document(root);
		return d;
	}
	
	public static Document buildDirectoryXML(List<File> directories) {
		Document d = new Document(buildDirectoryElement(directories));
		return d;
	}
	
	public static Module parseIvyFile(File ivy, Document ivyDoc) {
		Module module = moduleAdapter.fromElement(ivyDoc.getRootElement());
		module.setFilePath(ivy.getPath());
		return module;
	}
	
	public static Module parseIvyFile(File ivy) {
		Document ivyDoc;
		Module ivyFile = null;
		try {
			Builder builder = new Builder();
			ivyDoc = builder.build(ivy);
			ivyFile = parseIvyFile(ivy, ivyDoc);
		} catch (Exception e) {
			logger.error(PropertiesUtil.getValue(IvyFacadeConstants.KEY_ERR_FILE, ivy.getPath()));
		}
		return ivyFile;
	}
	
	static final String EL_IVY_FILES = "ivyFiles";
	static final String EL_IVY_FILE = "ivyFile";
	
	public static Document buildIndexDocument() {
		Element root = new Element(EL_IVY_FILES);
		
		Document doc = new Document(root);
		return doc;
	}
	
	public static void setAttribute(Element root, String attrName, String attrVal) {
		Attribute attr = null;
		if (root != null && !isEmpty(attrName) && !isEmpty(attrVal)) {
			attr = new Attribute(attrName, attrVal);
			root.addAttribute(attr);
		}
	}
	public static String getAttributeValue(Element root, String attrName) {
		String out = "";
		if (root != null && !isEmpty(attrName)) {
			if (root.getAttribute(attrName)!= null)
				out = root.getAttributeValue(attrName);
		}
		return out;
	}
}
