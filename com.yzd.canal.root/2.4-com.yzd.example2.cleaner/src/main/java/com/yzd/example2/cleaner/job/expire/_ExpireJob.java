package com.yzd.example2.cleaner.job.expire;

import com.yzd.example2.cacheConf.cacheSetting.CacheConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class _ExpireJob {
    //理论上是不存在没有设置过期时间的SaveAllKeySet存在的。
    //只有在程序出现问题时才有可能，所以它的运行周期可以是1小时或者是更长时间。
    //保存资源时间戳版本对应的所有缓存,为其设置过期时间
    @Scheduled(initialDelay = 3000, fixedDelay = 1000 * 5)
    public void setExpireForSavaAllKeySet() throws InterruptedException {
        String expireAllKeySet = CacheConfig.ExpireAllKeySet;
        //保存资源时间戳版本对应的所有缓存的set集合的过期时间
        int expireSecForSaveAllKeySet = CacheConfig.TimeoutForPublicKey;
        while (true) {
            List<String> saveAllKeySetList = ExpireTask.getSaveAllKeySetList(expireAllKeySet);
            if (saveAllKeySetList.size() == 0) {
                break;
            }
            for (String saveAllKeySetName : saveAllKeySetList) {
                ExpireTask.setExpire(expireAllKeySet, saveAllKeySetName, expireSecForSaveAllKeySet);
            }
        }
    }
}
