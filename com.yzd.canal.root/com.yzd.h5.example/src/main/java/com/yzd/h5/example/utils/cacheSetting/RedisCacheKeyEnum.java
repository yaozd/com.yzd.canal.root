package com.yzd.h5.example.utils.cacheSetting;

/**
 * @author YZD
 */

public enum RedisCacheKeyEnum {
    userBaseInfo("作用：缓存用户基本信息");

    RedisCacheKeyEnum(String description) {
        this.setDescription(description);
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
