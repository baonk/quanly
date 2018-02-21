package com.nv.baonk.chat.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nv.baonk.chat.service.ChatService;
import com.nv.baonk.chat.vo.ChatUserVO;

@RestController
public class ChatGWController {
	@Autowired
	private ChatService chatSerivce;
	
	private final Logger logger = LoggerFactory.getLogger(ChatGWController.class);
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/baonk/chatgw/user/{userid}/user-list", method= RequestMethod.GET, produces="application/json;charset=utf-8")
	public JSONObject getUserList(@PathVariable(value="userid") String userId, HttpServletRequest request) {
		int tenantId      = request.getParameter("tenantId")  != null ? Integer.parseInt(request.getParameter("tenantId"))  : -1;
		int currIdx       = request.getParameter("currIndex") != null ? Integer.parseInt(request.getParameter("currIndex")) :  1;
		int blockSize     = 10;
		int totalRows     = 0;
		int totalPages    = 0;
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
			List<ChatUserVO> listUser = chatSerivce.getChatUserList(userId, startPoint, blockSize, tenantId);
			totalRows                 = chatSerivce.getChatUserListCnt(userId, tenantId);
			totalPages                = (totalRows + blockSize - 1)/blockSize;
			
			result.put("status", "ok");
			result.put("code", 0);
			result.put("data", listUser);
			result.put("totalPages", totalPages);
			result.put("totalRows", totalRows);
		} 
		catch (Exception e) {
			e.printStackTrace();
			result.put("status", "error");
			result.put("code", 1);
		}
		
		return result;
	}
}
