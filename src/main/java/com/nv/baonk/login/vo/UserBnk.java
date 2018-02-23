package com.nv.baonk.login.vo;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserBnk extends User implements Serializable {
	private static final long serialVersionUID = 1346547387834L;
	private int tenant;
	
	public UserBnk(String username, String password, Collection<? extends GrantedAuthority> authorities, int tenantId) {
		super(username, password, authorities);
		this.tenant = tenantId;
	}

	public int getTenant() {
		return tenant;
	}

	public void setTenant(int tenant) {
		this.tenant = tenant;
	}

}
