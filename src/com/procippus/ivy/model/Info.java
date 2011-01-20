package com.procippus.ivy.model;

import nu.xom.Attribute;
import nu.xom.Element;

public class Info implements Comparable<Info> {
	public static final String EL_INFO = "info";
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
	}
	
	public Element toElement() {
		Element root = new Element(EL_INFO);
			root.addAttribute(new Attribute(ATTR_ORG, organization));
			root.addAttribute(new Attribute(ATTR_MOD, module));
			root.addAttribute(new Attribute(ATTR_REV, revision));
			root.addAttribute(new Attribute(ATTR_STAT, status));
			root.addAttribute(new Attribute(ATTR_PUB, publication));
		return root;
	}
	
	@Override
	public int compareTo(Info o) {
		return this.module.compareTo(o.getModule());
	}
}