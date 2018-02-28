function search_Set(currentIdx) {
	$.ajax({
		type: "POST",
		url: "/chat/getUserList.do",
		data: {
			currentIndex : currentIdx
		},
		dataType: "JSON",
		async: true,
		success : function(data) {
			var result1 = data.userList;
			var result2 = data.groupList;
			totalGroups = data.totalGroups;
			totalUsers  = data.totalUsers;
			
			console.log(result1);
			console.log(result2);
			
			processResults(result1, result2);
			//makePageSelPage();
		},
		error : function(error) {
			alert("<spring:message code='chat.t1' />" + error);
		}
	});
}

function processResults(result1, result2) {
	displayUserList(result1);
	displayGroupList(result2);
	document.getElementById("chatTargetList").style.display = "";
}

function displayUserList(result) {
	var userList = document.getElementById("listChatUsers");
	while (userList.hasChildNodes()) {
		userList.removeChild(userList.lastChild);
	}
	
	if (result == null || result.length == 0) {
		var divLine  = document.createElement("div");
		divLine.textContent = "<spring:message code='chat.t2'/>";
		userList.appendChild(divLine);
	}
	else {
		for (var i = 0; i < result.length; i++) {
			var divLine  = document.createElement("div");
			var imgElmt1 = document.createElement("img");
			var divElmt  = document.createElement("div");
			var imgElmt2 = document.createElement("img");
			var divCnt   = document.createElement("div");
			
			imgElmt1.setAttribute("class", "avatarImage");
			imgElmt1.src = result[i]["image"];
			
			divElmt.setAttribute("class", "chatUserName");
			divElmt.textContent = result[i]["userName"];
			
			imgElmt2.setAttribute("class", "statusImg");
			imgElmt2.src = (result[i]["active"] == 1) ? "/images/chat/online.png" : "/images/chat/offline.png";
			
			divCnt.setAttribute("class", "count");
			
			if (result[i]["unreadCnt"] == 0) {
				divCnt.style.display = "none";
			}
			else {
				divCnt.textContent = processUnreadCnt(result[i]["unreadCnt"]);
			}
			
			divLine.setAttribute("class", "userChat");
			divLine.setAttribute("userId", result[i]["userId"]);
			divLine.setAttribute("userName", result[i]["userName"]);
			divLine.onclick = function() {displayConversation(this);};
			
			divLine.appendChild(imgElmt1);
			divLine.appendChild(divCnt);
			divLine.appendChild(divElmt);
			divLine.appendChild(imgElmt2);
			userList.appendChild(divLine);
			
			if (i == 0) {
				divCnt.textContent   = "0";
				divCnt.style.display = "none";
				displayConversation(divLine);
			}
		}
	}
}

function displayGroupList(result) {
	var groupList = document.getElementById("listChatGroups");
	while (groupList.hasChildNodes()) {
		groupList.removeChild(userList.lastChild);
	}
	
	if (result == null || result.length == 0) {
		var divLine  = document.createElement("div");
		divLine.textContent = "<spring:message code='chat.t5'/>";
		groupList.appendChild(divLine);
	}
	else {
		for (var i = 0; i < result.length; i++) {
			var divLine  = document.createElement("div");
			var imgElmt1 = document.createElement("img");
			var divElmt  = document.createElement("div");
			var divCnt   = document.createElement("div");
			//var imgElmt2 = document.createElement("img");
			
			imgElmt1.setAttribute("class", "avatarImage");
			imgElmt1.src = result[i]["image"];
			
			divElmt.setAttribute("class", "chatUserName");
			divElmt.textContent = result[i]["userName"];
			
			//imgElmt2.setAttribute("class", "statusImg");
			//imgElmt2.src = (result[i]["active"] == 1) ? "/images/chat/online.png" : "/images/chat/offline.png";
			
			divCnt.setAttribute("class", "count");
			
			if (result[i]["unreadCnt"] == 0) {
				divCnt.style.display = "none";
			}
			else {
				divCnt.textContent = processUnreadCnt(result[i]["unreadCnt"]);
			}
			
			divLine.setAttribute("class", "userChat");
			divLine.setAttribute("userId", result[i]["userId"]);
			divLine.setAttribute("userName", result[i]["userName"]);
			divLine.onclick = function() {displayConversation(this);};
			
			divLine.appendChild(imgElmt1);
			divLine.appendChild(divCnt);
			divLine.appendChild(divElmt);
			//divLine.appendChild(imgElmt2);
			groupList.appendChild(divLine);
		}
	}
}

function processUnreadCnt(cnt) {
	var intCnt = parseInt(cnt);
	
	if (intCnt > 50) {
		return "+50";
	}
	else if (intCnt <= 10) {
		return  "+" + cnt;
	}
	
	var nguyen = (intCnt / 10) * 10;
	
	return "+" + nguyen;
}

function displayConversation(obj) {
	//Clear unread message
	clearUnreadMessage(obj);
	
	var userId   = obj.getAttribute("userId");
	var userName = obj.getAttribute("userName");
	chatUser     = userId;
	conType      = 'SINGLE';
	
	document.getElementById("chatHeader").textContent = userName;
	
	$.ajax({
		type: "POST",
		url: "/chat/getMessages.do",
		data: {
			friend : userId,
			type   : conType,
			page   : messageIdx
		},
		dataType: "JSON",
		async: true,
		success : function(data) {
			var result      = data.messageList;
			var friendInfo  = data.friendInfo;
			lastChattedUser = null;
			displayMessage(result, friendInfo);
		},
		error : function(error) {
			alert("<spring:message code='chat.t1' />" + error);
		}
	});
}

function clearUnreadMessage(obj) {
	var divCnt = obj.getElementsByClassName("count")[0];
	divCnt.textContent   = "0";
	divCnt.style.display = "none";
}



























