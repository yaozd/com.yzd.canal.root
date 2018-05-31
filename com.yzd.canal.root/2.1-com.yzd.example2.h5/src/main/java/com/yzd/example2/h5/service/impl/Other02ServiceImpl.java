package com.yzd.example2.h5.service.impl;

import com.yzd.example2.cacheConf.cacheSetting.CacheKeyForPrivateList;
import com.yzd.example2.cacheConf.cacheSetting.CacheKeyForTimestamp;
import com.yzd.example2.h5.dao.entity.Other02;
import com.yzd.example2.h5.dao.mapper.Other02Mapper;
import com.yzd.example2.h5.service.inf.IOther02ServiceInf;
import com.yzd.example2.h5.utils.cacheExt.RedisCachePrivate;
import com.yzd.example2.h5.utils.cacheExt.RedisCachePrivateEvict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class Other02ServiceImpl implements IOther02ServiceInf {
    @Autowired
    Other02Mapper other02Mapper1;

    @Override
    @RedisCachePrivate(key = CacheKeyForPrivateList.UserBaseInfo,modelType = Other02.class)
    public Other02 selectByPrimaryKey(Integer uid) {
        return other02Mapper1.selectByPrimaryKey(uid);
    }

    @Override
    @RedisCachePrivateEvict(keyForTimestamp = CacheKeyForTimestamp.UserIdTM)
    public int updateByPrimaryKeySelective(Other02 record) {
        return other02Mapper1.updateByPrimaryKeySelective(record);
    }
}
