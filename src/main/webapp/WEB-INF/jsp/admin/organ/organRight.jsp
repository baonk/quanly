<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="/css/general.css" />
		<script	src="/js/jquery/jquery.min.js"></script>
		<script	src="/js/admin/organization/management.js"></script>
		<script	src="/js/popup.js"></script>
		<script type="text/javascript">
			var listDepts  = ${listDepartment};
			var userDeptID = "<c:out value='${userdeptID}'/>";
			var usercompID = "<c:out value='${usercompID}'/>";
			var arrSubDept = [];
			var value      = "muser";

			window.onload = function () {
				initData();
			}

		</script>
	</head>
	<body class="rightbody">
		<h1>Company organization</h1>
		<a class="exportBttn" style="vertical-align:middle" href="/admin/exportExcelFile?companyId=<c:out value='${usercompID}'/>"><span>Export File</span></a>
		<div style="height: 703px;">
			<table style="margin-top:10px; width: 100%;">
				<tr>
					<th style="width: 50%;">Company management</th>
					<th>
						<div style="display: inline;">
							<input type="radio" name="listOpt" id="listOpt1" value="muser" onClick="changeList(this)" checked />
							<span>Employee list&nbsp;&nbsp;&nbsp;&nbsp;</span>
						</div>
						<div style="display: inline;">
							<input type="radio" name="listOpt" id="listOpt2" value="mdept" onClick="changeList(this)" />
							<span>Department list&nbsp;&nbsp;&nbsp;&nbsp;</span>
						</div>
						<div style="display: inline;">
							<input type="radio" name="listOpt" id="listOpt2" value="mcomp" onClick="changeList(this)" />
							<span>Company list</span>
						</div>
					</th>
				</tr>
				<tr>
					<th>
						<input id="deptkeyword" onKeyPress="deptsearchPress();" style="width: 200px; height: 20px;" />
						<a class="imgbtn" style="vertical-align:middle"><span onClick="deptsearchClick()">Dept Search</span></a>
					</th>
					<th>
						<select id="search_type1" style="width:100px; height: 20px; text-align: center; text-align-last: center;">
							<option selected value="name">Name</option>
							<option value="userid">UserID</option>
							<option value="position">Position</option>
							<option value="email">Email</option>
							<option value="phone_number">Phone</option>
							<option value="homephone">HomePhone</option>
							<option value="nickname">Nick Name</option>
							<option value="country">Country</option>
							<option value="homeaddress">Address</option>
						</select>
						<select id="search_type2" style="width:130px; height: 20px; text-align: center; text-align-last: center; display: none;">
							<option selected value="department_name">Department Name</option>
							<option value="department_id">DeparmentID</option>
							<option value="email">Email</option>
							<option value="company_id">Company ID</option>
							<option value="company_name">Company Name</option>
						</select>
						<select id="search_type3" style="width:120px; height: 20px; text-align: center; text-align-last: center; display: none;">
							<option selected value="company_name">Company Name</option>
							<option value="company_id">Company ID</option>
							<option value="email">Email</option>
						</select>
						<input id="keyword" onKeyPress="searchPress(event);" style="width:160px; height: 20px;" />
						<a class="imgbtn" style="vertical-align:middle"><span onClick="searchClick()">Search</span></a>
					</th>
				</tr>
				<tr>
					<th style="padding: 3px; text-align: left; font-weight: normal;vertical-align:top">
						<div id="deptView" class="divTable" style="overflow: auto;" ></div>
					</th>
					<th style="padding: 3px; text-align: left;vertical-align:top">
						<div id="detailDeptView" class="divTable" style="overflow-x: hidden; overflow-y:auto; ">
							<div id="employeeList" class="subMainTab">
								<div style="left: 10%;" class="subHeaderTab">User Name</div>
								<div style="left: 40%;" class="subHeaderTab">Department</div>
								<div style="left: 80%;" class="subHeaderTab">Position</div>
							</div>
							<div id="deptList" class="subMainTab" style="display:none;">
								<div class="subHeaderTab" style="left: 10%;">Department Name</div>
								<div class="subHeaderTab" style="left: 60%;">Company Name</div>
							</div>
							<div id="companyList" class="subMainTab" style="display:none;">
								<div class="subHeaderTab" style="left: 10%;">Company ID</div>
								<div class="subHeaderTab" style="left: 60%;">Company Name</div>
							</div>
							<div id="errorDiv" class="subDisplayTab" style= "display: none;">
								<div class="subDisplayElmTab" style="width: 100%; text-align: center; ;">No data found!</div>
							</div>
						</div>
						<div style="height: 5px; overflow: hidden">&nbsp;</div>
					</th>
				</tr>
				<tr>
					<th colspan="2" style="height: 50px;"> 
						<div id="userOpt" style="display: block;">
							<a class="imgbtn" id="usermenu3"><span onClick="displayUserInfo()">User Information</span></a>
							<a class="imgbtn"><span onClick="addUser()">Add User</span></a>
							<a class="imgbtn" id="usermenu2"><span onClick="delUser()">Delete User</span></a>
							<a class="imgbtn" id="usermenu1"><span onClick="moveUser()">Move User</span></a>
							<a class="imgbtn" id="userRetire"><span onClick="changeUserPassWd()">Change Password</span></a>
						</div>
						<div id="deptOpt" style="display: none;">
							<a class="imgbtn"><span onClick="viewDept()">Department Information</span></a>
							<a class="imgbtn"><span onClick="addDept()">Add Department</span></a>
							<a class="imgbtn" id="usermenu10"><span onClick="delDept()">Delete Department</span></a>
							<a class="imgbtn" id="usermenu8"><span onClick="movDept()">Move Department</span></a>
						</div>
						<div id="compOpt" style="display: none;">
							<a class="imgbtn"><span onClick="viewCompany()">Company Information</span></a>
							<a class="imgbtn"><span onClick="addCompany()">Add Company</span></a>
							<a class="imgbtn"><span onClick="delCompany()">Delete Company</span></a>
						</div>
					</th>
				</tr>
			</table>
		</div>
		
		<div style="width: 100%; height: 100%; position: absolute; top: 0; left: 0; z-index: 1000; background: none rgba(0,0,0,0.7); display: none;" id="fogPanel">&nbsp;</div>
		
		<div class="layerpopup"  style="z-index: 2000; position: absolute;display: none;" id="iFramePanel">
			<iframe src="/blank.htm" style="border:none;" id="iFrameLayer"></iframe>
		</div>
	</body>
</html>