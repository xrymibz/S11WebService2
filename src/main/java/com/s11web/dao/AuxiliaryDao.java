package com.s11web.dao;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xietian
 * 2017/4/18.
 */
@Repository
public class AuxiliaryDao {
    private static final Logger log = Logger.getLogger(AuxiliaryDao.class);
    @Autowired
    private SessionFactory sessionFactory;



    public List<String> getScanIDbyDate(String Date){
        List<String> result = new ArrayList<>();
        try {
            log.debug(Date);
            Session session = sessionFactory.getCurrentSession();
            String str =
                   " select scanId " +
                           "from  S11_task_item  " +
                           "where date_format(scanDatetime,'%Y-%m-%d') = :Date";

            Query query = session.createSQLQuery(str);
            query.setParameter("Date", Date);


            result = query.list();

        } catch (Exception e) {
            log.error(e);
        }
        return result;
    }

public void uodateScanInfo(String ScanID,String PV,String PW, String Box){
    try {
        log.debug(ScanID +"  "+PV +"  "+PW + "   "+Box);
        Session session = sessionFactory.getCurrentSession();
        String str =
                "update S11_task_item as sb " +
                        "SET sb.PV = :PV ," +
                        "sb.PW = :PW," +
                        "sb.box = :Box " +
                        "where sb.scanId = :ScanID";

        Query query = session.createSQLQuery(str);
        query.setParameter("ScanID", ScanID);
        query.setParameter("PV", PV);
        query.setParameter("PW", PW);
        query.setParameter("Box", Box);
        query.executeUpdate();
    } catch (Exception e) {
        log.error(e);
    }
}
}
