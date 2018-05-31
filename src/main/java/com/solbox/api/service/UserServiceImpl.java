package com.solbox.api.service;

import com.solbox.api.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ??? on 2017-11-30.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserDao userDao;

    @Override
    public Map<String, Object> getUserInfo(Map<String, Object> param) {
        return userDao.getUserInfo(param);
    }
}
