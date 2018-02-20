package com.nv.baonk.vo;

import java.util.Map;

public class ValidateResponseObject {
	private int result;
	private Map<String, String> errorMessages;
	
	public ValidateResponseObject() {
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Map<String, String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(Map<String, String> errorMessages) {
		this.errorMessages = errorMessages;
	}
}