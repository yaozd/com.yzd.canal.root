package com.yzd.example2.cacheConf.cacheSetting;

import java.util.ArrayList;
import java.util.List;
/**
 * 缓存时间戳
 * */
public enum  CacheKeyForTimestamp {
    UserIdTM(TimestampType.PrivateType),
    TbOther01TM(TimestampType.PublicType);

    CacheKeyForTimestamp(TimestampType timestampType){
        this.timestampType=timestampType;
    }
    /**
     * 缓存时间戳类型
     * */
    private TimestampType timestampType;

    public TimestampType getTimestampType() {
        return timestampType;
    }

    public void setTimestampType(TimestampType timestampType) {
        this.timestampType = timestampType;
    }

    public String getKeyFullName(){
        return CacheConfig.getKeyFullNameForTimestamp(this);
    }
}
