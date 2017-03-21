<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



<!doctype html>
<html>
	<head>
		<title>Linker-包裹追踪</title>

		<%@ include file='resources.jsp'%></head>
	<body>
		<%@ include file='head.jsp'%>
		<div class="panalclapse">
			<div class="row" style="margin: 20px 10px 5px 10px;">
				<div class="col-sm-3">
					<div class="panel panel-default">
						<div class="panel-heading">运输商选择:</div>
						<div class="panel-body">
							<select id = "carrierList" multiple="multiple"></select>
						</div>
					</div>
				</div>
				<div class="col-sm-3">
					<div class="panel panel-default">
						<div class="panel-heading">Lane选择:</div>
						<div class="panel-body divLaneList">
							<select id = "laneList" multiple="multiple"></select>
						</div>
					</div>
				</div>
				<div class="col-sm-3">
					<div class="panel panel-default">
						<div class="panel-heading">Arc选择:</div>
						<div class="panel-body">
							<select id = "arcList" multiple="multiple"></select>
						</div>
					</div>
				</div>
				<div class="col-sm-3">
					<div class="panel panel-default">
						<div class="panel-heading">CargoType选择:</div>
						<div class="panel-body">
							<select id = "cargoList" multiple="multiple">
								<option value="Transfer">Transfer</option>
								<option value="MLPS">MLPS</option>
								<option value="VReturn">VReturn</option>
								<option value="Injection">Injection</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-sm-3">
					<div class="panel panel-default">
						<div class="panel-heading">异常类型选择:</div>
						<div class="panel-body">
							<select id = "exceptionList" multiple = "multiple">
								<option value="货物破损">货物破损</option>
								<option value="额外收货">额外收货</option>
								<option value="货物遗失">货物遗失</option>
								<option value="条码缺失">条码缺失</option>
								<option value="其他异常">其他异常</option>
							</select>
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
				<div class="col-sm-3">
					<div class="row" style="margin: 56px auto 10px auto; width: 100%;">
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
		var exceptionList = $("#exceptionList");
		var fromDate = $("#fromDate");
		var toDate = $("#toDate");
		var btnQuery = $("#btnQuery");
		var btnDownLoad = $("#btnDownload");
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
		var exceptionSelected = [];

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
						carrierSelected = [];
						if (options.length === 0) {
							return 'No carrier selected(Query All)  <b class="caret"></b>';
						} else {
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
						laneSelected = [];
						if (options.length === 0) {
							return 'No lane selected  <b class="caret"></b>';
						} else {
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
						arcSelected = [];
						if (options.length === 0) {
							return 'No arc selected  <b class="caret"></b>';
						} else {
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
						cargoSelected = [];
						if (options.length === 0) {
							return 'No cargoType selected  <b class="caret"></b>';
						} else {
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

		exceptionList.multiselect(
				{
					includeSelectAllOption : true,
					enableFiltering : true,
					maxHeight : 150,
					buttonWidth : '100%',
					buttonText : function(options) {
						exceptionSelected = [];
						if (options.length === 0) {
							return 'No exception type selected  <b class="caret"></b>';
						} else {
							var selected = '';
							var count = 0;
							options.each(function() {
								count++;
								selected += $(this).text() + ', ';
								exceptionSelected.push(this.attributes[0].value);
							});
							if (count < 3) {
								return selected.substr(0,selected.length - 2) + ' <b class="caret"></b>';
							} else {
								selected = "You had chose " + count
										+ " exception types";
								return selected + ' <b class="caret"></b>';
							}
						}
					}
				});

		function showTaskInfo(data){
			$("#tabTaskInfo thead").html(
					"<tr>\n"+
					"<th style='text-align:center;display:none'>TaskId</th>\n"+
					"<th style='text-align:center'>LaneE</th>\n"+
					"<th style='text-align:center'>Arc</th>\n"+
					"<th style='text-align:center'>CargoType</th>\n"+
					"<th style='text-align:center'>SortCode</th>\n"+
					"<th style='text-align:center'>异常类型</th>\n"+
					"<th style='text-align:center'>总计</th>\n"+
					"<th style='text-align:center'>发货日期</th>\n</tr>"
			);
			var total = 0;
			tabTaskInfo.find('tbody');
			var htmlStr = "";
			console.log(data);
			for(var i = 0; i <  data.length; i++){
				htmlStr = htmlStr +
						"<tr>"+
						"<td style='display:none' class='taskId'>" + data[i][0] +"</td>" +
						"<td class='laneE'>"+  data[i][0] +"</td>" +
						"<td class='arcName'>"+  data[i][1] +"</td>" +
						"<td class='cargoType'>"+  data[i][2] +"</td>" +
						"<td class='sortCode'>"+  data[i][3] +"</td>" +
						"<td class='exceptionType'>"+  data[i][4] +"</td>" +
						"<td><a href='javascript:void(0)' class='count'>"+  data[i][5] +"</a></td>"+
						"<td class='creDate'>"+  data[i][6] +"</td></tr>";
				total += parseInt(data[i][5]);
			}

			tabTaskInfo.find('tbody').html("");

			tabTaskInfo.find('tbody').append(htmlStr);

			tabTaskInfo.dataTable({
				"bPaginate": true,
				"bLengthChange": false,
				"iDisplayLength": 20,
				"bFilter": true,
				"bSort": true,
				"order": [[ 7, "desc" ]],
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
			if( carrierSelected.length != 0 ) {
				carriers = carrierSelected;
			}
			else {
				carriers = allCarriers;
			}
			var fromDateVal = $.trim(fromDate.val());
			var toDateVal = $.trim(toDate.val());

			formdata.laneList = laneSelected;
			formdata.carriers = carriers;
			formdata.fromDate = fromDateVal;
			formdata.toDate = toDateVal;
			formdata.arcList = arcSelected;
			formdata.cargoTypeList = cargoSelected;
			formdata.exceptionList = exceptionSelected;

			console.log(formdata);
			formData = $.toJSON(formdata);
			showTaskInfo( getCountByCondition(basePath, formData) );
		});

		btnDownLoad.click(function(){
			downloadFile(basePath + "downloadExceptionInfo", formData);
		});

		tabTaskInfo.on("click",".count",function(){
			console.log("total click");
			var tr = $(this).parent().parent();
			var taskId = tr.find(".taskId").html();
			var laneE = tr.find(".laneE").html();
			var arcName = tr.find(".arcName").html();
			var cargoType = tr.find(".cargoType").html();
			var sortCode = tr.find(".sortCode").html();
			var exceptionType = tr.find(".exceptionType").html();
			var count = tr.find(".count").html();
			var operateDate = tr.find(".creDate").html();
			var url = basePath + "exceptionItem?laneE=" + laneE + "&arcName=" + arcName
                + "&cargoType=" + cargoType
                + "&sortCode=" + sortCode
                + "&exceptionType=" + exceptionType
                + "&count=" + count
                + "&operateDate=" + operateDate;
            url=encodeURI(url);
            url=encodeURI(url);
			window.open(url);
		});

		if( '<%=session.getAttribute("tabAuth")%>' != '1' ) {
			carrierList.multiselect('select', ['<%=session.getAttribute("carrier")%>']);
			carrierList.multiselect('disable');
		}
		$(".exceptionQuery").css("color", "white");
		btnQuery.trigger("click");
	</script>
</html>
