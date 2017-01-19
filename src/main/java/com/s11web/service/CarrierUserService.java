package com.s11web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.s11web.dao.CarrierUserDao;

@Service
public class CarrierUserService {

    @Autowired
    private CarrierUserDao carrierUserDao;

    public Object[] carrierUserLogin(String carrierName, String userName, String password) {
        return carrierUserDao.carrierUserLogin(carrierName, userName, password);
    }

    public List<Object[]> getScanHistory(String laneE, String date, int userId) {
        return carrierUserDao.getScanHistory(laneE, date, userId);
    }
}
