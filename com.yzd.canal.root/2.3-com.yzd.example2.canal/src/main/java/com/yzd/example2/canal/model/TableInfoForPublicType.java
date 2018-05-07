package com.yzd.example2.canal.model;

import com.yzd.example2.cacheConf.cacheSetting.CacheKeyForPublicList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableInfoForPublicType {
    private static TableInfoForPublicType ourInstance = new TableInfoForPublicType();

    public static TableInfoForPublicType getInstance() {
        return ourInstance;
    }

    Map<String, List<TableWithKey>> tableGroups;
    private TableInfoForPublicType() {
        List<KeyWithTable> keyWithTablesList = getKeyWithTable();
        List<TableWithKey> tableWithKeyList=new ArrayList<>();
        for (KeyWithTable item:keyWithTablesList){
            for(String table:item.getTableList()){
                TableWithKey tableWithKey=new TableWithKey();
                tableWithKey.setKey(item.getKey());
                tableWithKey.setTable(table);
                tableWithKeyList.add(tableWithKey);
            }
        }
        tableGroups = tableWithKeyList.stream().
                collect(Collectors.groupingBy(TableWithKey::getTable));
    }
    public List<TableWithKey> getKeyByTable(String table){
        return tableGroups.get(table);
    }

    private List<KeyWithTable> getKeyWithTable() {
        List<KeyWithTable> keyWithTablesList=new ArrayList<>();
        for (CacheKeyForPublicList c : CacheKeyForPublicList.values()) {
            if(c.getTableList().isEmpty()){
                continue;
            }
            KeyWithTable item= new KeyWithTable();
            item.setKey(c.getCacheKeyTimestamp().getKeyFullName());
            item.setTableList(c.getTableList());
            keyWithTablesList.add(item);
        }
        return keyWithTablesList;
    }
}
