package com.yzd.example2.cleaner.job.expire;

import com.yzd.common.cache.redis.sharded.ShardedRedisUtil;

import java.util.ArrayList;
import java.util.List;

public class ExpireTask {
    public static void setExpire(String expireAllKeySet,String saveAllKeySetName,int expireSecForSaveAllKeySet){
            ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
            redisUtil.expire(saveAllKeySetName, expireSecForSaveAllKeySet);
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
