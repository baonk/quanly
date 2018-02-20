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
	<script type="text/javascript" src="/js/fileUpload.js"></script>
	<script type="text/javascript">
		var file     = null;
		var fileSize = 0;
		var xhr      = new XMLHttpRequest();

		window.onload = function() {
			fileUploadStart();
		}

		function uploadbtn() {
			document.getElementById("file").click();
		}

		function downloadAttach(r) {
			var fileinfo                  = r.getAttribute("_path");
			var folderPath                = fileinfo.split("/")[0];
			var fileName                  = fileinfo.split("/")[1];
			var downloadUrl               = "/downloadAttach?folderPath=" + folderPath + "&filename=" + encodeURI(fileName);
			AttachDownFrame.location.href = downloadUrl;
		}
	</script>
</head>

<body>
	<div class="container">
		<div class="panel-group" style="margin-top:40px">
			<div class="panel panel-primary">
				<div id="listAttachFiles" ondragenter="onDragEnter(event)" ondragover="onDragOver(event)" ondrop="onDrop(event)" style="height: 200px;border-top: 1px solid #337ab7;">
					<div id="addFile" style="color: #818181;padding-top: 10px; width: 100%;">
						<img src="/images/upload.png" style="height:150px;width:150px;margin-left: 40%;" onclick="uploadbtn()">
					</div>
					<div style="color: #818181;width: 100%; padding-left: 34%; font-size: 20px;">
						<label style="cursor: pointer;" onclick="uploadbtn()">
							<strong style="font-weight: bold;">Choose a file</strong>
							<span> or drag it here</span>
						</label>
					</div>
				</div>
				<input id="file" type="file" onchange="onDrop()" multiple="multiple" style="width: 1px; height: 1px">
				<div id="fileTable" style="width: 100%; display:none; border-top: 1px solid #337ab7;overflow: auto;"></div>
			</div>
		</div>
	</div>
	<a id="test" style="display:none"></a>
	<iframe name="AttachDownFrame" id="AttachDownFrame" style="display:none"></iframe>
</body>
</html>