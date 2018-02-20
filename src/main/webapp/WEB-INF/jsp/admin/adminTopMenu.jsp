<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
	<link rel="stylesheet" type="text/css" href="/css/home.css" />
	<link rel="stylesheet" href="/css/bootstrap.min.css">
	<script	src="/js/jquery/jquery.min.js"></script>
	<script	src="/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/js/fileUpload.js"></script>
	<script type="text/javascript">
		function back() {
			parent.open("/home", "_self");
		}

		function organ() {
			parent.open("/admin/organization", "adBoard", "top=0");
		}
	</script>
</head>

<body>
	<div class="container">
		<div id ="topmenu" style="float: right; margin-top: 20px; clear: both; width: auto;">
			<div style="display:block; float:left;"><span class="welcome">Welcome ${userName}</span></div>
			<div class ="logout" onclick="back();">Back</div>
		</div>
		<div class="panel-group" style="margin-top:40px; margin-bottom: 0px;">
			<div class="panel panel-primary">
				<div class="menu-wrap" style="overflow: hidden;">
					<nav class="menu">
						<ul class="clearfix" style="margin-bottom: 0px;">
							<li><a onclick="organ();">Organization</a></li>
							<li>
								<a href="#">Movies </a>
								<ul class="sub-menu">
									<li><a href="#">In Cinemas Now</a></li>
									<li><a href="#">Coming Soon</a></li>
									<li><a href="#">On DVD/Blu-ray</a></li>
								</ul>
							</li>
							<li><a href="#">T.V. Shows</a></li>
							<li><a href="#">Photos</a></li>
							<li><a href="#">Site Help</a></li>
						</ul>
					</nav>
					<div id="submenu" style="height: 25px; width: 100%; background:#003369;">
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>