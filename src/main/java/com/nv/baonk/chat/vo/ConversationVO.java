package com.nv.baonk.chat.vo;

public class ConversationVO {
	private String userId;
	private String relatedId;
	private String lastMessage;
	private String lastChated;
	private int    tenantId;
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRelatedId() {
		return relatedId;
	}
	
	public void setRelatedId(String relatedId) {
		this.relatedId = relatedId;
	}
	
	public String getLastMessage() {
		return lastMessage;
	}
	
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	
	public String getLastChated() {
		return lastChated;
	}
	
	public void setLastChated(String lastChated) {
		this.lastChated = lastChated;
	}
	
	public int getTenantId() {
		return tenantId;
	}
	
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}
	
}
