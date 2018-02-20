<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
	<link rel="stylesheet" type="text/css" href="/css/home.css" />
	<link rel="stylesheet" href="/css/bootstrap.min.css">
	<script src="/js/jquery/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/js/fileUpload.js"></script>
	<script type="text/javascript">
		var clickedLiElmt = null;

		window.onload = function() {
			
		}
		function logout() {
			top.location.href = "/logout";
		}

		function adminPage() {
			parent.open("/admin", "_self");
		}

		function getChatBoard(obj) {
			var check = preProcess(obj);
			
			if (check == 1) {
				window.open("/chatBoard","mainMenu");
			}
		}

		function getPollBoard(obj) {
			var check = preProcess(obj);
			
			if (check == 1) {
				window.open("/pollBoard","mainMenu");
			}
		}

		function goHome(obj) {
			var check = preProcess(obj);
			
			if (check == 1) {
				window.open("/mainMenu","mainMenu");
			}
		}

		function preProcess(obj) {
			var result = 0;
			var objId  = obj.getAttribute("id");
			if (clickedLiElmt != null && clickedLiElmt != objId) {
				var previousClickedLi       = document.getElementById(clickedLiElmt);
				previousClickedLi.className = "";
				result                      = 1;
			}
			else {
				if (clickedLiElmt == null) {
					result = 1;
				}
			}
			
			clickedLiElmt = objId;
			obj.className = "selectedLi";
			
			return result;
		}
	</script>
</head>

<body>
	<div class="container" style="width: 100%; padding: 0px;">
		<div id ="topmenu" style="float: right; margin-top: 20px; clear: both; width: auto;">
			<div style="display:block; float:left;"><span class="welcome">Welcome ${userName}</span></div>
			<c:if test="${role == 'ADMIN' }">
				<div style="display:block; float:left;"><span class="welcome" id="admin" onclick="adminPage();">Administrator</span></div>
			</c:if>
			<div class ="logout" onclick="logout();">Logout</div>
		</div>
		<div class="panel-group" style="margin-top:40px; margin-bottom: 0px;">
			<div class="panel panel-primary">
				<div class="menu-wrap" style="overflow: hidden;">
					<nav class="menu">
						<ul class="clearfix" style="margin-bottom: 0px;">
							<li><a id="home" onClick="goHome(this);">Home</a></li>
							<li>
								<a id="movie">Movies </a>
								<ul class="sub-menu">
									<li><a id="cinema">In Cinemas Now</a></li>
									<li><a id="comming">Coming Soon</a></li>
									 <li><a id="dvd">On DVD/Blu-ray</a></li>
								</ul>
							</li>
							<li><a id="shows">T.V. Shows</a></li>
							<li><a id="photo" onClick="getPollBoard(this);">Photos</a></li>
							<li><a id="chat"  onClick="getChatBoard(this);">Chat</a></li>
						</ul>
					</nav>
					<div id="submenu" style="height: 25px; width: 100%; background:#003369;"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>