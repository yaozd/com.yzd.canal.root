package com.yzd.canal.example;

/**
 * 当前记录信息
 */
public class RecordInfo {
    private String schemaName;
    private String tableName;
    private String eventType;
    private String primaryKey;

    public RecordInfo(String schemaName, String tableName, String eventType,String primaryKey) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.eventType = eventType;
        this.primaryKey=primaryKey;
    }


    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    @Override
    public String toString(){
        return "schemaName="+schemaName+",tableName="+tableName+",eventType="+eventType+",primaryKey="+primaryKey;
    }
}
