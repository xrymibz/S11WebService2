package com.s11web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by xietian
 * 2017/4/18.
 */
public class DataOperation {


    //合并车型显示
    public static String MergeCarType(String s) {
        String[] temp = s.split(",");
        HashMap<String, Integer> carType = new HashMap<String, Integer>();
        for (String i : temp) {
//            log.debug(i);
            if (!carType.containsKey(i)) {
                carType.put(i, 1);
            } else {
                carType.put(i, carType.get(i) + 1);
            }
        }
        Set<Map.Entry<String, Integer>> items = carType.entrySet();
        String res = "";
        for (Map.Entry item : items) {
            if ((int) item.getValue() != 1){
                res += item.getKey() + " * " + item.getValue() + ",";
            }else{
                res += item.getKey() + ",";
            }
        }
//        log.debug(res+"carytpe----------------------");
        if (res.length() > 0) {
            return res.substring(0, res.length() - 1);
        } else {
            return res;
        }
    }

    //合并车牌显示
    public static String MergeCarNum(String s) {
        String[] temp = s.split(",");
        String res = "";
        for (String i : temp) {
            if (!i.trim().equals("")) {
                res += i + ",";
            }
        }
        if (res.length() > 0) {
            return res.substring(0, res.length() - 1);
        } else {
            return res;
        }
    }

    //解决前端jsp传输中文乱码
    public static String decode(String value){
            try {
                if (value == null) return null;
                return new String(value.getBytes("ISO-8859-1"),"utf-8");
//注: 这里的utf-8, 应视提交页面的编码而定.
            }
            catch(Exception ex) {
                return value;
            }
        }
    }

