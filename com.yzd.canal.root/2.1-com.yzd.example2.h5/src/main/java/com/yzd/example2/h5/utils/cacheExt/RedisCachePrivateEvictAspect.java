package com.yzd.example2.h5.utils.cacheExt;

import com.google.common.base.Preconditions;
import com.yzd.common.cache.redis.sharded.ShardedRedisUtil;
import com.yzd.common.cache.utils.wrapper.CachedWrapper;
import com.yzd.example2.cacheConf.cacheSetting.CacheConfig;
import com.yzd.example2.cacheConf.cacheSetting.TimestampType;
import com.yzd.example2.h5.utils.sessionExt.LoginSessionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RedisCachePrivateEvictAspect {
    @Pointcut("@annotation(RedisCachePrivateEvict)")
    public void setJoinPoint() {
    }

    //环绕通知:可以获取返回值
    @Around(value = "setJoinPoint()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {

        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        } finally {
            //获得请求参数，目前缓存方法只接受一个请求参数
            Object[] where = RedisCacheAspectUtil.getRequestArgs(proceedingJoinPoint);
            //获得缓存方法的返回值类型
            Method method = RedisCacheAspectUtil.getMethod(proceedingJoinPoint);
            //获得当前方法的注解信息
            RedisCachePrivateEvict methodCache = RedisCacheAspectUtil.getAnnotation(method, RedisCachePrivateEvict.class);
            Boolean isPrivateUserIdType = TimestampType.PrivateType.equals(methodCache.keyForTimestamp().getTimestampType());
            Preconditions.checkArgument(isPrivateUserIdType, "缓存资源版本的缓存时间戳类型必须是TimestampType.PrivateType（私有时间戳类型）；当前方法路径：" + method.toString());
            //个人用户数据的缓存，需要在KEY上增加用户的ID--个人用户的ID是通过全局的用户登录信息获取
            Long currentUserIdVal= LoginSessionUtil.getCurrentUser().getId();
            Preconditions.checkNotNull(currentUserIdVal,"无法获得当前用户的userId;当前方法路径："+ method.toString());
            //P01.T:UserIdTM:1000 目前格式-201805-31-1301
            String timestampKeyNameByUserId = CacheConfig.getKeyFullNameForTimestampByUserId(methodCache.keyForTimestamp().getKeyFullName(),currentUserIdVal.toString());
            System.out.println(timestampKeyNameByUserId);
            ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
            //timestampKeyValue
            CachedWrapper<String> timestampKeyValue = redisUtil.getCachedWrapper(timestampKeyNameByUserId);
            System.out.println("/////////////////////");
            if (timestampKeyValue != null) {
                System.out.println("step01=timestampKeyValue=" + timestampKeyValue.getData());
                System.out.println("step02=redisUtil.del(timestampKeyName);");
                //
                String evictAllKeyList = CacheConfig.EvictAllKeyList;
                String saveAllKeySet = CacheConfig.getKeyFullNameForSaveAllKeySet(timestampKeyValue.getData());
                redisUtil.lpush(evictAllKeyList, saveAllKeySet);
                //
                redisUtil.del(timestampKeyNameByUserId);
            }
            System.out.println("RedisCachePrivateEvictAspect->redis cache aspect step end");
        }
        //返回结果
        return result;
    }
}

