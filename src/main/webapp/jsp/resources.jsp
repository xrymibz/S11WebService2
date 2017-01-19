<%--
  Created by IntelliJ IDEA.
  User: fengxion
  Date: 16/8/3
  Time: 22:40
  To change this template use File | Settings | File Templates.
--%>
<%
    String path = request.getContextPath();
    String basePath = String.format("%s://%s:%s%s/", request.getScheme(),
            request.getServerName(), request.getServerPort(), path);
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
%>
<meta http-equiv=Content-Type content="text/html;charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="description" content="S11WebService">
<meta name="author" content="fengxion">
<meta name="viewport" content="width=device-width,initial-scale=1.0">

<link rel="stylesheet" href="../resources/css/jquery-ui.css" type="text/css"/>
<link rel="stylesheet" href="../resources/css/bootstrap.css" type="text/css"/>
<link rel="stylesheet" href="../resources/css/bootstrap-multiselect.css" type="text/css"/>
<link rel="stylesheet" href="../resources/css/jquery-ui-timepicker-addon.min.css" type="text/css"/>
<link rel="shortcut icon" href="../resources/img/favicon/favicon.ico" type="image/x-icon"/>
<link rel="icon" href="../resources/img/favicon/favicon.ico" type="image/x-icon"/>
<link rel="stylesheet" href="../resources/css/fonts.css" type="text/css"/>
<link rel="stylesheet" href="../resources/dataTables/css/jquery.dataTables.css" type="text/css"/>

<link rel="stylesheet" href="../custom/css/head.css" type="text/css"/>

<script type="text/javascript" src="../resources/js/jquery/jquery-2.0.2.min.js"></script>
<script type="text/javascript" src="../resources/js/jquery/jquery.json-2.4.min.js"></script>
<script type="text/javascript" src="../resources/js/jquery/jquery-ui-1.10.3.min.js"></script>
<script type="text/javascript" src="../resources/js/bootstrap/bootstrap-multiselect.js"></script>
<script type="text/javascript" src="../resources/js/jquery/jquery-ui-timepicker-addon.min.js"></script>
<script type="text/javascript" src="../resources/js/jquery/jquery-ui-timepicker-zh-CN.js"></script>
<script type="text/javascript" src="../resources/js/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="../resources/js/bootstrap/bootstrap.js"></script>
<script type="text/javascript" src="../resources/dataTables/js/jquery.dataTables.js"></script>