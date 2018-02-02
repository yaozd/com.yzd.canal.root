package com.yzd.h5.example.utils.settingExt;

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class ProjectSetting {
    private static ResourceBundle PROJECT_CONFIG = ResourceBundle.getBundle("project");

    public static String getConfigProperty(String key) {
        if(!PROJECT_CONFIG.containsKey(key)){
            throw  new IllegalArgumentException("配置文件project.properties中，不包含此KEY="+key);
        }
        return PROJECT_CONFIG.getString(key);
    }
}