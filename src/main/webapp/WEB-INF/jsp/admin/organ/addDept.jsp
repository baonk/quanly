<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html style="width:100%;">
<head>
	<title>Add Department</title>
	<link rel="stylesheet" type="text/css" href="/css/registration.css" />
	<link rel="stylesheet" type="text/css" href="/js/jquery/jquery-ui.css" />
	<script src="/js/jquery/jquery-1.12.4.js"></script>
	<script	src="/js/popup.js"></script>
	<script src = "/js/jquery/jquery-ui.js"></script>
	<script type="text/javascript">
		var pDeptId   = "<c:out value='${pDeptID}'/>";
		var pDeptName = "<c:out value='${pDeptName}'/>";
		var compId    = "<c:out value='${compID}'/>";
		var compName  = "<c:out value='${compName}'/>";
		var mode      = "<c:out value='${mode}'/>";

		window.onload = function () {
			document.getElementById("parentDeptName").value = pDeptName;
			
			if (mode == "add") {
				document.getElementById("parentDeptID").value = pDeptId;
				document.getElementById("_compID").value      = compId;
				document.getElementById("_compName").value    = compName;
			}	
		}
		
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
				url = "/admin/updateDept";
			}
			else {
				url = "/admin/saveNewDept";
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
						if (mode == "add") {
							parent.reloadView(document.getElementById("_deptID").value, document.getElementById("_deptName").value);
							parent.divPopUpHidden();
						}
						else {
							parent.reloadView2(document.getElementById("_deptName").value);
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
				
				<table class="tblUserInfo" style="width: 700px;">
					<tr>
						<th style="width: 71px; text-align:center">Department ID<span style="color:red"> *</span></th>
						<td style="width: 220px">
							<form:input type="text" path="departmentid" placeholder="Department ID" class="baonk-control-1 baonk" maxlength="20"  id="_deptID" readonly="${mode != 'add' ? 'true': 'false'}" />
							<lable id="departmentid" class="text-danger"/>
						</td>
						<th style="width: 71px; text-align:center">Department Name<span style="color:red"> *</span></th>
						<td style="width: 220px;">
							<form:input type="text" path="departmentname" placeholder="Department Name" class="baonk-control-1 baonk" maxlength="50" id="_deptName" />
							<lable id="departmentname" class="text-danger"/>
						</td>
					</tr>
					<tr>
						<th style="width: 71px; text-align:center">Department Email<span style="color:red"> *</span></th>
						<td style="width: 220px;">
							<form:input type="text" path="email" placeholder="Email" class="baonk-control-1 baonk" id="_email" />
							<lable id="email" class="text-danger"/>
						</td>
						<th style="width: 71px; text-align:center">Parent Department</th>
						<td style="width: 220px;">
							<form:input type="text" id="parentDeptName" path="" placeholder="Parent Department Name" class="baonk-control-1 baonk" readonly="true" maxlength="50" />
							<form:input type="text" id="parentDeptID" path="parentdept" readonly="true" maxlength="50" style="display: none;"/>
							<form:errors path="parentdept" cssClass="text-danger"/>
						</td>
					</tr>
					<tr>
						<th style="width: 71px; text-align:center">Company ID</th>
						<td style="width: 220px">
							<form:input type="text" path="companyId" placeholder="Company ID" class="baonk-control-1 baonk" maxlength="20"  id="_compID" readonly="true" />
							<lable id="companyId" class="text-danger"/>
						</td>
						<th style="width: 71px; text-align:center">Company Name</span></th>
						<td style="width: 220px;">
							<form:input type="text" path="companyName" placeholder="Company Name" class="baonk-control-1 baonk" maxlength="50" id="_compName" readonly="true" />
							<lable id="companyName" class="text-danger"/>
						</td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>

</body>
</html>