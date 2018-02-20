<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html style="width:100%;">
<head>
	<title>Registration Form</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="/css/registration.css"/>
	<link rel="stylesheet" type="text/css" href="/js/jquery/jquery-ui.css"/>
	<script src="/js/jquery/jquery-1.12.4.js"></script>
	<script	src="/js/popup.js"></script>
	<script src = "/js/jquery/jquery-ui.js"></script>
	<script type="text/javascript">
		var deptId                               = "<c:out value='${deptID}'/>";
		var deptName                             = "<c:out value='${deptName}'/>";
		var personpicture_cross_dialogArguments  = new Array();
		var mode                                 = "<c:out value='${mode}'/>";
		var birthDay                             = "<c:out value='${user.birthday}'/>";

		window.onload = function () {
			var toYear  = new Date().getFullYear();
			var sYear   = parseInt(toYear-70);
			var eYear   = parseInt(toYear+10);
			
			$("#txtBirthday").datepicker({
				changeMonth: true,
				changeYear: true,
				yearRange: sYear + ":" + eYear,
				showOn: "button",
				buttonImage: "/images/calendar-month.gif",
				buttonImageOnly: true
			});
			
			$("#txtBirthday").datepicker("option", "dateFormat", "yy-mm-dd");
			
			if (mode != "add") {
				$("#txtBirthday").datepicker("setDate", birthDay);
			}
			else {
				document.getElementById("deptName").value  = deptName;
				document.getElementById("deptID").value    = deptId;
			}
		}

		function checkRequirement() {
			var userID    = document.getElementById("_userID").value;
			var passWd    = document.getElementById("_passWord").value;
			var userName  = document.getElementById("_userName").value;
			var email     = document.getElementById("_email").value;
			
			if (userID && passWd && userName && email) {
				return 0;
			}
			else {
				return 1;
			}
		}

		function imageAdd() {
			var userID = document.getElementById("_userID").value;
			if (checkRequirement()) {
				alert("Please provide all required fields before choosing an image!");
				return;
			}
			
			personpicture_cross_dialogArguments[0] = userID;
			personpicture_cross_dialogArguments[1] = photoUpload_Complete;
			divPopUpShow(415, 273, "/admin/addUserImage");
		}

		function photoUpload_Complete(ret) {
			divPopUpHidden();
			
			if (ret) {
				document.getElementById("userPhoto").src           = ret;
				document.getElementById("userImage").value         = ret;
				document.getElementById("userPhoto").style.display = "";
			}
		}

		function baonkClose() {
			parent.divPopUpHidden();
			window.close();
		}

		function saveUser() {
			//Clean stage
			document.getElementById("userid").innerHTML     = "";
			document.getElementById("password").innerHTML   = "";
			document.getElementById("username").innerHTML   = "";
			document.getElementById("email").innerHTML      = "";
			document.getElementById("birthday").innerHTML   = "";
			
			var form = document.getElementById("formSubmit");
			var url  = null;
			
			if (mode != "add") {
				url = "/admin/updateUser";
			}
			else {
				url = "/admin/addUser";
			}
			
			$.ajax({
				url: url,
				type: 'POST',
				dataType: "JSON",
				data: new FormData(form),
				processData: false,
				contentType: false,
				success: function (data)
				{
					if (data.result == 1) {
						parent.refreshView();
						parent.divPopUpHidden();
					}
					else {
						var list = data.errorMessages;
					
						Object.keys(list).forEach(function(key) {
							var element         = document.getElementById(key);
							element.innerHTML   = list[key];
						});
					}
				},
				error: function (xhr, desc, err)
				{
					alert("save user error!");
				}
			});
		}
	</script>
</head>
<body>	
	<div class="mainMenu">
		<div class="">
			<form:form modelAttribute="user" action="/admin/addUser" method="post" class="form-horizontal" id="formSubmit">
				<div class="mainTop">
					<div id="saveBttn">
						<ul>
							<li><span onclick="saveUser()">Save</span></li>
							<li><span onclick="imageAdd()">Add Image</span></li>
						</ul>
					</div>
					<div class="closeBttn" onclick="baonkClose();">
						<ul>
							<li><span >Close</span></li>
						</ul>
					</div>
				</div>
				
				<div class="warningText"><span style="color:red">Warning: fields contained * are required!</span></div>
				
				<table class="tblUserInfo">
					<tr>
						<td rowspan="5" style="width:119px; height:180px; text-align:center; min-width:119px; padding :0px;">
							<c:if test="${mode == 'add'}">
								<img id="userPhoto" src="" style="height: 158px; width: 119px; display:none;">
							</c:if>
							<c:if test="${mode != 'add'}">
								<img id="userPhoto" src="${user.image != null ? user.image : '/images/userIcon.png'}" style="height: 158px; width: 119px;">
							</c:if>
							
							<b>User Photo</b>
						</td>
						<th style="width: 71px; text-align:center">User ID<span style="color:red"> *</span></th>
						<td style="width: 220px">
							<form:input type="text" path="userid" placeholder="User ID" class="baonk-control-1 baonk" maxlength="20"  id="_userID" readonly="${mode != 'add' ? 'true': 'false'}" />
							<lable id="userid" class="text-danger"/>
						</td>
						<th style="width: 71px; text-align:center">User Password<span style="color:red"> *</span></th>
						<td style="width: 220px;">
							<form:input type="password" path="password" placeholder="Password" class="baonk-control-1 baonk" maxlength="50" id="_passWord" readonly="${mode != 'add' ? 'true': 'false'}" />
							<lable id="password" class="text-danger"/>
						</td>
					</tr>
					<tr>
						<th style="width: 71px; text-align:center">User Name<span style="color:red"> *</span></th>
						<td style="width: 220px;">
							<form:input type="text" path="username" placeholder="User Name" class="baonk-control-1 baonk" id="_userName" />
							<lable id="username" class="text-danger"/>
						</td>
						<th style="width: 71px; text-align:center">Department Name</th>
						<td style="width: 220px;">
							<form:input type="text" id="deptName" path="departmentname" placeholder="Department Name" class="baonk-control-1 baonk" readonly="true" maxlength="50" />
							<form:errors path="departmentname" cssClass="text-danger"/>
							<form:input type="text" id="deptID" path="departmentid" readonly="true" maxlength="50" style="display: none;"/>
							<form:errors path="departmentid" cssClass="text-danger"/>
						</td>
					</tr>
					<tr>
						<th style="width: 71px; text-align:center">Position</th>
						<td style="width: 220px;">
							<form:select path="position" class="baonk-control-5 baonk">
								<form:option value="System Developer"></form:option>
								<form:option value="Manager"></form:option>
								<form:option value="Web Developer"></form:option>
								<form:option value="Tester"></form:option>
								<form:option value="Designer"></form:option>
								<form:option value="Business"></form:option>
							</form:select>
							<form:errors path="position" cssClass="text-danger"/>
						</td>
						<th style="width: 71px; text-align:center">Other position</th>
						<td style="width: 220px;">
							<form:input type="text" path="otherpos" placeholder="Other Position" class="baonk-control-1 baonk" maxlength="50" />
							<form:errors path="otherpos" cssClass="text-danger"/>
						</td>
					</tr>
					<tr>
						<th style="width: 71px; text-align:center">Birthday<span style="color:red"> *</span></th>
						<td style="width: 220px;">
							<form:input type="text" path="birthday" placeholder="Birthday" class="baonk-control-1 baonk" id="txtBirthday" style="text-align: center; width: 120px;" />
							<lable id="birthday" class="text-danger"/>
						</td>
						<th style="width: 71px; text-align:center">Hobby</th>
						<td style="width: 220px;" >
							<form:input type="text" path="hobby" placeholder="Hobby" class="baonk-control-1 baonk" />
							<form:errors path="hobby" cssClass="text-danger"/>
						</td>
					</tr>
					<tr>
						<th style="width: 71px; text-align:center">Email<span style="color:red"> *</span></th>
						<td style="width: 220px;">
							<form:input type="text" path="email" placeholder="Email" class="baonk-control-1 baonk" id="_email" />
							<lable id="email" class="text-danger"/>
						</td>
						<th style="width: 71px; text-align:center">Phone number</th>
						<td style="width: 220px;" >
							<form:input type="text" path="phone" placeholder="Phone Number" class="baonk-control-1 baonk" maxlength="20" />
							<form:errors path="phone" cssClass="text-danger"/>
						</td>
					</tr>
				</table>
				
				<table class="tblOtherInfo">
					<tr>
						<th style="width: 125px; text-align:center;">Detail Information</th>
						<td colspan="5" style="text-align:center; width: 100%;">
							<span>In this section you can provide your detailed information.</span>
						</td>
					</tr>
					<tr>
						<th style="width: 70px; text-align:center">Home Phone</th>
						<td style="width: 150px">
							<form:input type="text" path="homephone" placeholder="Home Phone" class="baonk-control-2 baonk" maxlength="50" />
						</td>
						<th style="width: 70px; text-align:center">Fax Number</th>
						<td style="width: 150px">
							<form:input type="text" path="fax" placeholder="Fax Number" class="baonk-control-2 baonk" maxlength="50" />
						</td>
						<th style="width: 70px; text-align:center">Post Code</th>
						<td style="width: 150px">
							<form:input type="text" path="postcode" placeholder="Post Code" class="baonk-control-2 baonk" maxlength="50" />
						</td>
					</tr>
					<tr>
						<th style="width: 70px; text-align:center">Nick Name</th>
						<td style="width: 150px">
							<form:input type="text" path="nickname" placeholder="Nick Name" class="baonk-control-2 baonk" maxlength="50" />
						</td>
						<th style="width: 70px; text-align:center">Sex</th>
						<td style="width: 150px">
							<form:select path="sex" class="baonk-control-4 baonk" >
								<form:option value="Male"></form:option>
								<form:option value="Female"></form:option>
							</form:select>
						</td>
						<th style="width: 70px; text-align:center">Country</th>
						<td style="width: 150px">
							<form:select path="country" class="baonk-control-4 baonk" >
								<form:option value="VietNam"></form:option>
								<form:option value="Korea"></form:option>
								<form:option value="Japan"></form:option>
								<form:option value="England"></form:option>
								<form:option value="United States"></form:option>
							</form:select>
						</td>
					</tr>
					
					<tr>
						<th style="width: 70px; text-align:center">Home Address</th>
						<td colspan="5">
							<form:input type="text" path="homeaddress" placeholder="Home Address" class="baonk-control-3 baonk" maxlength="250" />
						</td>
					</tr>
				</table>
				<form:input type="text" path="image" style="display:none;" id="userImage"/>
			</form:form>
		</div>
	</div>
	
	<div style="width: 100%; height: 100%; position: absolute; top: 0; left: 0; z-index: 1000; background: none rgba(0,0,0,0.7); display: none;" id="fogPanel">&nbsp;</div>
		
	<div class="layerpopup"  style="z-index: 2000; position: absolute;display: none;" id="iFramePanel">
		<iframe src="/blank.htm" style="border:none;" id="iFrameLayer"></iframe>
	</div>

</body>
</html>