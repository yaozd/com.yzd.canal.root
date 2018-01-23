package com.yzd.h5.example.service.impl;

import com.yzd.h5.example.dao.entity.Other01;
import com.yzd.h5.example.dao.mapper.Other01Mapper;
import com.yzd.h5.example.service.inf.IOther01ServiceInf;
import com.yzd.h5.example.utils.cacheExt.RedisCache;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheKeyEnum;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheTimestampTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Other01ServiceImpl implements IOther01ServiceInf {

    @Autowired
    Other01Mapper other01Mapper1;
    @Override
    public int insertSelective(Other01 record) {
        return other01Mapper1.insertSelective(record);
    }

    @Override
    public List<Other01> selectAll() {
        Other01 item=other01Mapper1.selectByPrimaryKey(6);
        List<Other01> other01List=new ArrayList<>();
        other01List.add(item);
        return other01List;
    }
    @Override
    @RedisCache(key = RedisCacheKeyEnum.userBaseInfo, timestampType = RedisCacheTimestampTypeEnum.userId)
    public Other01 selectByPrimaryKey(Integer uid) {
        return other01Mapper1.selectByPrimaryKey(uid);
    }

    @Override
    public int updateByPrimaryKeySelective(Other01 record) {
        return other01Mapper1.updateByPrimaryKeySelective(record);
    }
}
