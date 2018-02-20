<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

<head>
	<title>Main Page</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" type="text/css" href="/css/home.css" />	
	<link rel="stylesheet" href="/css/bootstrap.min.css">
	<link rel="shortcut icon" type="image/png" href="/images/favicon.png"/>
	<script src="/js/jquery/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/js/fileUpload.js"></script>
</head>
<body>
	<div>
		<iframe src="/topMenu"  id="topMenu"  name="topMenu"  style="noresize; margin:0px 0px 0px 0px; padding:0px 0px 0px 0px;border:none; width:100%; height:100px;"></iframe>
	</div>
	<div>
		<iframe src="/mainMenu" id="mainMenu" name="mainMenu" style="noresize; margin:0px 0px 0px 0px; padding:0px 0px 0px 0px;border:none;width:100%; height: 1200px; "></iframe>
	</div>
</body>
</html>