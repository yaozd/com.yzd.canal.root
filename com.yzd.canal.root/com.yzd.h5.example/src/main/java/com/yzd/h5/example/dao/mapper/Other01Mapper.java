package com.yzd.h5.example.dao.mapper;

import com.yzd.h5.example.dao.entity.Other01;

import java.util.List;

/**
 * @author YZD
 */
public interface Other01Mapper {
    int insert(Other01 record);

    int insertSelective(Other01 record);

    Other01 selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(Other01 record);

    int updateByPrimaryKey(Other01 record);

    List<Other01> selectList();
}