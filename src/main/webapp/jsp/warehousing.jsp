<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html>
<head>
    <title>Linker-入库统计 </title>
    <%@ include file='resources.jsp'%>
</head>

<body>
<%@ include file='head.jsp'%>
<div class="panalclapse">
    <div class="row" style="margin: 20px 10px 5px 10px;">
        <div class="col-sm-6">
            <div class="panel panel-default">
                <div class="panel-heading">运输商选择:</div>
                <div class="panel-body">
                    <select id = "carrierList" multiple="multiple"></select>
                </div>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="panel panel-default">
                <div class="panel-heading">Arc选择:</div>
                <div class="panel-body">
                    <select id = "arcList" multiple="multiple"></select>
                </div>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="panel panel-default">
                <div class="panel-heading">日期选择:</div>
                <div class="panel-body">
                    <div class="col-xs-1" style="text-align: right;height: 34px;padding-bottom: 4px;padding-top: 10px;">
                        From:
                    </div>
                    <div class="col-xs-5">
                        <input type="text" class="form-control" id="fromDate" placeholder=""/>
                    </div>
                    <div class="col-xs-1" style="text-align: right;height: 34px;padding-bottom: 4px;padding-top: 10px;">
                        To:
                    </div>
                    <div class="col-xs-5">
                        <input type="text" class="form-control" id="toDate" placeholder=""/>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="row" style="margin: 56px auto 10px auto; width: 80%;">
                <div class="col-sm-6">
                    <button class="btn btn-primary" id="btnQuery" style="width:90%;">查 询</button>
                </div>
                <div class="col-sm-6">
                    <button class="btn btn-primary" id="btnDownload" style="width:90%;">下 载</button>
                </div>
            </div>
        </div>
    </div>
    <hr>

    <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-10">
            <div id="taskInfo">
                <table id="tabTaskInfo" class="display" cellspacing="0" width="80%">
                    <thead></thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
        <div class="col-sm-1"></div>
    </div>
</div>

<div class="row" style="margin-top: 20px;margin-bottom: 10px;"></div>
<%@ include file='tail.jsp'%>
</body>

<script type="text/javascript" src="../custom/js/query.js"></script>
<script>
    var basePath = '<%=basePath%>';

    var carrierList = $("#carrierList");
    var laneList = $("#laneList");
    var arcList = $("#arcList");
    var cargoList = $("#cargoList");
    var fromDate = $("#fromDate");
    var toDate = $("#toDate");
    var btnQuery = $("#btnQuery");
    var btnDownload = $("#btnDownload");
    var tabTaskInfo = $("#tabTaskInfo");

    carrierList.each (function(){
        this.selected = true;
    });

    var allCarriers;

    if( '<%=session.getAttribute("tabAuth")%>' == '1' )
        allCarriers = ['BFHY','BFLTA','CDJQ','GZLH','JJXD','JSYZ','SHAZX', 'SHSS', 'SZFLS', 'THWL', 'XKWL','ZYKY','WJWL'];
    else
        allCarriers = ['<%=session.getAttribute("carrier")%>'];

    for ( var i = 0; i < allCarriers.length; i++) {
        carrierList.append('<option value="' + allCarriers[i] + '">' + allCarriers[i] + '</option>');
    }

    var carrierSelected = [];
    var laneSelected = [];
    var arcSelected = [];
    var cargoSelected = [];

    fromDate.datetimepicker(getPickerOpts());
    fromDate.val(getBeforeDate(7));
    toDate.datetimepicker(getPickerOpts());
    toDate.val(getBeforeDate(0));

    var paramData;

    carrierList.multiselect(
        {
            includeSelectAllOption : true,
            enableFiltering : true,
            maxHeight : 150,
            buttonWidth : '100%',
            buttonText : function(options) {
                if (options.length === 0) {
                    carrierSelected = [];
                    return 'No carrier selected(Query All)  <b class="caret"></b>';
                } else {
                    carrierSelected = [];
                    var selected = '';
                    var count = 0;
                    options.each(function() {
                        count++;
                        selected += $(this).text() + ', ';
                        carrierSelected.push(this.attributes[0].value);
                    });
                    showLanesByCarrierAbbr(carrierSelected, laneList);
                    if (count < 3) {
                        return selected.substr(0,selected.length - 2) + ' <b class="caret"></b>';
                    } else {
                        selected = "You had chose " + count
                            + " carriers";
                        return selected + ' <b class="caret"></b>';
                    }

                }
            }
        });

    laneList.multiselect(
        {
            includeSelectAllOption : true,
            enableFiltering : true,
            maxHeight : 150,
            buttonWidth : '100%',
            buttonText : function(options) {
                if (options.length === 0) {
                    laneSelected = [];
                    return 'No lane selected  <b class="caret"></b>';
                } else {
                    laneSelected = [];
                    var selected = '';
                    var count = 0;
                    options.each(function() {
                        count++;
                        selected += $(this).text() + ', ';
                        laneSelected.push(this.attributes[0].value);
                    });
                    showArcsByLaneSelected(laneSelected, arcList);
                    if (count < 3) {
                        return selected.substr(0,selected.length - 2) + ' <b class="caret"></b>';
                    } else {
                        selected = "You had chose " + count
                            + " lanes";
                        return selected + ' <b class="caret"></b>';
                    }
                }
            }
        });

    arcList.multiselect(
        {
            includeSelectAllOption : true,
            enableFiltering : true,
            maxHeight : 150,
            buttonWidth : '100%',
            buttonText : function(options) {
                if (options.length === 0) {
                    arcSelected = [];
                    return 'No arc selected  <b class="caret"></b>';
                } else {
                    arcSelected = [];
                    var selected = '';
                    var count = 0;
                    options.each(function() {
                        count++;
                        selected += $(this).text() + ', ';
                        arcSelected.push(this.attributes[0].value);
                    });
                    if (count < 3) {
                        return selected.substr(0,selected.length - 2) + ' <b class="caret"></b>';
                    } else {
                        selected = "You had chose " + count
                            + " arcs";
                        return selected + ' <b class="caret"></b>';
                    }
                }
            }
        });

    cargoList.multiselect(
        {

            includeSelectAllOption : true,
            enableFiltering : true,
            maxHeight : 150,
            buttonWidth : '100%',
            buttonText : function(options) {
                if (options.length === 0) {
                    cargoSelected = [];
                    return 'No cargoType selected  <b class="caret"></b>';
                } else {
                    cargoSelected = [];
                    var selected = '';
                    var count = 0;
                    options.each(function() {
                        count++;
                        selected += $(this).text() + ', ';
                        cargoSelected.push(this.attributes[0].value);
                    });
                    if (count < 3) {
                        return selected.substr(0,selected.length - 2) + ' <b class="caret"></b>';
                    } else {
                        selected = "You had chose " + count
                            + " cargoType";
                        return selected + ' <b class="caret"></b>';
                    }
                }
            }
        });

    btnQuery.click(function(){
        //如果datatable存在，则将其删除
        if($.fn.dataTable.isDataTable('#tabTaskInfo')){
            var datable = tabTaskInfo.dataTable();
            datable.fnDestroy();
            tabTaskInfo.find('tbody').html("");
            tabTaskInfo.find('thead').html("");
        }

        var formdata = new Object();

        var carriers;
        if(carrierSelected.length != 0){
            carriers = carrierSelected;
        } else {
            carriers = allCarriers;
        }
        var fromDateVal = $.trim(fromDate.val());
        var toDateVal = $.trim(toDate.val());
        formdata.carriers = carriers;
        formdata.fromDate = fromDateVal;
        formdata.toDate = toDateVal;
        formdata.arcList = arcSelected;


        formData = $.toJSON(formdata);
        console.log("formdata : " + formdata);

        if(new Date(toDate.val())-new Date(fromDate.val())>1000*60*60*24*93){
            alert("时间范围不得超过三个月");
        }else {
            showTaskInfo(getWareHousing(basePath, formData));
        }
    });

    btnDownload.click(function(){
        downloadFile(basePath + "downloadScanInfo", formData);
    });

    tabTaskInfo.on("click",".StorageRate1",function(){
        console.log("total click");
        var tr = $(this).parent().parent();
        var departureDate = tr.find(".departureDate").html();
        var arc = tr.find(".arc").html();
        var destinationDate = tr.find(".destinationDate").html();
        var cargoesType = tr.find(".cargoesType").html();
        var carrierName = tr.find(".carrierName").html();

        window.open(basePath + "warehousingItem?carrierName=" + carrierName + "&cargoesType=" + cargoesType
            + "&arc=" + arc
            + "&departureDate=" + departureDate
            + "&destinationDate=" + destinationDate);

    });

    function showTaskInfo(data){
        $("#tabTaskInfo thead").html(
            "<tr>\n" +
            "<th style='text-align:center'>运输商</th>\n" +
            "<th style='text-align:center'>Arc</th>\n" +
            "<th style='text-align:center'>出发库房</th>\n" +
            "<th style='text-align:center'>目的库房</th>\n" +
            "<th style='text-align:center'>cargoseType</th>\n" +
            "<th style='text-align:center'>出库日期</th>\n" +
            "<th style='text-align:center'>出库数量</th>\n" +
            "<th style='text-align:center'>入库日期</th>\n" +
            "<th style='text-align:center'>入库数量</th>\n" +
            "<th style='text-align:center'>入库率</th>\n</tr>"
        );
        var total = 0;
        tabTaskInfo.find('tbody');
        var htmlStr = "";
        console.log(data);

        for(var i = 0; i <  data.length; i++){
            htmlStr = htmlStr +
                "<tr>" +
                "<td class='carrierName'>" + data[i][0] + "</td>" +
                "<td class='arc'>" + data[i][1] + "</td>" +
                "<td class='departure'>" + data[i][2] + "</td>" +
                "<td class='destination'>" + data[i][3] + "</td>" +
                "<td class='cargoesType'>" + data[i][4] + "</td>" +
                "<td class='departureDate'>" + data[i][5] + "</td>" +
                "<td class='departureNum'>" + data[i][6] + "</td>" +
                "<td class='destinationDate'>" + data[i][7] + "</td>" +
                "<td class='destinationNum'>" + data[i][8] + "</td>" +
                "<td><a href='javascript:void(0)' class='StorageRate1'>" + data[i][9] + "</a></td></tr>";

        }

//        htmlStr = htmlStr +
//            "<tr><td class='arc'>" + "CAN4-SHA2" + "</td>" +
//            "<td class='departure'>" + "广州" + "</td>" +
//            "<td class='destination'>" + "昆山" + "</td>" +
//            "<td class='cargoseType'>" + "Transfer" + "</td>" +
//            "<td class='departureDate'>" + "2017-04-26" + "</td>" +
//            "<td class='departureNum'>" + 3 + "</td>" +
//            "<td class='destinationDate'>" + "2017-04-28" + "</td>" +
//            "<td class='destinationNum'>" + 3 + "</td>" +
//            "<td><a href='javascript:void(0)' class='StorageRate1'>" + "100%" + "</a></td></tr>";
//
//
//        htmlStr = htmlStr +
//            "<tr><td class='arc'>" + "CAN4-SHA2" + "</td>" +
//            "<td class='departure'>" + "广州" + "</td>" +
//            "<td class='destination'>" + "昆山" + "</td>" +
//            "<td class='cargoseType'>" + "Transfer" + "</td>" +
//            "<td class='departureDate'>" + "2017-04-27" + "</td>" +
//            "<td class='departureNum'>" + 4 + "</td>" +
//            "<td class='destinationDate'>" + "2017-04-29" + "</td>" +
//            "<td class='destinationNum'>" + 2 + "</td>" +
//            "<td><a href='javascript:void(0)' class='StorageRate1'>" + "50%" + "</a></td></tr>";

        tabTaskInfo.find('tbody').html("");

        tabTaskInfo.find('tbody').append(htmlStr);

        tabTaskInfo.dataTable({
            "bPaginate": true,
 //           "bLengthChange": false,
            "iDisplayLength": 20,
            "bFilter": true,
            "bSort": true,
            "order": [[ 6, "desc" ]],
            "bInfo": true,
            "bAutoWidth": false,
            "oLanguage": {
                "sZeroRecords": "对不起，查询不到任何相关数据",
                "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
                "sInfoEmpty": "找不到相关数据",
                "sInfoFiltered": "表中共有 _MAX_ 条记录)",
                "sProcessing": "正在加载中...",
                "oPaginate": {
                    "sFirst": "第一页 ",
                    "sPrevious": " 上一页 ",
                    "sNext": " 下一页 ",
                    "sLast": " 最后一页"
                }
            }
        });
    }

    function getWareHousing(basePath, formData){

        var resultData = [];

        $.ajax({
            type:"POST",
            url:basePath + "getWareHousingByCondition",
            async : false,
            timeout : 5000,
            data : "data=" + formData,
            dataType : "json",
            success: function(data){
                resultData = data.data;
            },
            error: function(XMLHttpRequest, textStatus) {
                console.log("getWareHousingByCondition   " + XMLHttpRequest.status);
            }
        });
        return resultData;
    }


    btnQuery.trigger("click");

    if( '<%=session.getAttribute("tabAuth")%>' != '1' ) {
        carrierList.multiselect('select', ['<%=session.getAttribute("carrier")%>']);
        carrierList.multiselect('disable');
    }
    $(".warehousing").css("color", "white");
</script>
</html>



