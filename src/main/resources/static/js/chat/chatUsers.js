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
			var result = data.userList;
			totalPages = data.totalPages;
			totalRows  = data.totalRows;
			displayUserList(result);
			makePageSelPage();
		},
		error : function(error) {
			alert("<spring:message code='chat.t1' />" + error);
		}
	});
}

function displayUserList(result) {
	var userList = document.getElementById("listChatUsers");
	while (userList.hasChildNodes()) {
		userList.removeChild(userList.lastChild);
	}
	
	if (result == null || result.length == 0) {
		var divLine  = document.createElement("div");
		divLine.textContent = "<spring:message code='chat.t2' />";
		userList.appendChild(divLine);
	}
	else {
		for (var i = 0; i < result.length; i++) {
			var divLine  = document.createElement("div");
			var imgElmt1 = document.createElement("img");
			var divElmt  = document.createElement("div");
			var imgElmt2 = document.createElement("img");
			
			imgElmt1.setAttribute("class", "avatarImage");
			imgElmt1.src = result[i]["image"];
			
			divElmt.setAttribute("class", "chatUserName");
			divElmt.textContent = result[i]["userName"];
			
			imgElmt2.setAttribute("class", "statusImg");
			imgElmt2.src = (result[i]["active"] == 1) ? "/images/chat/online.png" : "/images/chat/offline.png";
			
			divLine.setAttribute("class", "userChat");
			divLine.setAttribute("userId", result[i]["userId"]);
			divLine.setAttribute("userName", result[i]["userName"]);
			divLine.onclick = function() {displayConversation(this);};
			
			divLine.appendChild(imgElmt1);
			divLine.appendChild(divElmt);
			divLine.appendChild(imgElmt2);
			userList.appendChild(divLine);
			
			if (i == 0) {
				displayConversation(divLine);
			}
		}
	}
}

function displayConversation(obj) {
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
			var result     = data.messageList;
			var friendInfo = data.friendInfo;
			console.log(result);
			console.log(friendInfo);
			displayMessage(result, friendInfo);
		},
		error : function(error) {
			alert("<spring:message code='chat.t1' />" + error);
		}
	});
}





























