<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html>
	<head>
		<title>Linker-装载率</title>
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
						<div class="panel-heading">Lane选择:</div>
						<div class="panel-body divLaneList">
							  <select id = "laneList" multiple="multiple"></select>
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
			formdata.laneList = laneSelected;
			formdata.carriers = carriers;
			formdata.fromDate = fromDateVal;
			formdata.toDate = toDateVal;
			formdata.arcList = arcSelected;
			formdata.cargoTypeList = cargoSelected;


			formData = $.toJSON(formdata);
            console.log("formdata : " + formdata);

			showTaskInfo( getCountByCondition(basePath, formData) );
		});

		btnDownload.click(function(){
			downloadFile(basePath + "downloadScanInfo", formData);
		});

		tabTaskInfo.on("click",".count",function(){
			console.log("total click");
			var tr = $(this).parent().parent();
			var taskId = tr.find(".taskId").html();
			var laneE = tr.find(".laneE").html();
			var arcName = tr.find(".arcName").html();
			var cargoType = tr.find(".cargoType").html();
			var sortCode = tr.find(".sortCode").html();
			var count = tr.find(".count").html();
			var operateDate = tr.find(".creDate").html();

			window.open(basePath + "loadingRateItem?laneE=" + laneE + "&arcName=" + arcName
					+ "&cargoType=" + cargoType
					+ "&sortCode=" + sortCode
					+ "&count=" + count
					+ "&operateDate=" + operateDate);
		});


		function showTaskInfo(data){
			$("#tabTaskInfo thead").html(
					"<tr>\n"+
					"<th style='text-align:center;display:none'>TaskId</th>\n"+
                    "<th style='text-align:center'>运输商</th>\n"+
					"<th style='text-align:center'>LaneE</th>\n"+
					"<th style='text-align:center'>发货日期</th>\n"+
					"<th style='text-align:center'>SortCode</th>\n"+
					"<th style='text-align:center'>总计</th>\n"+
                    "<th style='text-align:center'>总体积</th>\n"+
                    "<th style='text-align:center'>总重量</th>\n"+
                    "<th style='text-align:center'>车型</th>\n"+
                    "<th style='text-align:center'>车牌</th>\n"+
                    "<th style='text-align:center'>水方</th>\n"+
					"<th style='text-align:center'>装载率</th>\n</tr>"
			);
			var total = 0;
			tabTaskInfo.find('tbody');
			var htmlStr = "";
			console.log(data);
			for(var i = 0; i <  data.length; i++){
				htmlStr = htmlStr +
						"<tr>"+
						"<td style='display:none' class='taskId'>" + data[i][0] +"</td>" +
                        "<td class='carrier'>"+  '上海展欣' +"</td>" +
						"<td class='laneE'>"+  data[i][0] +"</td>" +
						"<td class='cargoType'>"+  data[i][5] +"</td>" +
						"<td class='sortCode'>"+  data[i][3] +"</td>" +
						"<td><a href='javascript:void(0)' class='count'>"+  data[i][4] +"</a></td>"+
                        "<td class='totalCapacity'>"+  15 +"</td>" +
                        "<td class='totalWeight'>"+  300 +"</td>" +
                        "<td class='models'>"+  '金杯' +"</td>" +
                        "<td class='LicensePlate'>"+  '京E15324' +"</td>" +
                        "<td class='cube'>"+  20 +"</td>" +
						"<td class='loadRate'>"+  '75%' +"</td></tr>";
				total += parseInt(data[i][4]);
			}

			tabTaskInfo.find('tbody').html("");

			tabTaskInfo.find('tbody').append(htmlStr);

			tabTaskInfo.dataTable({
				"bPaginate": true,
//				"bLengthChange": false,
				"iDisplayLength": 20,
				"bFilter": true,
				"bSort": true,
				"order": [[ 6, "desc" ]],
				"bInfo": true,
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
				},
			});
		}

		btnQuery.trigger("click");

		if( '<%=session.getAttribute("tabAuth")%>' != '1' ) {
			 carrierList.multiselect('select', ['<%=session.getAttribute("carrier")%>']);
			 carrierList.multiselect('disable');
		}
		$(".loadingRate").css("color", "white");







	</script>
</html>



