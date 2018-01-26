package com.yzd.h5.example.utils.cacheExt;

import com.yzd.h5.example.utils.cacheSetting.RedisCacheKeyListEnum;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheTimestampTypeEnum;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RedisCache {
    //缓存列表
    RedisCacheKeyListEnum key();
    //缓存资源版本类型：公共与个人
    RedisCacheTimestampTypeEnum timestampType();
}
