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

import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Represents the License element from Ivy, may develop more as
 * there are several name / url elements in Ivy.
 * 
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class License implements ElementAdapter {
	private static final long serialVersionUID = -7878108728402728855L;
	String name;
	String url;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		License other = (License) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	public static final String EL_LICENSE = "license";
	public static final String ATTR_NAME = "name";
	public static final String ATTR_URL = "url";
	public void fromElement(Element e) {
		this.name = e.getAttributeValue(ATTR_NAME);
		this.url = e.getAttributeValue(ATTR_URL);
	}
	
	public Element toElement() {
		Element license = new Element(EL_LICENSE);
		license.addAttribute(new Attribute(ATTR_NAME, name));
		license.addAttribute(new Attribute(ATTR_URL, url));
		return license;
	}
	
	public boolean isValid() {
		return (name != null && name.trim().length()>0 && url != null && url.trim().length() >0);
	}
}
