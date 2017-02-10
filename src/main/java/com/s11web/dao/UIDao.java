package com.s11web.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.s11web.model.ArcAverageScanTime;
import com.s11web.model.S11Task;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UIDao {

    @Autowired
    private SessionFactory sessionFactory;

    private static final Logger log = Logger.getLogger(UIDao.class);

    public List<String[]> getCountByConditions(JSONArray carrierSelected,
                                               JSONArray laneSelected,
                                               JSONArray arcSelected,
                                               JSONArray cargoSelected,
                                               String dateFrom,
                                               String dateTo) {


        List<String[]> result = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            String str = " select tb.laneE,tb.arcName,tb.cargoType,tb.sortCode,count(S11_task_item.scanId),date_format(tb.creDate,'%Y-%m-%d')" +
                    " from S11_task_item inner join " +
                    " (select laneE,concat(source,'-',destination) as arcName,cargoType,sortCode,taskId,creDate from S11_task " +
                    " where carrierAbbr in :carrierSelected " +
                    (laneSelected.size() > 0 ?
                            (" and laneE in :laneSelected ") : "") +
                    (arcSelected.size() > 0 ?
                            (" and concat(source,'-',destination) in :arcSelected") : "") +
                    (cargoSelected.size() > 0 ?
                            (" and cargoType in :cargoSelected") : "") +
                    " and creDate >= :dateFrom" +
                    " and creDate <= :dateTo) as tb " +
                    " on S11_task_item.taskId = tb.taskId " +
                    " group by tb.laneE,tb.arcName,tb.cargoType,tb.sortCode,date_format(tb.creDate,'%Y-%m-%d') ";

            Query query = session.createSQLQuery(str);
            query.setParameterList("carrierSelected", carrierSelected);
            if (laneSelected.size() > 0) query.setParameterList("laneSelected", laneSelected);
            if (arcSelected.size() > 0) query.setParameterList("arcSelected", arcSelected);
            if (cargoSelected.size() > 0) query.setParameterList("cargoSelected", cargoSelected);
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);

            List<Object[]> list = query.list();
            result = formatData(list, 6);
        } catch (Exception e) {
            log.error(e);
        }

        return result;
    }

    public List<String[]> getExceptionCountByConditions(JSONArray carrierSelected,
                                                        JSONArray laneSelected,
                                                        JSONArray arcSelected,
                                                        JSONArray cargoSelected,
                                                        JSONArray exceptionSelected,
                                                        String dateFrom,
                                                        String dateTo) {
        List<String[]> result = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            String str = " select tb.laneE,tb.arcName,tb.cargoType,tb.sortCode,exception_tb.exceptionType,count(exception_tb.scanId),date_format(tb.creDate,'%Y-%m-%d')" +
                    " from " +
                    "(select * from S11_exception_item " +
                    (exceptionSelected.size() > 0 ?
                            "where S11_exception_item.exceptionType in (:exceptionSelected) " : "")
                    + ") as exception_tb" +
                    " inner join " +
                    " (select laneE,concat(source,'-',destination) as arcName,cargoType,sortCode,taskId,creDate from S11_task " +
                    " where carrierAbbr in :carrierSelected " +
                    (laneSelected.size() > 0 ?
                            (" and laneE in :laneSelected ") : "") +
                    (arcSelected.size() > 0 ?
                            (" and concat(source,'-',destination) in :arcSelected") : "") +
                    (cargoSelected.size() > 0 ?
                            (" and cargoType in :cargoSelected") : "") +
                    " and creDate >= :dateFrom" +
                    " and creDate <= :dateTo) as tb " +
                    " on exception_tb.taskId = tb.taskId " +
                    " group by tb.laneE,tb.arcName,tb.cargoType,tb.sortCode,exception_tb.exceptionType,date_format(tb.creDate,'%Y-%m-%d') ";

            Query query = session.createSQLQuery(str);
            query.setParameterList("carrierSelected", carrierSelected);
            if (exceptionSelected.size() > 0) query.setParameterList("exceptionSelected", exceptionSelected);
            if (laneSelected.size() > 0) query.setParameterList("laneSelected", laneSelected);
            if (arcSelected.size() > 0) query.setParameterList("arcSelected", arcSelected);
            if (cargoSelected.size() > 0) query.setParameterList("cargoSelected", cargoSelected);
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);

            List<Object[]> list = query.list();
            result = formatData(list, 7);

        } catch (Exception e) {
            log.error(e);
        }

        return result;
    }

    public List<String[]> getTaskCount(String laneE,
                                       String arc,
                                       String cargoType,
                                       String sortCode,
                                       String operateDate) {
        List<String[]> result = new ArrayList<>();
        try {

            String sql = "select DATE_FORMAT(S11_task.creDate, '%Y-%m-%d %H:%i:%s'), count(S11_task_item.scanId) " +
                    " from S11_task INNER JOIN S11_task_item " +
                    " on S11_task.taskId = S11_task_item.taskId " +
                    " where S11_task.laneE = :laneE " +
                    " and concat(S11_task.source,'-',S11_task.destination) = :arc " +
                    " and cargoType = :cargoType " +
                    (cargoType.equals("VReturn") ?
                            " and sortCode = :sortCode " : "") +
                    " and DATE_FORMAT(creDate,'%Y-%m-%d') = :operateDate " +
                    " group by S11_task.taskId";

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setParameter("laneE", laneE);
            query.setParameter("arc", arc);
            query.setParameter("cargoType", cargoType);
            if (cargoType.equals("VReturn")) query.setParameter("sortCode", sortCode);
            query.setParameter("operateDate", operateDate);

            List<Object[]> list = query.list();
            result = formatData(list, 2);
        } catch (Exception e) {
            log.error(e);
        }

        return result;
    }

    public List<String[]> getExceptionTaskCount(String laneE,
                                                String arc,
                                                String cargoType,
                                                String sortCode,
                                                String exceptionType,
                                                String operateDate) {
        List<String[]> result = new ArrayList<>();
        try {

            String sql = "select DATE_FORMAT(S11_task.creDate, '%Y-%m-%d %H:%i:%s'), count(S11_exception_item.scanId) " +
                    " from S11_task INNER JOIN S11_exception_item " +
                    " on S11_task.taskId = S11_exception_item.taskId " +
                    " where S11_task.laneE = :laneE " +
                    " and concat(S11_task.source,'-',S11_task.destination) = :arc " +
                    " and cargoType = :cargoType " +
                    (cargoType.equals("VReturn") ?
                            " and sortCode = :sortCode " : "") +
                    " and S11_exception_item.exceptionType = :exceptionType " +
                    " and DATE_FORMAT(S11_task.creDate,'%Y-%m-%d') = :operateDate " +
                    " group by S11_task.taskId";

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setParameter("laneE", laneE);
            query.setParameter("arc", arc);
            query.setParameter("cargoType", cargoType);
            if (cargoType.equals("VReturn")) query.setParameter("sortCode", sortCode);
            query.setParameter("exceptionType", exceptionType);
            query.setParameter("operateDate", operateDate);

            List<Object[]> list = query.list();
            result = formatData(list, 2);
        } catch (Exception e) {
            log.error(e);
        }
        return result;
    }

    public List<String[]> getTaskItem(String laneE,
                                      String arc,
                                      String cargoType,
                                      String sortCode,
                                      String operateDate) {
        List<String[]> result = new ArrayList<>();
        try {
            String sql = "select DATE_FORMAT(S11_task_item.scanDatetime, '%Y-%m-%d %H:%i:%s'), S11_task_item.scanId, S11_task_item.box " +
                    " from S11_task INNER JOIN S11_task_item " +
                    " on S11_task.taskId = S11_task_item.taskId " +
                    " where S11_task.laneE = :laneE " +
                    " and concat(S11_task.source,'-',S11_task.destination) = :arc " +
                    " and cargoType = :cargoType " +
                    (cargoType.equals("VReturn") ?
                            " and sortCode = :sortCode " : "") +
                    " and DATE_FORMAT(creDate,'%Y-%m-%d') = :operateDate";

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setParameter("laneE", laneE);
            query.setParameter("arc", arc);
            query.setParameter("cargoType", cargoType);
            if (cargoType.equals("VReturn")) query.setParameter("sortCode", sortCode);
            query.setParameter("operateDate", operateDate);

            List<Object[]> list = query.list();
            result = formatData(list, 3);
        } catch (Exception e) {
            log.error(e);
        }

        return result;
    }

    /**
     *
     * @return 得到任务的类型type和taskid号
     * @auther xietian
     */
    public List<ArcAverageScanTime> getTaskID() {
        List<ArcAverageScanTime> result = new ArrayList<>();
        try {

            String sql = "from S11Task where creDate>='2017-01-14' and creDate<='2017-01-16'";


            Query query = sessionFactory.getCurrentSession().createQuery(sql);

            List<S11Task> s1 = query.list();

            for(S11Task i : s1) {
                ArcAverageScanTime temp = new ArcAverageScanTime();
                temp.setType(i.getCargoType());
                temp.setTaskid(i.getTaskId());
                result.add(temp);
                log.debug(temp.getTaskid());
            }


  //          result = formatData(list, 3);
        } catch (Exception e) {
            log.error(e);
        }

        return result;
    }

    /**
     *
     * @return 得到同一个taskid号下面所有的数据的扫描时间
     * @auther xietian
     */
    public List<Date> getScanDatetimeList(String taskId) {
        List<Date> result = new ArrayList<>();
        try {

            String sql = "select   scanDatetime from S11_task_item  where taskId = :taskId order BY  scanDatetime";


            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setParameter("taskId", taskId);
            List<Date> s1 = query.list();

            for(Date i : s1) {
                log.debug(i);
            }


            result = s1;
            //          result = formatData(list, 3);
        } catch (Exception e) {
            log.error(e);
        }

        return result;
    }

    /**
     *
     * @return 存储数据表
     * @auther xietian
     */
    public void addArcAverageScanTime(ArcAverageScanTime arcAverageScanTime) {

        Session session = sessionFactory.getCurrentSession();
        session.save(arcAverageScanTime);
        log.debug("ok");
    }





    public List<String[]> getExceptionTaskItem(String laneE,
                                               String arc,
                                               String cargoType,
                                               String sortCode,
                                               String exceptionType,
                                               String operateDate) {
        List<String[]> result = new ArrayList<>();
        try {
            String sql = "select scanId, DATE_FORMAT(creDate, '%Y-%m-%d %H:%i:%s'), exceptionType, description, pictureUrl " +
                    "from S11_exception_item " +
                    "where taskId in " +
                    "(select taskId from S11_task " +
                    "where S11_task.laneE = :laneE and concat(S11_task.source,'-',S11_task.destination) = :arc " +
                    "and cargoType = :cargoType " +
                    (cargoType.equals("VReturn") ?
                            "and sortCode = :sortCode " : "") +
                    " and DATE_FORMAT(creDate,'%Y-%m-%d') = :operateDate) " +
                    " and exceptionType = :exceptionType";

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setParameter("laneE", laneE);
            query.setParameter("arc", arc);
            query.setParameter("cargoType", cargoType);
            if (cargoType.equals("VReturn")) query.setParameter("sortCode", sortCode);
            query.setParameter("exceptionType", exceptionType);
            query.setParameter("operateDate", operateDate);

            List<Object[]> list = query.list();
            result = formatData(list, 5);
        } catch (Exception e) {
            log.error(e);
        }

        return result;
    }

    public List<String[]> getExceptionDownloadItem(String laneE,
                                                   String arc,
                                                   String cargoType,
                                                   String sortCode,
                                                   String exceptionType,
                                                   String operateDate) {
        List<String[]> result = new ArrayList<>();
        try {
            String sql = "select scanId, DATE_FORMAT(creDate, '%Y-%m-%d %H:%i:%s'), exceptionType, description " +
                    "from S11_exception_item " +
                    "where taskId in " +
                    "(select taskId from S11_task " +
                    "where S11_task.laneE = :laneE and concat(S11_task.source,'-',S11_task.destination) = :arc " +
                    "and cargoType = :cargoType " +
                    (cargoType.equals("VReturn") ?
                            "and sortCode = :sortCode " : "") +
                    " and DATE_FORMAT(creDate,'%Y-%m-%d') = :operateDate) " +
                    " and exceptionType = :exceptionType";

            Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setParameter("laneE", laneE);
            query.setParameter("arc", arc);
            query.setParameter("cargoType", cargoType);
            if (cargoType.equals("VReturn")) query.setParameter("sortCode", sortCode);
            query.setParameter("exceptionType", exceptionType);
            query.setParameter("operateDate", operateDate);

            List<Object[]> list = query.list();
            result = formatData(list, 4);
        } catch (Exception e) {
            log.error(e);
        }

        return result;
    }

    public List<String[]> getAllInfoByConditions(JSONArray carrierSelected,
                                                 JSONArray laneSelected,
                                                 JSONArray arcSelected,
                                                 JSONArray cargoSelected,
                                                 String dateFrom,
                                                 String dateTo) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String str = " select tb.laneE,tb.arcName,tb.cargoType,tb.sortCode,date_format(tb.creDate,'%Y-%m-%d')," +
                    "S11_task_item.scanId,DATE_FORMAT(S11_task_item.scanDatetime, '%Y-%m-%d %H:%i:%s'), S11_task_item.box" +
                    " from S11_task_item inner join " +
                    " (select laneE,concat(source,'-',destination) as arcName,cargoType,sortCode,taskId,creDate from S11_task " +
                    " where carrierAbbr in :carrierSelected " +
                    (laneSelected.size() > 0 ?
                            (" and laneE in :laneSelected ") : "") +
                    (arcSelected.size() > 0 ?
                            (" and concat(source,'-',destination) in :arcSelected ") : "") +
                    (cargoSelected.size() > 0 ?
                            (" and cargoType in :cargoSelected") : "") +
                    " and creDate >= :dateFrom " +
                    " and creDate <= :dateTo ) as tb " +
                    " on S11_task_item.taskId = tb.taskId ";

            Query query = session.createSQLQuery(str);
            query.setParameterList("carrierSelected", carrierSelected);
            if (laneSelected.size() > 0) query.setParameterList("laneSelected", laneSelected);
            if (arcSelected.size() > 0) query.setParameterList("arcSelected", arcSelected);
            if (cargoSelected.size() > 0) query.setParameterList("cargoSelected", cargoSelected);
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);

            List<Object[]> result = query.list();

            return formatData(result, 8);
        } catch (Exception e) {
            log.info(e);

            return null;
        }
    }

    public List<String[]> getExceptionAllInfoByConditions(JSONArray carrierSelected,
                                                          JSONArray laneSelected,
                                                          JSONArray arcSelected,
                                                          JSONArray cargoSelected,
                                                          JSONArray exceptionTypeSelected,
                                                          String dateFrom,
                                                          String dateTo) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String str = "select tb.laneE, tb.arcName, tb.cargoType, tb.sortCode, date_format(tb.creDate,'%Y-%m-%d')," +
                    "exception_tb.scanId,DATE_FORMAT(exception_tb.creDate, '%Y-%m-%d %H:%i:%s'), exception_tb.exceptionType, exception_tb.description " +
                    "from " +
                    "(select * from S11_exception_item " +
                    (exceptionTypeSelected.size() > 0 ?
                            "where exceptionType in :exceptionTypeSelected" : "") +
                    ") as exception_tb " +
                    "join " +
                    " (select laneE,concat(source,'-',destination) as arcName,cargoType,sortCode,taskId,creDate from S11_task " +
                    " where carrierAbbr in :carrierSelected " +
                    (laneSelected.size() > 0 ?
                            (" and laneE in :laneSelected ") : "") +
                    (arcSelected.size() > 0 ?
                            (" and concat(source,'-',destination) in :arcSelected ") : "") +
                    (cargoSelected.size() > 0 ?
                            (" and cargoType in :cargoSelected") : "") +
                    " and creDate >= :dateFrom " +
                    " and creDate <= :dateTo ) as tb " +
                    " on exception_tb.taskId = tb.taskId";

            Query query = session.createSQLQuery(str);
            if (exceptionTypeSelected.size() > 0)
                query.setParameterList("exceptionTypeSelected", exceptionTypeSelected);
            query.setParameterList("carrierSelected", carrierSelected);
            if (laneSelected.size() > 0) query.setParameterList("laneSelected", laneSelected);
            if (arcSelected.size() > 0) query.setParameterList("arcSelected", arcSelected);
            if (cargoSelected.size() > 0) query.setParameterList("cargoSelected", cargoSelected);
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);

            List<Object[]> result = query.list();

            return formatData(result, 9);
        } catch (Exception e) {
            log.info(e);

            return null;
        }
    }

    public List<String[]> getExceptionInfoByDay(String laneE,
                                                String source,
                                                String destination,
                                                String cargoType,
                                                String sortCode,
                                                String fromTime,
                                                String toTime) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String sql = "select scanId, DATE_FORMAT(creDate, '%Y-%m-%d %H:%i:%s'), exceptionType, description, pictureUrl " +
                    "from S11_exception_item " +
                    "where taskId in " +
                    " (select taskId from S11_task " +
                    " where laneE = :laneE " +
                    " and source = :source " +
                    " and destination = :destination " +
                    " and cargoType = :cargoType" +
                    (cargoType.equals("VReturn") ?
                            " and sortCode = :sortCode" : "") +
                    " and creDate >= :fromTime and creDate <= :toTime )";

            Query query = session.createSQLQuery(sql);
            query.setParameter("laneE", laneE);
            query.setParameter("source", source);
            query.setParameter("destination", destination);
            query.setParameter("cargoType", cargoType);
            if (cargoType.equals("VReturn")) query.setParameter("sortCode", sortCode);
            query.setParameter("fromTime", fromTime);
            query.setParameter("toTime", toTime);

            List<Object[]> result = query.list();
            log.info(String.format("%s |%s |%s |%s |%s |%s |%s", laneE, source, destination, cargoType, sortCode, fromTime, toTime));

     //       log.info(result.get(0)[0]+" "+result.get(0)[1]+" "+result.get(0)[2]+" "+result.get(0)[3]);

            return formatData(result, 5);
        } catch (Exception e) {
            log.info(e);

            return null;
        }
    }

    private List<String[]> formatData(List<Object[]> result, int len) {
        List<String[]> res = new ArrayList<>();
        for (Object[] obj : result) {
            String[] s = new String[len];
            for (int i = 0; i < len; ++i)
                s[i] = obj[i] == null ? "" : obj[i].toString();
            res.add(s);
        }
        log.debug("formatData is finished");
        return res;
    }


    public List<Object[]> getLanesByCarrier(String carrierId,String isInjection) {

        log.debug("isInjection  :"+isInjection);
        Session session = sessionFactory.getCurrentSession();
        String sql = "select laneId, laneE, laneName,laneType" +
                " FROM lc_v2_now_lane " +
                " where carrierId in (" +
                "select id from lc_carrier where lc_carrier.carrierId = :carrierId) ";

        if (isInjection.equals("Injection")){
            sql+="and laneType='Injection'  and isDeleted = 0";
        }else{
            sql+=  " and isDeleted = 0";
        }

        System.out.println(sql);
        Query query = session.createSQLQuery(sql);
        query.setParameter("carrierId", carrierId);

        List<Object[]> res = query.list();
        System.out.println(res);
        return res;
    }

    public List<Object[]> getcartypeBycarier(String carrierId) {

        Session session = sessionFactory.getCurrentSession();
        String sql = "select id,cartype" +
                " FROM zhw_cartype " ;



        System.out.println(sql);
        Query query = session.createSQLQuery(sql);

        List<Object[]> res = query.list();
        System.out.println(res);
        return res;
    }

}
