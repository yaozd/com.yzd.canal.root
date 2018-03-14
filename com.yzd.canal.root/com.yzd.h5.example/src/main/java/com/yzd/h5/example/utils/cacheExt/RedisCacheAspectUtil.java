package com.yzd.h5.example.utils.cacheExt;

import com.yzd.common.cache.redis.sharded.ShardedRedisUtil;
import com.yzd.common.cache.utils.setting.CachedSetting;
import com.yzd.common.cache.utils.wrapper.CachedWrapper;
import com.yzd.common.cache.utils.wrapper.CachedWrapperExecutor;
import com.yzd.h5.example.utils.fastjson.FastJsonUtil;
import com.yzd.h5.example.utils.timeVersionIdExt.TimeVersionId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class RedisCacheAspectUtil {

    public static String getCacheDataInRedis(ProceedingJoinPoint proceedingJoinPoint, String whereToJson, CachedSetting cachedSetting,String timestampKeyName,String saveAllKeySetName) {
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        CachedWrapper<String> resultCached = redisUtil.getPublicCachedWrapperByTimestampKeyValue(cachedSetting, whereToJson,timestampKeyName,saveAllKeySetName,
                new CachedWrapperExecutor<String>() {
                    @Override
                    public String execute() {
                        Object tempObj = null;
                        try {
                            tempObj = proceedingJoinPoint.proceed();
                        } catch (Throwable e) {
                            throw new IllegalStateException(e);
                        }
                        String resultToStr = FastJsonUtil.serialize(tempObj);
                        return resultToStr;
                    }
                });
        return resultCached.getData();
    }

    public static String getTimestampKey(String timestampKeyName,String ExpireAllKeySet,String prefixSaveAllKeySet,int timeoutForPublicKey) {
        //理论上Timestamp与SaveAllKeySet的超时时间应该是相同的。
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        CachedWrapper<String> wrapperValue_keyTimestamp = redisUtil.getTimestampKey(timestampKeyName,
                timeoutForPublicKey,
                5,
                3,
                300,
                ExpireAllKeySet,
                prefixSaveAllKeySet,
                timeoutForPublicKey,
                new CachedWrapperExecutor<String>() {
                    @Override
                    public String execute() {
                        //通过twitter的snowflake算法解决数据时间戳重复问题
                        return TimeVersionId.getInstance().getTimeVersion() ;
                    }
                });
        return wrapperValue_keyTimestamp.getData();
    }
    public static Object deserialize(String cacheDataInRedis,Class returnType,  Class modelType) {
        // 序列化结果应该是List对象
        if (returnType.isAssignableFrom(List.class)) {
            return FastJsonUtil.deserializeList(cacheDataInRedis, modelType);
        } else {
            return FastJsonUtil.deserialize(cacheDataInRedis, modelType);
        }
    }

    //获得当前方法的请求参数
    public static Object[] getRequestArgs(ProceedingJoinPoint jp) {
        Object[] args = jp.getArgs();
        return args;
    }

    //获得当前方法
    public static Method getMethod(ProceedingJoinPoint jp) {
        MethodSignature sign = (MethodSignature) jp.getSignature();
        Method method = sign.getMethod();
        return method;
    }

    //获得当前方法的注解信息
    public static <T extends Annotation> T getAnnotation(Method method, Class<T> clazz) {
        return method.getAnnotation(clazz);
    }

    //获得当前方法的返回值类型
    public static Class getReturnType(Method method) {
        Class returnType = method.getReturnType();
        return returnType;
    }
}
