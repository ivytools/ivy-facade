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
import com.procippus.ivy.model.Dependency;
import com.procippus.ivy.model.DependentList;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class DependentListAdapter implements XMLAdapter<DependentList> {
    public static final String EL_DEPENDENTS = "dependents";
    private static DependencyAdapter dependencyAdapter = new DependencyAdapter();

    @Override
    public DependentList fromElement(Element e) {
        DependentList dependentList = new DependentList();

        Elements dependencies = e.getChildElements();
        for (int i = 0; i < dependencies.size(); i++) {
            dependentList.addDependency(dependencyAdapter.fromElement(dependencies.get(i)));
        }
        return dependentList;
    }

    @Override
    public Element toElement(DependentList dependentList) {
        Element e = new Element(EL_DEPENDENTS);
        for (Dependency d : dependentList.getDependencies()) {
            e.appendChild(dependencyAdapter.toElement(d));
        }
        return e;
    }
}
