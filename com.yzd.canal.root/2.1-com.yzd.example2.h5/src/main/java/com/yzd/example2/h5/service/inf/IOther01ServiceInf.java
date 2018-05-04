package com.yzd.example2.h5.service.inf;



import com.yzd.example2.h5.dao.entity.Other01;

import java.util.List;

public interface IOther01ServiceInf {
    int insertSelective(Other01 record);

    List<Other01> selectAll();

    Other01 selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(Other01 record);
}
