<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="/css/bootstrap.min.css"/>	
		<link rel="stylesheet" type="text/css" href="/css/general.css" />
		<script	src="/js/jquery/jquery.min.js"></script>
		<script	src="/js/bootstrap.min.js"></script>
		<script	src="/js/admin/organization/organization.js"></script>
		<script type="text/javascript">
			var functId = "<c:out value = '${funcId}' />";
		</script>
	</head>
	<body class="leftbody" style="margin:0px 0px 0px 0px">
		<div id="left">
			<div class="left_admin"><img src="/images/admin/first.png" width="16px" height="16px" style= "margin-bottom: 3px;"/>&nbsp;Management</div>
			<h2>
				<span style="display:inline-block;width:100%;">Organization</span>
			</h2>
			<ul>
				<li><span id="organ" style="width: 100%; display: inline-block;" onClick="goPage(1, this)" >Company organization</span></li>
				<li><span id="privilege" style="width: 100%; display: inline-block;" onClick="goPage(2, this)" >Privilege setting</span></li>
			</ul>
			
			<h2>
				<span style="display:inline-block;width:100%;">Department</span>
			</h2>
			<ul>
				<li><span id="organ" style="width: 100%; display: inline-block;" onClick="goPage(3 , this)" >Department organization</span></li>
				<li><span id="privilege" style="width: 100%; display: inline-block;" onClick="goPage(4 , this)" >Privilege setting</span></li>
			</ul>
				
			<h2>
				<span style="display:inline-block;width:100%;">User</span>
			</h2>
			<ul>
				<li><span id="organ" style="width: 100%; display: inline-block;" onClick="goPage(5 , this)" >Test User 1</span></li>
				<li><span id="privilege" style="width: 100%; display: inline-block;" onClick="goPage(6 , this)" >Test User 2</span></li>
			</ul>
			
			<h2>
				<span style="display:inline-block;width:100%;">Test</span>
			</h2>
			<ul>
				<li><span id="organ" style="width: 100%; display: inline-block;" onClick="goPage(5 , this)" >Test User 3</span></li>
				<li><span id="privilege" style="width: 100%; display: inline-block;" onClick="goPage(6 , this)" >Test User 4</span></li>
			</ul>
			
			<h2>
				<span style="display:inline-block;width:100%;">TTTTTT</span>
			</h2>
			<ul>
				<li><span id="organ" style="width: 100%; display: inline-block;" onClick="goPage(5 , this)" >Test User 5</span></li>
				<li><span id="privilege" style="width: 100%; display: inline-block;" onClick="goPage(6 , this)" >Test User 6</span></li>
			</ul>
		</div>
		<script type="text/javascript">
			initToggleList(document.getElementById("left"), "h2", "ul", "li");
		</script>
	</body>
</html>