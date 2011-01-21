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

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class Module implements Comparable<Module> {
	String filePath;
	
	Info info = new Info();
	Publications publications = new Publications();
	DependencyList dependencyList = new DependencyList();
	DependentList dependentList = new DependentList();

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

	public Element toElement() {
		Element e = new Element(EL_IVY_MOD);
		
		e.addAttribute(new Attribute(ATTR_MISSING_DEPS, isMissingDependencies().toString()));
		
		e.appendChild(info.toElement());
		
		e.appendChild(publications.toElement());
		
		e.appendChild(dependencyList.toElement());
		
		e.appendChild(dependentList.toElement());
		
		return e;
	}
	
	public Element toDependencyElement() {
		Dependency d = new Dependency(this);
		return d.toElement();
	}
}
