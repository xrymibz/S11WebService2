<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html>
	<head>
		<title>Linker-异常详情</title>

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
							<th style='text-align:center'>laneE</th>
							<th style='text-align:center'>Arc</th>
							<th style='text-align:center'>Cargo Type</th>
							<th style='text-align:center'>Sort Code</th>
							<th style='text-align:center'>异常类型</th>
							<th style='text-align:center'>数量</th>
							<th style='text-align:center'>记录时间</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>

				<br>
				<div class="panel panel-default">
					<div class="panel-heading" style = "text-align:left;font-size:16px">
						<b>Exception Items:</b>
					</div>
				</div>
				<div style = "margin-left:80%">
					<button class="btn btn-primary" id = "btnDownLoadTaskDetail" style="margin-right: 25px;">下载明细</button>
					<button class="btn btn-primary" id = "btnDownLoadImage" style="margin-right: 10px;">下载图片</button>
				</div>
				<div style = "width:50%;margin:auto">
					<table id = "tabExceptionItem"  class="display">
						<thead>
							<tr>
								<th style='text-align:center'>Scan ID</th>
								<th style='text-align:center'>记录时间</th>
								<th style='text-align:center'>异常类型</th>
								<th style='text-align:center'>描述</th>
								<th style='text-align:center; display: none'>PictureUrl</th>
								<th style='text-align:center'>图片</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
		</div>
		<%--Modal for displaying picture--%>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
			 aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="vertical-alignment-helper">
				<div class="modal-dialog  vertical-align-center">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title" id="myModalLabel" style="text-align: center">
								异常图片
							</h4>
						</div>
						<div class="modal-body" style="text-align: center">
							<%--<img id="picArea"/>--%>
							<div id="myCarousel" class="carousel slide">
								<ol class="carousel-indicators"></ol>
								<!-- Carousel items -->
								<div class="carousel-inner"></div>
								<!-- Carousel nav -->
								<a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
								<a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">
								关闭
							</button>
						</div>
					</div>
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
		var tabExceptionItem = $("#tabExceptionItem");
		var btnDownLoadTaskDetail = $("#btnDownLoadTaskDetail");
		var btnDownLoadImage = $("#btnDownLoadImage");

		var formData = {};
		formData.laneE = "${laneE}";
		formData.arc = "${arcName}";
		formData.cargoType = "${cargoType}";
		formData.sortCode = "${sortCode}";
		formData.exceptionType = "${exceptionType}";
		formData.shipNumber = "${count}";
		formData.operateDate = "${operateDate}";
		formData = $.toJSON(formData);

		tabTaskInfo.dataTable({
			  "bPaginate": false,
			  "bLengthChange": false,
			  "bFilter": false,
			  "bSort": false,
			  "bInfo": false,
			  "bAutoWidth": false
		  });

		showTaskCount( getTaskCount(basePath, formData) );
		showTaskItem( getTaskItem(basePath, formData) );

		function showTaskCount(data){
			var html = "";
			var sameInfo = "<td>${laneE}</td>" +
						   "<td>${arcName}</td>" +
						   "<td>${cargoType}</td>" +
						   "<td>${sortCode}</td>"+
						   "<td>${exceptionType}</td>";
			for(var i = 0; i < data.length; ++i) {
				html += "<tr>" +
						sameInfo +
						"<td>" + data[i][1] + "</td>" +
						"<td>" + data[i][0] + "</td>" +
						"</tr>";
			}
			tabTaskInfo.find("tbody").html($(html));
		}

		function showTaskItem(data){
			var html = "";
			var pictureCellHtml;
			for (var i = 0; i < data.length; ++i){

				if( data[i][4] != "" )
					pictureCellHtml = "<a data-toggle=\"modal\" data-target=\"#myModal\" class=\"getPic\">查看图片</a>";
				else
					pictureCellHtml = "";

				html += "<tr>"+
						"<td class='scanId'>" + data[i][0] + "</td>" +
						"<td class='operateDate'>" + data[i][1] + "</td>" +
						"<td class='exceptionType'>" + data[i][2] + "</td>" +
						"<td class='description'>" + data[i][3] + "</td>" +
						"<td class='pictureUrl' style='display: none'>" + data[i][4] + "</td>" +
						"<td>" + pictureCellHtml + "</td>" +
						"</tr>";
			}
			tabExceptionItem.find("tbody").html("");
			tabExceptionItem.find("tbody").append(html);
			tabExceptionItem.dataTable({
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

		btnDownLoadTaskDetail.click(function(){
			downloadFile(basePath + "downloadScanItemDetail", formData);
		});

		btnDownLoadImage.click(function(){
			downloadFile(basePath + "downloadImgZip", formData);
		});

		$(".exceptionQuery").css("color", "white");

		tabExceptionItem.on("click",".getPic",function(){
			var picUrlList = $(this).parent().parent().find(".pictureUrl").html().split("\t");
			var indicators = $(".carousel-indicators");
			var inner = $(".carousel-inner");
			var indicatorsHtml = "";
			var innerHtml = "";

			for( var i = 0; i < picUrlList.length; ++i ) {
				if(i == 0) {
					indicatorsHtml += "<li data-target=\"#myCarousel\" data-slide-to=\"" + i + "\" class=\"active\"></li>";
					innerHtml += "<div class=\"active item\"><div style=\"text-align: center\"><img src=\"" + picUrlList[i] + "\"/></div></div>";
				}
				else {
					indicatorsHtml += "<li data-target=\"#myCarousel\" data-slide-to=\"" + i + "\"></li>";
					innerHtml += "<div class=\"item\"><div style=\"text-align: center\"><img src=\"" + picUrlList[i] + "\"/></div></div>";
				}
			}
			indicators.html(indicatorsHtml);
			inner.html(innerHtml);

			$('div.item').map(function () {
				var img = new Image();
				var _this = this;
				var imgSource = $(_this).children("div").children('img').attr("src");
				$(_this).children("div").children('img').attr("src", "../resources/img/ImageLoading.gif");
				$(img).attr("src", imgSource).addClass('hidden').appendTo($('body'));
				img.onload = function () {
					$(_this).children("div").children('img').attr("src", imgSource);
					$(img).remove();
				}
			});
		});


	</script>
</html>
