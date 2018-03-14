package com.yzd.h5.example.utils.cacheSetting;

import com.yzd.common.cache.utils.setting.CachedSetting;

import static com.yzd.h5.example.utils.cacheSetting.RedisCacheConfig.PROJECT_NO;

public enum RedisCacheKeyListEnum {
    //缓存--名称对应key
    UserBaseInfo(newCachedSetting("作用：缓存用户基本信息","1")),
    Other1SelectAll(newCachedSetting("作用：缓存Other1SelectAll信息","1"));

    RedisCacheKeyListEnum(CachedSetting cachedSetting) {
        this.setCachedSetting(cachedSetting);
    }

    private String description;
    private CachedSetting cachedSetting;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CachedSetting getCachedSetting() {
        return cachedSetting;
    }

    public void setCachedSetting(CachedSetting cachedSetting) {
        this.cachedSetting = cachedSetting;
    }
    //
    private static CachedSetting newCachedSetting(String desc, String version){
        //暂定过期时间为10天
        int keyExpireSec=60*60*24*10;
        return new CachedSetting(PROJECT_NO,"", keyExpireSec, 15, 5,300,version,desc);
    }

}
