package com.nv.baonk.login.vo;

import java.io.Serializable;

public class Server implements Serializable {
	private static final long serialVersionUID = 11312325325235L;
	private String servername;
	private int tenantid;

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public int getTenantid() {
		return tenantid;
	}

	public void setTenantid(int tenantid) {
		this.tenantid = tenantid;
	}
}