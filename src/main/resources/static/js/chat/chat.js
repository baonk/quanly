var currentGroupSticker  = -1;
var stickerIndex         = null;
var numberOfGroupSticker = 5;
var lastChattedUser      = null;

(function() {
	var days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];

	var months = ['January','February','March','April','May','June','July','August','September','October','November','December'];

	Date.prototype.getMonthName = function() {
		return months[this.getMonth()];
	};
	Date.prototype.getDayName = function() {
		return days[this.getDay()];
	};
})();

function check_key(event) {
	if (event.which == 13 && !event.shiftKey) {
		event.preventDefault();
		sendMessage();
	}
}

function sendMessage() {
	var textValue   = document.getElementById("bnkCmtTxt").value;
	var chatMessage = {
		sender       : currentUser,
		receiver     : chatUser,
		content      : textValue,
		contentType  : 'TEXT',
		fileName     : '',
		filePath     : '',
		clusterId    : messageIdx,
		receiverType : conType,
		type         : 'CHAT',
		tenantId     : tenantId
	};
	
	stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));
	
	document.getElementById("bnkCmtTxt").value = "";
	document.getElementById("bnkCmtTxt").blur();

}

function displayMessage(result, friendInf) {
	var chatTbl = document.getElementById("bnkChatTbl");
	while (chatTbl.hasChildNodes()) {
		chatTbl.removeChild(chatTbl.lastChild);
	}
	
	if (result == null || result.length == 0) {
		var divElmt   = document.createElement("div");
		var chImgElmt = document.createElement("img");
		var chDivElmt = document.createElement("div");
		var spanElmt1 = document.createElement("span");
		var spanElmt2 = document.createElement("span");
		var spanElmt3 = document.createElement("span");
		var spanElmt4 = document.createElement("span");
		
		chImgElmt.setAttribute("class", "startTalk");
		chImgElmt.src = "/images/chat/converstation.png";
		spanElmt1.setAttribute("class", "txt");
		spanElmt1.textContent = "Let's chat";
		spanElmt2.textContent = friendInf["username"];
		spanElmt3.setAttribute("class", "description");
		spanElmt3.textContent = friendInf["position"] + " at " + friendInf["companyname"];
		spanElmt4.setAttribute("class", "description");
		spanElmt4.textContent = "Last active " + friendInf["lastlogin"];
		
		chDivElmt.setAttribute("class", "letChatInfo");
		chDivElmt.appendChild(spanElmt1);
		chDivElmt.appendChild(spanElmt2);
		chDivElmt.appendChild(spanElmt3);
		chDivElmt.appendChild(spanElmt4);
		
		divElmt.setAttribute("class", "bnkNoTalk");
		divElmt.setAttribute("id", "bnkNoData");
		divElmt.appendChild(chImgElmt);
		divElmt.appendChild(chDivElmt);
		
		chatTbl.appendChild(divElmt);
	}
	else {
		for (var i = result.length - 1; i >= 0; i--) {
			var type        = result[i]["contType"];
			var sender      = result[i]["senderId"];
			
			if (sender == currentUser) {
				if (lastChattedUser == currentUser) {
					showChat2(result[i], type, "self");
				}
				else {
					showChat1(result[i], type, "self");
				}
			}
			else {
				if (lastChattedUser == chatUser) {
					showChat2(result[i], type, "other");
				}
				else {
					showChat1(result[i], type, "other");
				}
			}
		}
	}
}

/*function sendMessage() {
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
}*/

function displaySticker(obj) {
	var style     = obj.currentStyle || window.getComputedStyle(obj, false);
	var bgImage   = style.backgroundImage.slice(4, -1);
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
	//var strTime     = new Date().toTimeString().split(" ")[0];
	var date        = new Date();
	
	var strDateTime = date.toISOString();
	var strDate     = strDateTime.substring(0, 10);
	//return strDate + " " + strTime;
	return date.getDayName() + ", " + date.getMonthName() + " " + strDate;
}

//Type = 1: text
//Type = 2: sticker
//Type = 3: image
//Type = 4: file
function showChat1(jsonMessage, type, chatType) {
	var bnkChatTblElmt    = document.getElementById("bnkChatTbl");
	var mainOlElmt        = null;
	var mainLiElmt        = document.createElement("li");
	mainLiElmt.className  = chatType;
	
	//process user's avatar
	var avatarDivElmt = document.createElement("div");
	var avatarImgElmt = document.createElement("img");
	avatarDivElmt.className = "avatar";
	avatarImgElmt.setAttribute('draggable', false);
	avatarImgElmt.src       = jsonMessage["userImage"]; //tam thoi
	avatarDivElmt.appendChild(avatarImgElmt);
	
	//process messageContent
	var contentDivElmt = document.createElement("div");
	var timeElmt       = document.createElement("span");
	
	if (type == 1) {
		var paragraphElmt         = document.createElement("p");
		paragraphElmt.textContent = jsonMessage["content"];
		contentDivElmt.appendChild(paragraphElmt);
	}
	else {
		var imgElmt = document.createElement("img");
		imgElmt.src = jsonMessage["content"];
		if (type == 2) {
			imgElmt.className = "bnkSticker";
		}
		else {
		
		}
		contentDivElmt.appendChild(imgElmt);
	}
	
	contentDivElmt.className  = "msg";
	timeElmt.textContent      = jsonMessage["createdTime"];
	contentDivElmt.appendChild(timeElmt);
	
	//Add avatar and message content to main div
	mainLiElmt.appendChild(avatarDivElmt);
	mainLiElmt.appendChild(contentDivElmt);
	
	if (document.getElementById("bnkStartCon") != null) {
		mainOlElmt = document.getElementById("bnkStartCon");
		mainOlElmt.appendChild(mainLiElmt);	
	}
	else {
		//bnkChatTblElmt.removeChild(bnkChatTblElmt.firstElementChild);
		mainOlElmt = document.createElement("ol");
		mainOlElmt.className  = "chat";
		mainOlElmt.setAttribute("id", "bnkStartCon");
		mainOlElmt.appendChild(mainLiElmt);
		bnkChatTblElmt.appendChild(mainOlElmt);
	}

	lastChattedUser = jsonMessage["senderId"];
}

//Type = 1: text
//Type = 2: sticker
//Type = 3: image
//Type = 4: file
function showChat2(jsonMessage, type, chatType) {
	var mainOlElmt     = document.getElementById("bnkStartCon");
	var currentDivElmt = mainOlElmt.lastElementChild.lastElementChild;
	var spanTimeElmt   = currentDivElmt.lastElementChild;
	var timeContent    = spanTimeElmt.textContent;
	var newMessTime    = jsonMessage["createdTime"];
	var checkResult    = compareTime(newMessTime, timeContent);
	
	console.log("NewMessageTime: " + newMessTime + " || OldMessageTime: " + timeContent + " || CheckResult: " + checkResult);
	
	switch(checkResult) {
		case 1:
			var divTimeElmt = document.createElement("div");
			divTimeElmt.setAttribute("class", "day");
			divTimeElmt.textContent(getChatTime());
			mainOlElmt.appendChild(divTimeElmt);
			showChat1(jsonMessage, type, chatType);
			break;
		case 2:
			var newChatType = chatType + "_cont";
			showChatContinue(jsonMessage, type, newChatType);
			break;
		case 3:
			currentDivElmt.removeChild(spanTimeElmt);
			currentDivElmt.lastElementChild.setAttribute("title", timeContent);
			
			//process messageContent
			if (type == 1) {
				var paragraphElmt = document.createElement("p");
				paragraphElmt.textContent = jsonMessage["content"];
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

			var timeElmt         = document.createElement("span");
			timeElmt.textContent = newMessTime;
			
			//Add currentTime to current div
			currentDivElmt.appendChild(timeElmt);
			break;
	}
	
	lastChattedUser = jsonMessage["senderId"];
}

function compareTime(newTime, oldTime) {
	var newDate = newTime.substring(0, 10);
	var oldDate = oldTime.substring(0, 10);
	
	if (oldDate != newDate) {
		return 1;
	}
	else {
		var newHour = newTime.substring(11, 13);
		var oldHour = oldTime.substring(11, 13);
		
		if (newHour != oldHour) {
			return 2;
		}
		else {
			var newMinute = parseInt(newTime.substring(14, 16));
			var oldMinute = parseInt(oldTime.substring(14, 16));
			
			console.log("newMinute: " + newMinute + " || oldMinute: " + oldMinute);
			
			if ((newMinute - oldMinute) <= 2) {
				return 3;
			}
			else {
				return 2;
			}
		}
	}
}

function showChatContinue(jsonMessage, type, chatType) {
	var bnkChatTblElmt    = document.getElementById("bnkChatTbl");
	var mainOlElmt        = null;
	var mainLiElmt        = document.createElement("li");
	mainLiElmt.className  = chatType;
	
	//process messageContent
	var contentDivElmt = document.createElement("div");
	var timeElmt       = document.createElement("span");
	
	if (type == 1) {
		var paragraphElmt         = document.createElement("p");
		paragraphElmt.textContent = jsonMessage["content"];
		contentDivElmt.appendChild(paragraphElmt);
	}
	else {
		var imgElmt = document.createElement("img");
		imgElmt.src = jsonMessage["content"];
		if (type == 2) {
			imgElmt.className = "bnkSticker";
		}
		else {
		
		}
		contentDivElmt.appendChild(imgElmt);
	}
	
	contentDivElmt.className  = "msg";
	timeElmt.textContent      = jsonMessage["createdTime"];
	contentDivElmt.appendChild(timeElmt);
	
	//Add message content to main div
	mainLiElmt.appendChild(contentDivElmt);
	mainOlElmt = document.getElementById("bnkStartCon");
	mainOlElmt.appendChild(mainLiElmt);
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
		document.getElementById("_listG1").style.display          = "block";
		
		for (var i = 2; i <= numberOfGroupSticker; i++) {
			document.getElementById("_group" + i).style.backgroundColor = "#fff";
			document.getElementById("_listG" + i).style.display         = "none";
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
	else if (imageTag.src.indexOf("student.png") !== -1) {
		stickerIndex = 4;
	}
	else {
		stickerIndex = 5;
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
	document.getElementById("_group" + currentGroupSticker).style.display       = "none";
	document.getElementById("_group" + (currentGroupSticker - 8)).style.display = "block";
	currentGroupSticker = currentGroupSticker - 1;
	document.getElementById("nextEmoticon").src          = "/images/next.png";
	document.getElementById("nextEmoticon").style.cursor = "pointer";
	document.getElementById("nextEmoticon").onclick      = function () { showNextGroupSticker(); };
	
	if (currentGroupSticker == 8) {
		document.getElementById("previousEmoticon").src          = "/images/previous1.png";
		document.getElementById("previousEmoticon").onclick      = null;
		document.getElementById("previousEmoticon").style.cursor = "default";
	}
}