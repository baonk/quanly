function onDragEnter(event) {
	event.dataTransfer.dropEffect = "copy";
	event.stopPropagation();
	event.preventDefault();
}

function onDragOver(event) {
	event.dataTransfer.dropEffect = "copy";
	event.stopPropagation();
	event.preventDefault();
}

function onDrop(event) {
	file = new Array;
	var fileList;
	var tempFileSize = 0;
	//var fileCnt = file.length;
	
	if (event != null || event != undefined) {
		event.stopPropagation();
		event.preventDefault();
	}
	
	if (event == undefined) {
		fileList = document.getElementById("file").files;	
		console.log("file.Files: " + fileList.length);
	}
	else {
		fileList = event.dataTransfer.files;
		console.log("event.dataTransfer.files: " + fileList.length);
	}
	
	for (var i = 0; i < fileList.length; i++) {
		if(fileList[i].size / 1024 / 1024 > 5) {
			alert("The maximum file size is 5MB!");
			return;
		}
		else {
			file[i + 1] = fileList[i];
			tempFileSize += tempFileSize;
		}
	}
	
	fileSize += tempFileSize;
	fileUpload();	
}

function fileUploadStart() {
    var ua = navigator.userAgent;
    
    if (ua.indexOf("Safari") > 0 && ua.indexOf("Chrome") == -1 && ua.indexOf("Macintosh") == -1) {
        document.getElementById("file").multiple = false;
    }
    
	var oTable = document.getElementById("filelist");
	
	if (oTable == null) {
		oTable = document.createElement("TABLE");
	    oTable.style.width = "100%";
	    oTable.id = "filelist";
	    oTable.className = "sublist";
	}
	
    document.getElementById("fileTable").appendChild(oTable); 
}

function fileUpload() {	
    var fd 	= new FormData();   

    for (var i = 0; i < file.length; i++) {
        fd.append("fileToUpload", file[i]);
    }
    
    fd.append("boardid", window.parent.pBoardID);
    fd.append("maxsize", window.parent.AttachLimit * 1024 * 1024);
    fd.append("mode", "ATT");   
    isfileup = true;
    xhr.addEventListener("load", uploadComplete, false);
    xhr.open("POST", "/uploadFile");
    xhr.send(fd);        
}

function uploadComplete(evt) {
	xhr.removeEventListener("load", uploadComplete);
    displayAttachFile(xhr.responseText);    
}

function loadXMLString(xmlstring) {
    var xmlDoc;      
    var parser = new DOMParser();
    xmlDoc = parser.parseFromString(xmlstring, "text/xml");
    parser = null;
    return xmlDoc;
}

function displayAttachFile(strXML) {	
    if (strXML == "ERROR") {    	
        alert("Error!");        
        return;
    }     
    
    var xml = loadXMLString(strXML);      
    
    try {    	
        var strAttach = "";
        strPreViewAttach = "";
        var listtable;    
        listtable = document.getElementById("filelist");
        document.getElementById("fileTable").style.display = "inline-block";
        var extCheck = false;     
        
        for (i = 0; i < SelectNodes(xml, "ROOT/NODES/DATA").length; i++) {        	
            var fileinfo = getNodeText(SelectNodes(xml, "ROOT/NODES/DATA")[i]);           
            var attid = getNodeText(SelectNodes(xml, "ROOT/NODES/DATA2")[i]);           

            if (getNodeText(SelectNodes(xml, "ROOT/NODES/DATA3")[i]) == "OK") {          	
                objTr = document.createElement("TR");
                objTr.setAttribute("fileinfo", fileinfo);
                objTr.setAttribute("attid", attid);

                var objTd = document.createElement("TD");                
                objTd.style.paddingLeft  = "10px";
                objTd.style.paddingRight = "0px";
                objTd.style.paddingBottom = "0px";
                objTd.style.paddingTop = "0px";
                objTd.style.width = "24px";
                objTd.style.height = "24px";
                
                var image_tag = document.createElement("img");
                image_tag.setAttribute("_path", fileinfo);
                image_tag.src = "/images/icon_adddownload.gif";
                image_tag.setAttribute("style", "height: 24px; width: 24px; cursor: pointer;");
                image_tag.onclick = function () { downloadAttach(this); };
                objTd.appendChild(image_tag);
                objTr.appendChild(objTd);

                var objTd2 = document.createElement("TD");
                objTd2.setAttribute("style", "paddingBottom: 0px; paddingTop: 0px; cursor: pointer;");
                objTd2.setAttribute("_path", fileinfo);
                objTd2.onclick = function () { downloadAttach(this); };
                
                var fileSize = parseInt(fileinfo.split("/")[2]);

                if (fileSize / 1024 / 1024 > 1) {
                    fileSize = (Math.floor(parseFloat(fileSize / 1024 / 1024 * 10)) / 10).toFixed(1) + "MB";
                }
                else if (fileSize / 1024 > 1) {
                    fileSize = parseInt(fileSize / 1024) + "KB";
                }
                else {
                    fileSize = fileSize + "B";
                }
                
                var strFileSize = fileinfo.split("/")[1] + "(" + fileSize + ")";
                objTd2.innerHTML = strFileSize;
                objTr.appendChild(objTd2);
                document.getElementById("filelist").appendChild(objTr);
            }
            else{            	
            	extCheck = true;    
            }
                      
        }
        
        if (extCheck) {
            alert(strLang267);
        }
    }
    catch (e) { 
    	alert("returnvalue :: " + e.description); 
    }
}


function filedelete(r) {    
    var pBoardID = window.parent.pBoardID;
    var strRet = "";
    var fileinfo = r.getAttribute("_path");    
    var isFileDelete = false;    
    var i = r.parentNode.parentNode.rowIndex;
    document.getElementById("filelist").deleteRow(i);
    
    var filecnt = document.getElementById("filelist").childNodes.length;
    if (filecnt == 0) {        
        document.getElementById("fileTable").style.display = "none";    
    }
    
    //Send delete file request to server
    var fd = new FormData();
    fd.append("fileToDelete", fileinfo);
    xhr.open("POST", "/deleteFile");
    xhr.send(fd);
}


function getNodeText(node) {	
    var result = "";
    if (node != null) {    	
        if (CrossYN()) {        	
            if (typeof (node.textContent) != "undefined") {            	
                result = trim_Cross(node.textContent);                
            }
            else {
                result = trim_Cross(node.text);                
            }
        }
        else {
            if (typeof (node.innerText) == "undefined") {
                result = trim_Cross(node.text);
            }
            else {
                result = trim_Cross(node.innerText);
            }

        }
    }
    return result;
}

function trim_Cross(value) {
    value = String(value);
    value = value.trim();
    if (value == null || value == "undefind" || value == "" || value == "\n") {
        return "";
    }
    return value.trim();
}

function SelectNodes(xmlDoc, elementPath) {	
    var parentPath = "";
    var nodeName = "";
    var parentNode = null;     
    
    if (elementPath == null || elementPath == "" || elementPath == undefined) return false;   
    if (elementPath.indexOf("/") == -1) {    	
        parentPath = elementPath;
        nodeName = elementPath;
        parentNode = xmlDoc.ownerDocument == null ? xmlDoc : xmlDoc.ownerDocument;
    } else {    	  
        parentPath = elementPath.substr(0, elementPath.lastIndexOf("/"));
        nodeName = elementPath.substr(elementPath.lastIndexOf("/") + 1);

        if (parentPath.indexOf("//") != 0) {        	
            if (parentPath.indexOf("/") == 0) {
                parentPath = "/" + parentPath;
            } else {
                parentPath = "//" + parentPath;
            }
        }
        //Document 인경우      
        if (xmlDoc.nodeType == 9) {
            parentNode = SelectSingleNodeNew(xmlDoc, parentPath);
        }
        else {
            parentNode = SelectSingleNodeNew(xmlDoc, parentPath);
        }
    }
    if (parentNode == null) return false;

    return GetElementsByTagName(parentNode, nodeName);
}

//CrossBrowser적용
function CrossYN() {
	var ua = navigator.userAgent;
	var result = true;

    // 크로스 브라우저 IE9이하:false IE외: true
    if (/msie 10/i.test(ua)){
        result = true;	
    }else if (/msie/i.test(ua)){
		result = false;
	}else if (/firefox/i.test(ua)){
		result = true;
	}else if (/chrome/i.test(ua)){
		result = true;
	}else if (/safari/i.test(ua)){
		result = true;
	}else if (/opera/i.test(ua)){
		result = true;
	}else if (/trident/i.test(ua)){
		result = true;
	}
	
    return result;
}

function SelectSingleNodeNew(xmlDoc, elementPath) {
    if (CrossYN()) {
        if (elementPath.indexOf("//") != 0) {
            if (elementPath.indexOf("/") == 0) {
                elementPath = "/" + elementPath;
            } else {
                elementPath = "//" + elementPath;
            }
        }
        var nsResolver;
        try {
            nsResolver = xmlDoc.createNSResolver(xmlDoc.ownerDocument == null ? xmlDoc.documentElement : xmlDoc.ownerDocument.documentElement);
        } catch (e) {
            nsResolver = null;
        }
        try {
            var xpathResult = xmlDoc.evaluate(elementPath, xmlDoc, nsResolver, XPathResult.ANY_TYPE, null);
            var elements = null;
            var count = 0;

            var thisNode = xpathResult.iterateNext();
            while (thisNode) {
                if (thisNode != null) {
                    elements = thisNode;
                    break;
                }
                thisNode = xpathResult.iterateNext();
            }
        } catch (e) {
            var elementPathArry = elementPath.split("/");
            var selNode = xmlDoc;

            for (var i = 0; i < elementPathArry.length; i++) {
                if (elementPathArry[i] != "") {
                    selNode = SelectSingleNode(selNode, elementPathArry[i]);
                }
            }
            elements = selNode;
        }
    }
    else {
        try {
            var elements = xmlDoc.selectSingleNode(elementPath);
        } catch (e) { }
    }    
    return elements;
}

function GetElementsByTagName(node, tagName) {
    return node.getElementsByTagName(tagName);    
}

function SelectSingleNode(node, tagName) {
    var objNode = null;
    if (CrossYN()) {
        objNode = node.firstChild;
        while (objNode) {
            if (objNode.nodeType == 1 && objNode.tagName == tagName)
                break;
            else
                objNode = objNode.nextSibling;
        }
    }
    else {
        if(node != null)
        {
            if (node.selectSingleNode(tagName))
                return node.selectSingleNode(tagName);
        }
    }
    return objNode;
}













