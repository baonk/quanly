package com.nv.baonk.chat.vo;

public class ChatMessageTest {
	private MessageType type;
	private String content;
	private String sender;
	private String receive;
	
	public enum MessageType {
		CHAT,
		JOIN,
		LEAVE
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

}
