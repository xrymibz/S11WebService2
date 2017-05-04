package com.s11web.service;

import com.s11web.dao.AuxiliaryDao;
import com.s11web.model.StoreRate;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xietian
 * 2017/4/18.
 */
@Service
public class AuxiliaryService {
    private static final Logger log = Logger.getLogger(AuxiliaryService.class);
    @Autowired
    private AuxiliaryDao auxiliaryDao;




    public List<String> getScanIDbyDate(String Date){
        return auxiliaryDao.getScanIDbyDate(Date);
    }
    public void uodateScanInfo(String ScanID,String PV,String PW,String Box){
        log.debug("UpdateScanInfo is beginning  ---------");
        auxiliaryDao.uodateScanInfo(ScanID,PV,PW,Box);
        log.debug("UpdateScanInfo is finished  ---------");
    }



    public  List<String[]> getWareHousingByCondition( String dateFrom, String dateTo){

    try{
        return auxiliaryDao.getWareHousingByCondition(dateFrom, dateTo);

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
            //获得当天所有的出库扫描ID
            List<String> OutOfFCScanId = auxiliaryDao.getScanIdbyOutOfFC(carrier, arc, creDate);
            //获得前后五天内所有的出库扫描ID
            List<String> IntervalOutOfFCScanId = auxiliaryDao.getIntervalScanIdbyOutOfFC(carrier, arc, FromDate,toDate);
            //获得入库的所有的taskID
            List<String>taskId = auxiliaryDao.getTaskIdInOfFCbyScanId( JSONArray.fromObject(OutOfFCScanId),creDate);
            List<String> InOfFCScanId = new ArrayList<>();
            if(taskId!=null&&taskId.size()!=0) {
                InOfFCScanId = auxiliaryDao.getScanIDbyTaskId(JSONArray.fromObject(taskId),creDate);
            }
            log.debug("getWareHousingInfobyOutOfFC is finished");

            if(InOfFCScanId==null ||InOfFCScanId.size() ==0) //如果货物没有入库
            {
                res[0] = "-";
                res[1] = "0";
            }else{
                List<String>InOfFCDate = auxiliaryDao.getDatebyTaskId(JSONArray.fromObject(taskId));
                res[0] = InOfFCDate.get(0);
                res[1] = InOfFCDate.size()+"";
                //关掉查询漏件        res[1] = getNumByScanId(OutOfFCScanId,IntervalOutOfFCScanId,InOfFCScanId) +"";
                res[1] = getNumByScanId(OutOfFCScanId,IntervalOutOfFCScanId,InOfFCScanId) +"";
            }

            return res;
        }catch (Exception e ){
            log.error(e);
            return null;
        }
    }

    public void updateStoreRate(StoreRate storeRate){
        auxiliaryDao.updateStoreRate(storeRate);
    }

    public int getNumByScanId(List<String>outFC,List<String>intervalOutFC,List<String>inFC){
        int num = 0;
        for(String i:inFC){
          if(outFC.contains(i)||!intervalOutFC.contains(i)){
                num++;
            }
        }
        return num;
    }

}
