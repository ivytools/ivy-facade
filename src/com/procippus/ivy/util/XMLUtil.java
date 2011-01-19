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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Nodes;
import nu.xom.xslt.XSLTransform;

import com.procippus.ivy.model.IvyDependency;
import com.procippus.ivy.model.IvyFile;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class XMLUtil {
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd [HH:mm z]");
	static final Date processTime = new Date();
	
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
				transform.setParameter("timestamp", sdf.format(processTime));
				transform.setParameter("assetRoot", PropertiesUtil.getValue("assets.root.absolute.url"));
				transform.setParameter("imageExtension", PropertiesUtil.getValue("graphics.extension"));
				Nodes output = transform.transform(xml);
				result = XSLTransform.toDocument(output);
			}
		} catch (Exception e) {
			out.println("Unable to transform: " + xml.toXML());
			out.println("With stylesheet: " + stylesheet.toXML());
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
	
	private static final String EL_DIRECTORIES = "directories";
	private static final String EL_DIRECTORY = "directory";
	private static final String EL_NAME = "name";
	
	public static Document buildDirectoryXML(List<File> directories) {
		Element root = new Element(EL_DIRECTORIES);
		for (File d : directories) {
			Element dir = new Element(EL_DIRECTORY);
			dir.addAttribute(new Attribute(EL_NAME, d.getName()));
			root.appendChild(dir);
		}
		Document d = new Document(root);
		return d;
	}
	
	public static IvyFile parseIvyFile(File ivy, Document ivyDoc) {
		IvyFile i = new IvyFile();
		i.setFilePath(ivy.getPath());
		i.setOrganization(ivyDoc.getRootElement().getChildElements("info").get(0).getAttributeValue("organisation"));
		i.setModule(ivyDoc.getRootElement().getChildElements("info").get(0).getAttributeValue("module"));
		i.setRevision(ivyDoc.getRootElement().getChildElements("info").get(0).getAttributeValue("revision"));
		i.setStatus(ivyDoc.getRootElement().getChildElements("info").get(0).getAttributeValue("status"));
		i.setPublication(ivyDoc.getRootElement().getChildElements("info").get(0).getAttributeValue("publication"));
		
		Elements deps = ivyDoc.getRootElement().getChildElements("dependencies");
		
		if (deps != null && deps.size()>0) {
			if (deps.get(0) != null && deps.get(0).getChildCount() >0) {
				Elements children = deps.get(0).getChildElements("dependency");
				for (int j=0; j<children.size();j++) {
					Element c = children.get(j);
					IvyDependency id = new IvyDependency();
					id.setName(c.getAttributeValue("name"));
					id.setOrganization(c.getAttributeValue("org"));
					id.setRevision(c.getAttributeValue("rev"));
					i.addDependency(id);
				}
			}
		}
		return i;
	}
	
	public static IvyFile parseIvyFile(File ivy) {
		Document ivyDoc;
		IvyFile ivyFile = null;
		try {
			Builder builder = new Builder();
			ivyDoc = builder.build(ivy);
			ivyFile = parseIvyFile(ivy, ivyDoc);
		} catch (Exception e) {
			System.err.println(PropertiesUtil.getValue(PropertiesUtil.KEY_ERR_FILE, ivy.getPath()));
		}
		return ivyFile;
	}
}
