<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Admin Page</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" type="text/css" href="/css/home.css" />
	<link rel="stylesheet" href="/css/bootstrap.min.css">
	<link rel="shortcut icon" type="image/png" href="/images/favicon.png"/>
	<script	src="/js/jquery/jquery.min.js"></script>
	<script	src="/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/js/fileUpload.js"></script>
</head>

<body>
	<div style="height:100px;">
		<iframe src="/admin/topMenu" id="adTopMenu"  name="adTopMenu" style="noresize; margin:0px 0px 0px 0px; padding:0px 0px 0px 0px; border:none; width:100%;"></iframe>
	</div>
	<div class="panel panel-primary" style="padding: 0px 15px; height: 80%; border: none; margin-bottom: 0px;">
		<iframe src="/admin/organization" id="adBoard" name="adBoard" style="border: none; width:100%;"></iframe>
	</div>
	<div id="footer">
		&copy; Baonk 2017, Huong Ngai - Thach That - Ha Noi
		<br />
		All trademarks and registered trademarks appearing on this site are the property of the owners (Nguyen Khac Bao).
		Please contact me at khacbao1991@gmail.com to get more information!
	</div>
</body>
</html>