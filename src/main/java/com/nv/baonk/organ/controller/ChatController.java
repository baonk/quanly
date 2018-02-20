package com.nv.baonk.organ.controller;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	private final Logger logger = LoggerFactory.getLogger(ChatController.class);
	
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
}