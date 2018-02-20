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
</head>

<body>
	<div style="float:left; width:220px; display: block; height: 100%;">
		<iframe src="/admin/organLeft"  id="leftOrg"  name="leftOrg" class="ifrmLeft"></iframe>
	</div>
	<div style="padding-left: 220px; display: block; height: 100%;">
		<iframe src="about:blank" id="rightOrg" name="rightOrg" class="ifrmRight" ></iframe>
	</div>
</body>
</html>