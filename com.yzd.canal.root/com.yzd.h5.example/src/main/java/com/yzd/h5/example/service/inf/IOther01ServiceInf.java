package com.yzd.h5.example.service.inf;

import com.yzd.h5.example.dao.entity.Other01;

import java.util.List;

public interface IOther01ServiceInf {
    int insertSelective(Other01 record);

    List<Other01> selectAll();
}
