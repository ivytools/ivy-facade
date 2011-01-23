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
import nu.xom.Elements;

/**
 * Possibly the most import class, this represents the meat of
 * Ivy. Most critical attributes of a module are found here.
 * 
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class Info implements Comparable<Info>, ElementAdapter  {
	private static final long serialVersionUID = 6294884580423272431L;
	public static final String EL_INFO = "info";
	public static final String EL_DESCRIPTION = "description";
	static final String ATTR_ORG = "organisation";
	static final String ATTR_MOD = "module";
	static final String ATTR_REV = "revision";
	static final String ATTR_STAT = "status";
	static final String ATTR_PUB = "publication";
	
	String organization;
	String module;
	String revision;
	String status; 
	String publication;
	
	//This is a sub element, but no reason to create an object
	String description;
	License license = new License();
	
	public Info() {}
	
	public Info(String organization, String module, String revision,
			String status, String publication) {
		super();
		this.organization = organization;
		this.module = module;
		this.revision = revision;
		this.status = status;
		this.publication = publication;
	}

	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPublication() {
		return publication;
	}
	public void setPublication(String publication) {
		this.publication = publication;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((module == null) ? 0 : module.hashCode());
		result = prime * result
				+ ((organization == null) ? 0 : organization.hashCode());
		result = prime * result
				+ ((revision == null) ? 0 : revision.hashCode());
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
		Info other = (Info) obj;
		if (module == null) {
			if (other.module != null)
				return false;
		} else if (!module.equals(other.module))
			return false;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (revision == null) {
			if (other.revision != null)
				return false;
		} else if (!revision.equals(other.revision))
			return false;
		return true;
	}
	
	public void fromElement(Element e) {
		this.organization = e.getAttributeValue(ATTR_ORG);
		this.module = e.getAttributeValue(ATTR_MOD);
		this.revision = e.getAttributeValue(ATTR_REV);
		this.status = e.getAttributeValue(ATTR_STAT);
		this.publication = e.getAttributeValue(ATTR_PUB);
		Elements desElements = e.getChildElements(EL_DESCRIPTION);
		if (desElements.size()==1) {
			Element description = desElements.get(0);
			this.description = description.getValue();
		}
		Elements licElements = e.getChildElements(License.EL_LICENSE);
		if (licElements.size()>0) {
			Element lic = licElements.get(0);
			License l = new License();
			l.fromElement(lic);
			this.license=l;
		}
	}
	
	public Element toElement() {
		Element root = new Element(EL_INFO);
			root.addAttribute(new Attribute(ATTR_ORG, organization));
			root.addAttribute(new Attribute(ATTR_MOD, module));
			root.addAttribute(new Attribute(ATTR_REV, revision));
			root.addAttribute(new Attribute(ATTR_STAT, status));
			root.addAttribute(new Attribute(ATTR_PUB, publication));
			
		if (this.description != null) {
			Element elDescription = new Element(EL_DESCRIPTION);
			elDescription.appendChild(this.description);
			root.appendChild(elDescription);
		}
		if (this.license != null && this.license.isValid()) {
			root.appendChild(license.toElement());
		}
		return root;
	}
	
	@Override
	public int compareTo(Info o) {
		return this.module.compareTo(o.getModule());
	}
}
