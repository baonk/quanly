package com.nv.baonk.config;

import java.security.Principal;

public class BnkPrincipal implements Principal {
	private String name;
	
	BnkPrincipal(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
