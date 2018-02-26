package com.nv.baonk.config;

import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
		ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
		HttpServletRequest httpServletRequest   = servletRequest.getServletRequest();
		String userName                         = httpServletRequest.getParameter("token");
		
		return new BnkPrincipal(userName);
	}
}