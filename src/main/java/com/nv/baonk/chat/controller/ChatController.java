package com.nv.baonk.chat.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.nv.baonk.chat.vo.ChatMessageTest;
import com.nv.baonk.common.CommonUtil;
import com.nv.baonk.login.service.UserService;
import com.nv.baonk.login.vo.User;

@Controller
public class ChatController {
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Autowired
	private BCryptPasswordEncoder BCryptPass;
	
	@Autowired
	private SimpMessagingTemplate messageTemplate;
	
	private final Logger logger    = LoggerFactory.getLogger(ChatController.class);
	
	@Value("${chat.gw}")
	private String chatgwURL;
	
	@RequestMapping("/chatBoard")
	public String chatBoard(@CookieValue("loginCookie")String loginCookie, Model model, HttpServletRequest request) {
		User user = commonUtil.getUserInfo(loginCookie);
		
		//Get list of current Online users
		List<String> loggedInUserList = getUsersFromSessionRegistry();
		
		logger.debug("List of loggedin User: " + loggedInUserList.size());
		
		for(String test: loggedInUserList) {
			logger.debug("Test: " + test);
		}
		
		//Get all friends of current user
		List<User> listOfOnlineUser = userService.findAllCompanyEmployees(user.getCompanyid(), user.getTenantid());

		logger.debug("Size of List: " + listOfOnlineUser.size());
		
		Iterator<User> iterator = listOfOnlineUser.iterator();
		
		while (iterator.hasNext()) {
			User vUser = iterator.next();
			
			if (!loggedInUserList.contains(vUser.getUserid())) {
				iterator.remove();
			}
		}
		
		for (User u : listOfOnlineUser) {
			logger.debug("User Name: " + u.getUsername());
		}
		
		model.addAttribute("hasChat", 0);
		model.addAttribute("userId", user.getUserid());
		model.addAttribute("onlUserList", listOfOnlineUser);
		return "/chat/chatBoard";
	}

	private List<String> getUsersFromSessionRegistry() {
	/*List<String> resultList = new ArrayList<String>();
		List<Object> allPrincipals = sessionRegistry.getAllPrincipals().stream().filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty()).collect(Collectors.toList());
		
		for (Object principal : allPrincipals) {
			if (principal instanceof UserDetails) {
				UserDetails user = (UserDetails) principal;
				resultList.add(user.getUsername());
			}
		}
		
		return resultList;*/
		return sessionRegistry.getAllPrincipals().stream().filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty()).filter(u -> u instanceof UserDetails).map(u -> (UserDetails)u).map(UserDetails::getUsername).collect(Collectors.toList());
	}
	
	@RequestMapping(value="/chat/getUserList.do", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getFileList(@CookieValue("loginCookie") String loginCookie, HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		Map<String,Object> map = new HashMap<>();
		User userInfo          = commonUtil.getUserInfo(loginCookie);
		String currIdx         = request.getParameter("currentIndex");
		String url             = chatgwURL + "/user/" + userInfo.getUserid() + "/user-list";
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		UriComponentsBuilder builder  = UriComponentsBuilder.fromHttpUrl(url)
										.queryParam("tenantId", userInfo.getTenantid())
										.queryParam("currIndex", currIdx);
		RestTemplate rest             = new RestTemplate();
		ResponseEntity<String> result = rest.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
		
		JSONParser jp                 = new JSONParser();
		JSONObject resultBody         = (JSONObject) jp.parse(result.getBody());
		String status                 = resultBody.get("status").toString();
		
		if (status.equals("ok")) {
			JSONArray userList = (JSONArray) resultBody.get("data");
			long totalPages    = (long) resultBody.get("totalPages");
			long totalRows     = (long) resultBody.get("totalRows");
			map.put("userList", userList);
			map.put("totalPages", totalPages);
			map.put("totalRows", totalRows);
		}
		
		return map;
	}
	
	@MessageMapping("/sendMessage")
	public void greeting(@Payload ChatMessageTest message, Principal principal) throws Exception {
		String receiverId = message.getReceive();
		messageTemplate.convertAndSendToUser(receiverId, "/queue/reply", message);
		messageTemplate.convertAndSendToUser(principal.getName(), "/queue/reply", message);
	}
	
}