package com.yzd.h5.example.utils.cacheExt;

import com.yzd.h5.example.utils.cacheSetting.RedisCacheKeyEnum;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheTimestampTypeEnum;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RedisCache {
    RedisCacheKeyEnum key();

    RedisCacheTimestampTypeEnum timestampType();
}
