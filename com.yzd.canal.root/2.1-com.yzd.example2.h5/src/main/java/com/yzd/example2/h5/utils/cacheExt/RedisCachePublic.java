package com.yzd.example2.h5.utils.cacheExt;

import com.yzd.example2.cacheConf.cacheSetting.CacheKeyForPublicList;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RedisCachePublic {
    //缓存列表
    CacheKeyForPublicList key();
    //返回数据类型modelType
    Class modelType();
}