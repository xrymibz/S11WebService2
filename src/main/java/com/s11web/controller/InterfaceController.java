package com.s11web.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.s11web.model.*;
import com.s11web.util.ZipUtil;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.s11web.service.InterfaceService;
import com.s11web.util.JsonResult;
import com.s11web.util.Md5Util;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Controller
@RequestMapping("/interface")
public class InterfaceController {

    private static final Logger log = Logger.getLogger(InterfaceController.class);

    @Autowired
    private InterfaceService interfaceService;

    @Autowired
    private Md5Util md5Util;

    @Value("${s3Address}")
    private String s3Address;

    @Value("${bucketName}")
    private String bucketName;

    /**
     * S11 upload data
     *
     * @param sign
     * @param scanContent
     * @return upload message
     */
    @RequestMapping("/scanPickup")
    @ResponseBody
    public JsonResult scanPickup(@RequestParam String sign,
                                 @RequestParam String scanContent) {

        String scanPickupMethod = "scanPickup";
        String message;

        if (!md5Util.authentication(sign, scanPickupMethod)) {
            message = "Md5校验出错!\n";
            log.debug(message);

            return new JsonResult<>(false, message);
        }
//        try {
//            log.debug(scanContent);
//            try(ByteArrayOutputStream out = new ByteArrayOutputStream();
//                GZIPInputStream gzipInputStream = new GZIPInputStream(
//                        new ByteArrayInputStream(scanContent.getBytes("ISO-8859-1")))) {
//                IOUtils.copy(gzipInputStream, out);
//            }
//            scanContent = ZipUtil.unCompress(scanContent);
//        } catch (IOException e) {
//            message = "上传数据解析出错!";
//            log.error(message, e);
//
//            return new JsonResult<>(false, message);
//        }

        log.debug(scanContent);
        JSONObject scanContentJsonObject = JSONObject.fromObject(scanContent);
        if (!scanContentJsonObject.containsKey("taskInfo") || !scanContentJsonObject.containsKey("scanInfo")) {
            message = "未检测到任务数据和扫描数据!\n";
            log.debug(message);

            return new JsonResult<>(false, message);
        }

        interfaceService.addS11TaskInfo(scanContentJsonObject.getJSONObject("taskInfo"));
        message = interfaceService.addS11TaskItemInfo(scanContentJsonObject.getJSONObject("scanInfo"));
        log.debug(message);

        return new JsonResult<>(true, message);
    }

    /**
     * Get ToBeScan List and Scaned List
     *
     * @param sign
     * @param sourceFc
     * @param destinationFc
     * @param arcType
     * @param laneE
     * @param time
     * @return ToBeScan List & Scaned List
     */
    @RequestMapping("/getCompareList")
    @ResponseBody
    public JsonResult getCompareList(@RequestParam String sign,
                                     @RequestParam String sourceFc,
                                     @RequestParam String destinationFc,
                                     @RequestParam String arcType,
                                     @RequestParam String laneE,
                                     @RequestParam String time) {

        String getCompareListMethod = "getCompareList";
        boolean flag = false;
        String message;
        HashSet<String> toBeScanedIdSet = new HashSet<>();
        HashSet<String> scanedIdSet = new HashSet<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long CheckDuplicationTimeInterval = 3 * 60 * 60 * 1000; // 目前设定查重时间范围为三个小时以内

        if (!md5Util.authentication(sign, getCompareListMethod)) {
            message = "Md5校验出错!\n";
            log.debug(message);

            return new JsonResult<>(false, message, null);
        }

        try {
            Date currentTime = dateFormat.parse(time);

            List<Lane> LanesInArcList = this.interfaceService.getLaneInfoByArc(sourceFc, destinationFc, arcType);
            HashMap<String, String> previousLaneInfo = this.interfaceService.getPreviousLaneInfo(laneE, LanesInArcList, sourceFc, currentTime);

            // 判断是否有前序LaneE信息
            if (previousLaneInfo.containsKey("LaneE")) {
                // 判断是否有时间区间信息
                if (previousLaneInfo.containsKey("rightTime") && previousLaneInfo.containsKey("leftTime")) {
                    toBeScanedIdSet = this.interfaceService.getScanList(
                            previousLaneInfo.get("LaneE"), sourceFc, destinationFc, arcType, previousLaneInfo.get("leftTime"), previousLaneInfo.get("rightTime"));
                    log.debug(String.format("共获取%d条前序条码记录!", toBeScanedIdSet.size()));
                }
                // 对于没有获取到时间区间信息的LaneE, 只获取当前LaneE下已经上传的条码信息
                String rightTime = dateFormat.format(currentTime);
                String leftTime = dateFormat.format((new Date(currentTime.getTime() - CheckDuplicationTimeInterval)));
                scanedIdSet = this.interfaceService.getScanList(
                        laneE, sourceFc, destinationFc, arcType, leftTime, rightTime);
     //           log.debug(String.format("共获取%d条重复条码记录!", scanedIdSet.size()));

                for (String scanId : scanedIdSet) if (toBeScanedIdSet.contains(scanId)) toBeScanedIdSet.remove(scanId);
                flag = true;
                message = "获取条码清单成功!";
            } else {
                message = "未获取到前序LaneE信息!";
            }
        } catch (ParseException e) {
            message = "获取清单过程出现时间解析错误!";
            log.error(e.getMessage());
        } catch (Exception e) {
            message = "获取清单过程出现错误!";
            log.error(e.getMessage());
        }

        log.debug(message);

        boolean hasToBeScanedIdList = false;
        if (toBeScanedIdSet.size() != 0)
            hasToBeScanedIdList = true;

        // Remove scaned id in toBeScanedSet
        for (String id : scanedIdSet) {
            if (toBeScanedIdSet.contains(id))
                toBeScanedIdSet.remove(id);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("hasToBeScanedIdList", hasToBeScanedIdList);
        data.put("toBeScanedIdSet", toBeScanedIdSet);
        data.put("scanedIdSet", scanedIdSet);
        JsonResult downloadInfo = new JsonResult<>(flag, message, JSONObject.fromObject(data));
        log.debug(JSONObject.fromObject(downloadInfo).toString());

        return downloadInfo;
    }


}
