package com.s11web.dao;

import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xietian on 2017/1/9.
 * 提供统计查询服务
 */
//@Repository
//public class ArcOperationDao {
//
//    @Autowired
//    private SessionFactory sessionFactory;
//    private static final Logger log = Logger.getLogger(ArcOperationDao.class);
//    public List<String[]> getTaskIdList() {
//        List<String[]> result = new ArrayList<>();
//        try {
//            Session session = sessionFactory.getCurrentSession();
//            String str = " select  id from S11_task limit 4";
//            Query query = session.createSQLQuery(str);
//            query.setParameterList("carrierSelected", carrierSelected);
//            if (laneSelected.size() > 0) query.setParameterList("laneSelected", laneSelected);
//            if (arcSelected.size() > 0) query.setParameterList("arcSelected", arcSelected);
//            if (cargoSelected.size() > 0) query.setParameterList("cargoSelected", cargoSelected);
//            query.setParameter("dateFrom", dateFrom);
//            query.setParameter("dateTo", dateTo);
//            List<Object[]> list = query.list();
//            System.out.println(list.get(0).toString());
//            log.debug(list.get(0).toString());
//        } catch (Exception e) {
//            log.error(e);
//        }
//        return result;
//    }
//
//}
