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
import com.procippus.ivy.model.Artifact;
import com.procippus.ivy.model.Publications;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class PublicationsAdapter implements XMLAdapter<Publications> {
	private static ArtifactAdapter artifactAdapter = new ArtifactAdapter();

	static final String EL_PUBLICATIONS = "publications";

	@Override
	public Publications fromElement(Element e) {
		Publications publications = new Publications();
		Elements elements = e.getChildElements();
		for (int i=0; i<elements.size();i++) {
			publications.addArtifact(artifactAdapter.fromElement(elements.get(i)));
		}
		return publications;
	}
	
	@Override
	public Element toElement(Publications publications) {
		Element e = new Element(EL_PUBLICATIONS);
		for (Artifact a : publications.getArtifacts()) {
			e.appendChild(artifactAdapter.toElement(a));
		}
		return e;
	}
}
