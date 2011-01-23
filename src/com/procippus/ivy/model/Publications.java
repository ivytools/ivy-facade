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
 * A publication is a list of artifacts for a given Ivy project.
 * 
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class Publications implements ElementAdapter {
	private static final long serialVersionUID = 9003914813891505533L;
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
