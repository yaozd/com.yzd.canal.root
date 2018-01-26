package com.yzd.h5.example.utils.cacheSetting;

import static com.yzd.h5.example.utils.cacheSetting.RedisCacheConfig.PROJECT_NO;

/**
 * @author YZD
 * 缓存资源版本类型：公共与个人
 */

public enum RedisCacheTimestampTypeEnum {
    userId("userId", "P01.Timestamp.userId:123"),
    publicNormal("public:normal", "P01.Timestamp.public:普通"),
    publicCommissionTable("public:commissionTable", "P01.Timestamp.public:佣金表");

    RedisCacheTimestampTypeEnum(String timestampKey, String description) {
        this.timestampKey = timestampKey;
        this.description = description;
    }

    private String timestampKey;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestampKey() {
        return timestampKey;
    }

    public void setTimestampKey(String timestampKey) {
        this.timestampKey = timestampKey;
    }
    //例：P01.Timestamp:userId:123
    public String keyFullName(){
        return PROJECT_NO+".Timestamp:"+this.timestampKey+":";
    }
}

