package com.s11web.dao;

import com.s11web.model.StoreRate;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
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




    public List<String[]> getWareHousingByCondition(
                                                    String dateFrom,
                                                    String dateTo) {

        List<String[]> result = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            String str =
                    "select  carrierAbbr,CONCAT(source,'-',destination) as Arc, source ," +
                            " destination,cargoType,date_format(task.creDate,'%Y-%m-%d') as OutCreDate,  " +
                            " count(item.scanId) as OutNum    from S11_task as task  " +
                            " INNER JOIN S11_task_item as item on task.taskId = item.taskId   " +
                            " where SUBSTR(task.laneE,1,locate(\"-\",task.laneE)-1)  = task.source" +
                            " and cargoType = \"Transfer\"  " +
                            " and task.creDate >= :dateFrom" +
                            " and task.creDate <= :dateTo" +
                            " and task.scanType = 'out'" +
                            " GROUP BY Arc,OutCreDate";

            Query query = session.createSQLQuery(str);
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);

            List<Object[]> list = query.list();
            result = formatData(list, 10);
        } catch (Exception e) {
            log.error(e);
        }
        log.debug("getWareHousingByCondition is finished");
        return result;
    }

    public List<String> getScanIdbyOutOfFC(String carrier,
                                           String arc,
                                           String creDate ){
        List<String> result = new ArrayList<>();
        try {
            log.debug(carrier + arc + creDate);
            Session session = sessionFactory.getCurrentSession();

            String str =
                    "select DISTINCT item.scanId from S11_task_item  as item " +
                            " INNER JOIN  S11_task as task  on task.taskId = item.taskId " +
                            " where  task.carrierAbbr = :carrier" +
                            "  and date_format(task.creDate,'%Y-%m-%d') = :creDate " +
                            " and CONCAT(task.source,'-',task.destination) = :arc" +
                            " and task.scanType = 'out'";

            Query query = session.createSQLQuery(str);
            query.setParameter("carrier", carrier);
            query.setParameter("arc", arc);
            query.setParameter("creDate", creDate);
            List<Object> list = query.list();
            for(Object item : list){
                result.add(item.toString());
            }
        }catch (Exception e){
            log.error(e);
        }
        return result;
    }

    public List<String> getTaskIdInOfFCbyScanId(JSONArray ScanId,
                                                String creDate){
        List<String> result = new ArrayList<>();
        try {
            log.debug(ScanId);
            Session session = sessionFactory.getCurrentSession();

            String str =
                    "select DISTINCT task.taskId from S11_task as task  INNER JOIN S11_task_item as item" +
                            " on task.taskId = item.taskId where item.scanId in :ScanId " +
                            " and task.scanType = 'in'" +
                            " and task.creDate > :creDate";

            Query query = session.createSQLQuery(str);
            query.setParameterList("ScanId", ScanId);
            query.setParameter("creDate",creDate);
            List<Object> list = query.list();
            for(Object item : list){
                result.add(item.toString());
            }
        }catch (Exception e){
            log.error(e);
        }
        return result;
    }


    public  List<String> getDatebyTaskId(JSONArray taskId ){
        List<String> result = new ArrayList<>();
        try {

            Session session = sessionFactory.getCurrentSession();

            String str =
                    "select  DATE_FORMAT(item.scanDatetime,'%Y-%m-%d') from S11_task_item as item where taskId in :taskId limit 1";

            Query query = session.createSQLQuery(str);
            query.setParameterList("taskId", taskId);

            List<Object> list = query.list();
            for(Object item : list){
                result.add(item.toString());
            }
        }catch (Exception e){
            log.error(e);
        }
        return result;
    }


    public List<String> getIntervalScanIdbyOutOfFC(String carrier,
                                                   String arc,
                                                   String FromDate,
                                                   String ToDate){
        List<String> result = new ArrayList<>();
        try {
            log.debug(carrier + arc + FromDate  + ToDate);
            Session session = sessionFactory.getCurrentSession();

            String str =
                    "select item.scanId from S11_task_item  as item " +
                            " INNER JOIN  S11_task as task  on task.taskId = item.taskId " +
                            " where  task.carrierAbbr = :carrier" +
                            " and date_format(task.creDate,'%Y-%m-%d') >= :FromDate " +
                            " and date_format(task.creDate,'%Y-%m-%d') <= :ToDate " +
                            " and CONCAT(task.source,'-',task.destination) = :arc" +
                            " and task.scanType = 'out'";

            Query query = session.createSQLQuery(str);
            query.setParameter("carrier", carrier);
            query.setParameter("arc", arc);
            query.setParameter("FromDate", FromDate);
            query.setParameter("ToDate", ToDate);
            List<Object> list = query.list();
            for(Object item : list){
                result.add(item.toString());
            }
        }catch (Exception e){
            log.error(e);
        }
        return result;
    }

    public  List<String> getScanIDbyTaskId(JSONArray taskId,
                                           String creDate){
        List<String> result = new ArrayList<>();
        try {

            Session session = sessionFactory.getCurrentSession();

            String str =
                    " select DISTINCT scanId from S11_task_item as item where taskId in :taskId" +
                            " and scanDatetime> :creDate";

            Query query = session.createSQLQuery(str);
            query.setParameterList("taskId", taskId);
            query.setParameter("creDate",creDate);

            List<Object> list = query.list();
            for(Object item : list){
                result.add(item.toString());
            }
        }catch (Exception e){
            log.error(e);
        }
        return result;
    }

    private List<String[]> formatData(List<Object[]> result, int len) {
        log.debug("formatData is START");
        List<String[]> res = new ArrayList<>();
        for (Object[] obj : result) {
            String[] s = new String[len];
            for (int i = 0; i < len; ++i)
                s[i] = (i<obj.length&&obj[i]!=null) ? obj[i].toString():"";
            res.add(s);
        }
        log.debug("formatData is finished");
        return res;
    }

    public void updateStoreRate(StoreRate storeRate){
        try{
            Session session = sessionFactory.getCurrentSession();
            session.saveOrUpdate(storeRate);
        }catch (Exception e){
            log.error(e);
        }

    }

}
