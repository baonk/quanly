	var currentClickedDeptId = null;
	
	function initData(mode) {
		var deptView = document.getElementById("deptView");

		var divEl = document.createElement("div");
		var img1 = document.createElement("img");
		img1.setAttribute("class", "deptOn");			
		img1.onclick = function () {companyOnClick(this);}
		var img2 = document.createElement("img");
		img2.setAttribute("class", "companyImg");
		img2.setAttribute("deptId", listDepts["departmentid"]);
		img2.onclick = function () {getDetailData(this);};
		var span = document.createElement("span");
		span.innerHTML = listDepts["departmentname"];
		span.setAttribute("deptId", listDepts["departmentid"]);
		span.setAttribute("style", "cursor: pointer;");
		span.setAttribute("name", listDepts["departmentid"]);
		span.onclick = function () {getDetailData(this);};
		
		divEl.appendChild(img1);
		divEl.appendChild(img2);
		divEl.appendChild(span);
		divEl.setAttribute("id", "company");
		divEl.setAttribute("order", 0);
		divEl.setAttribute("style", "padding-top: 5px; padding-left: 5px;");								
		deptView.appendChild(divEl);		
		displaySubDept(divEl, listDepts["subDept"]);
		
	}
	
	function getDetailData(obj) {			
		var deptId = obj.getAttribute("deptId");
		
		if (currentClickedDeptId != deptId) {
			//Set previous clicked element class off 
			if (currentClickedDeptId != null) {
				var deptElemt = document.getElementById(currentClickedDeptId);			
				deptElemt.setAttribute("class", "subOff");				
			}
			
			currentClickedDeptId = deptId;
			//Set current clicked element class on
			if (obj.nodeName.toLowerCase() == "span") {
				obj.setAttribute("class", "subOn");
				obj.setAttribute("id", deptId);
			}
			else {
				var spanObj = obj.nextSibling;
				spanObj.setAttribute("class", "subOn");
				spanObj.setAttribute("id", deptId);
			}
		}
		
	}		
	
	function displaySubDept(mainDeptElm, list) {	
		var pos = null;
		var mainEl = null;
		
		if (mainDeptElm.getAttribute("parent")) {					
			pos = mainDeptElm.getAttribute("parent");
			mainEl = mainDeptElm.parentElement;
		}
		else {
			pos = mainDeptElm.getAttribute("order");
			mainEl = mainDeptElm;
		}							
		
		for (var i = 0; i < list.length; i++) {
			var divEl = document.createElement("div");
			
			var img1 = document.createElement("img");			
			var img2 = document.createElement("img");
			img2.setAttribute("class", "deptImg");
			img2.setAttribute("deptId", list[i]["departmentid"]);
			img2.onclick = function () {getDetailData(this);};
			var span = document.createElement("span");
			span.innerHTML = list[i]["departmentname"];
			span.setAttribute("style", "cursor: pointer;");
			span.setAttribute("deptId", list[i]["departmentid"]);
			span.setAttribute("name", list[i]["departmentid"]);
			span.onclick = function () {getDetailData(this);};
			
			divEl.appendChild(img1);
			divEl.appendChild(img2);
			divEl.appendChild(span);					
			divEl.setAttribute("style", "padding-top: 5px; padding-left: 15px;");	
			divEl.setAttribute("order", pos);
			insertAfter(divEl, mainEl);		
			
			if (list[i]["hasSubDept"] == 1) {
				//img1.setAttribute("class", "deptOff");	
				img1.setAttribute("parent", pos);	
				img1.setAttribute("deptId", list[i]["departmentid"]);
				img1.setAttribute("id", list[i]["departmentid"] + "+" + pos);
				img1.onclick = function () {deptOnClick(this);};
				
				if (list[i]["subDept"] != null && list[i]["subDept"] != "null") {
					var uniqueId = img1.getAttribute("id");		
					arrSubDept.push(uniqueId);
					img1.setAttribute("class", "deptOn");
					displaySubDept(divEl, list[i]["subDept"]);
				}
				else {
					img1.setAttribute("class", "deptOff");
				}
				
			}
			else {
				img1.setAttribute("class", "deptNone");
			}
		}					
	}
			
	function companyOnClick(obj) {				
		var position = parseInt(obj.parentElement.getAttribute("order"));				
		var childs = obj.parentElement.childNodes;
		
		if (obj.getAttribute("class") == "deptOn") {									
			for (var i = 3; i < childs.length; i++) {
				childs[i].style.display = "none";
			}					
			obj.setAttribute("class", "deptOff");
		}
		else {
			for (var i = 3; i < childs.length; i++) {
				childs[i].style.display = "";
			}					
			obj.setAttribute("class", "deptOn");
		}
		
	}		
	
	function deptOnClick(obj) {
		var parentElm = obj.parentElement;
		var position = parseInt(parentElm.getAttribute("parent"));
		var childs = parentElm.childNodes;
		
		if (obj.getAttribute("class") == "deptOn") {									
			for (var i = 3; i < childs.length; i++) {
				childs[i].style.display = "none";
			}					
			obj.setAttribute("class", "deptOff");
		}
		else {
			var uniqueId = obj.getAttribute("id");					
			if (arrSubDept.indexOf(uniqueId) > -1) {
				renderSubDept(obj, childs);
			}
			else {
				var deptId = obj.getAttribute("deptId");				
				
				$.ajax({
					type: "POST",
					url: "/admin/organ/getSimpleSubDept",
					data: {
						"deptID": deptId
					},
					dataType: "JSON",
					async: true,
					success: function(result) {							
						var listDept = result["subDept"];
						arrSubDept.push(uniqueId);
						displaySubDept(obj, listDept);	
						renderSubDept(obj, childs);								
					},
					error: function (xhr, status, e){
						alert("Get department data failed");
					}
				});	
			}															
		}									
	}		
	
	function renderSubDept(obj, childList) {
		for (var i = 3; i < childList.length; i++) {
			childList[i].style.display = "";
		}	
		
		obj.setAttribute("class", "deptOn");						
	}	
	
	
	function insertAfter(newNode, referenceNode) {
		if (referenceNode.getAttribute("parent")) {
			 referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
		}
		else {
			referenceNode.appendChild(newNode);
		}	   
	}
