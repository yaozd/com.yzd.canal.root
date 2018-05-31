package com.yzd.example2.h5.utils.cacheExt;

import com.yzd.example2.cacheConf.cacheSetting.CacheKeyForTimestamp;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RedisCachePrivateEvict {
    //缓存资源版本类型：个人私有数据（代指缓存时间戳类型）
    CacheKeyForTimestamp keyForTimestamp();
}
