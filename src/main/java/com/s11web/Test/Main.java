package com.s11web.Test;

import com.s11web.dao.ArcOperationDao;
import com.s11web.dao.UserDao;
import com.s11web.model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xietian on 2017/1/9.
 */
public class Main {


    public static void main(String[] args) {

   //     ArcOperationDao arcOperationDao = new ArcOperationDao();
   //     arcOperationDao.getTaskIdList();
        System.out.println("zzz");

        UserDao demo = new UserDao();
        User i =  demo.userLoginCheck("ZHW","1");
        System.out.println(i.getCompany());
    }

    public  static void doit(){



    }
}
