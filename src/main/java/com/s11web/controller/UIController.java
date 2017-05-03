package com.s11web.controller;

import java.io.UnsupportedEncodingException;
import java.util.*;

import com.s11web.util.DataOperation;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.s11web.service.UIService;
import com.s11web.util.JsonResult;

@Controller
public class UIController {

    private static final Logger log = Logger.getLogger(UIController.class);


    @Autowired
    private UIService uiService;

    @ResponseBody
    @RequestMapping(value = "/scanQuery")
    public ModelAndView scanQuery() {
        return new ModelAndView("jsp/scanQuery");
    }

//    @ResponseBody
//    @RequestMapping(value = "/scanAvegeArcTime")
//    public void scanAvegeArcTime() {
//        log.debug("kaishi");
//        uiService.getTaskId();
//        log.debug("jieshu");
//
//    }




    @ResponseBody
    @RequestMapping(value = "/exceptionQuery")
    public ModelAndView exceptionQuery() {
        return new ModelAndView("jsp/exceptionQuery");
    }

    @ResponseBody
    @RequestMapping(value = "/taskItem")
    public ModelAndView taskItem(@RequestParam("laneE") String laneE,
                                 @RequestParam("arcName") String arcName,
                                 @RequestParam("cargoType") String cargoType,
                                 @RequestParam("sortCode") String sortCode,
                                 @RequestParam("count") String count,
                                 @RequestParam("operateDate") String operateDate) {

        ModelAndView mav = new ModelAndView("jsp/taskItem");
        log.debug("laneE   :" + DataOperation.decode(laneE));
        mav.addObject("laneE", DataOperation.decode(laneE));
        mav.addObject("arcName", arcName);
        mav.addObject("cargoType", cargoType);
        mav.addObject("operateDate", operateDate);
        mav.addObject("sortCode", sortCode);
        mav.addObject("count", count);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/exceptionItem")
    public ModelAndView exceptionItem(@RequestParam("laneE") String laneE,
                                      @RequestParam("arcName") String arcName,
                                      @RequestParam("cargoType") String cargoType,
                                      @RequestParam("sortCode") String sortCode,
                                      @RequestParam("exceptionType") String exceptionType,
                                      @RequestParam("count") String count,
                                      @RequestParam("operateDate") String operateDate) {

        try {
            exceptionType = java.net.URLDecoder.decode(exceptionType, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ModelAndView mav = new ModelAndView("jsp/exceptionItem");
        mav.addObject("laneE", laneE);
        mav.addObject("arcName", arcName);
        mav.addObject("cargoType", cargoType);
        mav.addObject("operateDate", operateDate);
        mav.addObject("exceptionType", exceptionType);
        mav.addObject("sortCode", sortCode);
        mav.addObject("count", count);

        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/getCountByConditions")
    public JsonResult getCountByConditions(@ModelAttribute("data") String s) {


        log.debug("getCountByConditions is running " + s);
        boolean flag;
        String message;
        List<String[]> res = uiService.getCountByConditions(s);
        if (Objects.nonNull(res)) {
            flag = true;
            message = "get count info success!";
        } else {
            flag = false;
            message = "get count info error!";
        }
        log.debug(message);
        log.debug("getCountByConditions is completed ");
        return new JsonResult<>(flag, message, res);
    }

    @ResponseBody
    @RequestMapping(value = "/getTaskCount")
    public JsonResult getTaskCount(@ModelAttribute("data") String inputJsonStr) {

        String message;
        try {
            List<String[]> counts = uiService.getTaskCount(inputJsonStr);
            message = counts.size() <= 0 ? "the result has 0 element" : "success get task total count";
            log.debug(message);

            return new JsonResult<>(true, message, counts);
        } catch (Exception e) {
            message = "exception occur!";
            log.error(message, e);

            return new JsonResult<>(false, message, null);
        }

    }



    @ResponseBody
    @RequestMapping(value = "/getTaskItem")
    public JsonResult getTaskItem(@ModelAttribute("data") String inputJsonStr) {

        String message;
        try {
            List<String[]> result = uiService.getTaskItem(inputJsonStr);
            message = result.size() <= 0 ? "the result has 0 element" : "success get task item";
            log.debug(message);

            return new JsonResult<>(true, message, result);
        } catch (Exception e) {
            message = "exception occur!";
            log.error(message, e);

            return new JsonResult<>(false, message, null);
        }

    }



    @ResponseBody
    @RequestMapping(value = "/getLoadingRateByConditions")
    public JsonResult getLoadingRateByConditions(@ModelAttribute("data") String s) {


        log.debug("getLoadingRateByConditions is running " + s);
        boolean flag;
        String message;
        List<String[]> res = uiService.getLoadingRateByConditions(s);
        if (Objects.nonNull(res)) {
            flag = true;
            message = "getLoadingRateByConditions info success!";
        } else {
            flag = false;
            message = "get count info error!";
        }
        log.debug(message);
        for (String[] st : res) {
            st[10] = DataOperation.getNum(st[6],st[7]);
            st[6] = DataOperation.MergeCarType(st[6]);
            st[7] = DataOperation.MergeCarNum(st[7]);

        }
//        log.debug(res.get(0).toString());
        log.debug("getLoadingRateByConditions is completed ");
        return new JsonResult<>(flag, message, res);
    }


    @ResponseBody
    @RequestMapping(value = "/getLoadingRateOfChildren")
    public JsonResult getLoadingRateOfChildren(@ModelAttribute("data") String s) {


        log.debug("getLoadingRateOfChildren is running " + s);
        boolean flag;
        String message;
        List<String[]> res = uiService.getLoadingRateOfChildren(s);
        if (Objects.nonNull(res)) {
            flag = true;
            message = "getLoadingRateOfChildren info success!";
        } else {
            flag = false;
            message = "getLoadingRateOfChildren info error!";
        }
        log.debug(message);
//        log.debug(res.get(0).toString());
        log.debug("getLoadingRateOfChildren is completed ");
        return new JsonResult<>(flag, message, res);
    }

    @ResponseBody
    @RequestMapping(value = "/getLoadingRateCount")
    public JsonResult getLoadingRateCount(@ModelAttribute("data") String inputJsonStr) {

        String message;
        try {
            List<String[]> counts = uiService.getLoadingRateCount(inputJsonStr);
            message = counts.size() <= 0 ? "the result has 0 element" : "success getLoadingRateCount";
            log.debug(message);

            return new JsonResult<>(true, message, counts);
        } catch (Exception e) {
            message = "exception occur!";
            log.error(message, e);

            return new JsonResult<>(false, message, null);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/getLoadingRaTeItem")
    public JsonResult getLoadingRaTeItem(@ModelAttribute("data") String inputJsonStr) {

        String message;
        try {
            List<String[]> result = uiService.getLoadingRaTeItem(inputJsonStr);
            message = result.size() <= 0 ? "the result has 0 element" : "success getLoadingRaTeItem";
            log.debug(message);

            return new JsonResult<>(true, message, result);
        } catch (Exception e) {
            message = "exception occur!";
            log.error(message, e);

            return new JsonResult<>(false, message, null);
        }

    }




    @ResponseBody
    @RequestMapping(value = "/getWareHousingByCondition")
    public JsonResult getWareHousingByCondition(@ModelAttribute("data") String s) {


        log.debug("getWareHousingByCondition is running " + s);
        boolean flag;
        String message;
        List<String[]> res = uiService.getWareHousingByCondition(s);
        if (Objects.nonNull(res)) {
            if(res!=null&&res.size()>0){
                for(String[]item : res){
                    for(String i:item){
                        log.debug(i);
                    }
                    String[]temp = uiService.getWareHousingInfobyOutOfFC(item[0],item[1],item[5]);
                    item[7] = temp[1];
                    item[8] = temp[0];
                    log.debug( item[8]);
                    log.debug( item[6]);
                    item[9] = Integer.parseInt(item[8])/Integer.parseInt(item[6])+"";
                    for(String i:item){
                        log.debug(i);
                    }
                }
            }
            flag = true;
            message = "getWareHousingByCondition info success!";
        } else {
            flag = false;
            message = "get count info error!";
        }
        log.debug(message);

//        log.debug(res.get(0).toString());
        log.debug("getWareHousingByCondition is completed ");
        return new JsonResult<>(flag, message, res);
    }


    @RequestMapping(value = "/barcodeGenerate")
    @ResponseBody
    public ModelAndView barcodeGenerate() {
        return new ModelAndView("jsp/barcodeGenerate");
    }


    @RequestMapping(value = "/loadingRate")
    @ResponseBody
    public ModelAndView loadingRate() {
        return new ModelAndView("jsp/loadingRate");
    }

    @RequestMapping(value = "/loadingRateItem")
    @ResponseBody
    public ModelAndView loadingRateItem(@RequestParam("carrier") String carrier,
                                        @RequestParam("laneE") String laneE,
                                        @RequestParam("credate") String credate,
                                        @RequestParam("carType") String carType,
                                        @RequestParam("carNumber") String carNumber,
                                        @RequestParam("count") String count,
                                        @RequestParam("isSum") String isSum){


        ModelAndView mav = new ModelAndView("jsp/loadingRateItem");
        mav.addObject("carrier", DataOperation.decode(carrier));
        mav.addObject("laneE", DataOperation.decode(laneE));
        mav.addObject("credate", credate);
        mav.addObject("carType", DataOperation.decode(carType));
        mav.addObject("carNumber", DataOperation.decode(carNumber));
        mav.addObject("count", count);
        mav.addObject("isSum", isSum);
        return mav;
    }

    @RequestMapping(value = "/warehousing")
    @ResponseBody
    public ModelAndView warehousing() {
        return new ModelAndView("jsp/warehousing");
    }

    @RequestMapping(value = "/warehousingItem")
    @ResponseBody
    public ModelAndView warehousingItem() {
        return new ModelAndView("jsp/warehousingItem");
    }
}
