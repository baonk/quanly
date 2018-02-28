package com.nv.baonk.chat.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nv.baonk.chat.service.ChatService;
import com.nv.baonk.chat.vo.ChatMessageSimpleVO;
import com.nv.baonk.chat.vo.ChatMessageVO;
import com.nv.baonk.chat.vo.ChatUserVO;
import com.nv.baonk.chat.vo.ConversationVO;
import com.nv.baonk.common.CommonUtil;
import com.nv.baonk.login.service.UserService;
import com.nv.baonk.login.vo.Role;
import com.nv.baonk.login.vo.User;

@RestController
public class ChatGWController {
	@Autowired
	private ChatService chatSerivce;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private SimpMessagingTemplate messageTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(ChatGWController.class);
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/baonk/chatgw/user/{userid}/user-list", method= RequestMethod.GET, produces="application/json;charset=utf-8")
	public JSONObject getUserList(@PathVariable(value="userid") String userId, HttpServletRequest request) {
		int tenantId      = request.getParameter("tenantId")  != null ? Integer.parseInt(request.getParameter("tenantId"))  : -1;
		int currIdx       = request.getParameter("currIndex") != null ? Integer.parseInt(request.getParameter("currIndex")) :  1;
		int blockSize     = 10;
		int totalUsers    = 0;
		int totalGroups   = 0;
		int isAdmin       = 0;
		int startPoint    = (currIdx - 1) * blockSize;
		JSONObject result = new JSONObject();
		
		logger.debug("UserId: " + userId + " || tenantId: " + tenantId);
		
		if (tenantId == -1 || userId.equals("")) {
			logger.debug("Parameter error!");
			result.put("status", "error");
			result.put("code", 1);
			return result;
		}
		
		try {
			User user = userService.findUserByUseridAndTenantid(userId, tenantId);
			
			for (Role role: user.getRoles()) {
				if(role.getRolename().equals("ADMIN")) {
					isAdmin = 1;
					break;
				}
			}
			
			List<ChatUserVO> listUser  = (isAdmin == 1) ? chatSerivce.getAllChatUsers(userId, startPoint, blockSize, tenantId) : chatSerivce.getChatUserList(userId, startPoint, blockSize, tenantId);
			totalUsers                 = listUser.size();
			//totalUsers                 = (isAdmin == 1) ? chatSerivce.getAllChatUsersCnt(userId, tenantId)                     : chatSerivce.getChatUserListCnt(userId, tenantId);
			//totalPages                = (totalRows + blockSize - 1)/blockSize;
			List<ChatUserVO> listGroup = chatSerivce.getAllGroupChat(userId, user.getDepartmentid(), tenantId);
			totalGroups                = listGroup.size();
			
			result.put("status", "ok");
			result.put("code", 0);
			result.put("data1", listUser);
			result.put("data2", listGroup);
			result.put("totalGroups", totalGroups);
			result.put("totalUsers", totalUsers);
		} 
		catch (Exception e) {
			e.printStackTrace();
			result.put("status", "error");
			result.put("code", 1);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/baonk/chatgw/message/{userid}/friend/{friendid}/mode/{mode}", method= RequestMethod.GET, produces="application/json;charset=utf-8")
	public JSONObject getUserList(@PathVariable(value="userid") String userId, @PathVariable(value="friendid") String friendId, @PathVariable String mode, HttpServletRequest request) {
		int tenantId      = request.getParameter("tenantId")  != null ? Integer.parseInt(request.getParameter("tenantId"))  : -1;
		int currIdx       = request.getParameter("currIndex") != null ? Integer.parseInt(request.getParameter("currIndex")) :  1;
		int blockSize     = 10;
		int endPoint      = currIdx * blockSize;
		JSONObject result = new JSONObject();
		
		logger.debug("UserId: " + userId + " || tenantId: " + tenantId + " || Friend Id: " + friendId + " || mode: " + mode);
		
		if (tenantId == -1 || userId.equals("") || friendId.equals("") || mode.equals("")) {
			logger.debug("Parameter error!");
			result.put("status", "error");
			result.put("code", 1);
			return result;
		}
		
		try {
			List<ChatMessageVO> listMessages = chatSerivce.getPersonalMessages(userId, friendId, endPoint, tenantId);
			if (listMessages == null || listMessages.size() == 0) {
				User friend = userService.findUserByUseridAndTenantid(friendId, tenantId);
				result.put("friend", friend);
			}
			else {
				//Clear unread messages
				ConversationVO conversation = new ConversationVO();
				conversation.setLastMessage(listMessages.get(0).getMessageId());
				conversation.setUserId(userId);
				conversation.setRelatedId(friendId);
				conversation.setTenantId(tenantId);
				conversation.setLastChated(listMessages.get(0).getCreatedTime());
				chatSerivce.insertLastMessage(conversation);
			}
			
			result.put("status", "ok");
			result.put("code", 0);
			result.put("data", listMessages);
		} 
		catch (Exception e) {
			e.printStackTrace();
			result.put("status", "error");
			result.put("code", 1);
		}
		
		return result;
	}
	
	@MessageMapping("/sendMessage")
	public void greeting(@Payload ChatMessageSimpleVO message) throws Exception {
		ChatMessageVO messageVO = commonUtil.getChatMessage(message);
		chatSerivce.saveMessage(messageVO);
		
		logger.debug("Sender ID: " + message.getSender() + " || Receiver ID: " + message.getReceiver());
		
		//Send message to both users
		messageTemplate.convertAndSendToUser(message.getReceiver(), "/queue/reply", messageVO);
		messageTemplate.convertAndSendToUser(message.getSender(), "/queue/reply", messageVO);
	}
}
