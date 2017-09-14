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

import java.io.File;
import java.io.Serializable;

/**
 *
 * Represents an Ivy dependency, this would be better written
 * as a derived class, but the weight of the class does not
 * dictate such a measure.
 *
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class Dependency implements Comparable<Dependency>, Serializable {
    private static final long serialVersionUID = 6121477345687882481L;

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
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Dependency other = (Dependency) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (org == null) {
            if (other.org != null) {
                return false;
            }
        } else if (!org.equals(other.org)) {
            return false;
        }
        if (rev == null) {
            if (other.rev != null) {
                return false;
            }
        } else if (!rev.equals(other.rev)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Dependency o) {
        return (o != null && o.name != null && name != null) ? name.compareTo(o.name) : 0;
    }

    public String getPath() {
        return String.format("%s%s%s%s%s", org, File.separatorChar, name, File.separatorChar, rev);
    }

    public boolean isEqualToInfo(Info info) {
        Boolean out = Boolean.FALSE;
        if (info != null) {
            out = info.organization.equals(org) && info.revision.equals(rev) && info.module.equals(name);
        }
        return out;
    }
}
