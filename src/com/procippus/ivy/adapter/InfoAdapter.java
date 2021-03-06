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
import com.procippus.ivy.model.Info;
import nu.xom.Element;
import nu.xom.Elements;

import static com.procippus.ivy.util.XMLUtil.getAttributeValue;
import static com.procippus.ivy.util.XMLUtil.setAttribute;

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

        info.setOrganization(getAttributeValue(e, ATTR_ORG));
        info.setModule(getAttributeValue(e, ATTR_MOD));
        info.setRevision(getAttributeValue(e, ATTR_REV));
        info.setStatus(getAttributeValue(e, ATTR_STAT));
        info.setPublication(getAttributeValue(e, ATTR_PUB));

        Elements desElements = e.getChildElements(EL_DESCRIPTION);
        if (desElements.size() == 1) {
            Element description = desElements.get(0);
            info.setDescription(description.getValue());
        }

        Elements licElements = e.getChildElements(LicenseAdapter.EL_LICENSE);
        if (licElements.size() > 0) {
            info.setLicense(licenseAdapter.fromElement(licElements.get(0)));
        }
        return info;
    }

    @Override
    public Element toElement(Info info) {
        Element root = new Element(EL_INFO);

        if (info != null) {
            setAttribute(root, ATTR_ORG, info.getOrganization());
            setAttribute(root, ATTR_MOD, info.getModule());
            setAttribute(root, ATTR_REV, info.getRevision());
            setAttribute(root, ATTR_STAT, info.getStatus());
            setAttribute(root, ATTR_PUB, info.getPublication());

            if (info.getDescription() != null) {
                Element elDescription = new Element(EL_DESCRIPTION);
                elDescription.appendChild(info.getDescription());
                root.appendChild(elDescription);
            }
            if (info.getLicense() != null && info.getLicense().isValid()) {
                root.appendChild(licenseAdapter.toElement(info.getLicense()));
            }
        }
        return root;
    }
}
