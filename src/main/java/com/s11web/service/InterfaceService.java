package com.s11web.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.s11web.model.*;
import net.sf.json.JSONArray;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.SSLContextBuilder;

import com.s11web.dao.InterfaceDao;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("interfaceService")
public class InterfaceService {

    @Autowired
    private InterfaceDao interfaceDao;

    @Value("${laneConfigUrl}")
    private String laneConfigUrl;

    private static final Logger log = Logger.getLogger(InterfaceService.class);

    public String addS11TaskItemInfo(JSONObject s11TaskItemJson) {

        String message;
        String taskId = s11TaskItemJson.getString("taskId");
        JSONArray scanItems = s11TaskItemJson.getJSONArray("scanItems");
        //add all task item
        ArrayList<S11TaskItem> s11TaskItemList = new ArrayList<>();
        int scanItemNum = 0;
        for (int i = 0; i < scanItems.size(); ++i) {
            S11TaskItem s11_taskItem = new S11TaskItem();
            try {
                s11_taskItem.setTaskId(taskId);
                s11_taskItem.setScanId(scanItems.getJSONObject(i).getString("scanId"));
                Date scanItemTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(scanItems.getJSONObject(i).getString("scanDatetime"));
                s11_taskItem.setScanDatetime(scanItemTime);
                s11_taskItem.setBox(scanItems.getJSONObject(i).getString("box"));
                s11TaskItemList.add(s11_taskItem);
                ++scanItemNum;
            } catch (ParseException e) {
                log.info(scanItems.getJSONObject(i).getString("scanDatetime"), e);
            }
        }

        interfaceDao.addS11TaskItemList(s11TaskItemList);

        if (scanItems.size() > 0)
            message = String.format("共尝试上传 %d 条条码信息\n成功上传 %d 条", scanItems.size(), scanItemNum);
        else
            message = "未上传条码数据\n";
        log.info(message);

        return message;
    }

    public void addS11TaskInfo(JSONObject s11TaskJson) {

        S11Task s11_task = new S11Task();
        s11_task.setUserId(s11TaskJson.getInt("userId"));
        s11_task.setCargoType(s11TaskJson.getString("cargoType"));
        s11_task.setSource(s11TaskJson.getString("source"));
        s11_task.setDestination(s11TaskJson.getString("destination"));
        s11_task.setLaneE(s11TaskJson.getString("laneE"));
        s11_task.setLaneName(s11TaskJson.getString("laneName"));
        s11_task.setCarrierAbbr(s11TaskJson.getString("carrierAbbr"));
        s11_task.setTaskId(s11TaskJson.getString("taskId"));
        if(s11TaskJson.getString("scanType")==null){
            s11_task.setScanType("out");
        }else {
            s11_task.setScanType(s11TaskJson.getString("scanType"));
        }
        if(s11TaskJson.getString("carType")!=null&&s11TaskJson.getString("carType").length()!=0){
            s11_task.setCarType(s11TaskJson.getString("carType"));
            double WaterVol = getWaterVolbyCarType(s11TaskJson.getString("carType"));
//            log.debug(WaterVol+"WaterVol");
            s11_task.setWaterVol(WaterVol);
        }
        if(s11TaskJson.get("carNumber")!=null){
            s11_task.setCarNumber(s11TaskJson.getString("carNumber"));
        }
        if (s11TaskJson.getString("cargoType").equals("VReturn"))
            s11_task.setSortCode(s11TaskJson.getString("sortCode"));
        try {
            s11_task.setCreDate(s11TaskJson.getString("creDate").equalsIgnoreCase("null") ?
                    null : (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(s11TaskJson.getString("creDate")));
        } catch (ParseException e) {
            log.error(s11TaskJson.getString("creDate"), e);
        }

        interfaceDao.addS11Task(s11_task);
        log.info("任务信息保存成功!");
    }

    public boolean addS11ExceptionItemInfo(JSONObject exceptionItemJson) {

        S11ExceptionItem s11ExceptionItem = new S11ExceptionItem();
        try {
            s11ExceptionItem.setTaskId(exceptionItemJson.getString("taskId"));
            s11ExceptionItem.setScanId(exceptionItemJson.getString("scanId"));
            s11ExceptionItem.setExceptionType(exceptionItemJson.getString("exceptionType"));
            s11ExceptionItem.setDescription(exceptionItemJson.getString("description"));
            s11ExceptionItem.setCreDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(exceptionItemJson.getString("time")));
            s11ExceptionItem.setPictureUrl(exceptionItemJson.getString("imgUrlListStr"));
            interfaceDao.addS11ExceptionItem(s11ExceptionItem);

            log.info("异常信息保存成功!  :"+exceptionItemJson.getString("imgUrlListStr"));
            return true;
        } catch (ParseException e) {
            log.error(exceptionItemJson.getString("time"), e);
            return false;
        } catch (Exception e) {
            log.error("Exception Item upload error!", e);
            return false;
        }
    }

    public HashSet<String> getScanList(String LaneE,
                                       String sourceFc,
                                       String destinationFc,
                                       String arcType,
                                       String leftTime,
                                       String rightTime,
                                       String scanType) {
        return interfaceDao.getScanList(LaneE, sourceFc, destinationFc, arcType, leftTime, rightTime,scanType);
    }

    /**
     * 根据Arc信息获取所包含的Lane信息,Lane信息包括LaneE、departTime、deliveryDuration
     *
     * @param sourceFc
     * @param destinationFc
     * @param arcType
     * @return 返回Lane信息集合
     * @throws Exception
     */
    public List<Lane> getLaneInfoByArc(String sourceFc,
                                       String destinationFc,
                                       String arcType) throws Exception {
        String url = String.format("%s/getLanesByArc?sourceFc=%s&destinationFc=%s&arcType=%s", laneConfigUrl, sourceFc, destinationFc, arcType);

        //CloseableHttpClient httpclient = HttpClients.createDefault();
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
                sslsf).build();
        ArrayList<Lane> laneList = new ArrayList<>();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                HttpEntity entity = response.getEntity();
                JSONObject resultObj = JSONObject.fromObject(EntityUtils.toString(entity));
                log.info(resultObj.toString());
                if (resultObj.getBoolean("success")) {
                    JSONArray jsonArray = JSONArray.fromObject(resultObj.get("data"));
                    for (int i = 0; i < jsonArray.size(); ++i) {
                        JSONObject laneInfo = JSONObject.fromObject(jsonArray.get(i));
                        log.info(String.format("%s	%s	%s", laneInfo.getString("laneE"), laneInfo.getString("departTime"), laneInfo.getInt("deliveryDuration")));
                        Lane lane = new Lane();
                        lane.setLaneE(laneInfo.getString("laneE"));
                        lane.setDepartTime(laneInfo.getString("departTime"));
                        lane.setDeliveryDuration(laneInfo.getInt("deliveryDuration"));
                        laneList.add(lane);
                    }
                }
                EntityUtils.consume(entity);
            }
            response.close();
        } catch (Exception e) {
            log.info("Https get lane by arc error!", e);
        } finally {
            httpclient.close();
        }

        return laneList;
    }

    /**
     * 根据当前的Lane和Arc下的Lane信息计算得到前序的LaneE 以及 预估时间
     *
     * @param currentLaneE
     * @param LanesInArcList
     * @return 前序的LaneE 以及 预估时间
     */
    public HashMap<String, String> getPreviousLaneInfo(String currentLaneE, List<Lane> LanesInArcList, String arcSourceFc, Date currentTime,String scanType) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 设定与原定出发时间2小时误差内可获取
        long delayInterval = 2 * 60 * 60 * 1000;
        // 设定获取扫描清单的时间区间为24小时
        long scanInterval = 24 * 60 * 60 * 1000;
        // 用以保存前序LaneE以及预估时间
        HashMap<String, String> result = new HashMap<>();

        //入庫掃描
        if(scanType.equals("in")){
            result = proceedLaneInfo(LanesInArcList.get(LanesInArcList.size()-1),currentTime,dateFormat,delayInterval,scanInterval);
            return result;
        }


        String currentLaneStartFc = splitLaneE(currentLaneE)[0];
        // Lane的起始与Arc起始重合，当前的currentLaneE是第一条路径
        if (currentLaneStartFc.contains(arcSourceFc)) {
            result.put("LaneE", currentLaneE);
            return result;
        }
        // Lane不是Arc的起始
        for (Lane laneInfo : LanesInArcList) {
            String laneEndFc = splitLaneE(laneInfo.getLaneE())[1];
            if (isLaneFcMatched(currentLaneStartFc, laneEndFc)) {   //当前的开始是哪一条的结束，找到了之后，就能知道上一条的开始
                result = proceedLaneInfo(laneInfo,currentTime,dateFormat,delayInterval,scanInterval);
                break;
            }
        }
        return result;
    }

    private  HashMap<String, String> proceedLaneInfo( Lane laneInfo,Date currentTime,SimpleDateFormat dateFormat,long delayInterval,long scanInterval){
        HashMap<String, String> result = new HashMap<>();
        result.put("LaneE", laneInfo.getLaneE());
        String departDateStr = String.format("%s %s", (new SimpleDateFormat("yyyy-MM-dd")).format(currentTime), laneInfo.getDepartTime());
        try {
            long departDate = dateFormat.parse(departDateStr).getTime();
            //                   log.debug("departDate" + dateFormat.format(new Date(departDate)));
            departDate += laneInfo.getDeliveryDuration() * 60 * 60 * 1000;
            //                   log.debug("departDate" + dateFormat.format(new Date(departDate)));
            while (departDate >= currentTime.getTime())
                departDate -= 24 * 60 * 60 * 1000;
            //                   log.debug("currentTime.getTime()" + dateFormat.format(new Date(currentTime.getTime())));
            //                   log.debug("departDate" + dateFormat.format(new Date(departDate)));
            departDate = departDate - laneInfo.getDeliveryDuration() * 60 * 60 * 1000 + delayInterval;
            //                  log.debug("departDate" + dateFormat.format(new Date(departDate)));
            result.put("rightTime", dateFormat.format(new Date(departDate)));
            result.put("leftTime", dateFormat.format(new Date(departDate - scanInterval)));

            //                   log.debug("leftTime" + dateFormat.format(new Date(departDate - scanInterval)));
            //                   log.debug("rightTime" + dateFormat.format(new Date(departDate)));
        } catch (ParseException e) {
            log.info("Time Parse Exception", e);
        }
        return result;
    }
    /**
     * 根据现有业务情况切分LaneE,得到起始和终止FC信息
     *
     * @param laneE
     * @return 起始和终止FC信息
     */
    private String[] splitLaneE(String laneE) {
        if (laneE == null) return null;
        String[] fcList = laneE.split("-");
        String[] resultList = new String[2];
        if (fcList.length == 2) {
            resultList[0] = fcList[0];
            resultList[1] = fcList[1];
        } else if (fcList.length == 3) {
            if (laneE.startsWith("PEK3-TSN2") || laneE.startsWith("TSN2-PEK3")) {
                resultList[0] = fcList[0] + "-" + fcList[1];
                resultList[1] = fcList[2];
            } else if (laneE.endsWith("PEK3-TSN2") || laneE.endsWith("TSN2-PEK3")) {
                resultList[0] = fcList[0];
                resultList[1] = fcList[1] + "-" + fcList[2];
            } else if (fcList[2].matches("\\d+")) {
                resultList[0] = fcList[0];
                resultList[1] = fcList[1];
            }
        }
        return resultList;
    }

    /**
     * 判断某个LaneE的终点FC和当前LaneE的起点FC是否匹配
     *
     * @param startFc 当前LaneE的起点FC
     * @param endFc   某个LaneE的终点FC
     * @return
     */
    private boolean isLaneFcMatched(String startFc, String endFc) {
        ArrayList<String> startFcList = new ArrayList<>();
        ArrayList<String> endFcList = new ArrayList<>();

        for (String fc : startFc.split("-")) startFcList.add(fc);
        for (String fc : endFc.split("-")) endFcList.add(fc);

        // 起点和终点包含的FC数量相同
        if (startFcList.size() == endFcList.size()) {
            // 起点和终点信息均只有一个FC且相等
            if (startFcList.size() == 1 && startFc.equals(endFc))
                return true;
                // 起点和终点信息均包含两个FC,那么无论顺序如何,一定是PEK3与TSN2的组合,属于相等
            else
                return startFcList.size() == 2;
        }
        // 起点和终点包含的FC数量不相同时
        else {
            // 如果起点只包含一个FC,那么如果终点Lane内包含起点的FC即为匹配
            if (startFcList.size() == 1)
                return endFcList.contains(startFc);
                // 如果终点只包含一个FC,那么如果起点Lane内包含终点的FC即为匹配
            else if (endFcList.size() == 1)
                return startFcList.contains(endFc);
                // 这是异常情况,目前业务应该不包含当前情况
            else
                return false;
        }
    }

    public List<String> storeFilesToS3(String bucketName, List<MultipartFile> files) {

        String key;
        InputStream inputStream;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        String today = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        ArrayList<String> successKeyList = new ArrayList<>();

        AmazonS3 s3 = new AmazonS3Client();
        Region CN_NORTH_1 = Region.getRegion(Regions.CN_NORTH_1);
        s3.setRegion(CN_NORTH_1);

        for (MultipartFile file : files) {
            try {
                key = String.format("%s/%s", today, UUID.randomUUID().toString());
                inputStream = file.getInputStream();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());
//                log.debug(file.getSize()+"  "+file.getContentType());
                s3.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata));
                log.debug(String.format("S3 putObject success! key info: %s", key));

                successKeyList.add(key);
            } catch (AmazonClientException ace) {
                log.error("Caught an AmazonClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with S3, "
                        + "such as not being able to access the network."
                        + "\nError Message: " + ace.getMessage());
            } catch (IOException e) {
                log.error(e);
            }
        }
        return successKeyList;
    }
    public Double getWaterVolbyCarType(String s){
        String[]s1 = s.split("\\(");
        String[]s2 = s1[1].split("\\)");
        return Double.parseDouble(s2[0]);
    }
}
