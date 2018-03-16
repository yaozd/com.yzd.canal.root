package com.yzd.cache.cleaner.job.expire;

import com.yzd.cache.cleaner.cacheSetting.RedisCacheConfig;
import com.yzd.common.cache.redis.sharded.ShardedRedisUtil;

import java.util.ArrayList;
import java.util.List;

public class ExpireTask {
    public static void setExpire(String expireAllKeySet,String saveAllKeySetName){
            ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
            redisUtil.expire(saveAllKeySetName, RedisCacheConfig.TimeoutForPublicKey);
            redisUtil.srem(expireAllKeySet,saveAllKeySetName);
    }
    public static List<String> getSaveAllKeySetList(String expireAllKeySet){
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        List<String> saveAllKeySetList= redisUtil.srandMember(expireAllKeySet,10);
        if(saveAllKeySetList==null){
            return new ArrayList<>();
        }
        return saveAllKeySetList;
    }
}
