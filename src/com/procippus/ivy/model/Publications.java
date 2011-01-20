package com.procippus.ivy.model;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;

public class Publications {
	static final String EL_PUBLICATIONS = "publications";
	List<Artifact> artifacts = new ArrayList<Artifact>();
	public void addArtifact(Artifact artifact) {
		if (!artifacts.contains(artifact)) 
			artifacts.add(artifact);
	}
	
	public void fromElement(Element e) {
		Elements elements = e.getChildElements();
		for (int i=0; i<elements.size();i++) {
			Artifact a = new Artifact();
			a.fromElement(elements.get(i));
			addArtifact(a);
		}
	}
	
	public Element toElement() {
		Element e = new Element(EL_PUBLICATIONS);
		for (Artifact a : artifacts) {
			e.appendChild(a.toElement());
		}
		return e;
	}
}
