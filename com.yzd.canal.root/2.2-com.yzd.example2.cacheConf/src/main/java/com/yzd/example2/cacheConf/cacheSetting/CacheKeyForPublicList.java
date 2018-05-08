package com.yzd.example2.cacheConf.cacheSetting;

import com.yzd.common.cache.utils.setting.CachedSettingForTVCB;

import java.util.ArrayList;
import java.util.List;

public enum  CacheKeyForPublicList {
    Other1SelectAll(CacheKeyForTimestamp.TbOther01TM,10,"1.0","缓存Other1SelectAll信息","other_01");
    /**region*/
    CacheKeyForPublicList(CacheKeyForTimestamp cacheKeyTimestamp,int countForCopyData,String version,String desc,String... tables){
        if(cacheKeyTimestamp.getTimestampType()!=TimestampType.PublicType){
            throw new IllegalStateException("因为当前是公共缓存KEY的集合，所以缓存时间戳类型必须是TimestampType.PublicType（公有时间戳类型）:cacheKeyTimestamp="+cacheKeyTimestamp.name());
        }
        this.setCacheKeyTimestamp(cacheKeyTimestamp);
        String keyNameForTimestamp=cacheKeyTimestamp.getKeyFullName();
        this.setCachedSettingForTVCB(newCachedSetting(countForCopyData,version,desc,keyNameForTimestamp));
        if(tables!=null){
            for(String str:tables){
                this.getTableList().add(str);
            }
        }
    }
    private CacheKeyForTimestamp cacheKeyTimestamp;
    private CachedSettingForTVCB cachedSettingForTVCB;
    private List<String> tableList=new ArrayList<>();
    /**endregion*/
    private CachedSettingForTVCB newCachedSetting(int countForCopyData,String version,String desc,String keyNameForTimestamp){
        //暂定过期时间为10天
        int keyExpireSec=60*60*24*10;
        String projectNo=CacheConfig.PROJECT_NO;
        CachedSettingForTVCB cachedSettingForData = new CachedSettingForTVCB();
        cachedSettingForData
                .setProjectNo(projectNo)
                .setDesc(desc)
                .setVersion(version)
                .setKeyExpireSec(keyExpireSec)
                .setKeyExpireSecForNullValue(10)
                .setKeyExpireSecForMutexKey(5)
                .setSleepMillisecondsForMutexKey(300)
                .setKeyNameForTimestamp(keyNameForTimestamp)
                .setCountForCopyData(countForCopyData);
        return cachedSettingForData;
    }

    /**
     * 对应的缓存时间戳
     * */
    public CacheKeyForTimestamp getCacheKeyTimestamp() {
        return cacheKeyTimestamp;
    }

    public void setCacheKeyTimestamp(CacheKeyForTimestamp cacheKeyTimestamp) {
        this.cacheKeyTimestamp = cacheKeyTimestamp;
    }

    /**
     * 对应的缓存配置信息
     * */
    public CachedSettingForTVCB getCachedSettingForTVCB() {
        return cachedSettingForTVCB;
    }

    public void setCachedSettingForTVCB(CachedSettingForTVCB cachedSettingForTVCB) {
        this.cachedSettingForTVCB = cachedSettingForTVCB;
    }

    /**
     * 缓存时间戳关联的数据库表的集合
     * */
    public List<String> getTableList() {
        return tableList;
    }

    public void setTableList(List<String> tableList) {
        this.tableList = tableList;
    }

    public String getKeyName(){
        return CacheConfig.getKeyNameForPublicList(this.name());
    }
}
