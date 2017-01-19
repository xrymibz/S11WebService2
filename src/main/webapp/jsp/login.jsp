<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = String.format("%s://%s:%s%s/", request.getScheme(),
			request.getServerName(), request.getServerPort(), path);
%>
<!DOCTYPE html>
<html lang="en-us">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta name="description" content="">
		<meta name="author" content="">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

		<title> Linker-包裹追踪平台 </title>

		<link rel="stylesheet" type="text/css" media="screen" href="../resources/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" media="screen" href="../resources/css/font-awesome.min.css">
		<link rel="stylesheet" type="text/css" media="screen" href="../resources/css/smartadmin-production.css">
		<link rel="stylesheet" type="text/css" media="screen" href="../resources/css/smartadmin-skins.css">
		<link rel="stylesheet" type="text/css" media="screen" href="../resources/css/demo.css">
		<link rel="shortcut icon" href="../resources/img/favicon/favicon.ico" type="image/x-icon">
		<link rel="icon" href="../resources/img/favicon/favicon.ico" type="image/x-icon">
		<link rel="stylesheet" href="../resources/css/fonts.css" type="text/css"/>
	</head>

	<body id="login" class="animated fadeInDown  desktop-detected pace-done" style="min-height: 549px;">

		<div class="pace  pace-inactive">
			<div class="pace-progress" data-progress-text="100%" data-progress="99" style="width: 100%;">
				<div class="pace-progress-inner"></div>
			</div>
			<div class="pace-activity"></div>
		</div>

		<header id="header">
			<div id="logo-group">
				<span id="logo">
					<img src="../resources/img/logo.png" alt="SmartAdmin">
				</span>
			</div>
			<span id="login-header-space">
				<span class="hidden-mobile">需要新帐户?</span>
				<a href="register.html" class="btn btn-danger">创建帐户</a>
			</span>
		</header>

		<div role="main">
			<div id="content" class="container">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-7 col-lg-8 hidden-xs hidden-sm">
						<h1 class="txt-color-red login-header-big" style="margin-left: 160px;">Line Haul Linker 包裹追踪平台</h1>
						<h4 class="paragraph-header"></h4>
						<div style='margin-left:20%;margin-top:5%;'>
							<img src="../resources/img/ship_palet.png" />
						</div>
					</div>

					<div class="col-xs-12 col-sm-12 col-md-5 col-lg-4">
						<div class="well no-padding">
							<form action="<%=basePath%>login" method="post" id="login-form" class="smart-form client-form" novalidate>
								<header>
									登 录
								</header>

								<fieldset>
									<section>
										<label class="label">用户名</label>
										<label class="input"> <i class="icon-append fa fa-user"></i>
											<input type="text" name="name">
											<b class="tooltip tooltip-top-right"><i class="fa fa-user txt-color-teal"></i> 请输入用户名</b></label>
									</section>
									<section>
										<label class="label">密码</label>
										<label class="input"> <i class="icon-append fa fa-lock"></i>
											<input type="password" name="password">
											<b class="tooltip tooltip-top-right"><i class="fa fa-lock txt-color-teal"></i> 请输入密码</b> </label>
										<div class="note">
											<c:if test="${flag == false}">
												<c:out value="账号或密码错误，请重新输入！" />
											</c:if>
										</div>	
									</section>
								</fieldset>

								<footer>
									<button type="submit" class="btn btn-primary">
										登录
									</button>
								</footer>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>

		<script src="../resources/js/plugin/pace.min.js"></script>

	    <script src="../resources/js/jquery/jquery-2.0.2.min.js"></script>

		<script src="../resources/js/jquery/jquery-ui-1.10.3.min.js"></script>

		<!-- BOOTSTRAP JS -->
		<script src="../resources/js/bootstrap/bootstrap.js"></script>

		<!-- CUSTOM NOTIFICATION -->
		<script src="../resources/js/plugin/SmartNotification.min.js"></script>

		<!-- JARVIS WIDGETS -->
		<script src="../resources/js/plugin/jarvis.widget.min.js"></script>

		<!-- EASY PIE CHARTS -->
		<script src="../resources/js/jquery/jquery.easy-pie-chart.min.js"></script>

		<!-- SPARKLINES -->
		<script src="../resources/js/jquery/jquery.sparkline.min.js"></script>

		<!-- JQUERY VALIDATE -->
		<script src="../resources/js/jquery/jquery.validate.min.js"></script>

		<!-- JQUERY MASKED INPUT -->
		<script src="../resources/js/jquery/jquery.maskedinput.min.js"></script>

		<!-- JQUERY SELECT2 INPUT -->
		<script src="../resources/js/plugin/select2.min.js"></script>

		<!-- JQUERY UI + Bootstrap Slider -->
		<script src="../resources/js/bootstrap/bootstrap-slider.min.js"></script>

		<!-- browser msie issue fix -->
		<script src="../resources/js/jquery/jquery.mb.browser.min.js"></script>

		<!-- FastClick: For mobile devices -->
		<script src="../resources/js/plugin/fastclick.js"></script>

		<!--[if IE 7]>

		<h1>Your browser is out of date, please update your browser by going to www.microsoft.com/download</h1>

		<![endif]-->

		<!-- Demo purpose only -->
		<script src="../resources/js/plugin/demo.js"></script>

		<!-- MAIN APP JS FILE -->
		<script src="../resources/js/plugin/app.js"></script>


		<script type="text/javascript">
			runAllForms();

			$(function() {
				// Validation
				$("#login-form").validate({
					// Rules for form validation
					rules : {
						email : {
							required : true,
							email : true
						},
						password : {
							required : true,
							minlength : 1,
							maxlength : 100
						}
					},

					// Messages for form validation
					messages : {
						email : {
							required : 'Please enter your email address',
							email : 'Please enter a VALID email address'
						},
						password : {
							required : 'Please enter your password'
						}
					},

					// Do not change code below
					errorPlacement : function(error, element) {
						error.insertAfter(element.parent());
					}
				});
			});
		</script>
	</body>
</html>
<%@ include file='tail.jsp'%>