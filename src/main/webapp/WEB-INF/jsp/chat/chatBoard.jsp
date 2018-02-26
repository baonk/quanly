<%@ page language ="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri    ="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix ="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
	<link rel="stylesheet" type="text/css" href="/css/home.css" />
	<link rel="stylesheet" type="text/css" href="/css/chat/chat.css" />
	<link rel="stylesheet" href="/css/bootstrap.min.css">
	<script src="/js/jquery/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/chat/chat.js"></script>
	<script src="/webjars/sockjs-client/sockjs.min.js"></script>
	<script src="/webjars/stomp-websocket/stomp.min.js"></script>
	<script type="text/javascript">
		var currentUser = "<c:out value="${userId}"/>";
		var tenantId    = "<c:out value="${tenantId}"/>";
		var blockSize   = 10;
		var currentPage = null;
		var totalRows   = null;
		var totalPages  = null;
		var strLang39   = "<spring:message code = 'chat.t3'/>";
		var strLang40   = "<spring:message code = 'chat.t4'/>";
		var stompClient = null;
		var chatUser    = null;
		var cluster     = null;
		var messageIdx  = 1;
		var conType     = null;

		window.onload = function () {
			connect();
			search_Set("1");
			
			var stickerElmt      = document.getElementById("bnkEmoticon");
			stickerElmt.onclick  = function() {addSticker()};
			
			var chatAreaElmt     = document.getElementById("bnkCmtTxt");
			chatAreaElmt.onfocus = function() {checkSticker()};
		}

		function checkSticker() {
			if (document.getElementById("emoticonPanel").style.display != "none") {
				document.getElementById("emoticonPanel").style.display       = "none";
				document.getElementById("bnkEmoticon").style.backgroundImage = "url('/images/chat/emo3.png')";
			}
		}

		function connect() {
			var socket = new SockJS("/wsbaonk?token=" + currentUser);
			stompClient = Stomp.over(socket);
			stompClient.connect({}, function(frame) {
				stompClient.subscribe('/user/queue/reply', displayReceivedMess);
			});
		}

		function displayReceivedMess(payload) {
			var message = JSON.parse(payload.body);
			
			console.log("Payload: " + payload);
			
			var chatTbl = document.getElementById("bnkChatTbl");
			var type    = message["contType"];
			var sender  = message["senderId"];
			
			console.log("Sender: " + sender + " || CurrentUser: " + currentUser + " || LastlastChattedUser: " + lastChattedUser + " || ChatUser: " + chatUser);
			
			if (sender == currentUser) {
				if (lastChattedUser == currentUser) {
					showChat2(message, type);
				}
				else {
					showChat1(message, type, "self");
				}
			}
			else {
				if (lastChattedUser == chatUser) {
					showChat2(message, type);
				}
				else {
					showChat1(message, type, "other");
				}
			}
		}
	</script>
	
</head>

<body>
	<div class="container">
		<div class="bnkpanel-group">
			<div class="bnkpanel bnkpanel-primary">
				<div class="bnkContainer">
					<div class="bnk bnkLeft">
						<div class="leftHeaderMenu">
							<div class="settingImg"><img src="/images/chat/sss.png" class ="chatImage2" /></div>
							<div class="txtMessage" id="totalCnt"></div>
							<div class="editImg"><img src="/images/chat/group2.png" class ="chatImage2" /></div>
						</div>
						<div class="bnkContent">
							<div class="bnkSearch">
								<img src="/images/chat/search.png" style="height: 20px; width: 20px; margin: 2px;"/>
								<input class="bnkbttnSearch" placeholder="Search chat history" maxlength="50" autocomplete="off"/>
							</div>
							<div class="bnkHistory">
								<div class="bnkChatUsers" id="listChatUsers"></div>
							</div>
						</div>
						<div id="tblPageRayer"></div>
					</div>
					<div class="bnk bnkCenter" style="margin-left: 0px; margin-right: 0px;">
						<div class="centerHeaderMenu" id="chatHeader"></div>
						<div class="bnkChatContent">
							<div class="bnkChatMessages" id="bnkChatTbl">
							</div>
							<!-- <ol class="chat">
				    			<li class="self">
							      <div class="avatar"><img src="https://i.imgur.com/DY6gND0.png" draggable="false"/></div>
							      <div class="msg">
							        <p>Puff...</p>
							        <p>I'm still doing the Góngora comment... <emoji class="books"></emoji></p>
							        <p>Better other day</p>
							        <span>20:18</span>
							        <img class="bnkSticker" src="/images/emoticon/girl/135.png"/>
							        <p>Better other day</p>
							      </div>
							    </li>
				    		</ol> -->
						
							<!-- <ol class="chat">
							    <li class="other">
							      <div class="avatar"><img src="https://i.imgur.com/DY6gND0.png" draggable="false"/></div>
							      <div class="msg">
							          <div class="user">Marga<span class="range admin">Admin</span></div>
							        <p>Dude</p>
							        <p>Want to go dinner? <emoji class="pizza"></emoji></p>
							        <time>20:17</time>
							      </div>
							    </li>
							    <li class="self">
							      <div class="avatar"><img src="https://i.imgur.com/DY6gND0.png" draggable="false"/></div>
							      <div class="msg">
							        <p>Puff...</p>
							        <p>I'm still doing the Góngora comment... <emoji class="books"></emoji></p>
							        <p>Better other day</p>
							        <time>20:18</time>
							      </div>
							    </li>
							    <li class="other">
							      <div class="msg">
							          <div class="user">Brotons</div>
							        <p>What comment about Góngora? <emoji class="suffocated"></emoji></p>
							        <time>20:18</time>
							      </div>
							    </li>
							    <li class="self">
							      <div class="msg">
							        <p>The comment sent Marialu</p>
							        <p>It's for tomorrow</p>
							        <time>20:18</time>
							      </div>
							    </li>
							    <li class="other">
							      <div class="msg">
							          <div class="user">Brotons</div>
							        <p><emoji class="scream"></emoji></p>
							        <p>Hand it to me! <emoji class="please"></emoji></p>
							        <time>20:18</time>
							      </div>
							    </li>
							    <li class="self">
							      <div class="msg">
							        <img src="https://i.imgur.com/kUPxcsI.jpg" draggable="false"/>
							        <time>20:19</time>
							      </div>
							    </li>
							    <li class="other">
							      <div class="msg">
							          <div class="user">Brotons</div>
							        <p>Thank you! <emoji class="hearth_blue"></emoji></p>
							        <time>20:20</time>
							      </div>
							    </li>
							        <div class="day">Today</div>
							    <li class="self">
							      <div class="msg">
							        <p>Who wants to play Minecraft?</p>
							        <time>18:03</time>
							      </div>
							    </li>
							    <li class="other">
							      <div class="msg">
							          <div class="user">Charo</div>
							        <p>Come on, I didn't play it for four months</p>
							        <time>18:07</time>
							      </div>
							    </li>
							    <li class="self">
							      <div class="msg">
							        <p>Ehh, the launcher crash... <emoji class="cryalot"></emoji></p>
							        <time>18:08</time>
							      </div>
							    </li>
							    <li class="other">
							      <div class="msg">
							          <div class="user">Charo</div>
							        <p><emoji class="lmao"></emoji></p>
							        <p>Sure that is the base code</p>
							        <p>I told it to Mojang</p>
							        <time>18:08</time>
							      </div>
							    </li>
							    <li class="self">
							      <div class="msg">
							        <p>It's a joke</p>
							        <p>Moai attack!</p>
							        <p><emoji class="moai"></emoji><emoji class="moai"></emoji><emoji class="moai"></emoji><emoji class="moai"></emoji><emoji class="moai"></emoji><emoji class="moai"></emoji></p>
							        <time>18:10</time>
							      </div>
							    </li>
							    <li class="other">
							      <div class="msg">
							          <div class="user">Charo</div>
							        <p>XD</p>
							        <p><emoji class="funny"></emoji></p>
							        <p>Heart for this awesome design!</p>
							        <time>18:08</time>
							      </div>
							    </li>
							        <p class="notification">David joined the group <time>18:09</time></p>
							    <li class="self">
							      <div class="msg">
							        <p>Heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeellooooooooooooooooooooooooooooooo David <emoji class="smile"/></p>
							        <time>18:09</time>
							      </div>
							    </li>
							    <li class="other">
							      <div class="msg">
							          <div class="user">David</div>
							          <p>What is that <emoji class="shit"></emoji> ?</p>
							        <time>18:10</time>
							      </div>
							    </li>
							        <p class="notification">David left the group <time>18:11</time></p>
							    <li class="other">
							      <div class="msg">
							          <div class="user">Brotons</div>
							        <p>Lol?</p>
							        <time>18:12</time>
							      </div>
							    </li>
							    <li class="other">
							      <div class="msg">
							          <div class="user">Marga<span class="range admin">Admin</span></div>
							        <p>I'm boring...</p>
							        <p>Who wants to do some logarithms? <emoji class="smile"></emoji></p>
							        <time>18:15</time>
							      </div>
							    </li>
							</ol> -->
							
							
<%-- 							<c:choose>
								<c:otherwise>
									<div class="bnkNoTalk" id="bnkNoData">
										<img src="/images/chat/converstation.png" class="startTalk">
										<div class="letChatInfo">
											<span class="txt">Let's chat</span>
											<span>Nguyen Khac Bao</span>
											<span class="description">System Developer at HuongNgai coporation</span>
											<span class="description">Last active 2018-08-12</span>
										</div>
									</div>
								</c:otherwise>
							</c:choose> --%>
							
							<div class="typezone">
								<div id="emoticonPanel" style="display: none; width:400px; height:350px; margin-top: -350px;margin-left: 0px; background-color: #fff; border:1px solid #3399ff; position: absolute;">
									<div id="emoticonGroup" style="display:block;width:100%; height: 45px;background-color: #fff; border-bottom:1px solid #3399ff;;">
										<div style="float:left; display:block; height: 45px;">
											<img id="previousEmoticon" src="/images/previous1.png" height=40 width=30 style="padding-top: 3px; ">
										</div>
										<div id="_ePresentors" style="float:left; display:block; ">
											<div id="_group1" style="background-color: #d9d9d9; float:left; display: block; height:44px; width:44px; cursor: pointer; " onclick="changeStickerGroup(this);"><img src="/images/emoticon/hackerGirl.png" height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div>
											<div id="_group2" style="float:left; display: block; height:44px; width:44px; cursor: pointer;" onclick="changeStickerGroup(this);"><img src="/images/emoticon/crayonShin.png"  height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div>
											<div id="_group3" style="float:left; display: block; height:44px; width:44px; cursor: pointer;" onclick="changeStickerGroup(this);"><img src="/images/emoticon/catEmoticon.png" height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div>
											<div id="_group4" style="float:left; display: block; height:44px; width:44px; cursor: pointer;" onclick="changeStickerGroup(this);"><img src="/images/emoticon/student.png"     height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div>
											<div id="_group5" style="float:left; display: block; height:44px; width:44px; cursor: pointer;" onclick="changeStickerGroup(this);"><img src="/images/emoticon/bee.gif"         height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div>
											<div id="_group6" style="float:left; display: block; height:44px; width:44px; cursor: pointer;" onclick="changeStickerGroup(this);"><img src="/images/emoticon/crayonShin.png"  height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div>
											<div id="_group7" style="float:left; display: block; height:44px; width:44px; cursor: pointer;" onclick="changeStickerGroup(this);"><img src="/images/emoticon/catEmoticon.png" height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div>
											<div id="_group8" style="float:left; display: block; height:44px; width:44px; cursor: pointer;" onclick="changeStickerGroup(this);"><img src="/images/emoticon/student.png"     height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div>
											<!-- <div id="_group9" style="float:left; display: block; height:44px; width:44px; cursor: pointer; " onclick="changeStickerGroup(this);"><img src="/images/emoticon/hackerGirl.png" height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div>
											<div id="_group10" style="float:left; display: block; height:44px; width:44px; cursor: pointer;" onclick="changeStickerGroup(this);"><img src="/images/emoticon/crayonShin.png" height=40 width=40 style="padding-top: 2px; padding-left: 2px; "></div> --> 
										</div>
										<div style="float: right; display:block; height: 45px;">
											<img id="nextEmoticon" src="/images/next1.png" height=40 width=30 style="padding-top: 3px; ">
										</div>
									</div>	
									<div id="emoticonList" style="display:inline-block;width:100%; background-color: #fff;">
										<div id="_listG1" style="height:300px; overflow-y: auto; overflow-x: hidden; display: block;">
											<table id="_listG1Table">
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/45.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/65.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/75.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/85.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/95.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/105.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/118.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/119.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/125.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/135.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/145.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/155.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/165.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/172.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/182.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/192.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/202.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/215.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/216.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/222.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/232.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/242.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/252.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/262.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/272.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/282.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/292.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/302.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/314.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/315.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/322.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/332.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/341.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/351.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/361.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/371.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/381.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/391.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/401.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/girl/431.png);" onclick="displaySticker(this);"></div></td>
												</tr>
											</table>
										</div>
										<div id="_listG2" style="height:300px; overflow-y: auto; overflow-x: hidden; display: none;">
											<table id="_listG2Table">
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/2.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/3.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/4.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/5.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/6.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/7.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/8.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/9.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/10.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/11.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/12.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/13.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/14.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/15.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/16.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/17.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/18.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/19.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/20.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/21.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/22.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/23.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/24.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/25.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/26.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/27.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/28.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/29.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/30.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/31.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/32.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/shin/33.png);" onclick="displaySticker(this);"></div></td>
												</tr>
											</table>
										</div>
										<div id="_listG3" style="height:300px; overflow-y: auto; overflow-x: hidden; display: none;">
											<table id="_listG3Table">
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/1.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/2.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/3.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/4.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/5.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/6.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/7.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/8.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/9.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/10.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/11.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/12.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/13.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/14.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/15.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/16.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/17.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/18.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/19.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/20.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/21.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/22.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/23.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/cat/24.png);" onclick="displaySticker(this);"></div></td>
												</tr>
											</table>
										</div>
										<div id="_listG4" style="height:300px; overflow-y: auto; overflow-x: hidden; display: none;">
											<table id="_listG4Table">
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/1.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/2.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/3.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/4.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/5.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/6.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/7.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/8.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/9.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/10.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/11.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/12.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/13.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/14.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/15.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/16.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/17.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/18.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/19.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/20.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/21.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/22.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/23.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/24.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/25.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/26.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/27.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/28.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/29.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/30.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/31.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/32.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/33.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/34.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/35.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/36.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/37.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/38.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/39.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/40.png);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/41.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/42.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/43.png);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/boy/44.png);" onclick="displaySticker(this);"></div></td>
												</tr>
											</table>
										</div>
										<div id="_listG5" style="height:300px; overflow-y: auto; overflow-x: hidden; display: none;">
											<table id="_listG5Table">
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/1.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/2.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/3.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/4.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/5.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/6.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/7.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/8.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/9.gif);"  onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/10.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/11.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/12.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/13.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/14.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/15.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/16.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/17.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/18.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/19.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/20.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/21.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/22.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/23.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/24.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/25.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/26.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/27.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/28.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/29.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/30.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/31.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/32.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/33.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/34.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/35.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/36.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/37.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/38.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/39.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/40.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/41.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/42.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/43.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/44.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/45.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/46.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/47.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/48.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
												<tr style="width:100%; height:45px;">
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/49.gif);" onclick="displaySticker(this);"></div></td>
													<td><div class="emoticon" style="background-image: url(/images/emoticon/bee/50.gif);" onclick="displaySticker(this);"></div></td>
												</tr>
											</table>
										</div>
									</div>
								</div>
								<div class="emojis" id="bnkEmoticon"></div>
								<div class="bnkUploadFile" style="border-right: 1px solid #DDD; height: 50px; width: 50px;">
									<!-- <img id="bnkFile" src="/images/chat/upload.png" style="display: block; height: 30px; width: 35px; cursor: pointer; margin-top: 11px; padding-left: 5px;" onclick=""> -->
								</div>
								<div class="bnkChatInput">
									<textarea id="bnkCmtTxt" placeholder="Say something" style="overflow: hidden;" onkeypress="check_key(event);"></textarea>
								</div>
								<div class="send"></div>
							</div>
							
						</div>
					</div>
					<div class="bnk bnkRight">right</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="/js/chat/chatNavi.js"></script>
	<script type="text/javascript" src="/js/chat/chatUsers.js"></script>
</body>
</html>