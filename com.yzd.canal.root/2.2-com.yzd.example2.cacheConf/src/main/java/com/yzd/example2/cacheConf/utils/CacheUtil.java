package com.yzd.example2.cacheConf.utils;

import com.yzd.common.cache.utils.setting.CachedSetting;
import com.yzd.common.cache.utils.setting.CachedSettingForTVCB;

public class CacheUtil {
    /**
     * 相当于克隆了一个副本-数据更改不会影响主体-互不影响
     * */
    public static CachedSettingForTVCB newCachedSettingForTVCB(CachedSettingForTVCB model){
        CachedSettingForTVCB item=new CachedSettingForTVCB()
                .setProjectNo(model.getProjectNo())
                .setKeyName(model.getKeyName())
                .setKeyExpireSec(model.getKeyExpireSec())
                .setKeyExpireSecForNullValue(model.getKeyExpireSecForNullValue())
                .setKeyExpireSecForMutexKey(model.getKeyExpireSecForMutexKey())
                .setKeyNameForTimestamp(model.getKeyNameForTimestamp())
                .setCountForCopyData(model.getCountForCopyData())
                .setDesc(model.getDesc())
                .setVersion(model.getVersion());
        return item;
    }
    /**
     * 相当于克隆了一个副本-数据更改不会影响主体-互不影响
     * */
    public static CachedSetting newCachedSetting(CachedSetting model){
        CachedSetting item=new CachedSetting();
        item.setProjectNo(model.getProjectNo());
        item.setKey(model.getKey());
        item.setKeyExpireSec(model.getKeyExpireSec());
        item.setNullValueExpireSec(model.getNullValueExpireSec());
        item.setKeyMutexExpireSec(model.getKeyMutexExpireSec());
        item.setSleepMilliseconds(model.getSleepMilliseconds());
        item.setDesc(model.getDesc());
        item.setVersion(model.getVersion());
        return item;
    }
}
