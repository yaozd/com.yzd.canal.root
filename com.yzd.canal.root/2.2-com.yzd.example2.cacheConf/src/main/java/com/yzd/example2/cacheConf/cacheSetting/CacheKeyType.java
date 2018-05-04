package com.yzd.example2.cacheConf.cacheSetting;

public enum CacheKeyType {
    //用户私有缓存信息(user)
    U,
    //共有缓存信息(public)
    P,
    //时间戳信息(timestamp)
    T,
    //特定缓存KEY(specific)
    S;
}
