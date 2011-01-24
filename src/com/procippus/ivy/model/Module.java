package com.procippus.ivy.model;

import java.io.Serializable;

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


/**
 * This is the container class for all things Ivy and represents the
 * primary transfer object in the system.
 * 
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class Module implements Comparable<Module>, Serializable {
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
}