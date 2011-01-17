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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class IvyFile implements Comparable<IvyFile> {
	String filePath;
	
	String organization;
	String module;
	String revision;
	String status; 
	String publication;
	
	List<IvyDependency> dependencies = new ArrayList<IvyDependency>();
	List<IvyFile> dependents = new ArrayList<IvyFile>();
	
	public IvyFile() {}
	
	public IvyFile(String filePath, String organization, String module,
			String revision, String status, String publication) {
		super();
		this.filePath = filePath;
		this.organization = organization;
		this.module = module;
		this.revision = revision;
		this.status = status;
		this.publication = publication;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void addDependency(IvyDependency dependency) {
		if (!dependencies.contains(dependency))
			dependencies.add(dependency);
	}
	
	public void addDependent(IvyFile ivyFile) {
		if (!dependents.contains(ivyFile))
			dependents.add(ivyFile);
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
	

	public List<IvyDependency> getDependencies() {
		return dependencies;
	}

	public List<IvyFile> getDependents() {
		return dependents;
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
		IvyFile other = (IvyFile) obj;
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
	
	@Override
	public String toString() {
		return "" + this.organization + " / " + this.module + " / " + this.revision;
	}

	@Override
	public int compareTo(IvyFile o) {
		return this.module.compareTo(o.getModule());
	}
	
}
