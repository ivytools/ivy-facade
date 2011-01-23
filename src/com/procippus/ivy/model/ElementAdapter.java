package com.procippus.ivy.model;

import java.io.Serializable;

import nu.xom.Element;

public interface ElementAdapter extends Serializable {
	void fromElement(Element e);
	Element toElement();
}
