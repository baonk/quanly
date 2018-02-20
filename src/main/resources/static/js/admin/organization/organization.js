	var level1El;
	var level2El;
	var level3El;
	var currentClickItemPos;
	var currentH2ItemPos;
	
	function initToggleList(ulEl, level1, level2, level3)
	{		
	    currentListNum = true;
	    
	    level1El = ulEl.getElementsByTagName(level1);
	    level2El = ulEl.getElementsByTagName(level2);
	    level3El = ulEl.getElementsByTagName(level3);	

	    
	    for(var i = 0 ; i < level1El.length ; i++ ) {						
			level1El.item(i).setAttribute("order", i);
			level1El.item(i).setAttribute("onclick", "toggleList(this);");
			level1El.item(i).className = "off";
			
			level2El.item(i).setAttribute("order", i);
			level2El.item(i).className = "off";			
		} 	
	    
		
		for (var j = 0; j < level3El.length; j++) {
			level3El.item(j).setAttribute("order", j);
			level3El.item(j).className = "off";	
		}
		
		level2El.item(0).className = "on";
		level1El.item(0).className = "on";
		level3El.item(0).className = "on";
		currentClickItemPos = 0;
		currentH2ItemPos = 0;
		goPage(1, level3El.item(0));
	}	
	
	function toggleList(obj) {				
		var order = obj.getAttribute("order");
		var ulElm = level2El.item(order);		
		
		if (order != currentH2ItemPos) {			
			level1El.item(currentH2ItemPos).className = "off";
			level2El.item(currentH2ItemPos).className = "off";
			currentH2ItemPos = order;
		}
		
		if (obj.className == "off") {			
			obj.className = "on";
			ulElm.className = "on";
		}
		else {
			obj.className = "off";
			ulElm.className = "off";
		} 							
	}
	
	function goPage(idx, obj) {		
		var url = "";
		if (obj.getAttribute("order") == null) {			
			obj = obj.parentElement;
		}
		
		if (obj.getAttribute("order") != currentClickItemPos) {			
			level3El.item(currentClickItemPos).className = "off";	
			currentClickItemPos = obj.getAttribute("order");			
			obj.className = "on";			
		}		
		
		switch(idx){
		    case 1:
		        url = "/admin/organ/organRight";
				break;
		    case 2:
		        url = "/admin/ezEmail/mailDistributionList.do";
				break;
			case 3:
				url = "/admin/ezEmail/mailDefaultQuota.do" ;
				break;
			case 4:
				url = "/myoffice/ezEmail/Admin/mail_spamfilter_category.aspx"  ;
				break;			
		}
		window.open(url,"rightOrg");		
		
	}