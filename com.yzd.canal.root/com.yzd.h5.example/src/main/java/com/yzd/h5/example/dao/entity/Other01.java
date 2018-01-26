package com.yzd.h5.example.dao.entity;

import java.io.Serializable;

public class Other01 implements Serializable {
    public Other01(){

    }
    private Integer uid;

    private String name;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}