package com.yzd.h5.example.utils.cacheExt;

import com.google.common.base.Preconditions;
import com.yzd.common.cache.redis.sharded.ShardedRedisUtil;
import com.yzd.common.cache.utils.setting.CachedSetting;
import com.yzd.common.cache.utils.wrapper.CachedWrapper;
import com.yzd.common.cache.utils.wrapper.CachedWrapperExecutor;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheTimestampTypeEnum;
import com.yzd.h5.example.utils.fastjson.FastJsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 参考：java aop redis缓存
 * http://blog.csdn.net/u014695188/article/details/51499947/
 */
@Aspect
@Component
public class RedisCacheAspect {
    @Pointcut("@annotation(RedisCache)")
    public void setJoinPoint() {
    }

    //环绕通知:可以获取返回值
    @Around(value = "setJoinPoint()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {

        Object result = null;
        //获得请求参数，目前缓存方法只接受一个请求参数
        Object[] args = getRequestArgs(proceedingJoinPoint);
        Preconditions.checkArgument(args.length < 2, "缓存方法只接受一个请求参数");
        Object where = args.length == 0 ? null : args[0];
        //获得缓存方法的返回值类型
        Method method = getMethod(proceedingJoinPoint);
        Class returnType = getReturnType(method);
        Preconditions.checkArgument(!"void".equalsIgnoreCase(returnType.getTypeName()), "缓存方法的返回值类型不能是void；当前方法路径：" + method.toString());
        //key策略：KEY_NAME+WHERE_MD5(数据结构版本+请求参数)
        //获得当前方法的注解信息
        RedisCache methodCache = getAnnotation(method, RedisCache.class);
        //1,通过keyName获得key的缓存配置信息
        //P01.userBaseInfo.1516955775585:2ce72016154982aef705a2c453c9cbf5 //独立时间戳的部分，时间戳代指资源的时间版本
        //P01.userBaseInfo:2ce72016154982aef705a2c453c9cbf5 //将时间戳的部分，放在where条件里做MD5
        Integer userId=123;
        String timestampKey = getTimestampKey(methodCache.timestampType());
        String keyName = methodCache.key().name();
        // CachedSetting cachedSetting = methodCache.key().getCachedSetting();是有问题的是错误的，methodCache.key()是枚举相当于是一个单例
        CachedSetting cachedSetting = RedisCacheAspectUtil.newCachedSetting(methodCache.key().getCachedSetting()) ;
        //
        cachedSetting.setKey(keyName+"."+userId);
        //2,
        String whereToJson = timestampKey+"|"+FastJsonUtil.serialize(where);
        //1，查询缓存
        //2，执行方法
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        //P01.HelloWorld:b0baee9d279d34fa1dfd71aadb908c3f  //公共数据的缓存样例
        //P01.UserBaseInfo.123:d1e71408dfa37abc28dacbbff74c8101 //个人用户数据的缓存，需要在KEY上增加用户的ID--个人用户的ID是通过全局的用户登录信息获取
        CachedWrapper<String> resultCached = redisUtil.getPublicCachedWrapperByMutexKey(cachedSetting, whereToJson, () -> {
            Object tempObj = null;
            try {
                tempObj = proceedingJoinPoint.proceed();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
            String resultToStr = FastJsonUtil.serialize(tempObj);
            return resultToStr;
        });
        String cacheDataInRedis = resultCached.getData();
        result = deserialize(returnType, cacheDataInRedis);
        //3，缓存至Redis
        System.out.println("redis cache aspect step end");
        //返回结果
        return result;
    }

    private String getTimestampKey(RedisCacheTimestampTypeEnum timestampType) {
        Integer userId=123;
        String timestampKeyFullName= timestampType.getKeyFullNameForTimestamp()+userId;
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        CachedWrapper<String> wrapperValue_keyTimestamp = redisUtil.getCachedWrapperByMutexKey(timestampKeyFullName, 60 * 60 * 24, 5, 3,
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
