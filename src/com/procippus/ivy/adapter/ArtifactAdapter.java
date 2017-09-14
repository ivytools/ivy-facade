package com.procippus.ivy.adapter;
/*
 *
 * Copyright 2011 Procippus, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.procippus.ivy.XMLAdapter;
import com.procippus.ivy.model.Artifact;
import nu.xom.Attribute;
import nu.xom.Element;

import static org.apache.commons.lang.StringUtils.isEmpty;

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
        String name = "", type = "", ext = "";
        if (e.getAttribute(ATTR_NAME) != null) {
            name = e.getAttributeValue(ATTR_NAME);
        }
        if (e.getAttribute(ATTR_TYPE) != null) {
            type = e.getAttributeValue(ATTR_TYPE);
        }
        if (e.getAttribute(ATTR_EXT) != null) {
            ext = e.getAttributeValue(ATTR_EXT);
        }
        return new Artifact(name, type, ext);
    }

    @Override
    public Element toElement(Artifact artifact) {
        Element e = new Element(EL_ATTRIBUTE);
        if (artifact != null) {
            if (!isEmpty(artifact.getName())) {
                e.addAttribute(new Attribute(ATTR_NAME, artifact.getName()));
            }

            if (isEmpty(artifact.getType())) {
                artifact.setType("jar");
            }
            e.addAttribute(new Attribute(ATTR_TYPE, artifact.getType()));

            if (isEmpty(artifact.getExt())) {
                artifact.setExt("jar");
            }
            e.addAttribute(new Attribute(ATTR_EXT, artifact.getExt()));
        }
        return e;
    }
}
