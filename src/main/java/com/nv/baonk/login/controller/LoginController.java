package com.nv.baonk.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.nv.baonk.common.CommonUtil;
import com.nv.baonk.login.service.UserService;
import com.nv.baonk.login.vo.Role;
import com.nv.baonk.login.vo.User;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;

	@Autowired
	private CommonUtil commonUtil;
	
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public String login(Model model, HttpServletRequest request, HttpSession session) {
		String error    = "";
		String username = "";
		
		if (session != null) {
			SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
			
			if (savedRequest != null) {
				String redirectUrl = savedRequest.getRedirectUrl();
				session.setAttribute("prior_url", redirectUrl);
			}
		}
		
		if (request.getParameter("username") != null) {
			username = request.getParameter("username");
		}
		
		if (request.getParameter("test") != null) {
			error = request.getParameter("test");
		}
	
		model.addAttribute("error", error);
		model.addAttribute("username", username);
		return "login";
	}
	
	@RequestMapping(value="/home", method = RequestMethod.GET)
	public String home(@CookieValue("loginCookie") String loginCookie, Model model, HttpServletRequest request) {
		User userInfo = commonUtil.getUserInfo(loginCookie);
		logger.debug("UserID: " + userInfo.getUserid() + " || Tenant ID: " + userInfo.getTenantid() + " || User password: " + userInfo.getPassword());
		
		return "home";
	}
	
	@RequestMapping(value="/mainMenu", method = RequestMethod.GET)
	public String mainMenu(Model model){
		return "mainMenu";
	}
	
	@RequestMapping(value="/topMenu", method = RequestMethod.GET)
	public String topMenu(Model model, HttpServletRequest request) {
		//Get tenant Id from serverName
		String serverName   = request.getServerName();
		int tenantId        = userService.getTenantId(serverName);
		int isAdmin         = 0;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user           = userService.findUserByUseridAndTenantid(auth.getName(), tenantId);
		
		for (Role role: user.getRoles()) {
			if(role.getRolename().equals("ADMIN")) {
				isAdmin = 1;
				break;
			}
		}
		
		if (isAdmin == 0) {
			logger.debug("Normal user!");
			model.addAttribute("role", "USER");
		}
		else {
			logger.debug("Administrator!");
			model.addAttribute("role", "ADMIN");
		}
		
		model.addAttribute("userName", user.getUserid());
		model.addAttribute("email", user.getEmail());
		return "topMenu";
	}	
	
	@RequestMapping(value="/uploadMenu", method = RequestMethod.GET)
	public String uploadMenu(Model model) {
		return "uploadMenu";
	}
	
}