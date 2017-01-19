package com.s11web.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.s11web.model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterfaceDao {
    private static final Logger log = Logger.getLogger(InterfaceDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void addS11TaskItemList(ArrayList<S11TaskItem> s11TaskItemList) {
        Session session = sessionFactory.getCurrentSession();
        for (int i = 0; i < s11TaskItemList.size(); ++i) {
            session.save(s11TaskItemList.get(i));
            if (i / 50 == 0) {
                session.flush();
                session.clear();
            }
        }
    }

    public void addS11Task(S11Task s11_task) {
        Session session = sessionFactory.getCurrentSession();
        session.save(s11_task);
    }

    public void addS11ExceptionItem(S11ExceptionItem s11ExceptionItem) {
        Session session = sessionFactory.getCurrentSession();
        session.save(s11ExceptionItem);
    }

    public void addS11ExceptionItemList(ArrayList<S11ExceptionItem> s11ExceptionItemList) {
        Session session = sessionFactory.getCurrentSession();
        for (int i = 0; i < s11ExceptionItemList.size(); ++i) {
            session.save(s11ExceptionItemList.get(i));
            if (i / 50 == 0) {
                session.flush();
                session.clear();
            }
        }
    }

    public HashSet<String> getScanList(String LaneE,
                                       String sourceFc,
                                       String destinationFc,
                                       String arcType,
                                       String leftTime,
                                       String rightTime) {
        Session session = sessionFactory.getCurrentSession();
        String sql = "select scanId from S11_task_item as taskItem, (SELECT taskId FROM S11_task where laneE = :LaneE and source = :sourceFc" +
                " and destination = :destinationFc and cargoType = :arcType and creDate > :leftTime and creDate < :rightTime ) as task " +
                "where taskItem.taskId = task.taskId";
        Query query = session.createSQLQuery(sql);
        query.setParameter("LaneE", LaneE);
        query.setParameter("sourceFc", sourceFc);
        query.setParameter("destinationFc", destinationFc);
        query.setParameter("arcType", arcType);
        query.setParameter("leftTime", leftTime);
        query.setParameter("rightTime", rightTime);
        log.info(String.format("%s	%s	%s	%s	%s	%s", LaneE, sourceFc, destinationFc, arcType, leftTime, rightTime));

        HashSet<String> result = new HashSet<>();
        result.addAll(query.list());
        return result;
    }

}
