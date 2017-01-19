package com.s11web.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.s11web.model.ArcAverageScanTime;
import com.s11web.model.SheetEntity;
import com.s11web.util.Constants;
import com.s11web.util.ZipUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.s11web.util.ExcelOperator;
import com.s11web.dao.UIDao;


@Component("uiService")
public class UIService {



    @Autowired
    private UIDao uiDao;

    @Value("${s3Address}")
    private String s3Address;

    @Value("${bucketName}")
    private String bucketName;

    private static final Logger log = Logger.getLogger(UIService.class);

    public List<String[]> getCountByConditions(String inputJsonStr) {

        try {

            log.debug("uiService.getCountByConditions is running :" + inputJsonStr);
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String dateFrom = data.getString("fromDate");
            log.debug("dateFrom : " + dateFrom);
            String dateTo = data.getString("toDate");
            JSONArray carriers = data.getJSONArray("carriers");
            JSONArray laneList = data.getJSONArray("laneList");
            JSONArray arcList = data.getJSONArray("arcList");
            JSONArray cargoTypeList = data.getJSONArray("cargoTypeList");
            if (data.containsKey("exceptionList")) {
                JSONArray exceptionList = data.getJSONArray("exceptionList");



                return uiDao.getExceptionCountByConditions(carriers, laneList, arcList,
                        cargoTypeList, exceptionList, dateFrom, dateTo);
            }

            return uiDao.getCountByConditions(carriers, laneList, arcList, cargoTypeList, dateFrom, dateTo);
        } catch (Exception e) {
            log.info("error when get count", e);

            return null;
        }
    }

    public List<String[]> getTaskCount(String inputJsonStr) {
        try {
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String laneE = data.getString("laneE");
            String arc = data.getString("arc");
            String cargoType = data.getString("cargoType");
            String sortCode = data.getString("sortCode");
            String operateDate = data.getString("operateDate");
            if (data.containsKey("exceptionType")) {
                String exceptionType = data.getString("exceptionType");

                return uiDao.getExceptionTaskCount(laneE, arc, cargoType, sortCode, exceptionType, operateDate);
            }

            return uiDao.getTaskCount(laneE, arc, cargoType, sortCode, operateDate);
        } catch (Exception e) {
            log.info("error when get task count", e);

            return null;
        }
    }




    public List<String[]> getTaskItem(String inputJsonStr) {

        try {
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String laneE = data.getString("laneE");
            String arc = data.getString("arc");
            String cargoType = data.getString("cargoType");
            String sortCode = data.getString("sortCode");
            String operateDate = data.getString("operateDate");
            if (data.containsKey("exceptionType")) {
                String exceptionType = data.getString("exceptionType");

                return formatImgUrl(uiDao.getExceptionTaskItem(laneE, arc, cargoType, sortCode, exceptionType, operateDate), 4);
            }

            return uiDao.getTaskItem(laneE, arc, cargoType, sortCode, operateDate);
        } catch (Exception e) {
            log.info("error when get task item", e);

            return null;
        }

    }

    /**
     *
     * @return得到arc的平均扫描时间
     */
    public List<String[]> getTaskId() {

        try {

            List<ArcAverageScanTime>   ArcAverageScanTimes =  uiDao.getTaskID();
            SimpleDateFormat myFmt1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd");

            for(ArcAverageScanTime demo1:ArcAverageScanTimes){

                List<Date> dates =  uiDao.getScanDatetimeList(demo1.getTaskid());

                if(dates.size()>0){


                Date date = myFmt2.parse(myFmt2.format(dates.get(0)));
       //         String    date = myFmt2.format(dates.get(0));
                int scanNumber = dates.size();
                long start = dates.get(0).getTime();
                long end = dates.get(scanNumber-1).getTime();
                double avgscantime = (end-start)/1000.00/scanNumber;
                //avgscantime 保留两位小数
                DecimalFormat df = new DecimalFormat("#.00");
                String f_trans = df.format(avgscantime);
                avgscantime = Double.parseDouble(f_trans);

                demo1.setDate(date);
                demo1.setScanNumber(scanNumber);
                demo1.setAvgScanTime(avgscantime);

                uiDao.addArcAverageScanTime(demo1);
                System.out.println("date : "+ demo1.getDate() +"   type :  "+demo1.getType() +"   taskid  :"+demo1.getTaskid() +"   scanNumber : "+ demo1.getScanNumber() +"   avgscantime  :"+demo1.getAvgScanTime());

                log.debug(avgscantime);

            }
            }









   //         System.out.println("now : "+i);
  //          System.out.println("later:  " + myFmt1.format(new Date(i.getTime()+1000)));




            return null;
        } catch (Exception e) {
            log.info("error when  getTaskId", e);

            return null;
        }

    }



    private List<String[]> getAllInfoByConditions(String inputJsonStr) {
        try {
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String dateFrom = data.getString("fromDate");
            String dateTo = data.getString("toDate");
            JSONArray carriers = data.getJSONArray("carriers");
            JSONArray laneList = data.getJSONArray("laneList");
            JSONArray arcList = data.getJSONArray("arcList");
            JSONArray cargoTypeList = data.getJSONArray("cargoTypeList");
            if (data.containsKey("exceptionList")) {
                JSONArray exceptionList = data.getJSONArray("exceptionList");

                return uiDao.getExceptionAllInfoByConditions(carriers, laneList, arcList,
                        cargoTypeList, exceptionList, dateFrom, dateTo);
            }

            return uiDao.getAllInfoByConditions(carriers, laneList, arcList, cargoTypeList, dateFrom, dateTo);
        } catch (Exception e) {
            log.info("error when get all info", e);

            return null;
        }
    }

    public List<String[]> getExceptionInfoByDay(String inputJsonStr) {

        JSONObject data = JSONObject.fromObject(inputJsonStr);
        String laneE = data.getString("laneE");
        String source = data.getString("source");
        String destination = data.getString("destination");
        String cargoType = data.getString("cargoType");
        String sortCode = data.getString("sortCode");
        String fromTime = data.getString("fromTime");
        String toTime = data.getString("toTime");

        return formatImgUrl(uiDao.getExceptionInfoByDay(laneE, source, destination, cargoType, sortCode, fromTime, toTime), 4);
    }

    public XSSFWorkbook downloadScanInfo(String inputJsonStr) {

        String[] colNameList;

        SheetEntity summarySheetInfo = new SheetEntity();
        List<String[]> taskResult = getCountByConditions(inputJsonStr);
        colNameList = new String[]{
                Constants.excelColumnName.LANE_NAME.val(),
                Constants.excelColumnName.ARC_NAME.val(),
                Constants.excelColumnName.CARGO_TYPE.val(),
                Constants.excelColumnName.SORT_CODE.val(),
                Constants.excelColumnName.SUM.val(),
                Constants.excelColumnName.SHIP_DATE.val()};
        taskResult.add(0, colNameList);
        summarySheetInfo.setSheetName("Scan Summary");
        summarySheetInfo.setRowList(taskResult);

        SheetEntity detailSheetInfo = new SheetEntity();
        List<String[]> scanResult = getAllInfoByConditions(inputJsonStr);
        colNameList = new String[]{
                Constants.excelColumnName.LANE_NAME.val(),
                Constants.excelColumnName.ARC_NAME.val(),
                Constants.excelColumnName.CARGO_TYPE.val(),
                Constants.excelColumnName.SORT_CODE.val(),
                Constants.excelColumnName.SHIP_DATE.val(),
                Constants.excelColumnName.SCAN_ID.val(),
                Constants.excelColumnName.SCAN_TIME.val(),
                Constants.excelColumnName.BOX_TYPE.val()};
        scanResult.add(0, colNameList);
        detailSheetInfo.setSheetName("Scan Detail");
        detailSheetInfo.setRowList(scanResult);

        ArrayList<SheetEntity> sheetEntityList = new ArrayList<>();
        sheetEntityList.add(summarySheetInfo);
        sheetEntityList.add(detailSheetInfo);

        return (new ExcelOperator()).generateExcel(sheetEntityList);
    }

    public XSSFWorkbook downloadExceptionInfo(String inputJsonStr) {

        String[] colNameList;

        SheetEntity summarySheetInfo = new SheetEntity();
        List<String[]> taskResult = getCountByConditions(inputJsonStr);
        colNameList = new String[]{
                Constants.excelColumnName.LANE_NAME.val(),
                Constants.excelColumnName.ARC_NAME.val(),
                Constants.excelColumnName.CARGO_TYPE.val(),
                Constants.excelColumnName.SORT_CODE.val(),
                Constants.excelColumnName.EXCEPTION_TYPE.val(),
                Constants.excelColumnName.SUM.val(),
                Constants.excelColumnName.SHIP_DATE.val()};
        taskResult.add(0, colNameList);
        summarySheetInfo.setSheetName("Exception Summary");
        summarySheetInfo.setRowList(taskResult);

        SheetEntity detailSheetInfo = new SheetEntity();
        List<String[]> scanResult = getAllInfoByConditions(inputJsonStr);
        colNameList = new String[]{
                Constants.excelColumnName.LANE_NAME.val(),
                Constants.excelColumnName.ARC_NAME.val(),
                Constants.excelColumnName.CARGO_TYPE.val(),
                Constants.excelColumnName.SORT_CODE.val(),
                Constants.excelColumnName.SHIP_DATE.val(),
                Constants.excelColumnName.SCAN_ID.val(),
                Constants.excelColumnName.SCAN_TIME.val(),
                Constants.excelColumnName.EXCEPTION_TYPE.val(),
                Constants.excelColumnName.DESCRIPTION.val()};
        scanResult.add(0, colNameList);
        detailSheetInfo.setSheetName("Exception Detail");
        detailSheetInfo.setRowList(scanResult);

        ArrayList<SheetEntity> sheetEntityList = new ArrayList<>();
        sheetEntityList.add(summarySheetInfo);
        sheetEntityList.add(detailSheetInfo);

        return (new ExcelOperator()).generateExcel(sheetEntityList);
    }

    public XSSFWorkbook downLoadItemDetail(String inputJsonStr) {

        String exceptionType = null;
        JSONObject data = JSONObject.fromObject(inputJsonStr);
        String laneE = data.getString("laneE");
        String arcName = data.getString("arc");
        String cargoType = data.getString("cargoType");
        String sortCode = data.getString("sortCode");
        String operateDate = data.getString("operateDate");
        String shipNumber = data.getString("shipNumber");
        List<String[]> taskResult;

        if (data.containsKey("exceptionType")) {
            exceptionType = data.getString("exceptionType");
            taskResult = uiDao.getExceptionDownloadItem(laneE, arcName, cargoType, sortCode, exceptionType, operateDate);
        } else {
            taskResult = uiDao.getTaskItem(laneE, arcName, cargoType, sortCode, operateDate);
        }

        String[] colNameList;
        String[] dataList;

        SheetEntity summarySheetInfo = new SheetEntity();

        if (Objects.isNull(exceptionType)) {
            colNameList = new String[]{
                    Constants.excelColumnName.LANE_NAME.val(),
                    Constants.excelColumnName.ARC_NAME.val(),
                    Constants.excelColumnName.CARGO_TYPE.val(),
                    Constants.excelColumnName.SORT_CODE.val(),
                    Constants.excelColumnName.OPERATE_DATE.val(),
                    Constants.excelColumnName.SHIP_NUMBER.val()};
            taskResult.add(0, colNameList);
            dataList = new String[]{laneE, arcName, cargoType, sortCode, operateDate, shipNumber};
            taskResult.add(1, dataList);
            taskResult.add(2, new String[0]);
            colNameList = new String[]{
                    Constants.excelColumnName.OPERATE_DATE.val(),
                    Constants.excelColumnName.SCAN_ID.val(),
                    Constants.excelColumnName.BOX_TYPE.val()};
            taskResult.add(3, colNameList);
        } else {
            colNameList = new String[]{
                    Constants.excelColumnName.LANE_NAME.val(),
                    Constants.excelColumnName.ARC_NAME.val(),
                    Constants.excelColumnName.CARGO_TYPE.val(),
                    Constants.excelColumnName.SORT_CODE.val(),
                    Constants.excelColumnName.EXCEPTION_TYPE.val(),
                    Constants.excelColumnName.OPERATE_DATE.val(),
                    Constants.excelColumnName.SHIP_NUMBER.val()};
            taskResult.add(0, colNameList);
            dataList = new String[]{laneE, arcName, cargoType, sortCode, exceptionType, operateDate, shipNumber};
            taskResult.add(1, dataList);
            taskResult.add(2, new String[0]);
            colNameList = new String[]{
                    Constants.excelColumnName.OPERATE_DATE.val(),
                    Constants.excelColumnName.SCAN_ID.val(),
                    Constants.excelColumnName.EXCEPTION_TYPE.val(),
                    Constants.excelColumnName.DESCRIPTION.val()};
            taskResult.add(3, colNameList);

        }
        summarySheetInfo.setSheetName("Task Details");
        summarySheetInfo.setRowList(taskResult);

        List<SheetEntity> sheetEntityList = Arrays.asList(summarySheetInfo);

        return (new ExcelOperator()).generateExcel(sheetEntityList);
    }

    private List<String[]> formatImgUrl(List<String[]> inputData, int imgCol) {
        for (String[] strings : inputData) {
            if (strings.length <= imgCol) {
                log.error("img column number excess length of data");
                return inputData;
            }
            if (StringUtils.isNotEmpty(strings[imgCol])) {
                String formatUrl = "";
                String[] urlArray = strings[imgCol].split("\t");
                for (String url : urlArray) {
                    formatUrl += String.format("\t%s/%s/%s", s3Address, bucketName, url);
                }
                strings[imgCol] = formatUrl.trim();
            }
        }
        return inputData;
    }

    public void downloadExceptionImg(String inputJsonStr, OutputStream os) throws IOException {

        JSONObject data = JSONObject.fromObject(inputJsonStr);
        String laneE = data.getString("laneE");
        String arc = data.getString("arc");
        String cargoType = data.getString("cargoType");
        String sortCode = data.getString("sortCode");
        String operateDate = data.getString("operateDate");
        String exceptionType = data.getString("exceptionType");
        List<String[]> exceptionTaskItem = uiDao.getExceptionTaskItem(laneE, arc, cargoType, sortCode, exceptionType, operateDate);

        AmazonS3 s3 = new AmazonS3Client();
        Region cnNorth1 = Region.getRegion(Regions.CN_NORTH_1);
        s3.setRegion(cnNorth1);

        ArrayList<InputStream> inputStreamList = new ArrayList<>();
        ArrayList<String> fileNameList = new ArrayList<>();
        for (String[] itemInfo : exceptionTaskItem) {
            String scanId = itemInfo[0];
            String imgUrlStr = itemInfo[4];
            if (StringUtils.isEmpty(imgUrlStr)) {
                continue;
            }
            String[] imgUrlArray = imgUrlStr.split("\t");
            for (int i = 0; i < imgUrlArray.length; ++i) {
                try {
                    InputStream inputStream = s3.getObject(new GetObjectRequest(bucketName, imgUrlArray[i])).getObjectContent();
                    if (!Objects.isNull(inputStream)) {
                        inputStreamList.add(inputStream);
                        fileNameList.add(String.format("%s-%d.png", scanId, i));
                    }
                } catch (AmazonClientException ace) {
                    log.error("Caught an AmazonClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with S3, "
                            + "such as not being able to access the network."
                            + "\nError Message: " + ace.getMessage());
                }
            }
        }
        ZipUtil.ZipMultiFile(inputStreamList, fileNameList, os);
    }

    public List<Object[]> getLanesByCarrier(String carrierId) {

        return uiDao.getLanesByCarrier(carrierId);
    }

}
