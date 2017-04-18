package com.s11web.util;

public class Constants {

    public enum mimeType {

        EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        ZIP("application/zip");

        private String mimeString;

        mimeType(String mimeStr) {
            this.mimeString = mimeStr;
        }

        public String val() {
            return this.mimeString;
        }
    }

    public enum excelColumnName {

        LANE_NAME("laneName"),
        ARC_NAME("arcName"),
        CARGO_TYPE("cargoType"),
        SORT_CODE("sortCode"),
        SUM("总数"),
        SHIP_DATE("发货日期"),
        SCAN_ID("scanId"),
        SCAN_TIME("扫描时间"),
        BOX_TYPE("箱型"),
        OPERATE_DATE("operateDate"),
        SHIP_NUMBER("shipNumber"),
        EXCEPTION_TYPE("异常类型"),
        DESCRIPTION("异常描述"),
        CARRIER_NAME("运输商"),
        VOI_SUM("总体积"),
        WEI_SUM("总重量"),
        CAR_TYPE("车型"),
        CAR_NUMBER("车牌"),
        WATER_VOL("水方"),
        LOADING_RATE("装载率");


        private String nameString;

        excelColumnName(String nameStr) {
            this.nameString = nameStr;
        }

        public String val() {
            return this.nameString;
        }
    }

}
