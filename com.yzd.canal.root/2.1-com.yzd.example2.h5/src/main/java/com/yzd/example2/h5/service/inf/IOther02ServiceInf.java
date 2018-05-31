package com.yzd.example2.h5.service.inf;

import com.yzd.example2.h5.dao.entity.Other02;

public interface IOther02ServiceInf {

    Other02 selectByPrimaryKey(Integer uid);
    int updateByPrimaryKeySelective(Other02 record);
}
