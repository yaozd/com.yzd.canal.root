package com.yzd.example2.h5.dao.mapper;

import com.yzd.example2.h5.dao.entity.Other02;

public interface Other02Mapper {
    int insert(Other02 record);

    int insertSelective(Other02 record);

    Other02 selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(Other02 record);

    int updateByPrimaryKey(Other02 record);
}