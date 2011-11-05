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

import static com.procippus.ivy.util.XMLUtil.setAttribute;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import com.procippus.ivy.IvyFacadeConstants;
import com.procippus.ivy.XMLAdapter;
import com.procippus.ivy.model.Dependency;
import com.procippus.ivy.model.Module;
import com.procippus.ivy.util.PropertiesUtil;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class ModuleAdapter implements XMLAdapter<Module> {
	private static DependencyAdapter dependencyAdapter = new DependencyAdapter();
	private static DependentListAdapter dependentListAdapter = new DependentListAdapter();
	private static DependencyListAdapter dependencyListAdapter = new DependencyListAdapter();
	private static InfoAdapter infoAdapter = new InfoAdapter();
	private static PublicationsAdapter publicationsAdapter = new PublicationsAdapter();
	
	
	@Override
	public Module fromElement(Element e) {
		Module module = new Module();
		Elements eInfo = e.getChildElements(InfoAdapter.EL_INFO);
		Elements ePublications = e.getChildElements(PublicationsAdapter.EL_PUBLICATIONS);
		Elements eDependencies = e.getChildElements(DependencyListAdapter.EL_DEPENDENCIES);
		if (eInfo != null && eInfo.size()==1)
			module.setInfo(infoAdapter.fromElement(eInfo.get(0)));
		if (ePublications != null && ePublications.size()==1)
			module.setPublications(publicationsAdapter.fromElement(ePublications.get(0)));
		if (eDependencies != null && eDependencies.size()>=1)
			module.setDependencyList(dependencyListAdapter.fromElement(eDependencies.get(0)));
		return module;
	}
	
	static final String AT_W = "width";
	static final String AT_H = "height";
	static final String AT_MT = "mimeType";
	
	@Override
	public Element toElement(Module module) {
		Element e = new Element(IvyFacadeConstants.EL_IVY_MOD);
		e.addAttribute(new Attribute(IvyFacadeConstants.ATTR_VERSION, IvyFacadeConstants.VAL_VERSION_NUM));
		e.addNamespaceDeclaration(IvyFacadeConstants.XSI, IvyFacadeConstants.XSI_URL);
		e.addAttribute(new Attribute(IvyFacadeConstants.ATTR_NO_NS, IvyFacadeConstants.XSI_URL, IvyFacadeConstants.IVY_XSD));
		
		e.addAttribute(new Attribute(IvyFacadeConstants.ATTR_MISSING_DEPS, module.isMissingDependencies().toString()));
		
		e.appendChild(infoAdapter.toElement(module.getInfo()));
		
		e.appendChild(publicationsAdapter.toElement(module.getPublications()));
		
		e.appendChild(dependencyListAdapter.toElement(module.getDependencyList()));
		
		e.appendChild(dependentListAdapter.toElement(module.getDependentList()));
		
		if (module.getBase64Image() != null) {
			Element img = new Element(IvyFacadeConstants.EL_IMAGE);
			setAttribute(img, AT_W, PropertiesUtil.getValue(IvyFacadeConstants.KEY_GRAPHIC_WIDTH));
			setAttribute(img,AT_H, PropertiesUtil.getValue(IvyFacadeConstants.KEY_GRAPHIC_HEIGHT));
			setAttribute(img, AT_MT, PropertiesUtil.getValue(IvyFacadeConstants.KEY_GRAPHICS_MIME_TYPE));
			img.appendChild(module.getBase64Image());
			e.appendChild(img);
		}
		
		return e;
	}
	
	public Element toDependencyElement(Module module) {
		Dependency d = new Dependency(module);
		return dependencyAdapter.toElement(d);
	}
}
