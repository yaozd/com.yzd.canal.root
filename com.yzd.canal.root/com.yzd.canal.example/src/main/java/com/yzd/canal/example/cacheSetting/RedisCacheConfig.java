package com.yzd.canal.example.cacheSetting;

public class RedisCacheConfig {
    //当前缓存项目代码
    public final static String PROJECT_NO="P01";
    //保证所有的SaveAllKeySet都设置了过期时间
    public final static String ExpireAllKeySet=getKeyFullName("ExpireAllKeySet");
    //保存资源时间戳版本对应的所有缓存
    public final static String SaveAllKeySet=getKeyFullName("SaveAllKeySet:");
    //删除旧的资源时间戳版本对应的所有缓存
    public final static String EvictAllKeyList=getKeyFullName("EvictAllKeyList");
    //超时时间为10天,作用于：Timestamp，SaveAllKeySet;
    //理论上：Timestamp与SaveAllKeySet的超时时间应该是相同的。相当于timestampKeyExpireSec等于prefixSaveAllKeySetExpireSecr的时间
    public final static Integer TimeoutForPublicKey=60*60*24*10;
    //临时测试使用过期时间为60秒
    //public final static Integer TimeoutForPublicKey=60;
    private static String getKeyFullName(String value) {
        return PROJECT_NO+"."+value;
    }
    //理论来讲，多个项目的KEY的获取方法是要使用统一方法
    public static String getKeyFullNameForTimestamp(String value) {
        return PROJECT_NO+".Timestamp:"+value;
    }
}
