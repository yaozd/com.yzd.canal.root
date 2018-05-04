package com.yzd.example2.h5.service.impl;

import com.yzd.example2.cacheConf.cacheSetting.CacheKeyForPublicList;
import com.yzd.example2.h5.dao.entity.Other01;
import com.yzd.example2.h5.dao.mapper.Other01Mapper;
import com.yzd.example2.h5.service.inf.IOther01ServiceInf;
import com.yzd.example2.h5.utils.cacheExt.RedisCachePublic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Other01ServiceImpl implements IOther01ServiceInf {

    @Autowired
    Other01Mapper other01Mapper1;

    @Override
    public int insertSelective(Other01 record) {
        return 0;
    }

    @Override
    @RedisCachePublic(key = CacheKeyForPublicList.Other1SelectAll,modelType = Other01.class)
    public List<Other01> selectAll() {
        List<Other01> other01List = other01Mapper1.selectList();
        return other01List;
    }

    @Override
    public Other01 selectByPrimaryKey(Integer uid) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(Other01 record) {
        return 0;
    }


}
