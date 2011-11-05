package com.procippus.ivy.adapter;
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

import nu.xom.Element;
import nu.xom.Elements;

import com.procippus.ivy.XMLAdapter;
import com.procippus.ivy.model.Dependency;
import com.procippus.ivy.model.DependencyList;


/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class DependencyListAdapter implements XMLAdapter<DependencyList> {
	public static final String EL_DEPENDENCIES = "dependencies";
	
	private DependencyAdapter dependencyAdapter = new DependencyAdapter();
	
	@Override
	public DependencyList fromElement(Element e) {
		DependencyList dependencyList = new DependencyList();
		Elements dependencies = e.getChildElements();
		for (int i = 0; i<dependencies.size(); i++) {
			dependencyList.addDependency(dependencyAdapter.fromElement(dependencies.get(i)));
		}
		return dependencyList;
	}
	
	@Override
	public Element toElement(DependencyList dependencyList) {
		Element e = new Element(EL_DEPENDENCIES);
		for (Dependency d : dependencyList.getDependencies()) {
			e.appendChild(dependencyAdapter.toElement(d));
		}
		return e;
	}
}
