package com.yzd.h5.example.utils.cacheExt;

import com.google.common.base.Preconditions;
import com.yzd.common.cache.utils.setting.CachedSetting;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheConfig;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheKeyListEnum;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheTimestampTypeEnum;
import com.yzd.h5.example.utils.fastjson.FastJsonUtil;
import com.yzd.h5.example.utils.sessionExt.LoginSessionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
public class RedisCachePublicAspect {
    @Pointcut("@annotation(RedisCachePublic)")
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
        RedisCachePublic methodCache = RedisCacheAspectUtil.getAnnotation(method, RedisCachePublic.class);
        Boolean isPrivateUserIdType = RedisCacheTimestampTypeEnum.privateUserId.equals(methodCache.timestampType());
        Preconditions.checkArgument(!isPrivateUserIdType, "缓存资源版本类型：公共数据有数据,不能是RedisCacheTimestampTypeEnum.privateUserId类型缓存时间戳；当前方法路径：" + method.toString());
        //P01.Timestamp:userId:1000 目前格式-201802-28-1714
        String timestampKeyName = methodCache.timestampType().keyFullName();
        String timestampKeyValue = RedisCacheAspectUtil.getTimestampKey(timestampKeyName, RedisCacheConfig.ExpireAllKeySet,RedisCacheConfig.SaveAllKeySet,RedisCacheConfig.TimeoutForPublicKey);
        String whereToJson = FastJsonUtil.serialize(where);
        //P01.UserBaseInfo.1000:1519809133085:86d794ec9adae08014b485df7acf3dac 目前格式-201802-28-1714
        String dataKeyNameWithTimestamp = methodCache.key().name()+ ":" + timestampKeyValue;
        CachedSetting cachedSetting = methodCache.key().getCachedSetting();
        cachedSetting.setKey(dataKeyNameWithTimestamp);
        //1，查询缓存2，执行方法
        String saveAllKeySetName= RedisCacheConfig.SaveAllKeySet+timestampKeyValue;
        String cacheDataInRedis = RedisCacheAspectUtil.getCacheDataInRedis(proceedingJoinPoint, whereToJson, cachedSetting,timestampKeyName,saveAllKeySetName);
        result = RedisCacheAspectUtil.deserialize(cacheDataInRedis,returnType,methodCache.modelType());
        System.out.println("RedisCachePublicAspect->redis cache aspect step end");
        //返回结果
        return result;
    }
}
