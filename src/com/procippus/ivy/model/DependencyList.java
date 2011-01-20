package com.procippus.ivy.model;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;

public class DependencyList {
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