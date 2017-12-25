package com.s11web.controller;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import com.s11web.model.User;
import com.s11web.service.UserService;
import com.s11web.util.JsonResult;


@Controller
public class UserController {

  /** 日志 **/
  private static final Logger log = Logger.getLogger(UserController.class);

  private static final String SUCCESS = "成功!";
  private static final String FAILURE = "失败!";

  @Autowired
  private UserService userService;

  /**
   * 获取用户信息
   * 
   * @param userName 用户名
   * @return 用户的ID
   */
  @ResponseBody
  @RequestMapping(value = "/getUserId")
  public JsonResult<Integer> getUserId(@RequestParam String userName) {

    String message = String.format("get userId trucks for userName : %s\n", userName);

    try {
      message += SUCCESS;
      return new JsonResult<>(true, message, userService.getUserId(userName));
    } catch (Exception e) {
      message += FAILURE;
      log.error(message, e);
      return new JsonResult<>(false, message, null);
    }
  }

  /**
   * 账户登录
   *
   * @param name 用户名
   * @param password 密码
   * @param session Http的Session
   * @return 返回视图
   */
  @ResponseBody
  @RequestMapping("/login")
  public ModelAndView userLogin(@RequestParam String name, @RequestParam String password,
      HttpSession session) {

    User user = userService.userLoginCheck(name, password);

    if (Objects.isNull(user)) {
      ModelAndView mav = new ModelAndView("jsp/login");
      mav.addObject("flag", false);

      return mav;
    }
    log.debug(String.format("登陆成功!  登陆用户: %s", name));

    List<Object[]> tabs = userService.getTabsByRole(user.getRole());

    String jumpPage = "scanQuery";
    session.setAttribute("tabAuth", user.getRole());
    session.setAttribute("tabs", tabs);
    session.setAttribute("username", user.getName());
    session.setAttribute("user", user.toString());
    session.setAttribute("carrierList", userService.getCarriersAbbr());
    session.setAttribute("carrier", user.getCompany());

    ModelAndView mav = new ModelAndView(jumpPage);
    mav.setViewName("redirect:" + jumpPage);

    return mav;
  }

  /**
   * 账户登出
   *
   * @param status Session状态
   * @param session http的Session对象
   * @return
   */
  @ResponseBody
  @RequestMapping("/logout")
  public ModelAndView userLogout(SessionStatus status, HttpSession session) {

    status.setComplete();
    session.invalidate();
    ModelAndView mav = new ModelAndView();
    mav.setViewName("jsp/login");

    return mav;
  }

}
