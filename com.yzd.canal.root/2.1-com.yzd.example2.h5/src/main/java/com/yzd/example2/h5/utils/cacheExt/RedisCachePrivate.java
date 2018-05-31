package com.yzd.example2.h5.utils.cacheExt;

import com.yzd.example2.cacheConf.cacheSetting.CacheKeyForPrivateList;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RedisCachePrivate {
    //缓存列表
    CacheKeyForPrivateList key();
    //返回数据类型modelType
    Class modelType();
}
