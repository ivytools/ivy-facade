package com.procippus.ivy.model;

import nu.xom.Attribute;
import nu.xom.Element;

public class Artifact {
	public static final String EL_ATTRIBUTE = "artifact";
	static final String ATTR_NAME = "name";
	static final String ATTR_TYPE = "type";
	static final String ATTR_EXT = "ext";
	
	String name;
	String type;
	String ext;
	
	public Artifact() {}
	
	public Artifact(String name, String type, String ext) {
		this.name = name;
		this.type = type;
		this.ext = ext;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ext == null) ? 0 : ext.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Artifact other = (Artifact) obj;
		if (ext == null) {
			if (other.ext != null)
				return false;
		} else if (!ext.equals(other.ext))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	public void fromElement(Element e) {
		this.name = e.getAttributeValue(ATTR_NAME);
		this.type = e.getAttributeValue(ATTR_TYPE);
		this.ext = e.getAttributeValue(ATTR_EXT);
	}
	
	public Element toElement() {
		Element e = new Element(EL_ATTRIBUTE);
			if (name != null)
				e.addAttribute(new Attribute(ATTR_NAME, name));
			if (type != null)
				e.addAttribute(new Attribute(ATTR_TYPE, type));
			if (ext != null)
				e.addAttribute(new Attribute(ATTR_EXT, ext));
		return e;
	}
}
