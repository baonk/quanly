<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
	<link rel="stylesheet" type="text/css" href="/css/home.css" />
	<link rel="stylesheet" href="/css/bootstrap.min.css">
	<script	src="/js/jquery/jquery.min.js"></script>
	<script	src="/js/bootstrap.min.js"></script>
	<script	src="/js/main/main.js"></script>
	<script src="/js/popup.js"></script>
	<script type="text/javascript" src="/js/fileUpload.js"></script>
	<script type="text/javascript">
		window.onload = function() {
			preProcess();
		}
	</script>
</head>
<body>
		<div class="bnkMain">
			<div class="bnkMainLeft">
				<div style="border: 1px solid #eee; margin-left: 5px; display: flex; flex-direction: column;">
					<div style="height: 40px; display: flex;">
						<img id="favouriteListTop" src="/images/main/list.png" style="height: 40px; width:40px; border-radius: 50%; margin-left: 5px; cursor: pointer;" />
						<span style="height: 40px; line-height: 40px; margin-left: 10px; font-size: 16px; font-weight: bold;">Danh sach danh dau</span>
					</div>
					
					<div id="bnkfTopicMain" style="display: flex; flex-direction: column;">
						<div style="margin-top: 2px;"><hr size="0" style="color:#fff; background-color:#fff; margin: 0px 10px; border-top: 1px solid #bfbfbf;"></div>
						
						<div id="favListTopic" style="flex-grow: 1; display: flex; flex-direction: column;">
							<div id="fTopic0" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/video.png" class="fImage" />
								<div class="fTopicTitle">[Chia se] Lam the nao de mon thit nuong thom va ngon</div>
							</div>
							<div id="fTopic1" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/conversation.png" class="fImage" />
								<div class="fTopicTitle">[Thong tin] Thu tuong Nga co chuyen vieng tham den Viet Nam</div>
							</div>
							<div id="fTopic2" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/books.png" class="fImage" />
								<div class="fTopicTitle">[Sach] Can mua sach "Nha gia kim"</div>
							</div>
							<div id="fTopic3" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/conversation.png" class="fImage" />
								<div class="fTopicTitle"">[Chuyen tro] Hom nay chan nan khong muon di lam</div>
							</div>
							<div id="fTopic4" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/sport.png" class="fImage" />
								<div class="fTopicTitle">[Bong da] Hom nay co ai xem tran Sieu kinh dien khong?</div>
							</div>
							<div id="fTopic5" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/design.png" class="fImage" />
								<div class="fTopicTitle">[Design] Cach lay nen nhanh va chuan cua cac cao thu</div>
							</div>
							<div id="fTopic6" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/conversation.png" class="fImage" />
								<div class="fTopicTitle">[Bitcoin] Gia bitcoin dang giam nhanh!</div>
							</div>
							<div id="fTopic7" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/code.png" class="fImage" />
								<div class="fTopicTitle"">[Coding] Chia se kinh nghiem lam mot project thuc te</div>
							</div>
							<div id="fTopic8" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/video.png" class="fImage" />
								<div class="fTopicTitle">[Video] Tong hop cac video hai hay nhat the gioi</div>
							</div>
							<div id="fTopic9" style="height: 40px; display: flex; margin-top: 2px;">
								<img src="/images/main/video.png" class="fImage" />
								<div class="fTopicTitle"">[Phim] Tuyen tap cac bo phim hay nhat the gioi</div>
							</div>
						</div>
						<div style="height: 40px;">
							<div style="display: flex;">
								<span class="bnkfmore">View more</span>
								<img src="/images/main/more.png" style="height: 15px; width: 15px; margin: 12.5px 2px; cursor: pointer;"/>
							</div>
							<!-- <div style="height: 40px; line-height: 40px; text-align: center; display: none;">Ban chua danh dau topic nao</div> -->
						</div>
					</div>
				</div>
			</div>
			
			<div class="bnkMainCenter">
				<div class="newTopicPanel">
					<div class="nextImg"><img src="/images/main/back1.png" style="height: 80px; width: 80px; margin: 10px 10px; cursor: pointer;"/></div>
					<div class="hotTopicList">
						<div class="favouriteTopic" style="">
							<table class="bnkFaTop">
								<tr>
									<td class="bnkTdFaTop"><img src="/images/test1.png" class="bnkUserImg"/></td>
									<td>
										<div>Nguyen Van A</div>
										<div>Linh vuc: Design</div>
										<div>2018-01-15 15:34:50</div>
									</td>
								</tr>
								<tr>
									<td colspan="2" style="background-color: rgba(52, 152, 219, 0.34);" >
										<div class="topicTitle" title="Xoa nen chuan & nhanh">Xoa nen chuan & nhanh<span class="bnkNote">(316)</span></div>
									</td>
								</tr>
							</table>
						</div>
						<div class="favouriteTopic">
							<table class="bnkFaTop">
								<tr>
									<td class="bnkTdFaTop"><img src="/images/test3.png" class="bnkUserImg"/></td>
									<td>
										<div>Nguyen Thi B</div>
										<div>Linh vuc: Programing</div>
										<div>2018-01-14 23:21:45</div>
									</td>
								</tr>
								<tr>
									<td colspan="2" style="background-color: rgba(52, 152, 219, 0.34);" >
										<div class="topicTitle" title="Ngon ngu lap trinh swift">Ngon ngu lap trinh swift<span class="bnkNote">(1056)</span></div>
									</td>
								</tr>
							</table>
						</div>
						<div class="favouriteTopic">
							<table class="bnkFaTop">
								<tr>
									<td class="bnkTdFaTop"><img src="/images/test2.png" class="bnkUserImg"/></td>
									<td>
										<div>Can Van C</div>
										<div>Linh vuc: Video</div>
										<div>2018-01-16 11:54:06</div>
									</td>
								</tr>
								<tr>
									<td colspan="2" style="background-color: rgba(52, 152, 219, 0.34);" >
										<div class="topicTitle" title="Video day cau long">Video day cau long<span class="bnkNote">(825)</span></div>
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div class="backImg"><img src="/images/main/next1.png" style="height: 80px; width: 80px; margin: 10px 10px; cursor: pointer;"/></div>
				</div>
				<div class="topTopicMenu" style="position: relative;">
					<div style="display: flex;">
						<div id="bnkTopicCreate" class="bnkTopicWrite">
							<img class="bnkTopicImg" src="/images/createTopic.png" />
							<span style="line-height: 40px;">New Topic</span>
						</div>
						<div style="margin-left: 4px;"><img src="/images/vertical-line.png" style="height: 25px; width: 10px; margin-top: 8px;"/></div>
						<div id="bnkPollCreate" class="bnkPoll">
							<img class="bnkTopicImg2" src="/images/poll.png" />
							<span style="line-height: 40px;">New Poll</span>
						</div>
					</div>
					<div style="position: absolute; bottom: 0px; right: 0px; width: 400px; height: 30px;" class="bnkNav">
						<table class="bnkTblNav">
							<tr>
								<td class="bnkTdMain">Page 1 of 100</td>
								<td class="bnkTdCurrent">1</td>
								<td class="bnkTdNorm">2</td>
								<td class="bnkTdNorm">3</td>
								<td class="bnkTdLast">Last</td>
								<td class="bnkTdGoto" style="position: relative;">
									<div class="bnkGotoDiv" index="0">
										<span class="bnkTdGotoTxt">Go to</span>
										<img src="/images/goto.gif" style="margin-left: 2px;"/>
									</div>
									<div tabindex="-1" style="position: absolute; z-index: 50; left: -66px; top: 26px; height: 42px; display: none; outline: 0;" id="gotoPanel0">
										<table cellpadding="4" cellspacing="1" style="background-color: #eee; width: 128px; height:100%; border: 1px solid #3498db;">
										<tbody>
											<tr>
												<td style="background-color: #113676; color: #fff; text-align: center; font-size: 12px;">Go to Page...</td>
											</tr>
											<tr>
												<td>
													<div>
														<input type="text" style="font-size:14px; margin: 2px 5px; width: 75px; height: 20px;" size="4" id="gotoInput0">
														<span class="bnkGoBttn">Go</span>
													</div>
												</td>
											</tr>
										</tbody>
										</table>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div class="bnkTopicList">
  					<div style="display: flex; flex-direction: column; margin-bottom: 5px;" id="bnkTopic0">
						<div class="bnkTopicTopBar">
							<img src="/images/main/video.png" class="bnkTopicIcon">
							<span class="bnkTopicTime2">Today</span>
							<span class="bnkTopicTime">(2018-01-17 10:41:50)</span>
							<div style="position: absolute; top: 0px; right: 5px; display: flex;">
								<span class="bnkJoin">Join</span>
								<img src="/images/main/nextImg.ico" class="bnkJoinImg"/>
							</div>
						</div>
						
						<div style="height: 115px; border: 1px solid #3498db;">
							<div style="display: flex; position: relative; height: 80px;">
								<img src="/images/emoticon/boy/2.png" style="height: 60px; width: 60px; margin: 10px;"/>
								<div>
									<span style="display: block; margin-top: 15px;">Nguyen Khac Bao</span>
									<span style="display: block; margin-top: 3px;">Admin</span>
								</div>
								<div style="position: absolute; top: 0px; right: 5px;">
									<span style="display: block;margin-top: 10px;">Join date : 2018-01-09</span>
									<span style="display: block;">Topics&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: 132</span>
									<span style="display: block;">Posts&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: 1024</span>
								</div>
							</div>
							<div><hr size="0" style="color:#fff; background-color:#fff; margin: 0px 40px; border-top: 1px solid #000;"></div>
							<div style="text-align: center; height: 32px; line-height: 32px; font-weight: bold; font-size: 18px;">[Chia se] Lam the nao de co cu smash tot trong cau long</div>
						</div>
						
						<div id="topicContent" style="max-width: 1200px; border: 1px solid #3498db; border-top: 0px;">
							<div class="bnkTopicContent">
								끓는 피에 뛰노는 심장은 거선의 기관과 같이 힘있다 이것이다 인류의 역사를 꾸며 내려온 동력은 바로 이것이다 이성은 투명하되 얼음과 
								같으며 지혜는 날카로우나 갑 속에 든 칼이다 청춘의 끓는 피가 아니더면 인간이 얼마나 쓸쓸하랴? 얼음에 싸인 만물은 얼음이 있을 뿐이다 
								그들에게 생명을 불어 넣는 것은 따뜻한 봄바람이다 풀밭에 속잎나고 가지에 싹이 트고 꽃 피고 새 우는 봄날의 천지는 얼마나 기쁘며 얼마나
								 아름다우냐? 이것을 얼음 속에서 불러 내는 것이 따뜻한 봄바람이다 인생에 따뜻한 봄바람을 불어 보내는 것은 청춘의 끓는 피다 청춘의 피가
								  뜨거운지라 인간의 동산에는 사랑의 풀이 돋고 이상의 꽃이.
							</div>
						</div>
					</div>
					
					<!-- Topic2 -->
					<div style="display: flex; flex-direction: column; margin-bottom: 5px;" id="bnkTopic1">
						<div class="bnkTopicTopBar">
							<img src="/images/main/design.png" class="bnkTopicIcon">
							<span class="bnkTopicTime2">Today</span>
							<span class="bnkTopicTime">(2018-01-10 24:30:50)</span>
							<div style="position: absolute; top: 0px; right: 5px; display: flex;">
								<span class="bnkJoin">Join</span>
								<img src="/images/main/nextImg.ico" class="bnkJoinImg"/>
							</div>
						</div>
						
						<div style="height: 115px; border: 1px solid #3498db;">
							<div style="display: flex; position: relative; height: 80px;">
								<img src="/images/emoticon/girl/302.png" style="height: 60px; width: 60px; margin: 10px;"/>
								<div>
									<span style="display: block; margin-top: 15px;">Blue sky</span>
									<span style="display: block; margin-top: 3px;">Member</span>
								</div>
								<div style="position: absolute; top: 0px; right: 5px;">
									<span style="display: block;margin-top: 10px;">Join date : 2014-01-09</span>
									<span style="display: block;">Topics&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: 15</span>
									<span style="display: block;">Posts&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: 29</span>
								</div>
							</div>
							<div><hr size="0" style="color:#fff; background-color:#fff; margin: 0px 40px; border-top: 1px solid #000;"></div>
							<div style="text-align: center; height: 32px; line-height: 32px; font-weight: bold; font-size: 18px;">[Chia se] Cach xoa nen trong photoshop</div>
						</div>
						
						<div id="topicContent" style="max-width: 1200px; border: 1px solid #3498db; border-top: 0px;">
							<div class="bnkTopicContent">
								끓는 피에 뛰노는 심장은 거선의 기관과 같이 힘있다 이것이다 인류의 역사를 꾸며 내려온 동력은 바로 이것이다 이성은 투명하되 얼음과 
								같으며 지혜는 날카로우나 갑 속에 든 칼이다 청춘의 끓는 피가 아니더면 인간이 얼마나 쓸쓸하랴? 얼음에 싸인 만물은 얼음이 있을 뿐이다 
								그들에게 생명을 불어 넣는 것은 따뜻한 봄바람이다 풀밭에 속잎나고 가지에 싹이 트고 꽃 피고 새 우는 봄날의 천지는 얼마나 기쁘며 얼마나
								 아름다우냐? 이것을 얼음 속에서 불러 내는 것이 따뜻한 봄바람이다 인생에 따뜻한 봄바람을 불어 보내는 것은 청춘의 끓는 피다 청춘의 피가
								  뜨거운지라 인간의 동산에는 사랑의 풀이 돋고 이상의 꽃이.
							</div>
						</div>
					</div>
					
					<!-- Topic3 -->
					<div style="display: flex; flex-direction: column; margin-bottom: 5px;" id="bnkTopic2">
						<div class="bnkTopicTopBar">
							<img src="/images/main/video.png" class="bnkTopicIcon">
							<span class="bnkTopicTime2">Today</span>
							<span class="bnkTopicTime">(2018-01-17 10:41:50)</span>
							<div style="position: absolute; top: 0px; right: 5px; display: flex;">
								<span class="bnkJoin">Join</span>
								<img src="/images/main/nextImg.ico" class="bnkJoinImg"/>
							</div>
						</div>
						
						<div style="height: 115px; border: 1px solid #3498db;">
							<div style="display: flex; position: relative; height: 80px;">
								<img src="/images/emoticon/boy/3.png" style="height: 60px; width: 60px; margin: 10px;"/>
								<div>
									<span style="display: block; margin-top: 15px;">Dark knight</span>
									<span style="display: block; margin-top: 3px;">Member</span>
								</div>
								<div style="position: absolute; top: 0px; right: 5px;">
									<span style="display: block;margin-top: 10px;">Join date : 2015-03-17</span>
									<span style="display: block;">Topics&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: 34</span>
									<span style="display: block;">Posts&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: 137</span>
								</div>
							</div>
							<div><hr size="0" style="color:#fff; background-color:#fff; margin: 0px 40px; border-top: 1px solid #000;"></div>
							<div style="text-align: center; height: 32px; line-height: 32px; font-weight: bold; font-size: 18px;">[Chia se] Lam the nao de mon thit nuong thom va ngon</div>
						</div>
						
						<div id="topicContent" style="max-width: 1200px; border: 1px solid #3498db; border-top: 0px;">
							<div class="bnkTopicContent">
								끓는 피에 뛰노는 심장은 거선의 기관과 같이 힘있다 이것이다 인류의 역사를 꾸며 내려온 동력은 바로 이것이다 이성은 투명하되 얼음과 
								같으며 지혜는 날카로우나 갑 속에 든 칼이다 청춘의 끓는 피가 아니더면 인간이 얼마나 쓸쓸하랴? 얼음에 싸인 만물은 얼음이 있을 뿐이다 
								그들에게 생명을 불어 넣는 것은 따뜻한 봄바람이다 풀밭에 속잎나고 가지에 싹이 트고 꽃 피고 새 우는 봄날의 천지는 얼마나 기쁘며 얼마나
								 아름다우냐? 이것을 얼음 속에서 불러 내는 것이 따뜻한 봄바람이다 인생에 따뜻한 봄바람을 불어 보내는 것은 청춘의 끓는 피다 청춘의 피가
								  뜨거운지라 인간의 동산에는 사랑의 풀이 돋고 이상의 꽃이.
							</div>
						</div>
					</div>
				</div>
				
				<div style="position: relative; height: 80px;">
					<div style="position: absolute; top: 0px; right: 0px; width: 400px; height: 30px;" class="bnkNav">
						<table class="bnkTblNav">
							<tr>
								<td class="bnkTdMain">Page 1 of 100</td>
								<td class="bnkTdCurrent">1</td>
								<td class="bnkTdNorm">2</td>
								<td class="bnkTdNorm">3</td>
								<td class="bnkTdLast">Last</td>
								<td class="bnkTdGoto" style="position: relative;">
									<div class="bnkGotoDiv" index="1">
										<span class="bnkTdGotoTxt">Goto</span>
										<img src="/images/goto.gif" style="margin-left: 2px;"/>
									</div>
									<div style="position: absolute; z-index: 50; left: -66px; top: 26px; height: 42px; display: none;" id="gotoPanel1">
										<table cellpadding="4" cellspacing="1" style="background-color: #eee; width: 128px; height:100%; border: 1px solid #3498db;">
										<tbody>
											<tr>
												<td style="background-color: #113676; color: #fff; text-align: center; font-size: 12px;">Go to Page...</td>
											</tr>
											<tr>
												<td>
													<div>
														<input type="text" style="font-size:14px; margin: 2px 5px; width: 75px; height: 20px;" size="4" id="gotoInput1">
														<span class="bnkGoBttn">Go</span>
													</div>
												</td>
											</tr>
										</tbody>
										</table>
									</div>
									
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			
			<div class="bnkMainRight"></div>
		</div>
		
		
		<div style="width: 100%; height: 100%; position: absolute; top: 0; left: 0; z-index: 1000; background: none rgba(0,0,0,0.7); display: none;" id="fogPanel">&nbsp;</div>
		
		<div class="layerpopup"  style="z-index: 2000; position: absolute;display: none;" id="iFramePanel">
			<iframe src="/blank.htm" style="border:none;" id="iFrameLayer"></iframe>
		</div>
</body>
</html>