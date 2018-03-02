package com.yzd.h5.example.utils.cacheExt;

import com.yzd.h5.example.utils.cacheSetting.RedisCacheKeyListEnum;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheTimestampTypeEnum;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RedisCachePrivateEvict {
    //缓存资源版本类型：个人私有数据（代指缓存时间戳类型）
    RedisCacheTimestampTypeEnum timestampType();
}