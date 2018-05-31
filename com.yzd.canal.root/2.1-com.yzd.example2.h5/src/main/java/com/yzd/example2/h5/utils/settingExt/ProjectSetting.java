package com.yzd.example2.h5.utils.settingExt;

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