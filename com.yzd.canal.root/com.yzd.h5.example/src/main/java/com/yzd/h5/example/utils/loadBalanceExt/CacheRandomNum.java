package com.yzd.h5.example.utils.loadBalanceExt;
/**
 * 获得某个公共缓存KEY的随机编号--主要用于公共缓存热点数据的水平扩展N倍
 * */
public class CacheRandomNum {
    public static Long getRandomNum(String timestampKeyName,Integer count){
        Long value=CacheRoundRobin.getInstance().getValue(timestampKeyName);
        return value%count;
    }
}
