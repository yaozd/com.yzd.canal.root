package com.yzd.h5.example.utils.cacheExt;

import com.yzd.h5.example.utils.cacheSetting.RedisCacheKeyListEnum;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheTimestampTypeEnum;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RedisCachePublic {
    //缓存列表
    RedisCacheKeyListEnum key();
    //返回数据类型modelType
    Class modelType();
    //缓存资源版本类型：公共数据（代指缓存时间戳类型）
    RedisCacheTimestampTypeEnum timestampType();

}