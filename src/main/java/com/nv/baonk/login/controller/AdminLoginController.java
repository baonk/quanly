package com.nv.baonk.login.controller;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.nv.baonk.login.service.UserService;
import com.nv.baonk.login.vo.User;

@Controller
public class AdminLoginController {
	@Autowired
	private UserService userService;
	private final Logger logger = LoggerFactory.getLogger(AdminLoginController.class);
	
	@RequestMapping(value="/admin", method = RequestMethod.GET)
	public String adminPage(Model model, HttpServletRequest request){
		logger.debug("-------------------Run in adminPage--------------------");
		
		//Get tenant Id from serverName
		String serverName = request.getServerName();
		int tenantId      = userService.getTenantId(serverName);
		
		logger.debug("Server Name: " + serverName + " || Tenant ID: " + tenantId);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user           = userService.findUserByUseridAndTenantid(auth.getName(), tenantId);
		
		model.addAttribute("userName", user.getUserid());
		model.addAttribute("email", user.getEmail());
		
		logger.debug("--------------------adminPage end----------------------");
		return "admin/admin";
	}
	
	@RequestMapping(value="/admin/topMenu", method = RequestMethod.GET)
	public String adminTopMenu(Model model, HttpServletRequest request){
		logger.debug("-----------------Run in adminTopMenu------------------");
		
		//Get tenant Id from serverName
		String serverName   = request.getServerName();
		int tenantId        = userService.getTenantId(serverName);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user           = userService.findUserByUseridAndTenantid(auth.getName(), tenantId);
		
		if (user == null) {
			return "access-denied";
		}
		
		model.addAttribute("userName", user.getUserid());
		
		logger.debug("-------------------adminTopMenu end-------------------");
		return "admin/adminTopMenu";
	}
}
