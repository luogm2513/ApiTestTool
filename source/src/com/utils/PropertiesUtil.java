package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    final static String CORE_CONFIG_ROOT = "/config/core-config.properties";

    private static PropertiesUtil instance = new PropertiesUtil();

    private static Properties p;

    private PropertiesUtil() {
        loadConfigs();
    }

    public static PropertiesUtil getInstance() {
        return instance;
    }

    private void loadConfigs() {
        String filePath = System.getProperty("user.dir") + CORE_CONFIG_ROOT;
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            try {
                InputStream in = new FileInputStream(file);
                p = new Properties();
                p.load(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getConfig(String name) {
        if (p.containsKey(name)) {
            return p.getProperty(name);
        } else {
            return "";
        }
    }
}
