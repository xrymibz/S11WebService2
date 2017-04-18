function showLanesByCarrierAbbr(carrierSelected, lanelist) {
    if(carrierSelected.length == 0) {
        return;
    }

    var formData = {};
    formData.carrierList = carrierSelected;
    var data = $.toJSON(formData);
    $.ajax({
        type:"GET",
        dataType: "jsonp",
        jsonp: "callback", //服务端用于接收callback调用的function名的参数
        jsonpCallback: "success_jsonpCallback",
        url:"https://54.223.193.245:8443/laneconfig/getLanesByCarrierAbbr",
        async : false,
        timeout : 5000,
        data : "data="+data,
        success:function(result){
            var lanes = result;
            lanesByCarrier = [];
            lanelist.html("");
            for(var i = 0; i < lanes.length; i++) {
                lanesByCarrier.push(lanes[i]["laneId"]);
                lanelist.append(
                    '<option value="' + lanes[i]["laneE"] + '">' + lanes[i]["laneE"] + '</option>');
                lanelist.multiselect('rebuild');
            }
        },
        error: function(XMLHttpRequest, textStatus) {
            console.log("showLanesByCarrierAbbr   " + XMLHttpRequest.status);
        }
    });
}

function showArcsByLaneSelected(laneSelected, arclist) {
    if(laneSelected.length == 0) {
        return;
    }
    var formData = {};
    formData.laneList = laneSelected;
    var data = $.toJSON(formData);
    $.ajax({
        type:"GET",
        dataType: "jsonp",
        jsonp: "callback", //服务端用于接收callback调用的function名的参数
        jsonpCallback: "success_jsonpCallback",
        url:"https://54.223.193.245:8443/laneconfig/getArcsByConditions",
        async : false,
        timeout : 5000,
        data : "data="+data,
        success:function(result){
            var arcs = result;
            arcsByLane = [];
            arclist.html("");
            for(var i = 0; i < arcs.length; i++) {
                arcsByLane.push(arcs[i]["sourceFC"]+"-"+arcs[i]["destinationFC"]);
                arclist.append(
                    '<option value="' + arcs[i]["sourceFC"]+"-"+arcs[i]["destinationFC"] + '">' + arcs[i]["sourceFC"]+"-"+arcs[i]["destinationFC"] + '</option>');
                arclist.multiselect('rebuild');
            }
        },
        error: function(XMLHttpRequest, textStatus) {
            console.log("showArcsByLaneSelected   " + XMLHttpRequest.status);
        }
    });
}

function getBeforeDate(days) {
    var defaultDate = new Date();
    defaultDate.setDate(defaultDate.getDate()-days);
    var year = defaultDate.getFullYear();
    var month = defaultDate.getMonth() + 1 < 10 ? "0"
    + (defaultDate.getMonth() + 1) : defaultDate.getMonth() + 1;
    var date = defaultDate.getDate() < 10 ? "0" + defaultDate.getDate()
        : defaultDate.getDate();
    var hour = "00";
    var minute = "00";
    if(days == 0) {
        hour = "23";
        minute = "59";
    }
    return year + "-" + month + "-" + date +" "+ hour + ":" + minute;
}

function getCountByCondition(basePath, formData){

    var resultData = [];

    $.ajax({
        type:"POST",
        url:basePath + "getCountByConditions",
        async : false,
        timeout : 5000,
        data : "data=" + formData,
        dataType : "json",
        success: function(data){
            resultData = data.data;
        },
        error: function(XMLHttpRequest, textStatus) {
            console.log("getCountByConditions   " + XMLHttpRequest.status);
        }
    });
    return resultData;
}

function getLoadingRateByConditions(basePath, formData){

    var resultData = [];

    $.ajax({
        type:"POST",
        url:basePath + "getLoadingRateByConditions",
        async : false,
        timeout : 5000,
        data : "data=" + formData,
        dataType : "json",
        success: function(data){
            resultData = data.data;
        },
        error: function(XMLHttpRequest, textStatus) {
            console.log("getCountByConditions   " + XMLHttpRequest.status);
        }
    });
    return resultData;
}


function getLoadingRateOfChildren(basePath, formData){

    var resultData = [];

    $.ajax({
        type:"POST",
        url:basePath + "getLoadingRateOfChildren",
        async : false,
        timeout : 5000,
        data : "data=" + formData,
        dataType : "json",
        success: function(data){
            resultData = data.data;
        },
        error: function(XMLHttpRequest, textStatus) {
            console.log("getCountByConditions   " + XMLHttpRequest.status);
        }
    });
    return resultData;
}


function downloadFile(url, formData) {
    var body = $('body');
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('method', 'post');
    form.attr('action', url);

    var input = $('<input>');
    input.attr('type', 'hidden');
    input.attr('name', 'data');
    input.attr('value', formData);
    body.append(form);
    form.append(input);
    form.submit();
    input.remove();
    form.remove();
}

function getPickerOpts() {
    return {
        changeMonth: true,
        changeYear: true,
        dateFormat: "yy-mm-dd",
        dayNamesMin:["日","一","二","三","四","五","六"],
        firstDay: 0,
        nextText: "下一月",
        prevText: "上一月",
        closeText: "关闭",
        currentText: "今天",
        monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        isRTL: false,
        showButtonPanel: true,
        yearRange: "-25:+25",
        defaultDate : +0,
        beforeShow: function (textbox, instance) {
            var txtBoxOffset = $(this).offset();
            var top = txtBoxOffset.top;
            var left = txtBoxOffset.left;
            var textBoxWidth = $(this).outerWidth();
            setTimeout(function () {
                instance.dpDiv.css({
                    top: top - 190,
                    left: left + textBoxWidth
                });
            }, 0);
        },
        onSelect: function(dateText, inst) {
        }
    };
}

function getTaskCount(basePath, formData) {
    var resultData = [];

    $.ajax({
        type: "POST",
        url: basePath + "/getTaskCount",
        data: "data=" + formData,
        dataType: "json",
        async: false,
        timeout: 5000,
        success: function(data) {
            resultData = data.data;
        },
        error: function(XMLHttpRequest, textStatus) {
            console.log("getTaskCount   " + XMLHttpRequest.status);
        }
    });

    return resultData;
}

function getTaskItem(basePath, formData){
    var resultData = [];

    $.ajax({
        type:"POST",
        url:basePath + "/getTaskItem",
        async : false,
        timeout : 5000,
        data : "data=" + formData,
        dataType : "json",
        success:function(data){
            resultData = data.data;
        },
        error: function(XMLHttpRequest, textStatus) {
            console.log("getTaskItem   " + XMLHttpRequest.status);
        }
    });

    return resultData;
}

function getLoadingRateCount(basePath, formData) {
    var resultData = [];

    $.ajax({
        type: "POST",
        url: basePath + "/getLoadingRateCount",
        data: "data=" + formData,
        dataType: "json",
        async: false,
        timeout: 5000,
        success: function(data) {
            resultData = data.data;
        },
        error: function(XMLHttpRequest, textStatus) {
            console.log("getTaskCount   " + XMLHttpRequest.status);
        }
    });

    return resultData;
}

function getLoadingRaTeItem(basePath, formData){
    var resultData = [];

    $.ajax({
        type:"POST",
        url:basePath + "/getLoadingRaTeItem",
        async : false,
        timeout : 5000,
        data : "data=" + formData,
        dataType : "json",
        success:function(data){
            resultData = data.data;
        },
        error: function(XMLHttpRequest, textStatus) {
            console.log("getTaskItem   " + XMLHttpRequest.status);
        }
    });

    return resultData;
}