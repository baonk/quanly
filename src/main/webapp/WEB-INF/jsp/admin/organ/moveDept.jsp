<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Move Department</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="/css/general.css" />
		<link rel="stylesheet" type="text/css" href="/css/popup.css" />
		<script	src="/js/jquery/jquery.min.js"></script>
		<script	src="/js/admin/organization/management2.js"></script>
		<script	src="/js/popup.js"></script>
		<script type="text/javascript">
			var listDepts  = ${listDepartment};
			var usercompID = "<c:out value='${usercompID}'/>";
			var deptID     = "<c:out value='${deptID}'/>";
			var arrSubDept = [];

			window.onload = function () {
				initData();
			}

			function close_Click() {
				parent.divPopUpHidden();
			}

			function ok_Click() {
				if (!currentClickedDeptId || currentClickedDeptId == usercompID) {
					alert("Please select a department!");
					return;
				}
				else {
					$.ajax({
						url : '/admin/saveMovedDept',
						method : 'POST',
						dataType : 'JSON',
						data : {
							newDeptId : currentClickedDeptId,
							crDeptId  : deptID
						},
						success : function(data) {
							if (data.result == 1) {
								alert('Dept has been moved');
								parent.reloadView4(deptID);
								parent.divPopUpHidden();
							}
							else {
								var list = data.errorMessages;
								var content = list["reason"];
							}
						},
						error : function(jqXHR, textStatus, errorThrown) {
							alert('Error : ' + jqXHR.status + ", " + textStatus);
						}
					});
				}
			}
			
		</script>
	</head>
	<body class="popup">
		<h1>Employee moving</h1>
		
		<div id="deptView" class="divTable2" style="overflow: auto;" ></div>
		
		<div style="margin: 6px 0px 6px 130px; position:fixed; bottom: 0px; left: 0px;">
			<a id="btnSave" class="baonBttn2"  onClick="ok_Click()"   ><span>OK</span></a>
			<a id="btnCancel"class="baonBttn2" onClick="close_Click()"><span>Cancel</span></a>
		</div>
	
	</body>
</html>