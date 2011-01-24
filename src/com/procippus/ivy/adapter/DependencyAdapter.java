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
import com.procippus.ivy.model.Dependency;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class DependencyAdapter implements XMLAdapter<Dependency> {
	public static final String EL_DEPENDENCY = "dependency";
	static final String ATTR_ORG = "org";
	static final String ATTR_NAME = "name";
	static final String ATTR_REV = "rev";
	static final String ATTR_MISSING = "missing";
	
	@Override
	public Dependency fromElement(Element e) {
		Dependency dep = new Dependency();
		dep.setName(e.getAttributeValue(ATTR_NAME));
		dep.setOrg(e.getAttributeValue(ATTR_ORG));
		dep.setRev(e.getAttributeValue(ATTR_REV));
		return dep;
	}
	
	@Override
	public Element toElement(Dependency d) {
		Element root = new Element(EL_DEPENDENCY);
			root.addAttribute(new Attribute(ATTR_ORG, d.getOrg()));
			root.addAttribute(new Attribute(ATTR_NAME, d.getName()));
			root.addAttribute(new Attribute(ATTR_REV, d.getRev()));
			if (d.getMissing()==Boolean.TRUE) {
				root.addAttribute(new Attribute(ATTR_MISSING, d.getMissing().toString()));
			}
		return root;
	}
}
