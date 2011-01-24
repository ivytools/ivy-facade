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
import nu.xom.Elements;

import com.procippus.ivy.XMLAdapter;
import com.procippus.ivy.model.Info;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class InfoAdapter implements XMLAdapter<Info> {
	private static LicenseAdapter licenseAdapter = new LicenseAdapter();
	
	public static final String EL_INFO = "info";
	public static final String EL_DESCRIPTION = "description";
	static final String ATTR_ORG = "organisation";
	static final String ATTR_MOD = "module";
	static final String ATTR_REV = "revision";
	static final String ATTR_STAT = "status";
	static final String ATTR_PUB = "publication";
	
	@Override
	public Info fromElement(Element e) {
		Info info = new Info();
		
		info.setOrganization(e.getAttributeValue(ATTR_ORG));
		info.setModule(e.getAttributeValue(ATTR_MOD));
		info.setRevision(e.getAttributeValue(ATTR_REV));
		info.setStatus(e.getAttributeValue(ATTR_STAT));
		info.setPublication(e.getAttributeValue(ATTR_PUB));
		
		Elements desElements = e.getChildElements(EL_DESCRIPTION);
		if (desElements.size()==1) {
			Element description = desElements.get(0);
			info.setDescription(description.getValue());
		}
		
		Elements licElements = e.getChildElements(LicenseAdapter.EL_LICENSE);
		if (licElements.size()>0) {
			info.setLicense(licenseAdapter.fromElement(licElements.get(0)));
		}
		return info;
	}
	
	@Override
	public Element toElement(Info info) {
		Element root = new Element(EL_INFO);
			root.addAttribute(new Attribute(ATTR_ORG, info.getOrganization()));
			root.addAttribute(new Attribute(ATTR_MOD, info.getModule()));
			root.addAttribute(new Attribute(ATTR_REV, info.getRevision()));
			root.addAttribute(new Attribute(ATTR_STAT, info.getStatus()));
			root.addAttribute(new Attribute(ATTR_PUB, info.getPublication()));
			
		if (info.getDescription() != null) {
			Element elDescription = new Element(EL_DESCRIPTION);
			elDescription.appendChild(info.getDescription());
			root.appendChild(elDescription);
		}
		if (info.getLicense() != null && info.getLicense().isValid()) {
			root.appendChild(licenseAdapter.toElement(info.getLicense()));
		}
		return root;
	}
}
