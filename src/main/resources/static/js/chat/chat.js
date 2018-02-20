    var currentGroupSticker 	= -1;
    var stickerIndex 			= null;
    var numberOfGroupSticker 	= 4;
    var lastChattedUser			= null;

    function check_key(event) {
    	if (event.which == 13 && !event.shiftKey) {
    		event.preventDefault();			
			sendMessage();
    	}
    }
    
    function sendMessage() {
    	var textValue = document.getElementById("bnkCmtTxt").value;
		document.getElementById("bnkCmtTxt").value = "";
		document.getElementById("bnkCmtTxt").blur();
    	
    	if (textValue == '') {
    		return;
    	}
    	    	
    	if (lastChattedUser == null || lastChattedUser != currentUser) {  		
    		showSelfChat1(textValue, 1);    		
    	}
    	else {
    		if (lastChattedUser == currentUser) {	    			
    			showSelfChat2(textValue, 1);
    		}
    		else { 			
    			showSelfChat1(textValue, 1);
    		}
    	}
    }
    
    function displaySticker(obj) {
    	var style = obj.currentStyle || window.getComputedStyle(obj, false);
    	var bgImage = style.backgroundImage.slice(4, -1);
    	var actualUrl = "";
    	
    	if (bgImage.slice(-1) === '"') {		    		
    		actualUrl = bgImage.slice(bgImage.indexOf("/images/"), -1);
    	}
    	else {		    		
    		actualUrl = bgImage.slice(bgImage.indexOf("/images/"));
    	}
    	
    	//Close sticker picker
    	document.getElementById("emoticonPanel").style.display = "none";
    	document.getElementById("bnkEmoticon").style.backgroundImage = "url('/images/chat/emo3.png')";
    	
    	if (lastChattedUser == null || lastChattedUser != currentUser) {  		
    		showSelfChat1(actualUrl, 2);    		
    	}
    	else {
    		if (lastChattedUser == currentUser) {	    			
    			showSelfChat2(actualUrl, 2);
    		}
    		else { 			
    			showSelfChat1(actualUrl, 2);
    		}
    	}
    	    	
    }
    
    function getChatTime() {		    	
    	var strTime = new Date().toTimeString().split(" ")[0];
    	var strDateTime = new Date().toISOString();
    	var strDate = strDateTime.substring(0, 10);
    	return strDate + " " + strTime;
    }
    
    //Type = 1: text
    //Type = 2: sticker
    //Type = 3: image
    function showSelfChat1(textValue, type) {
    	var bnkChatTblElmt    = document.getElementById("bnkChatTbl");  	
		var mainOlElmt        = null;		
		var mainLiElmt        = document.createElement("li");
		mainLiElmt.className  = "self";		
		
		//process user's avatar
		var avatarDivElmt = document.createElement("div");
		var avatarImgElmt = document.createElement("img");
		avatarDivElmt.className = "avatar";
		avatarImgElmt.setAttribute('draggable', false);
		avatarImgElmt.src 		= "https://vi.gravatar.com/userimage/119146805/dcb3ad95a00ec4a4284c36d7c401a156.png"; //tam thoi
		avatarDivElmt.appendChild(avatarImgElmt);
		
		//process messageContent
		var contentDivElmt 		  = document.createElement("div");		
		var timeElmt 			  = document.createElement("span");
		
		if (type == 1) {
			var paragraphElmt = document.createElement("p");
			paragraphElmt.textContent = textValue;
			contentDivElmt.appendChild(paragraphElmt);
		}
		else {
			var imgElmt = document.createElement("img");
			imgElmt.src = textValue;
			if (type == 2) {
				imgElmt.className = "bnkSticker";				
			}
			else {
								
			}
			contentDivElmt.appendChild(imgElmt);
		}		
		
		contentDivElmt.className  = "msg";		
		timeElmt.textContent      = getChatTime();		
		contentDivElmt.appendChild(timeElmt);
		
		//Add avatar and message content to main div
		mainLiElmt.appendChild(avatarDivElmt);
		mainLiElmt.appendChild(contentDivElmt);
		
    	if (document.getElementById("bnkStartCon") != null) {
    		mainOlElmt = document.getElementById("bnkStartCon");
    		mainOlElmt.appendChild(mainLiElmt);	
    	}
    	else {
    		bnkChatTblElmt.removeChild(bnkChatTblElmt.firstElementChild);
    		mainOlElmt = document.createElement("ol");
    		mainOlElmt.className  = "chat";
    		mainOlElmt.setAttribute("id", "bnkStartCon");
    		mainOlElmt.appendChild(mainLiElmt);		
    		bnkChatTblElmt.appendChild(mainOlElmt);
    	}		

		lastChattedUser = currentUser;
    }
    
    //Type = 1: text
    //Type = 2: sticker
    //Type = 3: image
    function showSelfChat2(textValue, type) {
    	var mainOlElmt     = document.getElementById("bnkStartCon");
    	var currentDivElmt = mainOlElmt.lastElementChild.lastElementChild;
    	var spanTimeElmt   = currentDivElmt.lastElementChild;
    	var timeContent    = spanTimeElmt.textContent;
    	currentDivElmt.removeChild(spanTimeElmt);
    	currentDivElmt.lastElementChild.setAttribute("title", timeContent);
    	
		//process messageContent		
    	if (type == 1) {
			var paragraphElmt = document.createElement("p");
			paragraphElmt.textContent = textValue;
			currentDivElmt.appendChild(paragraphElmt);
		}
		else {
			var imgElmt = document.createElement("img");
			imgElmt.src = textValue;
			if (type == 2) {
				imgElmt.className = "bnkSticker";				
			}
			else {
								
			}
			currentDivElmt.appendChild(imgElmt);
		}		

		var timeElmt 			  = document.createElement("span");		
		timeElmt.textContent      = getChatTime();
		
		//Add currentTime to current div	
		currentDivElmt.appendChild(timeElmt);
    }
    
    function showOtherChat1() {
    	
    }
    
    function showOtherChat2() {
    	
    }
    
	function checkScrollBars() {		
		if (document.getElementById("_listG" + stickerIndex + "Table").scrollHeight > 320) {
    		document.getElementById("emoticonPanel").style.width = "420px";    		
    	}
    }
    
    function addSticker() {
    	if (document.getElementById("emoticonPanel").style.display == "block") {
    		document.getElementById("emoticonPanel").style.display = "none";
    		document.getElementById("bnkEmoticon").style.backgroundImage = "url('/images/chat/emo3.png')";
    	}
    	else {
    		//baonk added
	    	document.addEventListener("keydown", function handleKeyDown(evt) {	
	    		evt = evt || window.event;
	    		
	    	    if (evt.keyCode == 27) {
		    		document.getElementById("emoticonPanel").style.display = "none";
		    		document.getElementById("bnkEmoticon").style.backgroundImage = "url('/images/chat/emo3.png')";
	    	    }
	    	    
		    	document.removeEventListener("keydown", handleKeyDown);				    	
	    	});	 
    		//end
    		
    		document.getElementById("bnkEmoticon").style.backgroundImage = "url('/images/chat/emo4.png')";
	    	processGroupStickers();
	    	stickerIndex = 1;		    	
	    	document.getElementById("_group1").style.backgroundColor  = "#d9d9d9";
	    	document.getElementById("_listG1").style.display = "block";
	    	
	    	for (var i = 2; i <= numberOfGroupSticker; i++) {
	    		document.getElementById("_group" + i).style.backgroundColor  = "#fff";
	    		document.getElementById("_listG" + i).style.display = "none";
	    	}		    		    	
	    	
	    	document.getElementById("emoticonPanel").style.display = "block";
	    	checkScrollBars();
    	}
    }
    
    function changeStickerGroup(obj) {		    	
    	document.getElementById("_group" + stickerIndex).style.backgroundColor  = "#fff";
    	obj.style.backgroundColor  = "#d9d9d9";
    	document.getElementById("_listG" + stickerIndex).style.display = "none";
    	var imageTag = obj.firstElementChild;
    	
    	if (imageTag.src.indexOf("hackerGirl.png") !== -1) {		    		
    		stickerIndex = 1;		    		
    	}
    	else if (imageTag.src.indexOf("crayonShin.png") !== -1) {
    		stickerIndex = 2;
    	}
    	else if (imageTag.src.indexOf("catEmoticon.png") !== -1) {
    		stickerIndex = 3;
    	}
    	else {
    		stickerIndex = 4;
    	}
    	document.getElementById("_listG" + stickerIndex).style.display = "block";
    	checkScrollBars();
    }
    
    function processGroupStickers() {				
    	if (numberOfGroupSticker > 8) {
    		currentGroupSticker = 8;
    		
    		for (var i = 9; i <= numberOfGroupSticker; i++) {
    			document.getElementById("_group" + i).style.display = "none";
    		}
    		
    		document.getElementById("nextEmoticon").src = "/images/next.png";
    		document.getElementById("nextEmoticon").style.cursor = "pointer";
    		document.getElementById("nextEmoticon").onclick = function () { showNextGroupSticker(); };
    	}
    	else {
    		for (var i = numberOfGroupSticker + 1; i <= 8; i++) {
    			document.getElementById("_group" + i).style.display = "none";
    		}		    		
    	}
    }
    
    function showNextGroupSticker() {
    	currentGroupSticker = currentGroupSticker + 1;
    	document.getElementById("_group" + (currentGroupSticker - 8)).style.display = "none";
    	document.getElementById("_group" + currentGroupSticker).style.display = "block";
    	document.getElementById("previousEmoticon").src = "/images/previous.png";
    	document.getElementById("previousEmoticon").style.cursor = "pointer";
    	document.getElementById("previousEmoticon").onclick = function() { showPreviousGroupSticker(); };
    	
    	if (currentGroupSticker >= numberOfGroupSticker) {
    		document.getElementById("nextEmoticon").src = "/images/next1.png";
    		document.getElementById("nextEmoticon").onclick = null;
    		document.getElementById("nextEmoticon").style.cursor = "default";
    	}
    }
    
    function showPreviousGroupSticker() {
    	document.getElementById("_group" + currentGroupSticker).style.display = "none";
    	document.getElementById("_group" + (currentGroupSticker - 8)).style.display = "block";
    	currentGroupSticker = currentGroupSticker - 1;			    	
    	document.getElementById("nextEmoticon").src = "/images/next.png";
		document.getElementById("nextEmoticon").style.cursor = "pointer";
		document.getElementById("nextEmoticon").onclick = function () { showNextGroupSticker(); };
		
    	if (currentGroupSticker == 8) {
    		document.getElementById("previousEmoticon").src = "/images/previous1.png";
    		document.getElementById("previousEmoticon").onclick = null;
    		document.getElementById("previousEmoticon").style.cursor = "default";
    	}
    }
    