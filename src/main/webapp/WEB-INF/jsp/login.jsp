<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
	<title>Login Pagel</title>
	<link rel="stylesheet" href="/css/bootstrap.min.css">
	<link rel="shortcut icon" type="image/png" href="/images/favicon.png"/>
	<script src="/js/jquery/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
</head>
<body >
	<div class="container">
		<img src="/images/login.jpg" class="img-responsive center-block" width="300" height="300" alt="Logo" />
		<form action="/login" method="POST" class="form-signin"  style="padding-left: 25%; padding-right: 25%;" id ="formCreate" name ="formCreate">
			<h3 class="form-signin-heading" style="padding-left: 40%;">Welcome</h3>
			<br/>
			
			<input type="text" id="userid" name="userid"  placeholder="UserId" class="form-control" />
			<br/> 
			<input type="password"  placeholder="Password" id="password" name="password" class="form-control" /> <br />
			
			<c:if test="${error != ''}">
				<p style="font-size: 20; color: #FF1C19;">UserId or Password invalid, please verify</p>
				<script type="text/javascript">
					document.getElementById("userid").value = "<c:out value='${sessionScope.SPRING_SECURITY_LAST_USERID}'/>";
					document.getElementById("password").value = "<c:out value='${sessionScope.SPRING_SECURITY_LAST_PASS}'/>";
				</script>
			</c:if>
			<button class="btn btn-lg btn-primary btn-block" >Login</button>
		</form>
	</div>
</body>
</html>