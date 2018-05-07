package com.yzd.example2.cleaner.job.cleaner;

import com.yzd.common.cache.redis.sharded.ShardedRedisUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CleanerTask {
    private static final Logger logger = LoggerFactory.getLogger(CleanerTask.class);
    //从redis里面读取消息
    //通过静态方法引用就可以
    public static String getListValue(String listKey){
        //
        String value=null;
        //redis-网络抖动等特殊情况下的异常处理
        try{
            //阻塞指令-读取reids的消息队列
            ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
            value=redisUtil.brpop(listKey, 5);
            if(logger.isDebugEnabled()){
                logger.debug("通过阻塞指令读取reids消息队列的值value="+value);
            }
        }catch (Exception e){
            //log 记录日志
            logger.error("[getValue]", e);
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e1) {
                logger.error("thread interrupted",e1);
            }
            return null;
        }
        //redis 阻塞超时的情况下处理
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value;
    }
    public static String getSetValue(String setKey){
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        return redisUtil.spop(setKey);
    }
    public static void delCache(String cacheKey){
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        redisUtil.del(cacheKey);
    }
}
