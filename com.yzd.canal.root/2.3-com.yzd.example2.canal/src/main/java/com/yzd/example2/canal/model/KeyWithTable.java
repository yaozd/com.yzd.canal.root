package com.yzd.example2.canal.model;


import java.util.List;

public class KeyWithTable {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getTableList() {
        return tableList;
    }

    public void setTableList(List<String> tableList) {
        this.tableList = tableList;
    }

    private String key;
    private List<String> tableList;
}
