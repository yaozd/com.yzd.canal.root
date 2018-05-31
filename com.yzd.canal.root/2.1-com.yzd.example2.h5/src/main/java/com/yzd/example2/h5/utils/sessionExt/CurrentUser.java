package com.yzd.example2.h5.utils.sessionExt;

public class CurrentUser {
    private Long Id;
    private String Name;
    public CurrentUser(){

    }
    public CurrentUser(Long id,String name) {
        setId(id);
        setName(name);
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
