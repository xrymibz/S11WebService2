package com.s11web.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.s11web.dao.UserDao;
import com.s11web.model.User;

@Component("userService")
public class UserService {


    @Autowired
    private UserDao userDao;
    private static final Logger log = Logger.getLogger(UserService.class);
    public User userLoginCheck(String name, String password) {



        return userDao.userLoginCheck(name, password);
    }

    public int getUserId(String username) {
        return userDao.getUserId(username);
    }

    public List<Object[]> getCarriersAbbr() {
        return userDao.getCarriersAbbr();
    }

    public List<Object[]> getTabsByRole(Integer role) {
        return userDao.getTabsByRole(role);
    }
}
