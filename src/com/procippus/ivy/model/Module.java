package com.procippus.ivy.model;

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

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import com.procippus.ivy.util.PropertiesUtil;

/**
 * This is the container class for all things Ivy and represents the
 * primary transfer object in the system.
 * 
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class Module implements Comparable<Module>, ElementAdapter  {
	private static final long serialVersionUID = 3689637244502107166L;

	String filePath;
	
	String base64Image;
	
	Info info = new Info();
	Publications publications = new Publications();
	DependencyList dependencyList = new DependencyList();
	DependentList dependentList = new DependentList();

	
	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Publications getPublications() {
		return publications;
	}

	public void setPublications(Publications publications) {
		this.publications = publications;
	}

	public DependencyList getDependencyList() {
		return dependencyList;
	}

	public void setDependencyList(DependencyList dependencyList) {
		this.dependencyList = dependencyList;
	}
	
	public DependentList getDependentList() {
		return dependentList;
	}

	public void setDependentList(DependentList dependentList) {
		this.dependentList = dependentList;
	}
	
	public Boolean isMissingDependencies() {
		Boolean out = Boolean.FALSE;
		if (getDependencyList() != null && getDependencyList().getDependencies() != null) {
			for (Dependency d : getDependencyList().getDependencies()) {
				if (d.getMissing()) {
					out = Boolean.TRUE;
					break;
				}
			}
		}
		return out;
	}

	@Override
	public int compareTo(Module o) {
		return (o != null) ? info.compareTo(o.info) : 0;
	}
	
	public void fromElement(Element e) {
		Elements eInfo = e.getChildElements("info");
		Elements ePublications = e.getChildElements("publications");
		Elements eDependencies = e.getChildElements(DependencyList.EL_DEPENDENCIES);
		
		if (eInfo != null && eInfo.size()==1)
			info.fromElement(eInfo.get(0));
		
		if (ePublications != null && ePublications.size()==1)
			publications.fromElement(ePublications.get(0));
		
		if (eDependencies != null && eDependencies.size()>=1)
			dependencyList.fromElement(eDependencies.get(0));
	}
	
	public static final String EL_IVY_MOD = "ivy-module";
	private static final String ATTR_MISSING_DEPS = "missingDependencies";
	private static final String EL_IMAGE = "image";
	
	private static final String XSI ="xsi";
	private static final String XSI_URL = "http://www.w3.org/2001/XMLSchema-instance";
	private static final String ATTR_NO_NS = "xsi:noNamespaceSchemaLocation";
	private static final String IVY_XSD = "http://incubator.apache.org/ivy/schemas/ivy.xsd";
	private static final String ATTR_VERSION = "version";
	private static final String VAL_VERSION_NUM = "1.3";
	
	//xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	//xsi:noNamespaceSchemaLocation="http://incubator.apache.org/ivy/schemas/ivy.xsd" version="1.3"

	public Element toElement() {
		Element e = new Element(EL_IVY_MOD);
		e.addAttribute(new Attribute(ATTR_VERSION, VAL_VERSION_NUM));
		e.addNamespaceDeclaration(XSI, XSI_URL);
		e.addAttribute(new Attribute(ATTR_NO_NS, XSI_URL, IVY_XSD));
		
		e.addAttribute(new Attribute(ATTR_MISSING_DEPS, isMissingDependencies().toString()));
		
		e.appendChild(info.toElement());
		
		e.appendChild(publications.toElement());
		
		e.appendChild(dependencyList.toElement());
		
		e.appendChild(dependentList.toElement());
		
		if (base64Image != null) {
			Element img = new Element(EL_IMAGE);
			img.addAttribute(new Attribute("mimeType", PropertiesUtil.getValue("graphics.mime.type")));
			img.addAttribute(new Attribute("width", PropertiesUtil.getValue("graphics.width")));
			img.addAttribute(new Attribute("height", PropertiesUtil.getValue("graphics.height")));
			img.appendChild(base64Image);
			e.appendChild(img);
		}
		
		return e;
	}
	
	public Element toDependencyElement() {
		Dependency d = new Dependency(this);
		return d.toElement();
	}
}
