function preProcess() {
			var fTopicListTopElmt = document.getElementById("favouriteListTop");
			fTopicListTopElmt.onclick = function () {fTopicListToggle();};
			
			var topicDivElmt = document.getElementById("bnkTopicCreate");
			topicDivElmt.onclick = function () {createNewTopic();};
			
			var listPageNav = document.getElementsByClassName("bnkGotoDiv");
			
			if (listPageNav != null && listPageNav.length > 0) {
				for (var i = 0; i < listPageNav.length; i++) {
					listPageNav[i].onclick = function (event) {displayGotoPanel(event, this);};
				}
			}	
			
			var gotoPanel0Emlt = document.getElementById("gotoPanel0");
			gotoPanel0Emlt.onclick = function (e) {e.stopPropagation();};
			
			var gotoPanel1Emlt = document.getElementById("gotoPanel1");
			gotoPanel1Emlt.onclick = function (e) {e.stopPropagation();};
			
		}
		
		function fTopicListToggle() {
			$("#bnkfTopicMain").toggle("1000");
		}
		
		function createNewTopic() {			
			divPopUpShow(717, 750, "/createTopic");
		}
		
		function displayGotoPanel(evt, obj) {
			evt.preventDefault();
		    evt.stopPropagation();
   
		    var index = obj.getAttribute("index");
		    var otherIdx = null;
		    
		    if (index == "0") {
		    	otherIdx = "1";
		    }
		    else {
		    	otherIdx = "0";
		    }
		    		    
		    var gotoPanelEmlt = document.getElementById("gotoPanel" + otherIdx);	
			if (gotoPanelEmlt != null && gotoPanelEmlt.style.display != "none") {
				var otherGotoElmt = document.getElementById("gotoInput" + otherIdx);
				otherGotoElmt.value = "";
				gotoPanelEmlt.style.display = "none";
			}
			
			var inputGotoElmt = document.getElementById("gotoInput" + index);
			inputGotoElmt.value = "";
		    
			$("#gotoPanel" + index).toggle("1000");
			
			var panelElmt = document.getElementById("gotoPanel" + index);
			
			
	    	document.addEventListener("click", function handleClick(e) {
	    		if (panelElmt != null && panelElmt.style.display != "none") {		    			    		
		    		$("#gotoPanel" + index).fadeOut("1000");
		    	}
		    	document.removeEventListener("click", handleClick);		    	
	    	});	 
		}