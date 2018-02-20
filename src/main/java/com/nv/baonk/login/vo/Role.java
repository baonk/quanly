package com.nv.baonk.login.vo;

import java.io.Serializable;

public class Role implements Serializable {
	private static final long serialVersionUID = 1768789769634534L;
	private int roleid;
	private String rolename;

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String roleName) {
		this.rolename = roleName;
	}
}