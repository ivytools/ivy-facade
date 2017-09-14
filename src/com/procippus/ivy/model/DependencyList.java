package com.procippus.ivy.model;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Collection of dependencies, use for mapping purposes.
 *
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class DependencyList implements Serializable {
    private static final long serialVersionUID = -2178636806376738513L;
    List<Dependency> dependencies = new ArrayList<Dependency>();
    public void addDependency(Dependency dependency) {
        if (!dependencies.contains(dependency)) {
            dependencies.add(dependency);
        }
    }
    public List<Dependency> getDependencies() {
        return dependencies;
    }
}
