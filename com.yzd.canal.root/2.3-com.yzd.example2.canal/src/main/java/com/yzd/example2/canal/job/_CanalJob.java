package com.yzd.example2.canal.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class _CanalJob {
    private static final Logger logger = LoggerFactory.getLogger(_CanalJob.class);
    /**
     * 数据库监听
     * */
    @Scheduled(initialDelay = 3000, fixedDelay = 1000*5)
    public void ListenDB(){
        logger.info("begin");
        try {
            CanalTask.doWork();
        } catch (Exception e) {
           throw new IllegalStateException(e);
        }
    }
}
