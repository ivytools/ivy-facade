package com.procippus.ivy.util;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class PropertiesUtil {
	private static final String DEFAULT_PROPS_FILE = "support/ivyfacade-default.properties";
	private static Properties defaultProperties;
	private static Properties userProperties;
	
	public static void init() {
		defaultProperties = new Properties();
		try {
			defaultProperties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(DEFAULT_PROPS_FILE));
		} catch (IOException e) {
			throw(new RuntimeException("Unable to load default properties file, verify classpath and try again."));
		}
	}
	
	public static void setUserProperties(String userPropertiesPath) {
		if (userPropertiesPath != null && userPropertiesPath.trim().length() > 0) {
			File userPropertiesFile = new File(userPropertiesPath);
			if (userPropertiesFile.exists() && userPropertiesFile.isFile() && userPropertiesFile.canRead()) {
				FileInputStream fis;
				try {
					fis = new FileInputStream(userPropertiesFile);
					userProperties = new Properties();
					userProperties.load(fis);
				} catch (Exception e) {
					throw(new RuntimeException("Unable to load/read user specified properties file: " + userPropertiesPath));
				}
			} else {
				throw(new RuntimeException("Unable to load/read user specified properties file: " + userPropertiesPath));
			}
		}
	}
	
	public static String getValue(String key) {
		String valueOut = null;
		if (userProperties!=null) {
			valueOut = userProperties.getProperty(key);
		}
		if (valueOut == null) {
			valueOut = defaultProperties.getProperty(key);
		}
		return valueOut;
	}
	
	public static String getValue(String key, String value) {
		String out = getValue(key);
		out = out.replace("{0}", value);
		return out;
	}
	
	public static String getValue(String key, Integer value) {
		return getValue(key, value.toString());
	}
	
	public static final String KEY_ERR_READ = "err.xsl.read";
	public static final String KEY_ERR_FILE = "err.xml.file";
	public static final String KEY_ERR_IO = "err.io";
	
	public static final String KEY_XSL_IVY = "default.xsl.ivy";
	public static final String KEY_XSL_DIR = "default.xsl.dir";
	public static final String KEY_XSL_HOME= "default.xsl.home";
	public static final String KEY_XSL_COMMON = "default.xsl.common";
	
	public static final String KEY_FIL_IGNORE = "files.ignore";
	public static final String KEY_IVY_PATTERNS = "files.ivy.patterns";
}
