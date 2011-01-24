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
import com.procippus.ivy.model.License;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class LicenseAdapter implements XMLAdapter<License> {
	public static final String EL_LICENSE = "license";
	public static final String ATTR_NAME = "name";
	public static final String ATTR_URL = "url";
	@Override
	public License fromElement(Element e) {
		License license = new License();
		license.setName(e.getAttributeValue(ATTR_NAME));
		license.setUrl(e.getAttributeValue(ATTR_URL));
		return license;
	}
	
	@Override
	public Element toElement(License license) {
		Element e = new Element(EL_LICENSE);
		e.addAttribute(new Attribute(ATTR_NAME, license.getName()));
		e.addAttribute(new Attribute(ATTR_URL, license.getUrl()));
		return e;
	}
}
