<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html style="width:100%;">
<head>
	<title>Add Company</title>
	<link rel="stylesheet" type="text/css" href="/css/registration.css" />
	<link rel="stylesheet" type="text/css" href="/js/jquery/jquery-ui.css" />
	<script src="/js/jquery/jquery-1.12.4.js"></script>
	<script	src="/js/popup.js"></script>
	<script src = "/js/jquery/jquery-ui.js"></script>
	<script type="text/javascript">
		var mode = "<c:out value='${mode}'/>";

		function checkRequirement() {
			var deptID    = document.getElementById("_deptID").value;
			var deptName  = document.getElementById("_deptName").value;
			var email     = document.getElementById("_email").value;
			
			if (deptID && deptName && email) {
				return 0;
			}
			else {
				return 1;
			}
		}

		function baonkClose() {
			parent.divPopUpHidden();
			window.close();
		}

		function saveDept() {
			if (checkRequirement()) {
				alert("Please provide all required information!");
				return;
			}
			
			//Clean stage
			document.getElementById("departmentid").innerHTML   = "";
			document.getElementById("departmentname").innerHTML = "";
			document.getElementById("email").innerHTML          = "";
			
			var form = document.getElementById("formSubmit");
			var url  = null;
			
			if (mode != "add") {
				url = "/admin/updateCompany";
			}
			else {
				url = "/admin/saveNewCompany";
			}
			
			$.ajax({
				url: url,
				type: 'POST',
				dataType: "JSON",
				data: new FormData(form),
				processData: false,
				contentType: false,
				success: function (data) { 
					if (data.result == 1) {
						if (mode == "add") {
							var numberOfComp = "<c:out value='${numberOfComp}'/>";
							parent.reloadView5(document.getElementById("_deptID").value, document.getElementById("_deptName").value, numberOfComp);
							parent.divPopUpHidden();
						}
						else {
							parent.refreshView();
							parent.divPopUpHidden();
						}
					}
					else {
						var list = data.errorMessages;
						
						Object.keys(list).forEach(function(key) {
							var element         = document.getElementById(key);
							element.innerHTML   = list[key];
						});
					}
				},
				error: function (xhr, desc, err) {
					alert("save user error!");
				}
			}); 
		}
	</script>
</head>
<body>
	<div class="mainMenu">
		<div class="">
			<form:form modelAttribute="dept" action="" method="post" class="form-horizontal" id="formSubmit">
				<div class="mainTop">
					<div id="saveBttn">
						<ul>
							<li><span onclick="saveDept()">Save</span></li>
						</ul>
					</div>
					<div class="closeBttn" onclick="baonkClose();">
						<ul>
							<li><span >Close</span></li>
						</ul>
					</div>
				</div>
				
				<div class="warningText"><span style="color:red">Warning: fields contained * are required!</span></div>
				
				<table class="tblUserInfo" style="width: 600px;">
					<tr>
						<th style="width: 71px; text-align:center">Company ID<span style="color:red"> *</span></th>
						<td style="width: 220px">
							<form:input type="text" path="departmentid" placeholder="Company ID" class="baonk-control-1 baonk" maxlength="20"  id="_deptID" readonly="${mode != 'add' ? 'true': 'false'}" />
							<lable id="departmentid" class="text-danger"/>
						</td>
						<th style="width: 71px; text-align:center">Company Name<span style="color:red"> *</span></th>
						<td style="width: 220px;">
							<form:input type="text" path="departmentname" placeholder="Company Name" class="baonk-control-1 baonk" maxlength="50" id="_deptName" readonly="${mode != 'add' ? 'true': 'false'}" />
							<lable id="departmentname" class="text-danger"/>
						</td>
					</tr>
					<tr>
						<th style="width: 71px; text-align:center">Company Email<span style="color:red"> *</span></th>
						<td style="width: 220px;" colspan="3">
							<form:input type="text" path="email" placeholder="Email" class="baonk-control-1 baonk" id="_email" style="width: 559px;"/>
							<lable id="email" class="text-danger"/>
						</td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>

</body>
</html>