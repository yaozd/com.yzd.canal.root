package com.yzd.canal.example.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableInfo {
    private static TableInfo ourInstance = new TableInfo();

    public static TableInfo getInstance() {
        return ourInstance;
    }
    Map<String, List<TableWithKey>> tableGroups;
    private TableInfo() {
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

    @NotNull
    private List<KeyWithTable> getKeyWithTable() {
        List<KeyWithTable> keyWithTablesList=new ArrayList<>();
        KeyWithTable item1= new KeyWithTable();
        item1.setKey("publicNormal");
        List<String> table1= new ArrayList<>();
        table1.add("other_01");
        item1.setTableList(table1);
        keyWithTablesList.add(item1);
        return keyWithTablesList;
    }
}
