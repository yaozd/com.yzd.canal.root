package com.yzd.example2.canal.model;

/**
 * 当前记录信息
 */
public class RecordInfo {
    //库名
    private String schemaName;
    //表名
    private String tableName;
    //操作
    private String eventType;
    //主键的信息主要用于针对个人私有缓存数据的及时更新
    //通过主键反查这条信息是属于那一个用户的个人信息
    //主键名称
    private String primaryKeyName;
    //主键值
    private String primaryKeyValue;

    public RecordInfo(String schemaName, String tableName, String eventType, String primaryKeyName, String primaryKeyValue) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.eventType = eventType;
        this.primaryKeyName=primaryKeyName;
        this.primaryKeyValue=primaryKeyValue;
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

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public String getPrimaryKeyValue() {
        return primaryKeyValue;
    }

    public void setPrimaryKeyValue(String primaryKeyValue) {
        this.primaryKeyValue = primaryKeyValue;
    }
    @Override
    public String toString(){
        return "schemaName="+schemaName+",tableName="+tableName+",eventType="+eventType+",primaryKeyName="+primaryKeyName+",primaryKeyValue="+primaryKeyValue;
    }
}
