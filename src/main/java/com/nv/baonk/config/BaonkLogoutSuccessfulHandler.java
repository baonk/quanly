package com.nv.baonk.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.nv.baonk.login.service.UserService;
import com.nv.baonk.login.vo.User;
import com.nv.baonk.login.vo.UserBnk;

@Component
public class BaonkLogoutSuccessfulHandler implements ApplicationListener<SessionDestroyedEvent>, LogoutSuccessHandler{
	private final Logger logger = LoggerFactory.getLogger(BaonkLogoutSuccessfulHandler.class);

	@Autowired
	private UserService userService;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		request.getSession().setAttribute("previous_page", "");
		response.sendRedirect("login");
	}

	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		List<SecurityContext> lstSecurityContext = event.getSecurityContexts();
		
		for (SecurityContext securityContext : lstSecurityContext) {
			UserBnk user  = (UserBnk) securityContext.getAuthentication().getPrincipal();
			User authUser = userService.findUserByUseridAndTenantid(user.getUsername(), user.getTenant());
			logger.debug("UserID: " + user.getUsername() + " || TenantId: " + user.getTenant());
			
			//Set active status
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date                  = new Date();
			String time                = formatter.format(date);
			authUser.setLastlogout(time);
			authUser.setActive(0);
			userService.updateUser(authUser);
		}
	}
}
