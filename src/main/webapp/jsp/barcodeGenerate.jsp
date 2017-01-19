<%--
  Created by IntelliJ IDEA.
  User: fengxion
  Date: 16/8/1
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html>
    <head>
        <title>Linker-条码生成</title>

        <%@ include file='resources.jsp'%>
    </head>

    <body>
    <%@ include file='head.jsp'%>
    <div class="panalclapse" style="height: 81vh; padding-top: 15vh;">
        <div class="row" style="margin: 20px 10px 5px 10px;">
            <div class="col-md-4">
                <div class="col-md-10 col-md-offset-1">
                    <div class="panel panel-default">
                        <div class="panel-heading">Cargotype 选择:</div>
                        <div class="panel-body">
                            <select id = "cargoList">
                                <option value="MLPS">MLPS</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="col-md-10 col-md-offset-1">
                    <div class="panel panel-default" id="sourceFcPanal">
                        <div class="panel-heading">Source FC 选择:</div>
                        <div class="panel-body">
                            <select id = "sourceList">
                                <option value="PEK3">PEK3</option>
                                <option value="XMN2">XMN2</option>
                                <option value="TSN2">TSN2</option>
                                <option value="CAN4">CAN4</option>
                                <option value="CTU4">CTU4</option>
                                <option value="SHA2">SHA2</option>
                                <option value="WUH2">WUH2</option>
                                <option value="SHE1">SHE1</option>
                                <option value="TNA1">XIY2</option>
                                <option value="NKG1">NKG1</option>
                                <option value="NKG1">TNA1</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="col-md-10 col-md-offset-1">
                    <div class="panel panel-default" id="destinationFcPanal">
                        <div class="panel-heading">Destination FC 选择:</div>
                        <div class="panel-body divLaneList">
                            <select id = "destinationList">
                                <option value="PEK3">PEK3</option>
                                <option value="XMN2">XMN2</option>
                                <option value="TSN2">TSN2</option>
                                <option value="CAN4">CAN4</option>
                                <option value="CTU4">CTU4</option>
                                <option value="SHA2">SHA2</option>
                                <option value="WUH2">WUH2</option>
                                <option value="SHE1">SHE1</option>
                                <option value="TNA1">XIY2</option>
                                <option value="NKG1">NKG1</option>
                                <option value="NKG1">TNA1</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="margin-top: 10vh;">
            <div class="form-inline">
                <div class="form-group input-group-lg">
                    <input type="number" class="form-control"
                           id="inputNumber" placeholder="请输入所需生成数量">
                </div>
                <button class="btn btn-primary btn-lg" style="margin-left: 20px;" id="btnGenerate">生成条码</button>
            </div>
        </div>
    </div>

    <div class="row" style="margin-top: 20px;margin-bottom: 10px;"></div>
    <%@ include file='tail.jsp'%>
    </body>

    <script type="text/javascript" src="../custom/js/query.js"></script>
    <script>

        var basePath = '<%=basePath%>';
        var cargoList = $("#cargoList");
        var sourceList = $("#sourceList");
        var destinationList = $("#destinationList");
        var btnGenerate = $("#btnGenerate");
        var inputNumber = $("#inputNumber");
        var sourceFcPanal = $("#sourceFcPanal");
        var destinationFcPanal = $("#destinationFcPanal");

        cargoList.multiselect(
                {
                    buttonWidth : '100%',
                    enableFiltering : true,
                    enableCaseInsensitiveFiltering: true,
                    maxHeight : 150
                }
        );
        sourceList.multiselect(
                {
                    buttonWidth : '100%',
                    enableFiltering : true,
                    enableCaseInsensitiveFiltering: true,
                    maxHeight : 150
                }
        );
        destinationList.multiselect(
                {
                    buttonWidth : '100%',
                    enableFiltering : true,
                    enableCaseInsensitiveFiltering: true,
                    maxHeight : 150
                }
        );
        inputNumber.popover({
            content: "请输入一个1~200之间的整数",
            placement: "top",
            trigger:"hover"
        });
        sourceFcPanal.popover({
            content: "请保证始发库房和目的库房不同",
            placement: "top",
            trigger:"hover"
        });
        destinationFcPanal.popover({
            content: "请保证始发库房和目的库房不同",
            placement: "top",
            trigger:"hover"
        });

        btnGenerate.click(function(){
            var cargoType = $("#cargoList option:selected").val();
            var source = $("#sourceList option:selected").val();
            var destination = $("#destinationList option:selected").val();
            var number = $("#inputNumber").val();

            if( number == null || number == "" || Number(number) < 1 || Number(number) > 200 ) {
                inputNumber.focus();
                inputNumber.popover('show');
                return;
            }
            inputNumber.popover('hide');
            if( source == destination ) {
                sourceFcPanal.popover('show');
                destinationFcPanal.popover('show');
                return;
            }
            sourceFcPanal.popover('hide');
            destinationFcPanal.popover('hide');

            formData = new Object();
            formData.cargoType = cargoType;
            formData.source = source;
            formData.destination = destination;
            formData.number = Number(number);
            console.log(formData);

            downloadFile(basePath + "barcodeGenerateByCondition", $.toJSON(formData));
        });
        $(".barcodeGenerate").css("color", "white");

    </script>
</html>
