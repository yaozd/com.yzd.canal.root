package com.yzd.example2.h5.utils.cacheExt;

import com.google.common.base.Preconditions;
import com.yzd.common.cache.utils.fastjson.FastJsonUtil;
import com.yzd.common.cache.utils.setting.CachedSettingForTVCB;
import com.yzd.example2.cacheConf.cacheSetting.CacheConfig;
import com.yzd.example2.cacheConf.utils.CacheUtil;
import com.yzd.example2.h5.utils.sessionExt.LoginSessionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

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
        //个人用户数据的缓存，需要在KEY上增加用户的ID--个人用户的ID是通过全局的用户登录信息获取
        Long currentUserIdVal= LoginSessionUtil.getCurrentUser().getId();
        Preconditions.checkNotNull(currentUserIdVal,"无法获得当前用户的userId;当前方法路径："+ method.toString());
        //P01.T:UserIdTM:1000 目前格式-201805-31-1301
        String timestampKeyNameByUserId =CacheConfig.getKeyFullNameForTimestampByUserId(methodCache.key().getCacheKeyTimestamp().getKeyFullName(),currentUserIdVal.toString()) ;
        String timestampKeyValue = RedisCacheAspectUtil.getTimestampKey(timestampKeyNameByUserId, CacheConfig.ExpireAllKeySet,CacheConfig.SaveAllKeySet,CacheConfig.TimeoutForPublicKey);
        String whereToJson = FastJsonUtil.serialize(where);
        //P01.U:UserBaseInfo:1000:1l5776xyo0sg:64b31032d8732c8c5f26f69c194477f1 目前格式-201805-31-1301
        String dataKeyNameWithTimestampByUserId = CacheConfig.getKeyFullNameForDataWithTimestampByUserId(methodCache.key().getKeyNameForData(),currentUserIdVal.toString(),timestampKeyValue);
        CachedSettingForTVCB cachedSettingForTVCB= CacheUtil.newCachedSettingForTVCB(methodCache.key().getCachedSettingForTVCB());
        cachedSettingForTVCB.setKeyName(dataKeyNameWithTimestampByUserId);
        cachedSettingForTVCB.setKeyNameForTimestamp(timestampKeyNameByUserId);
        //1，查询缓存2，执行方法
        String keyNameForSaveAllKeySetWithTimestamp= CacheConfig.getKeyFullNameForSaveAllKeySet(timestampKeyValue);
        String cacheDataInRedis = RedisCacheAspectUtil.getCacheDataInRedisForPuble(proceedingJoinPoint,cachedSettingForTVCB, whereToJson, keyNameForSaveAllKeySetWithTimestamp);
        result = RedisCacheAspectUtil.deserialize(cacheDataInRedis,returnType,methodCache.modelType());
        System.out.println("RedisCachePrivateAspect->redis cache aspect step end");
        //返回结果
        return result;
    }
}