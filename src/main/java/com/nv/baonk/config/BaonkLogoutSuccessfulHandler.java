package com.nv.baonk.config;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.nv.baonk.login.service.UserService;
import com.nv.baonk.login.vo.User;

@Component
public class BaonkLogoutSuccessfulHandler implements ApplicationListener<SessionDestroyedEvent>, LogoutSuccessHandler {
	@Autowired
	private UserService userService;
	
	private final Logger logger = LoggerFactory.getLogger(BaonkLogoutSuccessfulHandler.class);
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		String serverName   = request.getServerName();
		String userId       = authentication.getName();
		int tenantId        = userService.getTenantId(serverName);
		User authUser       = userService.findUserByUseridAndTenantid(userId, tenantId);
		
		//Set active status
		userService.updateUserActive(userId, 0, authUser.getCompanyid(), tenantId);
		
		request.getSession().setAttribute("previous_page", "");
		response.sendRedirect("login");
	}

	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		List<SecurityContext> lstSecurityContext = event.getSecurityContexts();
		UserDetails user;
		
		for (SecurityContext securityContext : lstSecurityContext) {
			user = (UserDetails) securityContext.getAuthentication().getPrincipal();
			logger.debug("UserID: " + user.getUsername());
		}
	}
}
