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

import nu.xom.Attribute;
import nu.xom.Element;

import com.procippus.ivy.XMLAdapter;
import com.procippus.ivy.model.Artifact;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class ArtifactAdapter implements XMLAdapter<Artifact> {
	public static final String EL_ATTRIBUTE = "artifact";
	static final String ATTR_NAME = "name";
	static final String ATTR_TYPE = "type";
	static final String ATTR_EXT = "ext";
	
	@Override
	public Artifact fromElement(Element e) {
		String name = e.getAttributeValue(ATTR_NAME);
		String type = e.getAttributeValue(ATTR_TYPE);
		String ext = e.getAttributeValue(ATTR_EXT);
		return new Artifact(name, type, ext);
	}
	
	@Override
	public Element toElement(Artifact artifact) {
		Element e = new Element(EL_ATTRIBUTE);
			if (artifact.getName() != null)
				e.addAttribute(new Attribute(ATTR_NAME, artifact.getName()));
			if (artifact.getType() != null)
				e.addAttribute(new Attribute(ATTR_TYPE, artifact.getType()));
			if (artifact.getExt() != null)
				e.addAttribute(new Attribute(ATTR_EXT, artifact.getExt()));
		return e;
	}
}
