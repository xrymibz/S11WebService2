package com.s11web.dao;

import java.util.ArrayList;
import java.util.List;
import com.s11web.exception.ExceptionMessage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.s11web.model.User;


/**
 * A data access object (DAO) providing persistence and search support for User entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how to configure it for
 * the desired type of transaction control.
 *
 * @author MyEclipse Persistence Tools
 */
@Repository
public class UserDao {
  private static final Logger log = Logger.getLogger(UserDao.class);

  @Autowired
  private SessionFactory sessionFactory;

  public int getUserId(String username) {

    int userID = 0;
    try {
      Query query = sessionFactory.getCurrentSession()
          .createSQLQuery("select id from zhw_user where name = :name");
      query.setString("name", username);
      if (query.list().size() != 0) {
        return Integer.parseInt(query.list().get(0).toString());
      }
      return userID;
    } catch (RuntimeException rte) {
      log.error("search failed", rte);
      throw rte;
    }
  }

  public User userLoginCheck(String name, String password) {

    try {
      String userQuery = "select name from User where name=:name and password=:password";
      Query query = sessionFactory.getCurrentSession().createQuery(userQuery);
      query.setString("name", name);
      query.setString("password", password);
      return query.list().size() > 0 ? (User) query.list().get(0) : null;
    } catch (RuntimeException rte) {
      log.error(ExceptionMessage.DATEBASE_CALL_EXCEPTION, rte);
      throw rte;
    }
  }

  public List<Object[]> getCarriersAbbr() {

    Session session = sessionFactory.getCurrentSession();
    String sqlStr = "select DISTINCT carrierId from zhw_carrier ORDER BY carrierId";
    Query query = session.createSQLQuery(sqlStr);
    List<Object[]> list = new ArrayList<>();
    if (query.list().size() != 0) {
      list = query.list();
    }
    return list;
  }

  public List getTabsByRole(Integer role) {

    String findTabAuth =
        "SELECT zhw_user_role.tabAuth FROM zhw_user_role WHERE zhw_user_role.id = :roleId  ";
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createSQLQuery(findTabAuth);
    query.setInteger("roleId", role);
    String tabAuth = (String) query.list().get(0);
    query = session.createSQLQuery("SELECT * FROM zhw_tabs WHERE zhw_tabs.tabAuth in ( :tabAuth )");
    query.setParameter("tabAuth", tabAuth);

    return query.list();
  }
}
