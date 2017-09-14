package com.procippus.ivy.util;
/*
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
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class AssetsUtil {
    static final String[] FILES = {
        "assets/css/ivy-facade-main.css",
        "assets/css/ivyfacade/ui-bg_diagonals-thick_90_eeeeee_40x40.png",
        "assets/css/ivyfacade/ui-bg_flat_15_cd0a0a_40x100.png",
        "assets/css/ivyfacade/ui-bg_glass_100_e4f1fb_1x400.png",
        "assets/css/ivyfacade/ui-bg_glass_50_3baae3_1x400.png",
        "assets/css/ivyfacade/ui-bg_glass_80_d7ebf9_1x400.png",
        "assets/css/ivyfacade/ui-bg_highlight-hard_100_f2f5f7_1x100.png",
        "assets/css/ivyfacade/ui-bg_highlight-hard_70_000000_1x100.png",
        "assets/css/ivyfacade/ui-bg_highlight-soft_100_deedf7_1x100.png",
        "assets/css/ivyfacade/ui-bg_highlight-soft_25_ffef8f_1x100.png",
        "assets/css/ivyfacade/ui-icons_2694e8_256x240.png",
        "assets/css/ivyfacade/ui-icons_2e83ff_256x240.png",
        "assets/css/ivyfacade/ui-icons_3d80b3_256x240.png",
        "assets/css/ivyfacade/ui-icons_72a7cf_256x240.png",
        "assets/css/ivyfacade/ui-icons_ffffff_256x240.png",
        "assets/js/ivy-facade-main.js",
        "assets/js/ivy-facade-main-ui.js",
        "assets/js/ivy-facade-search.js"};

    static String path;
    public static void setPath(String path) {
        if (path != null && path.trim().length() > 0 && !path.endsWith("" + File.pathSeparatorChar)) {
            path += File.separatorChar;
        }
        AssetsUtil.path = path;
    }

    public static void writeAssetsFromClasspath() {
        ClassLoader loader = AssetsUtil.class.getClassLoader();
        for (String classPathLocation : FILES) {
            InputStream is = loader.getResourceAsStream(classPathLocation);

            String fileLocation = classPathLocation;
            if (path != null) {
                fileLocation = path + fileLocation;
            }
            File f = new File(fileLocation);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            try {
                FileOutputStream fos = new FileOutputStream(f);
                byte buf[] = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
