package com.procippus.ivy.util;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
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
            logger.debug("Getting Property from user defined properties: " + userPropertiesPath);
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
        if (userProperties != null) {
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
}
