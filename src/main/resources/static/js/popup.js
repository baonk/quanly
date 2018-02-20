	function GetOpenWindowfeature(popUpW, popUpH) {
		var heigth = window.screen.availHeight;
		var width = window.screen.availWidth;
		var left = 0;
		var top = 0;
		var pleftpos;
		pleftpos = parseInt(width) - popUpW;
		heigth = parseInt(heigth) - popUpH;
		width = parseInt(width) - pleftpos;
		left = pleftpos / 2;
		top = heigth / 2;
		var feature = "height = " + popUpH + "px, width = " + popUpW + "px,left=" + left + ",top=" + top + ", status=no, toolbar=no, menubar=no,location=no, resizable=no, scrollbars=yes";
		return feature;
	}
	
	function divPopUpShow(popUpW, popUpH, URL) {
	    try {
	        var position = divPopUpPosition(popUpW, popUpH);
	        document.getElementById("iFrameLayer").src = URL;
	        document.getElementById("iFramePanel").style.top = position[0] + "px";
	        document.getElementById("iFramePanel").style.left = position[1] + "px";
	        document.getElementById("iFramePanel").style.height = popUpH + "px";
	        document.getElementById("iFrameLayer").style.width = popUpW + "px";
	        document.getElementById("iFrameLayer").style.height = popUpH + "px";
	        document.getElementById("fogPanel").style.display = "";	        
	        document.getElementById("iFramePanel").style.display = "";
	    } catch (e) {}
	}
	
	function divPopUpHidden() {
	    try {
	        document.getElementById("fogPanel").style.display = "none";
	        document.getElementById("iFramePanel").style.display = "none";
	        document.getElementById("iFrameLayer").src = "/blank.htm";
	    } catch (e) {}
	}
	
	function divPopUpPosition(popUpW, popUpH) {
	    var returnValue = new Array();
	    var heigth = document.documentElement.clientHeight;
	    if (heigth == 0)
	        heigth = document.body.clientHeight;

	    var width = document.documentElement.clientWidth;
	    if (width == 0)
	        width = document.body.clientWidth;

	    var left = 0;
	    var top = 0;
	    var pleftpos;
	    pleftpos = parseInt(width) - popUpW;
	    heigth = parseInt(heigth) - popUpH;
	    width = parseInt(width) - pleftpos;
	    if (heigth < (popUpH + 50))
	    	returnValue[0] = (heigth / 2);
	    else
	    	returnValue[0] = (heigth / 2) - 50;
	    returnValue[1] = pleftpos / 2;
	    return returnValue;
	}
	
	