package com.procippus.ivy;
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

import com.procippus.ivy.util.AssetsUtil;
import com.procippus.ivy.util.FileUtil;
import com.procippus.ivy.util.HTMLUtil;
import com.procippus.ivy.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class ExecutionStrategy {
    private static Logger logger = LoggerFactory.getLogger(ExecutionStrategy.class);
    private File ivyRootDirectory = null;

    private void validateIvyRoot(String ivyRoot) {
        ivyRootDirectory = new File(ivyRoot);
        if (!ivyRootDirectory.exists() || !ivyRootDirectory.isDirectory() || !ivyRootDirectory.canRead()) {
            logger.error("Unable to read ivy root: " + ivyRoot);
            ivyRootDirectory = null;
        }
    }

    public void execute(String ivyRoot) {
        validateIvyRoot(ivyRoot);
        if (ivyRootDirectory != null) {
            //Initialize the HTMLUtil with the root ivyDirectory
            HTMLUtil.init(ivyRootDirectory);

            //Initialize the File Util.
            FileUtil.init();

            //Write out the assets directory structure first
            logger.info("Writing assets");
            AssetsUtil.setPath(PropertiesUtil.getValue("assets.root.write"));
            AssetsUtil.writeAssetsFromClasspath();

            //Read the IvyRepository
            logger.info(PropertiesUtil.getValue("msg.reading", ivyRoot));
            FileUtil.readDirectoryStructure(ivyRootDirectory);

            //Generate dependency graph
            logger.info(PropertiesUtil.getValue("msg.dependents"));
            FileUtil.createDependentGraph();

            //Generate directory HTML
            logger.info("Generating Directory HTML");
            FileUtil.writeDirectoryFiles();

            //Generate Ivy HTML
            logger.info(PropertiesUtil.getValue("msg.create.ivy"));
            FileUtil.writeIvyHtmlFiles();

            logger.info(PropertiesUtil.getValue("msg.create.total", FileUtil.modules.size()));

        } else {
            logger.error(PropertiesUtil.getValue("err.process", ivyRootDirectory.getPath()));
        }
        logger.info(PropertiesUtil.getValue("msg.finished"));
    }
}
