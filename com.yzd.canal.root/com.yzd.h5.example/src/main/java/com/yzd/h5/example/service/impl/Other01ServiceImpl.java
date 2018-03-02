package com.yzd.h5.example.service.impl;

import com.yzd.h5.example.dao.entity.Other01;
import com.yzd.h5.example.dao.mapper.Other01Mapper;
import com.yzd.h5.example.service.inf.IOther01ServiceInf;
import com.yzd.h5.example.utils.cacheExt.RedisCache;
import com.yzd.h5.example.utils.cacheExt.RedisCachePrivate;
import com.yzd.h5.example.utils.cacheExt.RedisCachePrivateEvict;
import com.yzd.h5.example.utils.cacheExt.RedisCachePublic;
import com.yzd.h5.example.utils.cacheSetting.RedisCacheKeyListEnum;
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
    @RedisCachePrivateEvict(timestampType = RedisCacheTimestampTypeEnum.privateUserId)
    public int insertSelective(Other01 record) {
        return other01Mapper1.insertSelective(record);
    }

    @Override
    @RedisCachePublic(key = RedisCacheKeyListEnum.Other1SelectAll,modelType = Other01.class, timestampType = RedisCacheTimestampTypeEnum.publicNormal)
    public List<Other01> selectAll() {
        List<Other01> other01List = other01Mapper1.selectList();
        return other01List;
    }

    @Override
    @RedisCachePrivate(key = RedisCacheKeyListEnum.UserBaseInfo,modelType = Other01.class, timestampType = RedisCacheTimestampTypeEnum.privateUserId)
    public Other01 selectByPrimaryKey(Integer uid) {
        return other01Mapper1.selectByPrimaryKey(uid);
    }

    @Override
    @RedisCachePrivateEvict(timestampType = RedisCacheTimestampTypeEnum.privateUserId)
    public int updateByPrimaryKeySelective(Other01 record) {
        return other01Mapper1.updateByPrimaryKeySelective(record);
    }
}
