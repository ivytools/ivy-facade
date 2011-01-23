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
import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;

/**
 * A Collection of dependencies, usde for mapping purposes.
 * 
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class DependencyList implements ElementAdapter {
	private static final long serialVersionUID = -2178636806376738513L;

	public static final String EL_DEPENDENCIES = "dependencies";
	
	List<Dependency> dependencies = new ArrayList<Dependency>();
	public void addDependency(Dependency dependency) {
		if (!dependencies.contains(dependency))
			dependencies.add(dependency);
	}
	public List<Dependency> getDependencies() {
		return dependencies;
	}
	public void fromElement(Element e) {
		Elements dependencies = e.getChildElements();
		for (int i = 0; i<dependencies.size(); i++) {
			Dependency d = new Dependency();
			d.fromElement(dependencies.get(i));
			addDependency(d);
		}
	}
	
	public Element toElement() {
		Element e = new Element(EL_DEPENDENCIES);
		for (Dependency d : dependencies) {
			e.appendChild(d.toElement());
		}
		return e;
	}
}