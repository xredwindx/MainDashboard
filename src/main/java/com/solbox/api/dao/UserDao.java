package com.solbox.api.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Created by ??? on 2017-11-30.
 */
@Mapper
public interface UserDao {
    Map<String, Object> getUserInfo(Map<String, Object> param);
}
