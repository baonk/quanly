package com.nv.baonk.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.nv.baonk.login.service.UserService;
import com.nv.baonk.login.vo.Role;
import com.nv.baonk.login.vo.User;
import com.nv.baonk.login.vo.UserBnk;

@Component
public class MandatoryUserDetailsService implements UserDetailsService{
	private final Logger logger = LoggerFactory.getLogger(MandatoryUserDetailsService.class);
	private UserService userService;
	private HttpServletRequest httpRequest;

	public MandatoryUserDetailsService(UserService userService, HttpServletRequest request) {
		this.userService = userService;
		this.httpRequest = request;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
		logger.debug("Run in Load User By User Name!");
		try {
			//Get tenant Id from serverName
			String serverName = httpRequest.getServerName();
			int tenantId      = userService.getTenantId(serverName);
			User user         = userService.findUserByUseridAndTenantid(userID, tenantId);
			
			if (user == null) {
				logger.debug("User not found with the provided userId");
				return null;
			}
			
			logger.debug("UserId: " + userID + ", tenantId: " + tenantId);
			
			//Get User Role Id
			List<Integer> listRoleIDs = userService.getRoleId(userID, tenantId);
			Set<Role> userRoles       = new HashSet<Role>();
			
			for(Integer integer: listRoleIDs) {
				Role role = userService.findByRoleid(integer);
				userRoles.add(role);
			}
			
			user.setRoles(userRoles);
			return new UserBnk(user.getUserid(), user.getPassword(), getAuthorities(user), tenantId);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.debug("Run in Exception in User Detail Service!");
			throw new UsernameNotFoundException("User not found");
		}
	}
	
	private Set<GrantedAuthority> getAuthorities(User user){
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		
		for (Role role : user.getRoles()) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRolename());
			authorities.add(grantedAuthority);
		}
		
		logger.debug("User authorities are " + authorities.toString());
		return authorities;
	}
}
