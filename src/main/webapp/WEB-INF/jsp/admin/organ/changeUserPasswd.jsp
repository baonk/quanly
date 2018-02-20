<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Move User</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="/css/general.css" />
		<link rel="stylesheet" type="text/css" href="/css/popup.css" />
		<script	src="/js/jquery/jquery.min.js"></script>
		<script	src="/js/popup.js"></script>
		<script type="text/javascript">
			var userID = "<c:out value='${userID}'/>";

			function close_Click() {
				parent.divPopUpHidden();
			}

			function ok_Click() {
				var newPasswd    = document.getElementById("newPasswd");
				var newPqsswdCfm = document.getElementById("confmNewPasswd");
				
				if (!newPasswd.value.replace(/\s+/g, '')) {
					alert("Please enter valid password!");
					newPasswd.focus();
					return;
				}
				
				if (newPasswd.value.length < 5) {
					alert("Password must have at least 5 characters!");
					newPasswd.focus();
					return;
				}
				
				if (!newPqsswdCfm.value.replace(/\s+/g, '')) {
					alert("Please enter new password again!");
					newPqsswdCfm.focus();
					return;
				}
				
				if (newPqsswdCfm.value.length < 5) {
					alert("Password and confirm password are different!");
					newPasswd.focus();
					return;
				}
				
				$.ajax({
					url : '/admin/saveUserNewPassword',
					method : 'POST',
					dataType : 'JSON',
					data : {
						newPasswd : newPasswd.value,
						userId    : userID
					},
					success : function(data, textStatus, jqXHR) {
						if (data.result == "OK") {
							console.log("OK");
							alert("Changed password successfully!");
							parent.refreshView();
							parent.divPopUpHidden();
						}
						else if (data.result == "FAIL"){
							console.log("FAIL");
							alert("Server side errors!");
							newPasswd.value     = "";
							newPqsswdCfm.value  = "";
							newPasswd.focus();
						}
						else {
							console.log("OTHER");
							alert(data.result);
							newPasswd.value     = "";
							newPqsswdCfm.value  = "";
							newPasswd.focus();
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						alert('Error : ' + jqXHR.status + ", " + textStatus);
					}
				});
				
			}
			
		</script>
	</head>
	<body class="popup">
		<h1>Change User's Password</h1>
		
		<table class="changePasswdTbl">
			<tr>
				<th style="width: 140px; text-align: left; padding-left: 7px;">New Password</th>
				<td style="text-align:center;">
					<input type="password" placeholder="Enter new password" id="newPasswd" class="passwdInput">
				</td>
			</tr>
			<tr>
				<th style="width: 140px; text-align: left; padding-left: 7px;">Confirm new password</th>
				<td style="text-align:center;">
					<input type="password" placeholder="Enter new password again" id="confmNewPasswd" class="passwdInput">
				</td>
			<tr>
			</tr>
		</table>
		
		<div style="margin: 6px 0px 6px 130px; position:fixed; bottom: 0px; left: 0px;">
			<a id="btnSave" class="baonBttn2"  onClick="ok_Click()"   ><span>OK</span></a>
			<a id="btnCancel"class="baonBttn2" onClick="close_Click()"><span>Cancel</span></a>
		</div>
		
	</body>
</html>