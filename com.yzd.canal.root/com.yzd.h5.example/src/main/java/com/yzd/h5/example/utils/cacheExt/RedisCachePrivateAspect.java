package com.yzd.h5.example.utils.cacheExt;

import com.google.common.base.Preconditions;
import com.yzd.common.cache.redis.sharded.ShardedRedisUtil;
import com.yzd.common.cache.utils.setting.CachedSetting;
import com.yzd.common.cache.utils.wrapper.CachedWrapper;
import com.yzd.common.cache.utils.wrapper.CachedWrapperExecutor;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheConfig;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheKeyListEnum;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheTimestampTypeEnum;
import com.yzd.h5.example.utils.fastjson.FastJsonUtil;
import com.yzd.h5.example.utils.sessionExt.LoginSessionUtil;
import com.yzd.h5.example.utils.timeVersionIdExt.TimeVersionId;
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
        Object[] where = RedisCacheAspectUtil.getRequestArgs(proceedingJoinPoint);
        //获得缓存方法的返回值类型
        Method method = RedisCacheAspectUtil.getMethod(proceedingJoinPoint);
        Class returnType = RedisCacheAspectUtil.getReturnType(method);
        Preconditions.checkArgument(!"void".equalsIgnoreCase(returnType.getTypeName()), "缓存方法的返回值类型不能是void；当前方法路径：" + method.toString());
        //key策略：KEY_NAME+WHERE_MD5(数据结构版本+请求参数)
        //获得当前方法的注解信息
        RedisCachePrivate methodCache = RedisCacheAspectUtil.getAnnotation(method, RedisCachePrivate.class);
        Boolean isPrivateUserIdType=RedisCacheTimestampTypeEnum.privateUserId.equals(methodCache.timestampType());
        Preconditions.checkArgument(isPrivateUserIdType, "缓存资源版本类型：个人私有数据,必须是RedisCacheTimestampTypeEnum.privateUserId类型缓存时间戳；当前方法路径：" + method.toString());
        //个人用户数据的缓存，需要在KEY上增加用户的ID--个人用户的ID是通过全局的用户登录信息获取
        Long currentUserIdVal=LoginSessionUtil.getCurrentUser().getId();
        Preconditions.checkNotNull(currentUserIdVal,"LoginSessionUtil.getCurrentUser().getId()当前用户登录信息ID不能为空！");
        String currentUserId= currentUserIdVal.toString();
        //P01.Timestamp:userId:1000 目前格式-201802-28-1714
        String timestampKeyName=methodCache.timestampType().getKeyFullNameForTimestamp()+":"+currentUserId;
        String timestampKeyValue=RedisCacheAspectUtil.getTimestampKey(timestampKeyName,RedisCacheConfig.ExpireAllKeySet,RedisCacheConfig.SaveAllKeySet,RedisCacheConfig.TimeoutForPublicKey);
        String whereToJson =FastJsonUtil.serialize(where);
        //P01.UserBaseInfo.1000:1519809133085:86d794ec9adae08014b485df7acf3dac 目前格式-201802-28-1714
        String dataKeyNameWithTimestamp=methodCache.key().name()+"."+currentUserId+":"+timestampKeyValue;
        CachedSetting cachedSetting = methodCache.key().getCachedSetting();
        cachedSetting.setKey(dataKeyNameWithTimestamp);
        //1，查询缓存2，执行方法
        String saveAllKeySetName= RedisCacheConfig.SaveAllKeySet+timestampKeyValue;
        String cacheDataInRedis = RedisCacheAspectUtil.getCacheDataInRedis(proceedingJoinPoint, whereToJson, cachedSetting,timestampKeyName,saveAllKeySetName);
        result = RedisCacheAspectUtil.deserialize(cacheDataInRedis,returnType,methodCache.modelType());
        System.out.println("RedisCachePrivateAspect->redis cache aspect step end");
        //返回结果
        return result;
    }
}
