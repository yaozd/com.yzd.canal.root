package com.yzd.h5.example.dao.mapper;

import com.yzd.h5.example.dao.entity.Other02;

public interface Other02Mapper {
    int insert(Other02 record);

    int insertSelective(Other02 record);

    Other02 selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(Other02 record);

    int updateByPrimaryKey(Other02 record);
}