package com.yzd.canal.example;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.List;
/**
 * 定制化监听服务
 */
public class T4 {

    public static void main(String args[])throws Exception{
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
                String primaryKey=getPrimaryKeyByEventType(eventType,rowData);
                if(primaryKey==null){
                    StringBuilder error=new StringBuilder();
                    error.append("库名="+entry.getHeader().getSchemaName());
                    error.append("表名="+entry.getHeader().getTableName());
                    error.append("操作="+eventType).append(":").append("当前表记录没有主键");
                    throw new IllegalStateException(error.toString());
                }
                RecordInfo recordInfo=new RecordInfo(entry.getHeader().getSchemaName(),entry.getHeader().getTableName(),eventType.name(),primaryKey);
                //输出当前记录信息：schemaName=tb_other_test,tableName=other_01,eventType=UPDATE,primaryKey=2
                System.out.println("输出当前记录信息："+recordInfo.toString());
                //
                //将当前记录信息同步到消息队列当中，由其他的程序读取，再实现具体的业务逻辑

            }
        }
    }

    private static String getPrimaryKeyByEventType(CanalEntry.EventType eventType,CanalEntry.RowData rowData) {
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
    private static String getPrimaryKey(@NotNull List<CanalEntry.Column> columns){
        for (CanalEntry.Column column : columns) {
            if(column.getIsKey()){
                return column.getValue();
            }
        }
        return null;
    }
}
