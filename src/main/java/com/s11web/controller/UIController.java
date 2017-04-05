package com.s11web.controller;

import java.io.UnsupportedEncodingException;
import java.util.*;

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
        mav.addObject("laneE", laneE);
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
            exceptionType = java.net.URLDecoder.decode(exceptionType,"UTF-8");
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
    public ModelAndView loadingRateItem() {
        return new ModelAndView("jsp/loadingRateItem");
    }
}
