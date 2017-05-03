<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html>
<head>
    <title>Linker-任务详情</title>

    <%@ include file='resources.jsp'%>
</head>

<body>
<%@ include file='head.jsp'%>
<div class="panalclapse">
    <div class="taskInfoItem">
        <br>
        <div class="panel panel-default">
            <div class="panel-heading" style = "text-align:left;font-size:16px">
                <b>Task Items:  出库数量：4 ， 入库数量2 </b>
            </div>
        </div>
        <div style = "margin-left:90%">
            <button class="btn btn-primary" id = "btnDownLoadTaskDetail">下 载</button>
        </div>
        <div style = "width:50%;margin:auto">
            <table id = "tabTaskItem"  class="display">
                <thead>
                <tr>
                    <th style='text-align:center'>Scan ID</th>
                    <th style='text-align:center'>入库时间</th>
                    <th style='text-align:center'>出库时间</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>
<div class="row" style="margin-top: 20px;margin-bottom: 10px;"></div>
<%@ include file='tail.jsp'%>
</body>

<script type="text/javascript" src="../custom/js/query.js"></script>
<script>
    var basePath = '<%=basePath%>';


    var tabTaskItem = $("#tabTaskItem");
    var btnDownLoadTaskDetail = $("#btnDownLoadTaskDetail");

    var formData = {};
    formData.laneE = "${laneE}";
    formData.arc = "${arcName}";
    formData.cargoType = "${cargoType}";
    formData.sortCode = "${sortCode}";
    formData.operateDate = "${operateDate}";
    formData.shipNumber = "${count}";
    formData = $.toJSON(formData);




    showTaskItem( getTaskItem(basePath, formData) );

    btnDownLoadTaskDetail.click(function(){
        downloadFile(basePath + "downloadScanItemDetail", formData);
    });
    $(".scanQuery").css("color", "white");



    function showTaskItem(data){
        var html = "";
//        for (var i = 0; i < data.length; ++i){
//            html += "<tr>" +
//                "<td>" + data[i][1] + "</td>" +
//                "<td>" + data[i][0] + "</td>" +
//                "<td>" + data[i][2] + "</td>" +
//                "</tr>";
//        }


                    html += "<tr>" +
                "<td>" + "1" + "</td>" +
                "<td>" + "2017-04-25 16:50:04" + "</td>" +
                "<td>" + "2017-04-27 16:49:34" + "</td>" +
                "</tr>";
        html += "<tr>" +
            "<td>" + "2" + "</td>" +
            "<td>" + "2017-04-25 16:50:04" + "</td>" +
            "<td>" + "2017-04-27 16:49:34" + "</td>" +
            "</tr>";
        html += "<tr>" +
            "<td>" + "3" + "</td>" +
            "<td>" + "2017-04-25 16:50:04" + "</td>" +
            "<td>" + "-" + "</td>" +
            "</tr>";
        html += "<tr>" +
            "<td>" + "4" + "</td>" +
            "<td>" + "2017-04-25 16:50:04" + "</td>" +
            "<td>" + "-" + "</td>" +
            "</tr>";

        tabTaskItem.find("tbody").html("");
        tabTaskItem.find("tbody").append(html);
        tabTaskItem.dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "iDisplayLength": 10,
            "bFilter": false,
            "bSort": true,
            "order": [[ 1, "desc" ]],
            "bInfo": false,
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
</script>
</html>
