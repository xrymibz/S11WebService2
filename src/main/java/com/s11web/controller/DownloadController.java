package com.s11web.controller;

import com.s11web.model.SheetEntity;
import com.s11web.service.BarcodeGenerateService;
import com.s11web.service.UIService;
import com.s11web.util.Constants;
import com.s11web.util.ExcelOperator;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class DownloadController {

    private static final Logger log = Logger.getLogger(DownloadController.class);

    @Autowired
    private UIService uiService;

    @Autowired
    private BarcodeGenerateService barcodeGenerateService;

    @ResponseBody
    @RequestMapping(value = "/downloadScanItemDetail")
    public ModelAndView downloadScanItemDetail(@ModelAttribute(value = "data") String data,
                                               HttpServletResponse response) {

        log.debug("downloadScanItemDetail is running ");

        try {
            response.setContentType(Constants.mimeType.EXCEL.val());
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=DetailInfo-%d.xlsx", new Date().getTime()));
            XSSFWorkbook wb = uiService.downLoadItemDetail(data);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            log.debug("download detail information success!");

            return null;
        } catch (IOException e) {
            log.error(e);
            return new ModelAndView("jsp/errorPage");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/downloadLoadingRateItemDetail")
    public ModelAndView downloadLoadingRateItemDetail(@ModelAttribute(value = "data") String data,
                                               HttpServletResponse response) {

        log.debug("downloadLoadingRateItemDetail is running ");

        try {
            response.setContentType(Constants.mimeType.EXCEL.val());
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=DetailLoadingRateInfo-%d.xlsx", new Date().getTime()));
            XSSFWorkbook wb = uiService.downloadLoadingRateItemDetail(data);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            log.debug("download detail information success!");

            return null;
        } catch (IOException e) {
            log.error(e);
            return new ModelAndView("jsp/errorPage");
        }
    }


    @ResponseBody
    @RequestMapping(value = "/downloadScanInfo")
    public ModelAndView downloadScanInfo(@ModelAttribute(value = "data") String data,
                                         HttpServletResponse response) {

        try {
            response.setContentType(Constants.mimeType.EXCEL.val());
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=ScanInfo-%d.xlsx", new Date().getTime()));
            XSSFWorkbook wb = uiService.downloadScanInfo(data);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            log.debug("download scan information success!");

            return null;
        } catch (IOException e) {
            log.error(e);
            return new ModelAndView("jsp/errorPage");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/downloadExceptionInfo")
    public ModelAndView downloadExceptionInfo(@ModelAttribute(value = "data") String data,
                                              HttpServletResponse response) {

        try {
            response.setContentType(Constants.mimeType.EXCEL.val());
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=ExceptionInfo-%d.xlsx", new Date().getTime()));
            XSSFWorkbook wb = uiService.downloadExceptionInfo(data);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            log.debug("download exception information success!");

            return null;
        } catch (IOException e) {
            log.error(e);
            return new ModelAndView("jsp/errorPage");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/downloadImgZip")
    public ModelAndView downloadImgZip(@ModelAttribute(value = "data") String data,
                                       HttpServletResponse response) {

        try {
            response.setContentType(Constants.mimeType.ZIP.val());
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=ExceptionImage-%d.zip", new Date().getTime()));
            uiService.downloadExceptionImg(data, response.getOutputStream());
            log.debug("download exception image zip success!");

            return null;
        } catch (IOException e) {
            log.error(e);
            return new ModelAndView("jsp/errorPage");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/barcodeGenerateByCondition")
    public ModelAndView barcodeGenerateByCondition(@ModelAttribute("data") String inputJsonStr,
                                                   HttpServletResponse response,
                                                   HttpSession httpSession) {

        try {
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String cargoType = data.getString("cargoType");
            String source = data.getString("source");
            String destination = data.getString("destination");
            int number = data.getInt("number");

            log.debug(String.format("条码生成请求: %s, %s, %s, %d", cargoType, source, destination, number));
            List<String[]> barcodeList = barcodeGenerateService.barcodeGenerateByCondition(cargoType, source, destination, number, String.valueOf(httpSession.getAttribute("username")));

            log.debug("Begin to generate excel file and download...");

            SheetEntity sheetEntity = new SheetEntity();
            sheetEntity.setSheetName(cargoType);
            sheetEntity.setRowList(barcodeList);
            List<SheetEntity> sheetEntityList = Arrays.asList(sheetEntity);

            response.setContentType(Constants.mimeType.EXCEL.val());
            response.setHeader("Content-Disposition", String.format("attachment; filename=barcode-%d.xlsx", new Date().getTime()));
            XSSFWorkbook wb = new ExcelOperator().generateExcel(sheetEntityList);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            log.debug("download barcode file success!");

            return null;
        } catch (IOException e) {
            log.error(e);
            return new ModelAndView("jsp/errorPage");
        }
    }



    @ResponseBody
    @RequestMapping(value = "/downloadLoadingRateInfo")
    public ModelAndView downloadLoadingRateInfo(@ModelAttribute(value = "data") String data,
                                         HttpServletResponse response) {

        try {
            response.setContentType(Constants.mimeType.EXCEL.val());
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=LoadingRateInfo-%d.xlsx", new Date().getTime()));
            XSSFWorkbook wb = uiService.downloadLoadingRateInfo(data);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            log.debug("download  LoadingRate  Info success!");

            return null;
        } catch (IOException e) {
            log.error(e);
            return new ModelAndView("jsp/errorPage");
        }
    }
}
