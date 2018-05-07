package com.yzd.example2.cleaner.job.cleaner;

import com.yzd.example2.cacheConf.cacheSetting.CacheConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class _CleanerJob {
    private static final Logger logger = LoggerFactory.getLogger(_CleanerJob.class);
    @Scheduled(initialDelay = 3000, fixedDelay = 1000*5)
    //删除旧的资源时间戳版本对应的所有缓存数据
    public void evictOldTimestampCacheData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String beginLog="[_CleanerJob]-Begin-currentTime= " + dateFormat.format(new Date());
       while (true){
            String keyName_SaveAllKeySet= CleanerTask.getListValue(CacheConfig.EvictAllKeyList);
            if(StringUtils.isBlank(keyName_SaveAllKeySet)){
                continue;
            }
            System.out.println("旧的资源时间戳版本对应的所有缓存数据="+keyName_SaveAllKeySet);
            while (true){
                String keyName_cacheKey=CleanerTask.getSetValue(keyName_SaveAllKeySet);
                if(StringUtils.isBlank(keyName_cacheKey)){
                    break;
                }
                if(keyName_cacheKey.indexOf("##INIT-TIME##=")==0){
                    continue;
                }
                System.out.println("keyName_cacheKey"+keyName_cacheKey);
                CleanerTask.delCache(keyName_cacheKey);
            }
        }
    }
}
