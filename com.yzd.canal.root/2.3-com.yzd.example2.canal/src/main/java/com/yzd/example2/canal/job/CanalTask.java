package com.yzd.example2.canal.job;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.yzd.common.cache.redis.sharded.ShardedRedisUtil;
import com.yzd.common.cache.utils.wrapper.CachedWrapper;
import com.yzd.example2.cacheConf.cacheSetting.CacheConfig;
import com.yzd.example2.canal.model.RecordInfo;
import com.yzd.example2.canal.model.TableInfoForPublicType;
import com.yzd.example2.canal.model.TableWithKey;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定制化监听服务+缓存时间戳数据版本操作
 */
public class CanalTask {

    //public static void main(String args[])throws Exception{
    public static void doWork()throws Exception{
        //
        int batchSize = 1000;
        //
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(AddressUtils.getHostIp(), 11111), "example", "", "");
        while (true){
            try{
                connector.connect();
                //监听所有数据库
                //connector.subscribe(".*\\..*");
                //监听指定数据库tb_my_test
                connector.subscribe("tb_other_test\\..*");
                connector.rollback();
                while (true){
                    getMessage(batchSize, connector);
                }
            }catch (CanalClientException ex){
                Thread.sleep(1000);
                ex.printStackTrace();

            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                connector.disconnect();
            }

        }
    }

    private static void getMessage(int batchSize, CanalConnector connector) throws InterruptedException {
        // 获取指定数量的数据
        Message message = connector.getWithoutAck(batchSize);
        long batchId = message.getId();
        int size = message.getEntries().size();
        if (batchId == -1 || size == 0) {
            Thread.sleep(1000);
        } else {
            printEntry(message.getEntries());
        }
        // 提交确认
        connector.ack(batchId);
    }

    private static void printEntry(@NotNull List<CanalEntry.Entry> entrys) {
        List<RecordInfo> recordInfoList=new ArrayList<>();
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),e);
            }
            CanalEntry.EventType eventType = rowChage.getEventType();
            //
            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                //String primaryKey=getPrimaryKeyByEventType(eventType,rowData);
                Map<String,String> primaryKeyMap=getPrimaryKeyByEventType(eventType,rowData);
                if(primaryKeyMap.isEmpty()){
                    StringBuilder error=new StringBuilder();
                    error.append("库名="+entry.getHeader().getSchemaName());
                    error.append("表名="+entry.getHeader().getTableName());
                    error.append("操作="+eventType).append(":").append("当前表记录没有主键");
                    throw new IllegalStateException(error.toString());
                }
                String primaryKeyName=primaryKeyMap.get("name");
                String primaryKeyValue=primaryKeyMap.get("value");
                RecordInfo recordInfo=new RecordInfo(entry.getHeader().getSchemaName(),entry.getHeader().getTableName(),eventType.name(),primaryKeyName,primaryKeyValue);
                //输出当前记录信息：schemaName=tb_other_test,tableName=other_01,eventType=UPDATE,primaryKey=2
                System.out.println("输出当前记录信息："+recordInfo.toString());
                //
                //将当前记录信息同步到消息队列当中，由其他的程序读取，再实现具体的业务逻辑
                recordInfoList.add(recordInfo);
            }
        }
        //缓存时间戳数据版本操作
        for (RecordInfo recordInfo:recordInfoList){
            deleteTimestampForPublicType(recordInfo);
        }
    }
    /**
     *共有缓存信息-时间戳KEY的删除逻辑
     * */
    private static void deleteTimestampForPublicType(RecordInfo recordInfo) {
        List<TableWithKey> tableWithKeyList= TableInfoForPublicType.getInstance().getKeyByTable(recordInfo.getTableName());
        if(tableWithKeyList==null){
            return;
        }
        ShardedRedisUtil redisUtil = ShardedRedisUtil.getInstance();
        for (TableWithKey tableWithKey:tableWithKeyList){
            String timestampKeyName= tableWithKey.getKey();
            //timestampKeyValue
            CachedWrapper<String> timestampKeyValue=redisUtil.getCachedWrapper(timestampKeyName);
            System.out.println("/////////////////////");
            if(timestampKeyValue!=null){
                System.out.println("step01=timestampKeyValue="+timestampKeyValue.getData());
                System.out.println("step02=redisUtil.del(timestampKeyName="+timestampKeyName+");");
                //
                String evictAllKeyList= CacheConfig.EvictAllKeyList;
                String saveAllKeySet=CacheConfig.getKeyFullNameForSaveAllKeySet(timestampKeyValue.getData());
                redisUtil.lpush(evictAllKeyList,saveAllKeySet);
                //
                redisUtil.del(timestampKeyName);
            }
            System.out.println("输出当前表对应的缓存时间戳版本的KEY:"+timestampKeyName);
            redisUtil.del(timestampKeyName);
        }
    }

    private static Map<String,String> getPrimaryKeyByEventType(CanalEntry.EventType eventType,CanalEntry.RowData rowData) {
        if (eventType == CanalEntry.EventType.DELETE) {
            return getPrimaryKey(rowData.getBeforeColumnsList());
        } else if (eventType == CanalEntry.EventType.INSERT) {
            return getPrimaryKey(rowData.getAfterColumnsList());
        } else {
            //UPDATE
            //System.out.println("-------> before");
            //primaryKey=getPrimaryKey(rowData.getBeforeColumnsList(),recordInfo);
            //System.out.println("-------> after");
            return getPrimaryKey(rowData.getAfterColumnsList());
        }
    }

    private static void printColumn(@NotNull List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated()+ "    isKey=" + column.getIsKey());
        }
    }
    private static Map<String,String> getPrimaryKey(@NotNull List<CanalEntry.Column> columns){
        Map<String,String> result=new HashMap<>(10);
        for (CanalEntry.Column column : columns) {
            if(column.getIsKey()){
                result.put("name",column.getName());
                result.put("value",column.getValue());
                return result;
            }
        }
        return null;
    }
}
