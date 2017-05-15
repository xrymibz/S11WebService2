package com.s11web.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CarrierUserDao {

    private static final Logger log = Logger.getLogger(CarrierUserDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public Object[] carrierUserLogin(String carrierName, String userName, String password) {
        try {
            String sql = "select user.userId,c.carrierId,user.isInjection from zhw_carrier_user user,zhw_carrier c " +
                    "where user.carrierId=c.id and c.carrierName= :carrierName and user.username= :userName and user.password= :password";
            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setParameter("carrierName", carrierName);
            query.setParameter("userName", userName);
            query.setParameter("password", password);

            List<Object[]> userId = query.list();

            return userId.size() > 0 ? userId.get(0) : null;
        } catch (RuntimeException e) {
            log.error("数据库查询失败", e);
            return null;
        }
    }

    public List<Object[]> getScanHistory(String laneE, String date, int userId) {
        try {
            String sql = "select tb.laneE,tb.source,tb.destination,tb.cargoType,tb.time,count(S11_task_item.scanId) " +
                    " from S11_task_item inner join " +
                    " (select laneE,source,destination,cargoType,taskId,DATE_FORMAT(creDate,'%H:%i:%s') as time from S11_task " +
                    " where laneE = :laneE " +
                    " and userId = :userId " +
                    " and DATE_FORMAT(creDate,'%Y-%m-%d') = :date) as tb" +
                    " on S11_task_item.taskId = tb.taskId " +
                    " group by tb.taskId";

            log.debug("laneE :" + laneE +"date : " +date +"userId : "+userId );
            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setParameter("laneE", laneE);
            query.setParameter("date", date);
            query.setParameter("userId", userId);

            return query.list();
        } catch (RuntimeException e) {
            log.error("数据库查询失败", e);
            return null;
        }
    }
}
