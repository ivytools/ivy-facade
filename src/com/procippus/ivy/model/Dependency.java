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

import java.io.File;

import nu.xom.Attribute;
import nu.xom.Element;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class Dependency implements Comparable<Dependency> {
	public static final String EL_DEPENDENCY = "dependency";
	static final String ATTR_ORG = "org";
	static final String ATTR_NAME = "name";
	static final String ATTR_REV = "rev";
	static final String ATTR_MISSING = "missing";
	
	String org;
	String name; 
	String rev;
	Boolean missing = Boolean.FALSE;
	
	public Dependency() {}
	public Dependency(String org, String name, String rev) {
		super();
		this.org = org;
		this.name = name;
		this.rev = rev;
	}
	public Dependency(Module module) {
		super();
		Info info = module.getInfo();
		if (info != null) {
			this.org = info.getOrganization();
			this.name = info.getModule();
			this.rev = info.getRevision();
		}
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRev() {
		return rev;
	}
	public void setRev(String rev) {
		this.rev = rev;
	}
	public Boolean getMissing() {
		return missing;
	}
	public void setMissing(Boolean missing) {
		this.missing = missing;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((org == null) ? 0 : org.hashCode());
		result = prime * result + ((rev == null) ? 0 : rev.hashCode());
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
		Dependency other = (Dependency) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (org == null) {
			if (other.org != null)
				return false;
		} else if (!org.equals(other.org))
			return false;
		if (rev == null) {
			if (other.rev != null)
				return false;
		} else if (!rev.equals(other.rev))
			return false;
		return true;
	}
	
	public void fromElement(Element e) {
		this.name = e.getAttributeValue(ATTR_NAME);
		this.org = e.getAttributeValue(ATTR_ORG);
		this.rev = e.getAttributeValue(ATTR_REV);
	}
	
	public Element toElement() {
		Element root = new Element(EL_DEPENDENCY);
			root.addAttribute(new Attribute(ATTR_ORG, org));
			root.addAttribute(new Attribute(ATTR_NAME, name));
			root.addAttribute(new Attribute(ATTR_REV, rev));
			if (missing==Boolean.TRUE) {
				root.addAttribute(new Attribute(ATTR_MISSING, missing.toString()));
			}
		return root;
	}
	@Override
	public int compareTo(Dependency o) {
		return (o!=null) ? name.compareTo(o.name) : 0;
	}
	
	public String getPath() {
		StringBuilder builder = new StringBuilder();
		builder.append(org)
		       .append(File.separatorChar)
		       .append(name)
		       .append(File.separatorChar)
		       .append(rev);
		return builder.toString();
	}
	
	public boolean isEqualToInfo(Info info) {
		Boolean out = Boolean.FALSE;
		if (info != null)
			out = info.organization.equals(org) && info.revision.equals(rev) && info.module.equals(name);
		return out;
	}
}
