package com.nv.baonk.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class UsernameStoringUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	private final Logger logger      = LoggerFactory.getLogger(UsernameStoringUrlAuthenticationFailureHandler.class);
	private String defaultFailureUrl = "/login?test=true";
	
	@Override
	public void onAuthenticationFailure (HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		logger.debug("Run in failureHander");
		
		request.getSession (true).setAttribute("SPRING_SECURITY_LAST_USERID", request.getParameter ("userid"));
		request.getSession (true).setAttribute("SPRING_SECURITY_LAST_PASS",   request.getParameter ("password"));
		
		super.setDefaultFailureUrl(defaultFailureUrl);
		super.onAuthenticationFailure(request, response, exception);
	}
}