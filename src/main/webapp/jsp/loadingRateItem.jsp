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
			<div class="taskInforAndItem">
				<div class="panel panel-default">
					<div class="panel-heading" style = "text-align:left;font-size:16px">
						<b>Task Information:</b>
					</div>
				</div>

				<div style = "width:90%;margin:auto">
					<table id = "tabTaskInfo">
						<thead>
							<tr>
							<th style='text-align:center'>运输商</th>
							<th style='text-align:center'>laneE</th>
							<th style='text-align:center'>Sort Code</th>
							<th style='text-align:center'>数量</th>
							<th style='text-align:center'>操作时间</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>

				<br>
				<div class="panel panel-default">
					<div class="panel-heading" style = "text-align:left;font-size:16px">
						<b>Task Items:</b>
					</div>
				</div>
				<div style = "margin-left:90%">
					<button class="btn btn-primary" id = "btnDownLoadTaskDetail">下 载</button>
				</div>
				<div style = "width:50%;margin:auto">
					<table id = "tabTaskItem"  class="display">
						<thead>
							<tr>
								<th style='text-align:center'>laneE</th>
								<th style='text-align:center'>Scan ID</th>
								<th style='text-align:center'>扫描时间</th>
								<th style='text-align:center'>箱型</th>
								<th style='text-align:center'>体积</th>
								<th style='text-align:center'>重量</th>
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

		var tabTaskInfo = $("#tabTaskInfo");
		var tabTaskItem = $("#tabTaskItem");
		var btnDownLoadTaskDetail = $("#btnDownLoadTaskDetail");

		var formData = {};
		formData.carrier = "${carrier}";
		formData.laneE = "${laneE}";
		formData.credate = "${credate}";
		formData.carType = "${carType}";
		formData.carNumber = "${carNumber}";
		formData = $.toJSON(formData);

		tabTaskInfo.dataTable({
			  "bPaginate": false,
			  "bLengthChange": false,
			  "bFilter": false,
			  "bSort": false,
			  "bInfo": false,
			  "bAutoWidth": false
		  });

		showTaskCount( getLoadingRateCount(basePath, formData) );
		showTaskItem( getLoadingRaTeItem(basePath, formData) );

		btnDownLoadTaskDetail.click(function(){
			downloadFile(basePath + "downloadScanItemDetail", formData);
		});
		$(".loadingRate").css("color", "white");

		function showTaskCount(data){
			var html = "";
			var sameInfo = "<td>上海展欣</td>" +
						   "<td>NKG1-HFHW合肥-JODSSHUSHANNDD</td>" +
					       "<td>${sortCode}</td>" +
						   "<td>1</td>" +
			               "<td>2017-03-30 14:39:58</td>";
			for(var i = 0; i < 1; ++i) {
				html += "<tr>" +
						sameInfo
//						"<td>" + data[i][1] + "</td>" +
//						"<td>" + data[i][0] + "</td>" +
						"</tr>";
			}
			tabTaskInfo.find("tbody").html($(html));
		}

		function showTaskItem(data){
			var html = "";
			for (var i = 0; i < 1; ++i){
				html += "<tr>" +
						"<td>" + 'NKG1-HFHW合肥-JODSSHUSHANNDD' + "</td>" +
						"<td>" + '58810230021044' + "</td>" +
						"<td>" + '2017-03-30 14:39:54' + "</td>" +
                        "<td>" + '中型' + "</td>" +
                        "<td>" + '12' + "</td>" +
                        "<td>" + '25' + "</td>" +
						"</tr>";
			}
			tabTaskItem.find("tbody").html("");
			tabTaskItem.find("tbody").append(html);
			tabTaskItem.dataTable({
				"bPaginate": true,
//				"bLengthChange": false,
				"iDisplayLength": 10,
				"bFilter": false,
				"bSort": true,
				"order": [[ 1, "desc" ]],
				"bInfo": false,
				"bAutoWidth": false,
				"oLanguage": {
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
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
