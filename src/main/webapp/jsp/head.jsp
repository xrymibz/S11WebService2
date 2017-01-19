<%@ page contentType="text/html;charset=UTF-8"%>

<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-header">
	  <!-- 导航条LOGO -->
	  <a class="navbar-brand" href="#">
		  <img alt="" src="../resources/img/logo_black.PNG">
	  </a>
	</div>

	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
		<ul class="nav navbar-nav">
			<c:forEach var="tab" items="${tabs}">
				<li role="presentation">
					<a role="menuitem" tabindex="-1" class='${tab[2]}' href="${tab[2]}">${tab[1]}</a>
				</li>
			</c:forEach>
		</ul>
		<ul class="nav navbar-nav navbar-right">
		  <li><a href="javascript:void(0)">欢迎</a></li>
		  <li><a href="javascript:void(0)">${username}</a></li>
		  <li><a href="logout">注销</a></li>
		  <li><a></a></li>
		  <li><a></a></li>
		</ul>
	</div>
</div>

