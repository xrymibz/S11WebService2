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
import com.s11web.util.DataOperation;
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
import org.springframework.stereotype.Service;


@Service("uiService")
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


    public List<String[]> getWareHousingByCondition(String inputJsonStr) {
        try {
            log.debug("uiService.getWareHousingByCondition is running :" + inputJsonStr);
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String dateFrom = data.getString("fromDate");
            log.debug("dateFrom : " + dateFrom);
            String dateTo = data.getString("toDate");
            JSONArray carriers = data.getJSONArray("carriers");
            JSONArray arcList = data.getJSONArray("arcList");
            log.debug("the carrier is :" + carriers);
            return uiDao.getWareHousingByCondition(carriers, arcList, dateFrom, dateTo);
        } catch (Exception e) {
            log.info("error when get count", e);

            return null;
        }
    }
    public List<String[]> getWarehousingItem(String inputJsonStr) {
        try {
            log.debug("uiService.getWarehousingItem is running :" + inputJsonStr);
            JSONObject data = JSONObject.fromObject(inputJsonStr);

            String carrierName = data.getString("carrierName");
            String cargoesType = data.getString("cargoesType");
            String arc = data.getString("arc");
            String departureDate = data.getString("departureDate");
            log.debug("the carrier is :" + carrierName);
            return uiDao.getWarehousingItem(carrierName, cargoesType, arc, departureDate);
        } catch (Exception e) {
            log.info("error when get count", e);

            return null;
        }
    }



    public String[] getWareHousingInfobyOutOfFC(String carrier,String arc,String creDate){
       try {
           String[]res = new String[2];
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
           //发货当天前后两天内的扫描记录
           log.debug("get interval date  :"  +creDate);
           Date date = sdf.parse(creDate);
           String FromDate = sdf.format(new Date(date.getTime() - (long)2 * 24 * 60 * 60 * 1000));
           String toDate = sdf.format(new Date(date.getTime() + (long)2 * 24 * 60 * 60 * 1000));
           log.debug("getWareHousingInfobyOutOfFC is starting");
           //获得当天所有的扫描ID
           List<String> OutOfFCScanId = uiDao.getScanIdbyOutOfFC(carrier, arc, creDate);
           //获得前后五天内所有的扫描ID
 //          List<String> IntervalOutOfFCScanId = uiDao.getIntervalScanIdbyOutOfFC(carrier, arc, FromDate,toDate);
            //获得入库的所有的taskID
           List<String>taskId = uiDao.getTaskIdInOfFCbyScanId( JSONArray.fromObject(OutOfFCScanId),creDate);
           List<String> InOfFCScanId = new ArrayList<>();
           if(taskId!=null&&taskId.size()!=0) {
               InOfFCScanId = uiDao.getScanIDbyTaskId(JSONArray.fromObject(taskId),creDate);
           }
           log.debug("getWareHousingInfobyOutOfFC is finished");

            if(InOfFCScanId==null ||InOfFCScanId.size() ==0) //如果货物没有入库
            {
                res[0] = "-";
                res[1] = "0";
            }else{
                List<String>InOfFCDate = uiDao.getDatebyTaskId(JSONArray.fromObject(taskId));
                res[0] = InOfFCDate.get(0);
                res[1] = InOfFCDate.size()+"";
                //关掉查询漏件        res[1] = getNumByScanId(OutOfFCScanId,IntervalOutOfFCScanId,InOfFCScanId) +"";
                res[1] = getNumByScanId(OutOfFCScanId,InOfFCScanId) +"";
            }

           return res;
       }catch (Exception e ){
           log.error(e);
           return null;
       }
    }

    //关掉查询漏件
    public int getNumByScanId(List<String>outFC,List<String>inFC){
        int num = 0;
        for(String i:inFC){
 //           if(outFC.contains(i)||!intervalOutFC.contains(i)){
            if(outFC.contains(i)){
                num++;
            }
        }
        return num;
    }

    public List<String[]> getLoadingRateCount(String inputJsonStr) {
        try {
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String carrier = data.getString("carrier");
            String laneE = data.getString("laneE");
            String credate = data.getString("credate");
            String carType = data.getString("carType");
            String carNumber = data.getString("carNumber");
            String isSum = data.getString("isSum");
            return uiDao.getLoadingRateCount(carrier, laneE, credate, carType, carNumber,isSum);
        } catch (Exception e) {
            log.info("error when get task count", e);

            return null;
        }
    }

    public List<String[]> getLoadingRaTeItem(String inputJsonStr) {

        try {
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String carrier = data.getString("carrier");
            String laneE = data.getString("laneE");
            String credate = data.getString("credate");
            String carType = data.getString("carType");
            String carNumber = data.getString("carNumber");
            String isSum = data.getString("isSum");
            return uiDao.getLoadingRaTeItem(carrier, laneE, credate, carType, carNumber,isSum);
        } catch (Exception e) {
            log.info("error when get task item", e);

            return null;
        }

    }

    /**
     *
     * @return得到arc的平均扫描时间
     */
//    public List<String[]> getTaskId() {
//
//        try {
//
//            List<ArcAverageScanTime>   ArcAverageScanTimes =  uiDao.getTaskID();
//            SimpleDateFormat myFmt1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd");
//
//            for(ArcAverageScanTime demo1:ArcAverageScanTimes){
//
//                List<Date> dates =  uiDao.getScanDatetimeList(demo1.getTaskid());
//
//                if(dates.size()>0){
//
//
//                Date date = myFmt2.parse(myFmt2.format(dates.get(0)));
//       //         String    date = myFmt2.format(dates.get(0));
//                int scanNumber = dates.size();
//                long start = dates.get(0).getTime();
//                long end = dates.get(scanNumber-1).getTime();
//                double avgscantime = (end-start)/1000.00/scanNumber;
//                //avgscantime 保留两位小数
//                DecimalFormat df = new DecimalFormat("#.00");
//                String f_trans = df.format(avgscantime);
//                avgscantime = Double.parseDouble(f_trans);
//
//                demo1.setDate(date);
//                demo1.setScanNumber(scanNumber);
//                demo1.setAvgScanTime(avgscantime);
//
//                uiDao.addArcAverageScanTime(demo1);
//                System.out.println("date : "+ demo1.getDate() +"   type :  "+demo1.getType() +"   taskid  :"+demo1.getTaskid() +"   scanNumber : "+ demo1.getScanNumber() +"   avgscantime  :"+demo1.getAvgScanTime());
//
//                log.debug(avgscantime);
//
//            }
//            }
//
//   //         System.out.println("now : "+i);
//  //          System.out.println("later:  " + myFmt1.format(new Date(i.getTime()+1000)));
//          return null;
//        } catch (Exception e) {
//            log.info("error when  getTaskId", e);
//
//            return null;
//        }
//
//    }



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

    public XSSFWorkbook downloadLoadingRateInfo(String inputJsonStr) {

        String[] colNameList;
        SheetEntity summarySheetInfo = new SheetEntity();
        List<String[]> taskResult = getLoadingRateByConditions(inputJsonStr);

        for (String[] st : taskResult) {
            st[6] = DataOperation.MergeCarType(st[6]);
            st[7] = DataOperation.MergeCarNum(st[7]);
        }
        colNameList = new String[]{
                Constants.excelColumnName.CARRIER_NAME.val(),
                Constants.excelColumnName.LANE_NAME.val(),
                Constants.excelColumnName.SHIP_DATE.val(),
                Constants.excelColumnName.SUM.val(),
                Constants.excelColumnName.VOL_SUM.val(),
                Constants.excelColumnName.WEI_SUM.val(),
                Constants.excelColumnName.CAR_TYPE.val(),
                Constants.excelColumnName.CAR_NUMBER.val(),
                Constants.excelColumnName.WATER_VOL.val(),
                Constants.excelColumnName.LOADING_RATE.val()};
        taskResult.add(0, colNameList);
        summarySheetInfo.setSheetName("Scan Summary");
        summarySheetInfo.setRowList(taskResult);

//        SheetEntity detailSheetInfo = new SheetEntity();
//        List<String[]> scanResult = getAllInfoByConditions(inputJsonStr);
//        colNameList = new String[]{
//                Constants.excelColumnName.LANE_NAME.val(),
//                Constants.excelColumnName.ARC_NAME.val(),
//                Constants.excelColumnName.CARGO_TYPE.val(),
//                Constants.excelColumnName.SORT_CODE.val(),
//                Constants.excelColumnName.SHIP_DATE.val(),
//                Constants.excelColumnName.SCAN_ID.val(),
//                Constants.excelColumnName.SCAN_TIME.val(),
//                Constants.excelColumnName.BOX_TYPE.val()};
//        scanResult.add(0, colNameList);
//        detailSheetInfo.setSheetName("Scan Detail");
//        detailSheetInfo.setRowList(scanResult);
//
        ArrayList<SheetEntity> sheetEntityList = new ArrayList<>();
        sheetEntityList.add(summarySheetInfo);
//        sheetEntityList.add(detailSheetInfo);

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


    public XSSFWorkbook downloadLoadingRateItemDetail(String inputJsonStr) {

        String exceptionType = null;
        JSONObject data = JSONObject.fromObject(inputJsonStr);
        String carrier = data.getString("carrier");
        String laneE = data.getString("laneE");
        String credate = data.getString("credate");
        String carType = data.getString("carType");
        String carNumber = data.getString("carNumber");
        String count = data.getString("count");
        String isSum = data.getString("isSum");

        List<String[]> taskResult;


        taskResult = uiDao.getLoadingRaTeItem(carrier, laneE, credate, carType, carNumber,isSum);



        String[] colNameList;
        String[] dataList;

        SheetEntity summarySheetInfo = new SheetEntity();

            colNameList = new String[]{
                    Constants.excelColumnName.CARRIER_NAME.val(),
                    Constants.excelColumnName.LANE_NAME.val(),
                    Constants.excelColumnName.SUM.val(),
                    Constants.excelColumnName.SCAN_TIME.val()};
            taskResult.add(0, colNameList);
            dataList = new String[]{carrier, laneE, count, credate};
            taskResult.add(1, dataList);
            taskResult.add(2, new String[0]);
            colNameList = new String[]{
                    Constants.excelColumnName.LANE_NAME.val(),
                    Constants.excelColumnName.SCAN_ID.val(),
                    Constants.excelColumnName.SCAN_TIME.val(),
                    Constants.excelColumnName.BOX_TYPE.val(),
                    Constants.excelColumnName.VOL.val(),
                    Constants.excelColumnName.WEI.val()};
            taskResult.add(3, colNameList);

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
        log.debug("formatImgUrl is finished");
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

    public List<Object[]> getLanesByCarrier(String carrierId,String isInjection) {

        log.debug("isInjection :"+isInjection);
        if(isInjection==null) {
            isInjection="no";
        }else{
            if(isInjection.equals("1"))
                isInjection="Injection";
            else
                isInjection="no";
        }

        return uiDao.getLanesByCarrier(carrierId,isInjection);
    }

    public List<Object[]> getCarTypeBycarrier(String carrierAbbr) {

        return uiDao.getCarTypeBycarrier(carrierAbbr);
    }

    public List<Object[]> getCarNumberBycarrier(String carrierAbbr,String carType, String laneName) {

        return uiDao.getCarNumberBycarrier(carrierAbbr,carType, laneName);
    }

    public List<String[]> getLoadingRateByConditions(String inputJsonStr) {
        try {
            log.debug("uiService.getLoadingRateByConditions is running :" + inputJsonStr);
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String dateFrom = data.getString("fromDate");
            log.debug("dateFrom : " + dateFrom);
            String dateTo = data.getString("toDate");
            JSONArray carriers = data.getJSONArray("carriers");
            JSONArray laneList = data.getJSONArray("laneList");
            JSONArray cargoTypeList = data.getJSONArray("cargoTypeList");
            return uiDao.getLoadingRateByConditions(carriers, laneList, cargoTypeList, dateFrom, dateTo);
        } catch (Exception e) {
            log.info("error when get count", e);

            return null;
        }
    }

    public List<String[]> getLoadingRateOfChildren(String inputJsonStr) {
        try {
            log.debug("getLoadingRateOfChildren is running :" + inputJsonStr);
            JSONObject data = JSONObject.fromObject(inputJsonStr);
            String credate = data.getString("credate");
            log.debug("credate : " + credate);
            String carrier = data.getString("carrier");
            String laneE = data.getString("laneE");
            return uiDao.getLoadingRateOfChildren(carrier, laneE,credate);
        } catch (Exception e) {
            log.info("error when get count", e);

            return null;
        }
    }
}
