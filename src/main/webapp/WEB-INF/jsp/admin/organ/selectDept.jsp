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
			var curSelectTr = null;

			function close_Click() {
				parent.divPopUpHidden();
			}

			function ok_Click() {
				if (!curSelectTr) {
					alert("Please select a department!");
					return;
				}
				else {
					parent.reloadView6(curSelectTr);
					parent.divPopUpHidden();
				}
			}

			function changeBackGroundColor(obj) {
				var objID = obj.getAttribute("id");
				
				if (curSelectTr != null && objID != curSelectTr) {
					var previousTr       = document.getElementById(curSelectTr);
					previousTr.className = "tableTr";
				}
				
				curSelectTr   = objID;
				obj.className = "selectTr";
			}

		</script>
	</head>
	<body class="popup">
		<h1>Searching Department</h1>
		<div style="height: 168px; overflow: auto; margin-top: 4px;">
			<table id="deptView" class="divTable3" style="overflow: auto;" >
				<tr>
					<th>Department ID</th>
					<th>Department Name</th>
					<th>Department Email</th>
					<th>Company Name</th>
				</tr>
				<c:forEach items="${deptList}" var="list">
					<tr class="tableTr" onClick="changeBackGroundColor(this);" id="${list.departmentid}">
						<td><c:out value="${list.departmentid}" /></td>
						<td><c:out value="${list.departmentname}" /></td>
						<td><c:out value="${list.email}" /></td>
						<td><c:out value="${list.companyId}" /></td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div style="margin: 6px 0px 6px 130px; position:fixed; bottom: 0px; left: 140px;">
			<a id="btnSave"  class="baonBttn2" onClick="ok_Click()"   ><span>OK</span></a>
			<a id="btnCancel"class="baonBttn2" onClick="close_Click()"><span>Cancel</span></a>
		</div>
	
	</body>
</html>