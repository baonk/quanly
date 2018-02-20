<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html style="width:100%;">
	<head>
		<title>Employee Image Upload</title>
		<link rel="stylesheet" type="text/css" href="/css/popup.css" />
		<script type="text/javascript">
			var returnFunction;
			var retValue;
			var imageFile = "";

			window.onload = function() {
				try {
					retValue        = parent.personpicture_cross_dialogArguments[0];
					returnFunction  = parent.personpicture_cross_dialogArguments[1];
				}catch(e){
					try {
						retValue        = opener.personpicture_cross_dialogArguments[0];
						returnFunction  = opener.personpicture_cross_dialogArguments[1];
					} catch (e) {
						retValue = window.dialogArguments;
					}
				}
			}

			function close_Click() {
				parent.divPopUpHidden();
			}

			function img_search() {
				if (document.getElementById("file1").value != "") {
					var fileName = document.getElementById("file1").value;
					var extFile  = fileName.split(".");
					
					if (extFile && extFile.length > 1) {
						extFile = extFile[extFile.length - 1].toLowerCase();
						
						if (extFile == "jpg" || extFile == "jpeg" || extFile == "png" || extFile == "gif" || extFile == "bmp") {
							var fd = new FormData();
							fd.append("fileToUpload", document.getElementById("file1").files[0]);
							
							xhr = new XMLHttpRequest();
							xhr.addEventListener("load", uploadComplete, false);
							imageName = document.getElementById("file1").files[0].name;
							xhr.open("POST", "/admin/organ/signImageUpload?userID=" + retValue);
							xhr.send(fd);
						}
						else{
							alert("Only jpg/jpeg and png files are allowed!");
						}
					}
					else {
						alert("File format error!");
					}
				}
			}

			function image_onclick() {
				document.getElementById("file1").click();
			}

			function uploadComplete() {
				var result    = JSON.parse(xhr.responseText).data;
				var fileName  = JSON.parse(xhr.responseText).fname;
				
				if (result == "Fail") {
					alert("Upload Image failed!");
					document.getElementById("file1").value = "";
				}
				else {
					document.getElementById("preview").src      = result;
					document.getElementById("imagefile").value  = fileName;
					imageFile                                   = result;
				}
			}

			function save_Click() {
				if (!imageFile || !document.getElementById("imagefile").value) {
					alert("No image is uploaded!");
					return;
				}
				else {
					alert("Image is uploaded!");
					if (returnFunction != null){
						returnFunction(imageFile);
					}
					else {
						window.returnValue = imageFile;
					}
				}
			}

		</script>
	</head>
	<body>
		<div class="popup">
			<h1>User Image Upload</h1>
		</div>
		<table class="content">
			<tr>
				<th width="125" height="135">
					<img id="preview" name="preview" src="" width="119" height="128" alt="" border="0" style="visibility: visible; margin-top: 3px;">
				</th>
				<td>
					<div style="text-align: center;">Image preview <br>Size: 119*128</div>
				</td>
			</tr>
		</table>
		<table class="content">
			<tr style="height: 45px;">
				<th style="width: 80px;">Image Name</th>
				<td>
					<input id=imagefile name=imagefile class = "inputImage" readonly="readonly" />
					<iframe name="ifrm" src="about:blank" style="display: none"></iframe>
					<form method="post" id="form" name="form" enctype="multipart/form-data" target="ifrm" >
						<input type="file" name="file1" id="file1" style="width: 0px; height: 0px; float:left;" onchange="img_search()" accept="image/*" />
						<input type="hidden" name="mode" id="mode" />
					</form>
					<div class="baonBttn" style="width: 25px;"><span id="btnimagefile" onClick="image_onclick()" >Search Image</span></div>
				</td>
			</tr>
		</table>
		<div style="margin: 6px 0px 6px 145px;">
			<a class="baonBttn2"><span onClick="save_Click();">Save</span></a>
			<a class="baonBttn2"><span onClick="close_Click()">Cancel</span></a>
		</div>
	</body>
</html>