package com.yzd.h5.example.utils.cacheExt;

import com.google.common.base.Preconditions;
import com.yzd.common.cache.redis.sharded.ShardedRedisUtil;
import com.yzd.common.cache.utils.setting.CachedSetting;
import com.yzd.common.cache.utils.wrapper.CachedWrapper;
import com.yzd.common.cache.utils.wrapper.CachedWrapperExecutor;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheKeyListEnum;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheTimestampTypeEnum;
import com.yzd.h5.example.utils.fastjson.FastJsonUtil;
import com.yzd.h5.example.utils.sessionExt.LoginSessionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class RedisCachePrivateAspect {
    @Pointcut("@annotation(RedisCachePrivate)")
    public void setJoinPoint() {
    }

    //环绕通知:可以获取返回值
    @Around(value = "setJoinPoint()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {

        Object result = null;
        //获得请求参数，目前缓存方法只接受一个请求参数
        Object[] where = getRequestArgs(proceedingJoinPoint);
        //获得缓存方法的返回值类型
        Method method = getMethod(proceedingJoinPoint);
        Class returnType = getReturnType(method);
        Preconditions.checkArgument(!"void".equalsIgnoreCase(returnType.getTypeName()), "缓存方法的返回值类型不能是void；当前方法路径：" + method.toString());
        //key策略：KEY_NAME+WHERE_MD5(数据结构版本+请求参数)
        //获得当前方法的注解信息
        RedisCachePrivate methodCache = getAnnotation(method, RedisCachePrivate.class);
        Boolean isPrivateUserIdType=RedisCacheTimestampTypeEnum.privateUserId.equals(methodCache.timestampType());
        Preconditions.checkArgument(isPrivateUserIdType, "缓存资源版本类型：个人私有数据,必须是RedisCacheTimestampTypeEnum.privateUserId类型缓存时间戳；当前方法路径：" + method.toString());
        //个人用户数据的缓存，需要在KEY上增加用户的ID--个人用户的ID是通过全局的用户登录信息获取
        Long currentUserIdVal=LoginSessionUtil.getCurrentUser().getId();
        Preconditions.checkNotNull(currentUserIdVal,"LoginSessionUtil.getCurrentUser().getId()当前用户登录信息ID不能为空！");
        String currentUserId= currentUserIdVal.toString();
        //P01.Timestamp:userId:1000 目前格式-201802-28-1714
        String timestampKeyName=methodCache.timestampType().keyFullName()+":"+currentUserId;
        String timestampKeyValue=getTimestampKey(timestampKeyName);
        String whereToJson =FastJsonUtil.serialize(where);
        //P01.UserBaseInfo.1000:1519809133085:86d794ec9adae08014b485df7acf3dac 目前格式-201802-28-1714
        String dataKeyNameWithTimestamp=methodCache.key().name()+"."+currentUserId+":"+timestampKeyValue;
        CachedSetting cachedSetting = methodCache.key().getCachedSetting();
        cachedSetting.setKey(dataKeyNameWithTimestamp);
        //1，查询缓存2，执行方法
        String cacheDataInRedis = getCacheDataInRedis(proceedingJoinPoint, whereToJson, cachedSetting);
        result = deserialize(returnType, cacheDataInRedis);
        System.out.println("RedisCachePrivateAspect->redis cache aspect step end");
        //返回结果
        return result;
    }

    private String getCacheDataInRedis(ProceedingJoinPoint proceedingJoinPoint, String whereToJson, CachedSetting cachedSetting) {
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        CachedWrapper<String> resultCached = redisUtil.getPublicCachedWrapperByMutexKey(cachedSetting, whereToJson,
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

    private String getTimestampKey(String timestampKeyName) {
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        CachedWrapper<String> wrapperValue_keyTimestamp = redisUtil.getCachedWrapperByMutexKey(timestampKeyName, 60 * 60 * 24, 5, 3,
                new CachedWrapperExecutor<String>() {
                    @Override
                    public String execute() {
                        return String.valueOf(System.currentTimeMillis()) ;
                    }
                });
        return wrapperValue_keyTimestamp.getData();
    }
    private Object deserialize(Class returnType, String cacheDataInRedis) {
        // 序列化结果应该是List对象
        if (returnType.isAssignableFrom(List.class)) {
            return FastJsonUtil.deserializeList(cacheDataInRedis, returnType);
        } else {
            return FastJsonUtil.deserialize(cacheDataInRedis, returnType);
        }
    }

    //获得当前方法的请求参数
    private Object[] getRequestArgs(ProceedingJoinPoint jp) {
        Object[] args = jp.getArgs();
        return args;
    }

    //获得当前方法
    private Method getMethod(ProceedingJoinPoint jp) {
        MethodSignature sign = (MethodSignature) jp.getSignature();
        Method method = sign.getMethod();
        return method;
    }

    //获得当前方法的注解信息
    private <T extends Annotation> T getAnnotation(Method method, Class<T> clazz) {
        return method.getAnnotation(clazz);
    }

    //获得当前方法的返回值类型
    private Class getReturnType(Method method) {
        Class returnType = method.getReturnType();
        return returnType;
    }
}
