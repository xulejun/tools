package com.xlj.tools.service.impl;

import com.xlj.tools.bean.User;
import com.xlj.tools.dao.UserDao;
import com.xlj.tools.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xlj
 * @date 2021/4/29
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public Integer insertUser(User user) {
        return userDao.insertUser(user);
    }
}
