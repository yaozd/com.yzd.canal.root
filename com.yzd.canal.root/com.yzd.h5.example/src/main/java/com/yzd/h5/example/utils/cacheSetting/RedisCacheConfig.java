package com.yzd.h5.example.utils.cacheSetting;

public class RedisCacheConfig {
    //当前缓存项目代码
    public final static String PROJECT_NO="P01";
    //保证所有的SaveAllKeySet都设置了过期时间
    public final static String ExpireAllKeySet=getKeyFullName("ExpireAllKeySet");
    //保存资源时间戳版本对应的所有缓存
    public final static String SaveAllKeySet=getKeyFullName("SaveAllKeySet:");
    //删除旧的资源时间戳版本对应的所有缓存
    public final static String EvictAllKeyList=getKeyFullName("EvictAllKeyList");
    //
    private static String getKeyFullName(String value) {
        return PROJECT_NO+"."+value;
    }
}
