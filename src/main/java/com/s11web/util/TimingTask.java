package com.s11web.util;
import com.s11web.model.StoreRate;
import com.s11web.service.AuxiliaryService;
import com.s11web.service.UIService;
import com.squareup.okhttp.*;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
/**
 * Created by xietian
 * 2017/4/18.
 */

/**
 *  1  秒（0~59）
 *  2  分钟（0~59）
 *  3 小时（0~23）
 *  4 天（0~31）
 *  5 月（0~11）
 *  6 星期（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
 *  7.年份（1970－2099）
 */
@Component
public class TimingTask {

    private static final Logger log = Logger.getLogger(TimingTask.class);
    private final OkHttpClient client = new OkHttpClient();

    @Autowired
    private AuxiliaryService auxiliaryService;

    /**
     * 更新task_item表，根据ScanID,获取箱型、体积、重量等信息。
     */
    // 每晚凌晨1点更新
 //   @Scheduled(cron="0 0 1  * * ? ")
    public void updateBoxInfoByScanID(){
        log.debug("update is running");

        //获取当前日期
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String TDate = sdf.format(new Date(d.getTime() - (long)1 * 24 * 60 * 60 * 1000));
        log.debug("----------today is : " + TDate+"------------");
        //获取当天扫描所有的货物的ScanID
        List<String> ScanIDitems = auxiliaryService.getScanIDbyDate(TDate);

        if(ScanIDitems==null) return;
        else{
            for(String ScanIDitem:ScanIDitems){
                String[]ScanInfo = getScanInfo("URL",ScanIDitem);
                auxiliaryService.uodateScanInfo(ScanIDitem,ScanInfo[0],ScanInfo[1],ScanInfo[2]);
            }

        }
    }

    public String[] getScanInfo(String Url,String ScanId){
        String[] res = new String[3];

        try {
            RequestBody formBody = new FormEncodingBuilder()
                    .add("ScanId", ScanId)
                    .build();
            Request request = new Request.Builder()
                    .url(Url)
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .post(formBody)
                    .build();

//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
//            client.setSslSocketFactory(sc.getSocketFactory());
//            client.setHostnameVerifier(new MyHostnameVerifier());

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String string = response.body().string();
                            System.out.println("response.body().string() :" + string);
                            JSONObject resp = JSONObject.fromObject(string);
                            //System.out.println(res.get("message"));
                            //showResultList(res);
                            boolean isSuccess = resp.getBoolean("success");
                            if (isSuccess) {

                                log.debug(res.toString());
                                res[0] =resp.getString("vol");
                                res[1] =resp.getString("wei");
                                res[2] =resp.getString("boxType");
                            } else {
                                //...
                            }
                        } catch (Exception e) {
                            log.debug("response body can not change to jsonobject");
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    log.debug("fail to conn");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }




    //获取入库率等信息


    /**
     * 更新扫描数据的入库率的表格。
     */
    // 每晚凌晨1点更新
       @Scheduled(cron="0 33 *  * * ? ")
    public void updateStoreRate(){

        //更新十天内的数据
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String FromDate = sdf.format(new Date(d.getTime() - (long)10 * 24 * 60 * 60 * 1000));
        String ToDate = sdf.format(new Date(d.getTime() +(long)2 * 24 * 60 * 60 * 1000) );
        log.debug("----------today is : " + FromDate+"----" + ToDate+"--------");

        log.debug("getWareHousingByCondition is running " + new Date());
        boolean flag;
        String message;
        List<String[]> res = auxiliaryService.getWareHousingByCondition(FromDate,ToDate);
        log.debug("getWareHousingByCondition is finished");
        if (Objects.nonNull(res)) {
            if(res!=null&&res.size()>0){
                NumberFormat fmt = NumberFormat.getPercentInstance();
                fmt.setMaximumFractionDigits(2);//最多两位百分小数，如25.23%
                for(String[]item : res){
                    String[]temp = auxiliaryService.getWareHousingInfobyOutOfFC(item[0],item[1],item[5]);
                    item[7] = temp[0];
                    item[8] = temp[1];
                    item[9] =  fmt.format(Double.parseDouble(item[8])/Double.parseDouble(item[6]));
                    item[10] = temp[2];
                    StoreRate storeRate = new StoreRate(item[0],item[1],item[2],item[3],item[4],item[5],item[6],item[7],item[8],item[9],item[10]);
                    auxiliaryService.updateStoreRate(storeRate);
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
    }
}
