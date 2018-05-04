package com.yzd.example2.h5.dao.mapper;

import com.yzd.example2.h5.dao.entity.Other01;

import java.util.List;

public interface Other01Mapper {
    int insert(Other01 record);

    int insertSelective(Other01 record);

    Other01 selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(Other01 record);

    int updateByPrimaryKey(Other01 record);

    List<Other01> selectList();
}