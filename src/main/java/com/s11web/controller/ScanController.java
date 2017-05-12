package com.s11web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.s11web.service.UIService;
import com.s11web.util.ZipUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.s11web.service.CarrierUserService;
import com.s11web.util.JsonResult;
import com.s11web.util.Md5Util;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("scan")
public class ScanController {

    @Autowired
    private CarrierUserService carrierUserService;

    @Autowired
    private UIService uiService;

    @Autowired
    private Md5Util md5Util;

    private static final Logger log = Logger.getLogger(ScanController.class);

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/scanLogin", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult scanLogin(@RequestParam String sign,
                                @RequestParam String carrierName,
                                @RequestParam String username,
                                @RequestParam String password) {

        String scanLogin = "scanLogin";
        String message;

        if (!md5Util.authentication(sign, scanLogin)) {
            message = "Md5校验失败!";
            log.debug(message);
            return new JsonResult<>(false, message, null);
        }

        Object[] login_info = carrierUserService.carrierUserLogin(carrierName, username, password);
        if (login_info != null) {
            message = String.format("登陆成功! 登陆用户: %s", username);
            log.debug(message);
            return new JsonResult<>(true, message, login_info);
        } else {
            message = "用户名或密码错误,登陆失败!";
            log.debug(message);
            return new JsonResult<>(false, message, 0);
        }
    }

    @RequestMapping(value = "/getScanHistory")
    @ResponseBody
    public JsonResult getScanHistory(@RequestParam String sign,
                                     @RequestParam String laneE,
                                     @RequestParam String date,
                                     @RequestParam String userIdStr) {

        String getScanHistory = "getScanHistory";
        boolean flag;
        String message;
        List<Object[]> result = null;
        log.debug("getScanHistory is running ");
        if (!md5Util.authentication(sign, getScanHistory)) {
            message = "Md5校验失败!";
            log.debug(message);
            return new JsonResult<>(false, message, null);
        }

        try {
            int userId = Integer.parseInt(userIdStr);

            result = carrierUserService.getScanHistory(laneE, date, userId);
            log.debug(result.toString());
            message = (result.size() != 0 ? "获取历史记录成功" : "无历史记录");
            log.debug(message);
            flag = true;
        } catch (Exception e) {
            message = "获取历史记录失败!";
            log.error(message, e);
            flag = false;
        }

        return new JsonResult<>(flag, message, result);
    }

    @RequestMapping(value = "/getExceptionHistory")
    @ResponseBody
    public JsonResult getExceptionHistory(@RequestParam String sign,
                                          @RequestParam String laneE,
                                          @RequestParam String source,
                                          @RequestParam String destination,
                                          @RequestParam String cargoType,
                                          @RequestParam String sortCode,
                                          @RequestParam String fromTime,
                                          @RequestParam String toTime) {

        String getExceptionHistory = "getExceptionHistory";
        String message;
        if (!md5Util.authentication(sign, getExceptionHistory)) {
            message = "Md5校验失败!";
            log.debug(message);
            return new JsonResult<>(false, message, null);
        }

        try {
            JSONObject inputJsonObject = new JSONObject();
            inputJsonObject.put("laneE", laneE);
            inputJsonObject.put("source", source);
            inputJsonObject.put("destination", destination);
            inputJsonObject.put("cargoType", cargoType);
            inputJsonObject.put("sortCode", sortCode);
            inputJsonObject.put("fromTime", fromTime);
            inputJsonObject.put("toTime", toTime);

            List<String[]> result = uiService.getExceptionInfoByDay(inputJsonObject.toString());
            message = "获取异常记录成功!";
//            log.debug(result.get(0));
            log.debug(message);
            //数据压缩中文乱码。现在暂时先关掉
            String compressData = ZipUtil.compress(JSONArray.fromObject(result).toString());
//            String compressData = JSONArray.fromObject(result).toString();
            return new JsonResult<>(true, message, compressData);
        }
//        catch (IOException e) {
//            message = "压缩数据出错!";
//            log.error(message, e);
//
//            return new JsonResult<>(true, message, null);
         catch (Exception e) {
            message = "获取异常记录失败!";
            log.error(message, e);

            return new JsonResult<>(true, message, null);
        }
    }



    @RequestMapping(value = "/getcartypeBycarrier")
    @ResponseBody
    public JsonResult getcartypeBycarier(@RequestParam String carrierAbbr) {
        String message = null;
        boolean flag = false;
        message = "start getting cartype by carrierId";
        System.out.println(message);
//        String carrierAbbr = request.getParameter("carrierAbbr");
        System.out.println(carrierAbbr);
        if (carrierAbbr != null) {
            List<Object[]> cartypeList = uiService.getCarTypeBycarrier(carrierAbbr);
            List<JSONObject> result = new ArrayList<JSONObject>();

            log.debug(cartypeList);
            for (Object[] cartypeInfo : cartypeList) {
                String cartype = (String) cartypeInfo[1];

                String item = "{'cartype':'" + cartype + "'}";
                //System.out.println(item);
                JSONObject item2 = JSONObject.fromObject(item);
                result.add(item2);

            }
            message = "success getting lanes";
            flag = true;
            return new JsonResult<List<JSONObject>>(flag, message, result);
        } else {
            message = "the param is error";
            return new JsonResult<Integer>(flag, message, 0);
        }
    }


    @RequestMapping(value = "/getcarnumberBycarrier")
    @ResponseBody
    public JsonResult getcarnumberBycarier(@RequestParam String carrierAbbr,
                                            @RequestParam String carType,
                                            @RequestParam String laneName) {
        String message = null;
        boolean flag = false;
        message = "start getting cartype by carrierId";
        log.debug(message);
//        String carrierAbbr = request.getParameter("carrierAbbr");
//        String carType = request.getParameter("carType");
//        String laneName = request.getParameter("laneName");
        log.debug(carrierAbbr + carType + laneName);
        if (carrierAbbr != null) {
            List<Object[]> cartypeList = uiService.getCarNumberBycarrier(carrierAbbr, carType,laneName);
            List<JSONObject> result = new ArrayList<JSONObject>();

            log.debug(cartypeList);
            for (Object[] cartypeInfo : cartypeList) {
                String carNumber = (String) cartypeInfo[1];

                String item = "{'carNumber':'" + carNumber + "'}";
                //System.out.println(item);
                JSONObject item2 = JSONObject.fromObject(item);
                result.add(item2);

            }
            message = "success getting lanes";
            flag = true;
            return new JsonResult<List<JSONObject>>(flag, message, result);
        } else {
            message = "the param is error";
            return new JsonResult<Integer>(flag, message, 0);
        }
    }
}

