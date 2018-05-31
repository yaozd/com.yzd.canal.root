package com.yzd.example2.cacheConf.cacheSetting;

public class CacheConfig {
    public final static String PROJECT_NO="P01";
    //保证所有的SaveAllKeySet都设置了过期时间
    public final static String ExpireAllKeySet=getKeyFullNameForSpecific("ExpireAllKeySet");
    //保存资源时间戳版本对应的所有缓存-注意：此处不能删除“：”符号
    public final static String SaveAllKeySet=getKeyFullNameForSpecific("SaveAllKeySet:");
    //删除旧的资源时间戳版本对应的所有缓存
    public final static String EvictAllKeyList=getKeyFullNameForSpecific("EvictAllKeyList");
    //超时时间为10天,作用于：Timestamp，SaveAllKeySet;
    //理论上：Timestamp与SaveAllKeySet的超时时间应该是相同的。相当于timestampKeyExpireSec等于prefixSaveAllKeySetExpireSecr的时间
    public final static Integer TimeoutForPublicKey=60*60*24*10;
    //临时测试使用过期时间为60秒
    //public final static Integer TimeoutForPublicKey=60;
    //
    public static String getKeyNameForPublicList(String keyName){
        return CacheKeyType.P.name()+":"+keyName;
    }
    public static String getKeyNameForPrivateList(String keyName){
        return CacheKeyType.U.name()+":"+keyName;
    }
    public static String getKeyFullNameForSpecific(String keyName){
        return PROJECT_NO+"."+CacheKeyType.S.name()+":"+keyName;
    }
    public static String getKeyFullNameForTimestamp(String timestampKeyName){
        return PROJECT_NO+"."+CacheKeyType.T.name()+":"+timestampKeyName;
    }
    public static String getKeyFullNameForSaveAllKeySet(String timestampKeyValue){
        return SaveAllKeySet+timestampKeyValue;
    }

    /***
     * 方法只适用于私有缓存中
     * @param timestampKeyName
     * @param userId 当前用户的ID
     * @return
     */
    public static String getKeyFullNameForTimestampByUserId(String timestampKeyName,String userId){
        return timestampKeyName+":"+userId;
    }

    /***
     * 方法只适用于私有缓存中
     * @param dataKeyName
     * @param userId 当前用户的ID
     * @param timestampKeyValue 当前数据关联的时间戳的值
     * @return
     */
    public static String getKeyFullNameForDataWithTimestampByUserId(String dataKeyName,String userId,String timestampKeyValue){
        return dataKeyName+":"+userId+ ":" + timestampKeyValue;
    }
}
